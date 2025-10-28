package it.getinsight.module.client.service;

import it.getinsight.module.client.entity.ClientEntity;
import it.getinsight.module.client.repository.ClientRepository;
import it.getinsight.module.keycloak.config.KeycloakProperties;
import it.getinsight.module.keycloak.dto.ClientRepresentationDTO;
import it.getinsight.module.keycloak.service.IdentityProviderService;
import it.getinsight.module.role.service.RoleSynchronizationService;
import lombok.RequiredArgsConstructor;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class ClientSynchronizationService {

    public static final String IDP_KEYCLOAK_NAME_ACL_CLIENT_MANAGED = "acl.client.managed";
    
    private final ClientRepository clientRepository;
    private final IdentityProviderService identityProviderService;
    private final KeycloakProperties keycloakProperties;
    private final RoleSynchronizationService roleSynchronizationService;

    @Transactional(propagation = Propagation.REQUIRED)
    @CacheEvict(value = "clients", allEntries = true)
    public void synchronizeClients(List<String> clientIds) {
        if (CollectionUtils.isEmpty(clientIds)) {
            return;
        }
        final var searchableClientIds = prepareSearchableClientIds(clientIds);
        var clientEntities = clientRepository.findAllByClientIdIn(searchableClientIds);
        var clientsToSync = fetchManagedClientsFromIDP(clientEntities);
        clientsToSync.forEach(this::synchronizeClient);
    }

    private List<String> prepareSearchableClientIds(List<String> clientIds) {
        return clientIds.stream()
            .map(String::trim)
            .map(String::toLowerCase)
            .filter(id -> !keycloakProperties.getIgnoreClients().contains(id))
            .toList();
    }

    private List<ClientRepresentationDTO> fetchManagedClientsFromIDP(List<ClientEntity> clientEntities) {
        return identityProviderService.getClients().stream()
            .filter(this::isManagedClient)
            .filter(client -> clientEntities.stream()
                .anyMatch(entity -> Objects.equals(entity.getClientId(), client.getClientId())))
            .toList();
    }

    private boolean isManagedClient(ClientRepresentationDTO client) {
        return client.getAttributes().containsKey(IDP_KEYCLOAK_NAME_ACL_CLIENT_MANAGED) 
            && client.getAttributes().get(IDP_KEYCLOAK_NAME_ACL_CLIENT_MANAGED).equals("true");
    }

    @CacheEvict(value = "clients", allEntries = true)
    public ClientEntity synchronizeClient(ClientRepresentationDTO client) {
        var entity = findOrCreateClientEntity(client);
        updateClientEntity(entity, client);
        var savedClient = clientRepository.save(entity);
        roleSynchronizationService.synchronizeRoles(Collections.singletonList(client.getClientId()));
        return savedClient;
    }

    private ClientEntity findOrCreateClientEntity(ClientRepresentationDTO client) {
        return clientRepository.findByClientId(client.getClientId())
            .orElse(new ClientEntity());
    }

    private void updateClientEntity(ClientEntity entity, ClientRepresentationDTO client) {
        entity.setClientUUID(client.getId());
        entity.setDescription(client.getDescription());
        entity.setClientId(client.getClientId());
        entity.setManaged("true".equals(client.getAttributes().get(IDP_KEYCLOAK_NAME_ACL_CLIENT_MANAGED)));
        entity.setBaseUrl(client.getBaseUrl());
    }
}

