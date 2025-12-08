package it.getinsight.module.client.service;

import it.getinsight.core.pagination.PageableRequestModel;
import it.getinsight.core.pagination.PageableResponseModel;
import it.getinsight.module.client.dto.ClientDTO;
import it.getinsight.module.client.dto.ClientFilterDTO;
import it.getinsight.module.client.dto.ClientFullResponseDTO;
import it.getinsight.module.client.entity.ClientEntity;
import it.getinsight.module.client.entity.ClientStatus;
import it.getinsight.module.client.mapper.ClientFullResponseMapper;
import it.getinsight.module.client.mapper.ClientMapper;
import it.getinsight.module.client.mapper.ClientRepresentationMapper;
import it.getinsight.module.client.repository.ClientRepository;
import it.getinsight.module.configuration.dto.AttachmentConfigurationDTO;
import it.getinsight.module.configuration.entity.AttachmentConfigurationEntity;
import it.getinsight.module.configuration.mapper.AttachmentConfigurationMapper;
import it.getinsight.module.configuration.repository.AttachmentConfigurationRepository;
import it.getinsight.module.configuration.service.AttachmentConfigurationService;
import it.getinsight.module.keycloak.dto.ClientRepresentationDTO;
import it.getinsight.module.keycloak.service.IdentityProviderService;
import it.getinsight.module.role.entity.RoleEntity;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

import static it.getinsight.message.MessageProperty.*;


@Service
@RequiredArgsConstructor
@Slf4j
public class ClientService {

    private final ClientRepository clientRepository;
    private final ClientMapper clientMapper;
    private final ClientRepresentationMapper clientRepresentationMapper;
    private final IdentityProviderService identityProviderService;
    private final ClientSynchronizationService clientSynchronizationService;
    private final ClientFullResponseMapper clientFullResponseMapper;
    private final AttachmentConfigurationRepository attachmentConfigurationRepository;
    private final AttachmentConfigurationMapper attachmentConfigurationMapper;
    private final AttachmentConfigurationService attachmentConfigurationService;

    private final ClientValidationService clientValidationService;
    private final ClientQueryService clientQueryService;


    public List<ClientDTO> getAllClientsDynamicQuery() {
        return clientQueryService.getAllClientsDynamicQuery();
    }

    public List<ClientDTO> getAllClientsPublished() {
        return clientQueryService.getAllClientsPublished();
    }

    public PageableResponseModel<ClientDTO> getAllClientsPageable(PageableRequestModel<ClientFilterDTO> configPage) {
        return clientQueryService.getAllClientsPageable(configPage);
    }

    @Cacheable(value = "clients", key = "#id")
    public ClientFullResponseDTO findById(Long id) {
        var entity = clientRepository.findById(id).orElseThrow(CLIENT_NOT_FOUND_ERROR::resourceNotFoundException);
        return clientFullResponseMapper.toDto(entity);
    }

    @Cacheable(value = "clients", key = "#clientId")
    public ClientDTO findByClientId(String clientId) {
        var entity = clientRepository.findByClientId(clientId).orElseThrow(CLIENT_NOT_FOUND_ERROR::resourceNotFoundException);
        return clientMapper.toDto(entity);
    }


    @Transactional(propagation = Propagation.REQUIRED)
    @CacheEvict(value = "clients", allEntries = true)
    public void synchronizationClients(List<String> clientIds) {
        clientSynchronizationService.synchronizeClients(clientIds);
    }

    @CacheEvict(value = "clients", allEntries = true)
    public ClientEntity synchronize(ClientRepresentationDTO client) {
        return clientSynchronizationService.synchronizeClient(client);
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public ClientDTO create(ClientDTO dto) {
        var entity = clientMapper.toEntity(dto);
        var opConfigurations = Optional.ofNullable(entity.getConfigurations()).filter(CollectionUtils::isNotEmpty);
        entity.setClientId(dto.clientId().toLowerCase());

        clientValidationService.validateClientIdNotExists(entity.getClientId(), clientRepository);

        if (opConfigurations.isPresent()){
            entity.getConfigurations().forEach(o -> o.setClient(entity));
            clientValidationService.validateAttachmentConfigurations(entity.getConfigurations());
        }

        var clientEntity = clientRepository.save(entity);
        opConfigurations.ifPresent(attachmentConfigurationRepository::saveAll);
        if (Boolean.TRUE.equals(dto.managed())) {
            return handleManagedClient(entity);
        }
        return clientMapper.toDto(clientEntity);
    }


    private ClientDTO handleManagedClient(ClientEntity entity) {
        var existingClients = identityProviderService.getClientsByClientId(entity.getClientId());
        final boolean hasClient = !existingClients.isEmpty();
        if (hasClient) {
            var clientRepresentationDTO = existingClients.getFirst();
            return clientMapper.toDto(synchronize(clientRepresentationDTO));
        } else {
            var defaultClient = ClientRepresentationDTO.createDefault(entity.getClientId(), entity.getDescription(), entity.getBaseUrl());
            identityProviderService.createClient(defaultClient);
            return clientMapper.toDto(synchronize(identityProviderService.getClientsByClientId(entity.getClientId()).getFirst()));
        }
    }



    @CacheEvict(value = "clients", allEntries = true)
    public void updateManaged(Long id, ClientDTO clientUpdatedDTO) {
        var entityUpdated = clientRepository.findById(id)
            .orElseThrow(CLIENT_NOT_FOUND_ERROR::resourceNotFoundException);

        clientMapper.fromDtoWithoutImmutableFields(clientUpdatedDTO, entityUpdated);

        if (BooleanUtils.isTrue(clientUpdatedDTO.managed())) {
            if (StringUtils.isBlank(entityUpdated.getClientUUID())) {
                handleManagedClient(entityUpdated);
            } else {
                var clients = identityProviderService.getClientsByClientId(entityUpdated.getClientId());
                if (clients.isEmpty()) {
                    log.error("Managed client not found in IDP: {}", entityUpdated.getClientId());
                    throw CLIENT_NOT_FOUND_IN_IDP.businessException();
                }
                var client = clients.getFirst();
                log.debug("Updating managed client in IDP: {}", client.getId());


                if (!client.getId().equals(entityUpdated.getClientUUID())) {
                    log.warn("Updating clientUUID for client {} from '{}' to '{}'", entityUpdated.getClientId(), entityUpdated.getClientUUID(), client.getId());
                    entityUpdated.setClientUUID(client.getId());
                }

                clientRepresentationMapper.toDto(entityUpdated, client);
                Optional.of(entityUpdated)
                    .map(clientMapper::toDto)
                    .ifPresent(o -> clientRepresentationMapper.fromDtoRepresentation(o, client));
                identityProviderService.updateClient(client.getId(), client);
                handleManagedClient(entityUpdated);
            }
        }

        processAttachmentConfigurations(clientUpdatedDTO.configurations(), entityUpdated);
    }

    private void processAttachmentConfigurations(List<AttachmentConfigurationDTO> configurations, ClientEntity entityUpdated) {
        if (CollectionUtils.isNotEmpty(configurations)) {
            var attachmentConfigurationEntities = configurations.stream()
                .map(configuration -> {
                    AttachmentConfigurationEntity entity = (configuration.id() != null)
                        ? attachmentConfigurationRepository.findById(configuration.id())
                        .map(existing -> {
                            attachmentConfigurationMapper.fromDto(configuration, existing);
                            return existing;
                        })
                        .orElseThrow(ERROR_CONFIGURATION_NOT_FOUND::resourceNotFoundException)
                        : attachmentConfigurationMapper.toEntity(configuration);

                    entity.setClient(entityUpdated);
                    return entity;
                })
                .toList();

            clientValidationService.validateAttachmentConfigurations(attachmentConfigurationEntities);
            attachmentConfigurationRepository.saveAll(attachmentConfigurationEntities);
        }
    }


    @Cacheable(value = "getTotalClients")
    public long getTotalClients() {
        return clientRepository.count();
    }

    @Cacheable(value = "getTotalClients", key = "#roles")
    public long getTotalClients(List<RoleEntity> roles) {
        return roles.stream().map(RoleEntity::getClient).distinct().count();
    }

    @CacheEvict(value = "clients", allEntries = true)
    public ClientDTO update(Long id, String status) {
        var entity = clientRepository.findById(id).orElseThrow(CLIENT_NOT_FOUND_ERROR::resourceNotFoundException);
        clientValidationService.validateStatusTransition(entity, status);
        entity.setStatus(ClientStatus.valueOf(status));
        return clientMapper.toDto(clientRepository.save(entity));
    }

    public List<ClientDTO> getAssociateClients(Boolean attached) {
        return clientQueryService.getAssociateClients(attached);
    }


    public List<AttachmentConfigurationDTO> previewAttachmentConfiguration(MultipartFile file) {
            return attachmentConfigurationService.previewAttachmentConfiguration(file);
    }


    public byte[] exportAttachmentConfigurations(Long clientId) {
        var client = clientRepository.findById(clientId).orElseThrow(CLIENT_NOT_FOUND_ERROR::resourceNotFoundException);
        return attachmentConfigurationService.toCsv(client.getConfigurations());
    }
}
