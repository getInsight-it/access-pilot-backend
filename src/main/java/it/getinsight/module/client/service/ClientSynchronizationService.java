package it.getinsight.module.client.service;

import it.getinsight.module.client.dto.ClientSyncSummaryDTO;
import it.getinsight.module.client.entity.ClientEntity;
import it.getinsight.module.client.entity.ClientStatus;
import it.getinsight.module.client.repository.ClientRepository;
import it.getinsight.module.keycloak.config.KeycloakProperties;
import it.getinsight.module.keycloak.dto.ClientRepresentationDTO;
import it.getinsight.module.keycloak.service.IdentityProviderService;
import it.getinsight.module.role.service.RoleSynchronizationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
@Slf4j
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

    @Transactional(propagation = Propagation.REQUIRED)
    @CacheEvict(value = {"clients", "getTotalClients"}, allEntries = true)
    public ClientSyncSummaryDTO synchronizeAllClients(boolean syncRoles) {
        long start = System.nanoTime();
        long created = 0;
        long updated = 0;
        long ignored = 0;
        long errors = 0;

        List<ClientRepresentationDTO> clients = identityProviderService.getClients();
        if (CollectionUtils.isEmpty(clients)) {
            long duration = TimeUnit.NANOSECONDS.toMillis(System.nanoTime() - start);
            return new ClientSyncSummaryDTO(created, updated, ignored, errors, duration);
        }

        for (ClientRepresentationDTO client : clients) {
            if (client == null || client.getClientId() == null || client.getClientId().isBlank()) {
                errors++;
                continue;
            }

            String clientId = client.getClientId();
            if (isIgnoredClientId(clientId)) {
                ignored++;
                continue;
            }

            try {
                var existingEntity = clientRepository.findByClientId(client.getClientId());
                synchronizeClientInternal(client, existingEntity.orElse(new ClientEntity()), syncRoles, true);
                if (existingEntity.isEmpty()) {
                    created++;
                } else {
                    updated++;
                }
            } catch (Exception e) {
                errors++;
                log.error("Failed to synchronize client '{}'", clientId, e);
            }
        }

        long duration = TimeUnit.NANOSECONDS.toMillis(System.nanoTime() - start);
        return new ClientSyncSummaryDTO(created, updated, ignored, errors, duration);
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
        var existingEntity = clientRepository.findByClientId(client.getClientId());
        return synchronizeClientInternal(client, existingEntity.orElse(new ClientEntity()), true, false);
    }

    private void updateClientEntity(ClientEntity entity, ClientRepresentationDTO client, boolean forceManaged) {
        entity.setClientUUID(client.getId());
        entity.setDescription(client.getDescription());
        entity.setClientId(client.getClientId());
        if (forceManaged) {
            entity.setManaged(true);
        } else {
            String managedValue = client.getAttributes() != null
                ? client.getAttributes().get(IDP_KEYCLOAK_NAME_ACL_CLIENT_MANAGED)
                : null;
            entity.setManaged("true".equals(managedValue));
        }
        entity.setBaseUrl(client.getBaseUrl());
        if (entity.getStatus() == null) {
            entity.setStatus(ClientStatus.UNPUBLISHED);
        }
        if (entity.getLabel() == null || entity.getLabel().isBlank()) {
            String name = client.getName();
            if (name == null || name.isBlank()) {
                name = client.getClientId();
            }
            entity.setName(name);
            entity.setLabel(name);
        }
    }

    private boolean isIgnoredClientId(String clientId) {
        if (clientId == null) {
            return false;
        }
        List<String> ignoreClients = keycloakProperties.getIgnoreClients();
        if (CollectionUtils.isEmpty(ignoreClients)) {
            return false;
        }
        return ignoreClients.contains(clientId.trim().toLowerCase());
    }

    private ClientEntity synchronizeClientInternal(
        ClientRepresentationDTO client,
        ClientEntity entity,
        boolean syncRoles,
        boolean forceManaged
    ) {
        updateClientEntity(entity, client, forceManaged);
        var savedClient = clientRepository.save(entity);
        if (syncRoles) {
            roleSynchronizationService.synchronizeRoles(Collections.singletonList(client.getClientId()), true);
        }
        return savedClient;
    }
}
