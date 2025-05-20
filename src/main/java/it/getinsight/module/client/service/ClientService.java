package it.getinsight.module.client.service;

import it.getinsight.core.dynamicquery.parameters.DynamicParameters;
import it.getinsight.core.exception.BusinessException;
import it.getinsight.core.helper.PaginationHelper;
import it.getinsight.core.pagination.PageableRequestModel;
import it.getinsight.core.pagination.PageableResponseModel;
import it.getinsight.module.client.dto.ClientDTO;
import it.getinsight.module.client.dto.ClientFilterDTO;
import it.getinsight.module.client.dto.ClientFullResponseDTO;
import it.getinsight.module.client.entity.ClientEntity;
import it.getinsight.module.client.entity.ClientStatus;
import it.getinsight.module.client.mapper.ClientFilterMapper;
import it.getinsight.module.client.mapper.ClientFullResponseMapper;
import it.getinsight.module.client.mapper.ClientMapper;
import it.getinsight.module.client.mapper.ClientRepresentationMapper;
import it.getinsight.module.client.repository.ClientRepository;
import it.getinsight.module.configuration.dto.AttachmentConfigurationDTO;
import it.getinsight.module.configuration.entity.AttachmentConfigurationEntity;
import it.getinsight.module.configuration.mapper.AttachmentConfigurationMapper;
import it.getinsight.module.configuration.repository.AttachmentConfigurationRepository;
import it.getinsight.module.configuration.service.AttachmentConfigurationService;
import it.getinsight.module.keycloak.client.KeycloakClient;
import it.getinsight.module.keycloak.config.KeycloakProperties;
import it.getinsight.module.keycloak.dto.ClientRepresentationDTO;
import it.getinsight.module.role.entity.RoleEntity;
import it.getinsight.module.role.service.RoleService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;
import org.springframework.boot.actuate.web.mappings.MappingsEndpoint;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static it.getinsight.message.MessageProperty.*;


@Service
@RequiredArgsConstructor
@Slf4j
public class ClientService {

    public static final String IDP_KEYCLOAK_NAME_ACL_CLIENT_MANAGED = "acl.client.managed";
    private final ClientRepository clientRepository;
    private final ClientMapper clientMapper;
    private final ClientFilterMapper clientFilterMapper;
    private final RoleService roleService;
    private final ClientRepresentationMapper clientRepresentationMapper;
    private final KeycloakClient keycloakClient;
    private static final String NAME_QUERY_FIND_ALL_CLIENTS = "find-all-clients";
    private final KeycloakProperties keycloakProperties;
    private final MappingsEndpoint mappingsEndpoint;
    private final ClientFullResponseMapper clientFullResponseMapper;
    private final AttachmentConfigurationRepository attachmentConfigurationRepository;
    private final AttachmentConfigurationMapper attachmentConfigurationMapper;
    private final AttachmentConfigurationService attachmentConfigurationService;


    public List<ClientDTO> getAllClientsDynamicQuery() {
        final var parameters = DynamicParameters.get();
        return clientRepository.findAllNative(NAME_QUERY_FIND_ALL_CLIENTS, parameters, clientMapper);
    }

    public List<ClientDTO> getAllClientsPublished() {
        return clientRepository.findAllByStatus(ClientStatus.PUBLISHED).stream().map(clientMapper::toDto).toList();
    }


//    @Cacheable(value = "clients", key = "#configPage.toString()")
    public PageableResponseModel<ClientDTO> getAllClientsPageable(PageableRequestModel<ClientFilterDTO> configPage) {
        final var model = configPage
            .getFilter()
            .map(clientFilterMapper::toDto)
            .map(clientMapper::toEntity)
            .orElse(new ClientEntity());


        final var matcher = ExampleMatcher
            .matchingAny()
            .withIgnoreNullValues()
            .withMatcher("clientId", ExampleMatcher.GenericPropertyMatcher::contains)
            .withMatcher("description", ExampleMatcher.GenericPropertyMatcher::contains);

        final var example = Example.of(model, matcher);
        final var page = clientRepository.findAll(example, PaginationHelper.toPageable(configPage));
        final var clientsNotSynchronized = page.getContent().stream().filter(o -> o.getClientUUID() == null).map(clientMapper::toDto).toList();
        final var clientsSynchronized = fetchUpdatedClientFromIDP(page.getContent());
        final var dtos = Stream.concat(clientsNotSynchronized.stream(), clientsSynchronized.stream()).toList();
        return PaginationHelper.toPageResponse(dtos, page.getTotalElements());
    }

    @NotNull
    private List<ClientDTO> fetchUpdatedClientFromIDP(List<ClientEntity> page) {
        return page.stream()
            .filter(o -> o.getClientUUID() != null).map(o -> {
                try {
                    return keycloakClient.getClientByClientUUID(o.getClientUUID());
                } catch (Exception e) {
                    return null;
                }
            }).filter(Objects::nonNull)
            .map(obj -> clientRepresentationMapper.toDto(page.stream().filter(e -> Objects.equals(e.getClientUUID(), obj.getId()))
                .findFirst().orElse(null), obj)).toList();
    }

    @Cacheable(value = "clients", key = "#id")
    public ClientFullResponseDTO findById(Long id) {
        var entity = clientRepository.findById(id).orElseThrow(CLIENT_NOT_FOUND_ERROR::businessException);
        return clientFullResponseMapper.toDto(entity);
    }

    @Cacheable(value = "clients", key = "#clientId")
    public ClientDTO findByClientId(String clientId) {
        var entity = clientRepository.findByClientId(clientId).orElseThrow(CLIENT_NOT_FOUND_ERROR::businessException);
        return clientMapper.toDto(entity);
    }


    @Transactional(propagation = Propagation.REQUIRED)
    @CacheEvict(value = "clients", allEntries = true)
    public void synchronizationClients(List<String> clientIds) {
        if (CollectionUtils.isEmpty(clientIds)) return;
        final var searchableClientIds = clientIds.stream().map(String::trim).map(String::toLowerCase).filter(o -> !keycloakProperties.getIgnoreClients().contains(o)).toList();
        var clientEntities = clientRepository.findAllByClientIdIn(searchableClientIds);
        var clients = keycloakClient.getClients().stream()
            .filter(client -> client.getAttributes().containsKey(IDP_KEYCLOAK_NAME_ACL_CLIENT_MANAGED) && client.getAttributes().get(IDP_KEYCLOAK_NAME_ACL_CLIENT_MANAGED).equals("true"))
            .filter(obj -> clientEntities.stream().anyMatch(c -> Objects.equals(c.getClientId(), obj.getClientId()))).toList();
        clients.forEach(this::synchronize);
    }
    //@Transactional(propagation = Propagation.REQUIRED)
    @CacheEvict(value = "clients", allEntries = true)
    public ClientEntity synchronize(ClientRepresentationDTO client) {
        var entity = clientRepository.findByClientId(client.getClientId()).orElse(new ClientEntity());
        entity.setClientUUID(client.getId());
        entity.setDescription(client.getDescription());
        entity.setClientId(client.getClientId());
        entity.setManaged("true".equals(client.getAttributes().get(IDP_KEYCLOAK_NAME_ACL_CLIENT_MANAGED)));
        entity.setBaseUrl(client.getBaseUrl());
        var clientEntity = clientRepository.save(entity);
        roleService.synchronizeRoles(Collections.singletonList(client.getClientId()));
        return clientEntity;
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public ClientDTO create(ClientDTO dto) {
        var entity = clientMapper.toEntity(dto);
        var opConfigurations = Optional.ofNullable(entity.getConfigurations()).filter(CollectionUtils::isNotEmpty);
        entity.setClientId(dto.clientId().toLowerCase());
        if (clientRepository.existsByClientId(entity.getClientId())) {
            throw CLIENT_NOT_FOUND_ERROR.businessException();
        }
        if (opConfigurations.isPresent()){
            entity.getConfigurations().forEach(o -> o.setClient(entity));
            validateAttachment(entity.getConfigurations());
        }

        var clientEntity = clientRepository.save(entity);
        opConfigurations.ifPresent(attachmentConfigurationRepository::saveAll);
        if (Boolean.TRUE.equals(dto.managed())) {
            return handleManagedClient(entity);
        }
        return clientMapper.toDto(clientEntity);
    }

    private static void validateAttachment(List<AttachmentConfigurationEntity> configurations) {
        Optional.ofNullable(configurations)
            .orElse(Collections.emptyList())
            .stream()
            .collect(Collectors.groupingBy(AttachmentConfigurationEntity::getName))
            .forEach((nome, lista) -> {
                long actives = lista.stream().filter(AttachmentConfigurationEntity::getActive).count();
                if (actives > 1) {
                    throw ATTACHMENTS_MULTIPLE_ACTIVE_ERROR.bind(nome).businessException();
                }
            });
    }


    private ClientDTO handleManagedClient(ClientEntity entity) {
        var existingClients = keycloakClient.getClientsByClientId(entity.getClientId());
        final boolean hasClient = !existingClients.isEmpty();
        if (hasClient) {
            var clientRepresentationDTO = existingClients.getFirst();
            return clientMapper.toDto(synchronize(clientRepresentationDTO));
        } else {
            var defaultClient = ClientRepresentationDTO.createDefault(entity.getClientId(), entity.getDescription(), entity.getBaseUrl());
            keycloakClient.createClient(defaultClient);
            return clientMapper.toDto(synchronize(keycloakClient.getClientsByClientId(entity.getClientId()).getFirst()));
        }
    }



    @CacheEvict(value = "clients", allEntries = true)
    public void updateManaged(Long id, ClientDTO clientUpdatedDTO) {
        var entityUpdated = clientRepository.findById(id).orElseThrow(CLIENT_NOT_FOUND_ERROR::businessException);
        if (BooleanUtils.isTrue(clientUpdatedDTO.managed()) && StringUtils.isBlank(entityUpdated.getClientUUID())) {
            handleManagedClient(entityUpdated);
            return;
        }
        var client = keycloakClient.getClientsByClientId(entityUpdated.getClientId()).getFirst();
        clientMapper.fromDtoWithoutImmutableFields(clientUpdatedDTO, entityUpdated);
        entityUpdated.setClientUUID(client.getId());
        clientRepresentationMapper.toDto(entityUpdated, client);
        Optional.of(entityUpdated).map(clientMapper::toDto).ifPresent(o -> clientRepresentationMapper.fromDtoRepresentation(o, client));
        keycloakClient.updateClient(client.getId(), client);
        handleManagedClient(entityUpdated);
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
                        .orElseThrow(ERROR_CONFIGURATION_NOT_FOUND::businessException)
                        : attachmentConfigurationMapper.toEntity(configuration);

                    entity.setClient(entityUpdated);
                    return entity;
                })
                .toList();

            validateAttachment(attachmentConfigurationEntities);
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
        var entity = clientRepository.findById(id).orElseThrow(CLIENT_NOT_FOUND_ERROR::businessException);
        validateStatus(entity, status);
        entity.setStatus(ClientStatus.valueOf(status));
        return clientMapper.toDto(clientRepository.save(entity));
    }

    private static void validateStatus( ClientEntity entity, String status) {
        if (Arrays.stream(ClientStatus.values()).noneMatch(o -> StringUtils.equalsIgnoreCase(o.name(), status))) {
            throw new BusinessException("Invalid status");
        }
        if (ClientStatus.valueOf(status).equals(entity.getStatus())) {
            throw new BusinessException("Client already %s" .formatted(status));
        }
        if(BooleanUtils.isFalse(entity.getManaged()) && ClientStatus.PUBLISHED.name().equals(status)){
            throw new BusinessException("Managed clients cannot be published");
        }
    }

    public List<ClientDTO> getAssociateClients(Boolean attached) {
        var principal = (Jwt) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Map<String, List<String>> resourceAccess = principal.getClaim("resource_access");

        var clientEntities = clientRepository.findAllByManagedAndStatus(true, ClientStatus.PUBLISHED);
        return keycloakClient.getClients().stream()
            .filter(client -> client.getAttributes().containsKey(IDP_KEYCLOAK_NAME_ACL_CLIENT_MANAGED) && client.getAttributes().get(IDP_KEYCLOAK_NAME_ACL_CLIENT_MANAGED).equals("true"))
            .map(obj -> clientEntities.stream().filter(c -> Objects.equals(c.getClientId(), obj.getClientId())).findFirst().orElse(null))
            .filter(Objects::nonNull)
            .filter(obj -> BooleanUtils.isTrue(attached)  ? resourceAccess.entrySet().stream().anyMatch(e -> Objects.equals(e.getKey(), obj.getClientId())) : resourceAccess.entrySet().stream().noneMatch(e -> Objects.equals(e.getKey(), obj.getClientId())) )
            .map(clientMapper::toDto)
            .toList();
    }


    public List<AttachmentConfigurationDTO> previewAttachmentConfiguration(MultipartFile file) {
            return attachmentConfigurationService.previewAttachmentConfiguration(file);
    }


    public byte[] exportAttachmentConfigurations(Long clientId) {
        var client = clientRepository.findById(clientId).orElseThrow(CLIENT_NOT_FOUND_ERROR::businessException);
        return attachmentConfigurationService.toCsv(client.getConfigurations());
    }
}
