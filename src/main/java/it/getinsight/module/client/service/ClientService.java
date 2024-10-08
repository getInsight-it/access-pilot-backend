package it.getinsight.module.client.service;

import com.nimbusds.oauth2.sdk.util.CollectionUtils;
import it.getinsight.core.dynamicquery.parameters.DynamicParameters;
import it.getinsight.core.exception.BusinessException;
import it.getinsight.core.exception.ResourceNotFoundException;
import it.getinsight.core.helper.PaginationHelper;
import it.getinsight.core.pagination.PageableRequestModel;
import it.getinsight.core.pagination.PageableResponseModel;
import it.getinsight.module.client.dto.ClientDTO;
import it.getinsight.module.client.entity.ClientEntity;
import it.getinsight.module.client.mapper.ClientMapper;
import it.getinsight.module.client.mapper.ClientRepresentationMapper;
import it.getinsight.module.client.repository.ClientRepository;
import it.getinsight.module.keycloak.client.KeycloakClient;
import it.getinsight.module.keycloak.config.KeycloakProperties;
import it.getinsight.module.keycloak.dto.ClientRepresentationDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.Set;


@Service
@RequiredArgsConstructor
public class ClientService {

    public static final String IDP_KEYCLOAK_NAME_ACL_CLIENT_MANAGED = "acl.client.managed";
    private final ClientRepository clientRepository;
    private final ClientMapper clientMapper;
    private final ClientRepresentationMapper clientRepresentationMapper;
    private final KeycloakClient keycloakClient;
    private static final String NAME_QUERY_FIND_ALL_CLIENTS = "find-all-clients";
    private final KeycloakProperties keycloakProperties;


    public List<ClientDTO> getAllClientsDynamicQuery() {
        final var parameters = DynamicParameters.get();
        return clientRepository.findAllNative(NAME_QUERY_FIND_ALL_CLIENTS, parameters, clientMapper);
    }

    public PageableResponseModel<ClientDTO> getAllClientsPageable(PageableRequestModel<ClientDTO> configPage) {
        final var model = new ClientEntity();

        final var matcher = ExampleMatcher
            .matchingAll()
            .withIgnoreNullValues()
            .withMatcher("clientId", ExampleMatcher.GenericPropertyMatcher::contains);

        final var example = Example.of(model, matcher);
        final var page = clientRepository.findAll(example, PaginationHelper.toPageable(configPage));
        final var clientsNotSynchronized = page.getContent().stream().filter(o -> o.getClientUUID() == null).map(clientMapper::toDto).toList();
        final var clientsSynchronized = page.getContent().stream().filter(o -> o.getClientUUID() != null).map(o -> keycloakClient.getClientByClientUUID(o.getClientUUID())).map(obj -> clientRepresentationMapper.toDto(page.stream().filter(e -> Objects.equals(e.getClientUUID(), obj.id())).findFirst().orElse(null), obj)).toList();
        final var dtos = Set.of(clientsNotSynchronized, clientsSynchronized).stream().flatMap(List::stream).toList();
        return PaginationHelper.toPageResponse(dtos, page.getTotalElements());
    }

    public PageableResponseModel<ClientDTO> getAllClientsPageableByName(PageableRequestModel<String> configPage) {
        final var model = new ClientEntity();
        configPage.getFilter().ifPresent(model::setClientId);

        final var matcher = ExampleMatcher
            .matching()
            .withStringMatcher(ExampleMatcher.StringMatcher.CONTAINING)
            .withIgnoreNullValues()
            .withIgnoreCase();

        final var example = Example.of(model, matcher);

        final var page = clientRepository.findAll(example, PaginationHelper.toPageable(configPage));
        return PaginationHelper.toPageResponse(clientMapper.toDto(page.getContent()), page.getTotalElements());
    }

    public ClientDTO findById(Long id) {
        var entity = clientRepository.findById(id).orElseThrow(ResourceNotFoundException::new);
        return clientMapper.toDto(entity);
    }


    @Transactional(propagation = Propagation.REQUIRED)
    public void synchronizationClients(List<String> clientIds) {
        if (CollectionUtils.isEmpty(clientIds)) return;
        final var searchableClientIds = clientIds.stream().map(String::trim).map(String::toLowerCase).filter(o -> !keycloakProperties.getIgnoreClients().contains(o)).toList();
        var clientEntities = clientRepository.findAllByClientIdIn(searchableClientIds);
        var clients = keycloakClient.getClients().stream()
            .filter(client -> client.attributes().containsKey(IDP_KEYCLOAK_NAME_ACL_CLIENT_MANAGED) && client.attributes().get(IDP_KEYCLOAK_NAME_ACL_CLIENT_MANAGED).equals("true"))
            .filter(obj -> clientEntities.stream().anyMatch(c -> Objects.equals(c.getClientId(), obj.clientId()))).toList();
        clients.forEach(this::synchronize);
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public ClientEntity synchronize(ClientRepresentationDTO client) {
        var entity = clientRepository.findByClientId(client.clientId()).orElseGet(() -> {
            var newEntity = new ClientEntity();
            newEntity.setClientId(client.clientId());
            newEntity.setDescription(client.description());
            newEntity.setClientUUID(client.id());
            newEntity.setBaseUrl(client.baseUrl());
            return clientRepository.save(newEntity);
        });
        entity.setDescription(client.description());
        entity.setClientUUID(client.id());
        entity.setClientId(client.clientId());
        return clientRepository.save(entity);
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public ClientDTO create(ClientDTO dto) {
        var entity = clientMapper.toEntity(dto);
        entity.setClientId(dto.clientId().toLowerCase());
        if (clientRepository.existsByClientId(entity.getClientId())) {
            throw new BusinessException("Client already exists");
        }

        if (Boolean.TRUE.equals(dto.managed())) {
            return handleManagedClient(entity);
        } else {
            return clientMapper.toDto(clientRepository.save(entity));
        }
    }

    private ClientDTO handleManagedClient(ClientEntity entity) {
        var existingClients = keycloakClient.getClientsByClientId(entity.getClientId());
        if (!existingClients.isEmpty()) {
            var clientRepresentationDTO = existingClients.getFirst();
            synchronize(clientRepresentationDTO);
        } else {
            var defaultClient = ClientRepresentationDTO.createDefault(entity.getClientId(), entity.getDescription(), entity.getBaseUrl());
            keycloakClient.createClient(defaultClient);
            synchronize(keycloakClient.getClientsByClientId(entity.getClientId()).getFirst());
        }
        return clientMapper.toDto(clientRepository.save(entity));
    }


    public void updateManaged(Long id, Boolean managed) {
        var entity = clientRepository.findById(id).orElseThrow(ResourceNotFoundException::new);
        var client = keycloakClient.getClientsByClientId(entity.getClientId()).getFirst();
        client.attributes().put(IDP_KEYCLOAK_NAME_ACL_CLIENT_MANAGED, managed.toString());
        keycloakClient.updateClient(client.id(), client);
        entity.setManaged(managed);
        clientRepository.save(entity);
    }

    public long getTotalClients() {
        return clientRepository.count();
    }
}
