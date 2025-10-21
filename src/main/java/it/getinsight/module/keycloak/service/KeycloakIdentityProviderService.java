package it.getinsight.module.keycloak.service;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import it.getinsight.module.keycloak.client.KeycloakClient;
import it.getinsight.module.keycloak.dto.ClientRepresentationDTO;
import it.getinsight.module.keycloak.dto.RoleRepresentationDTO;
import it.getinsight.module.keycloak.dto.UserRepresentationDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Map;

@Service
@ConditionalOnProperty(name = "identity.provider", havingValue = "keycloak", matchIfMissing = true)
@Primary
@RequiredArgsConstructor
@Slf4j
public class KeycloakIdentityProviderService implements IdentityProviderService {

    private final KeycloakClient keycloakClient;

    @Override
    @CircuitBreaker(name = "keycloak", fallbackMethod = "getUsersByClientUUIDAndRoleNameFallback")
    @Retry(name = "keycloak")
    public List<UserRepresentationDTO> getUsersByClientUUIDAndRoleName(String clientUUID, String roleName) {
        log.debug("Getting users by client UUID {} and role {}", clientUUID, roleName);
        return keycloakClient.getUsersByClientUUIDAndRoleName(clientUUID, roleName);
    }

    private List<UserRepresentationDTO> getUsersByClientUUIDAndRoleNameFallback(String clientUUID, String roleName, Exception e) {
        log.error("Failed to fetch users for client {} and role {} after retries, returning empty list", clientUUID, roleName, e);
        return Collections.emptyList();
    }

    @Override
    @CircuitBreaker(name = "keycloak", fallbackMethod = "getRoleByNameAndClientUUIDFallback")
    @Retry(name = "keycloak")
    public RoleRepresentationDTO getRoleByNameAndClientUUID(String roleName, String clientUUID) {
        log.debug("Getting role {} for client {}", roleName, clientUUID);
        return keycloakClient.getRoleByNameAndClientUUID(roleName, clientUUID);
    }

    private RoleRepresentationDTO getRoleByNameAndClientUUIDFallback(String roleName, String clientUUID, Exception e) {
        log.error("Failed to fetch role {} for client {} after retries", roleName, clientUUID, e);
        return null;
    }

    @Override
    public void assignRoles(String userId, String clientUUID, List<RoleRepresentationDTO> roles) {
        log.debug("Assigning roles {} to user {}", roles, userId);
        keycloakClient.assignRoles(userId, clientUUID, roles);
    }

    @Override
    @CircuitBreaker(name = "keycloak", fallbackMethod = "getUsersFallback")
    @Retry(name = "keycloak")
    public List<UserRepresentationDTO> getUsers(Map<String, String> searchCriteria) {
        log.debug("Getting users with criteria: {}", searchCriteria);
        String userId = searchCriteria.values().iterator().next();
        return List.of(keycloakClient.getUsers(userId));
    }

    private List<UserRepresentationDTO> getUsersFallback(Map<String, String> searchCriteria, Exception e) {
        log.error("Failed to fetch users with criteria {} after retries, returning empty list", searchCriteria, e);
        return Collections.emptyList();
    }

    @Override
    public void updateUser(String userId, UserRepresentationDTO userData) {
        log.debug("Updating user {} with data: {}", userId, userData);
        keycloakClient.updateUser(userId, userData);
    }

    @Override
    public List<RoleRepresentationDTO> getUserRoles(String userId) {
        log.debug("Getting roles for user {}", userId);
        var rolesMap = keycloakClient.getUserRoles(userId);
        return rolesMap.getOrDefault("roles", List.of());
    }

    @Override
    public boolean hasRole(String userId, String roleName) {
        log.debug("Checking if user {} has role {}", userId, roleName);
        return false;
    }

    @Override
    public List<UserRepresentationDTO> getAllUsersByClient(String clientUUID) {
        log.debug("Getting all users for client {}", clientUUID);
        return List.of();
    }

    @Override
    public List<RoleRepresentationDTO> getAllRolesByClient(String clientUUID) {
        log.debug("Getting all roles for client {}", clientUUID);
        return List.of();
    }


    @Override
    public List<ClientRepresentationDTO> getClients() {
        log.debug("Keycloak: Getting all clients");
        return keycloakClient.getClients();
    }

    @Override
    public List<RoleRepresentationDTO> getRolesByClientUUID(String clientUUID) {
        log.debug("Keycloak: Getting roles for client UUID: {}", clientUUID);
        return keycloakClient.getRolesByClientUUID(clientUUID);
    }

    @Override
    public ClientRepresentationDTO getClientByUUID(String clientUUID) {
        log.debug("Keycloak: Getting client by UUID: {}", clientUUID);
        return keycloakClient.getClientByClientUUID(clientUUID);
    }

    @Override
    public List<ClientRepresentationDTO> getClientsByClientId(String clientId) {
        log.debug("Keycloak: Getting clients by client ID: {}", clientId);
        return keycloakClient.getClientsByClientId(clientId);
    }

    @Override
    public RoleRepresentationDTO getRole(String clientUUID, String roleName) {
        log.debug("Keycloak: Getting role {} for client {}", roleName, clientUUID);
        return keycloakClient.getRole(clientUUID, roleName);
    }

    @Override
    public void createClient(ClientRepresentationDTO client) {
        log.debug("Keycloak: Creating client: {}", client.getClientId());
        keycloakClient.createClient(client);
    }

    @Override
    public void updateClient(String clientUUID, ClientRepresentationDTO client) {
        log.debug("Keycloak: Updating client: {}", clientUUID);
        keycloakClient.updateClient(clientUUID, client);
    }

    @Override
    public void createRole(String clientUUID, RoleRepresentationDTO role) {
        log.debug("Keycloak: Creating role {} for client {}", role.name(), clientUUID);
        keycloakClient.createRole(clientUUID, role);
    }

    @Override
    public void updateRole(String clientUUID, String roleName, RoleRepresentationDTO role) {
        log.debug("Keycloak: Updating role {} for client {}", roleName, clientUUID);
        keycloakClient.updateRole(clientUUID, roleName, role);
    }

    @Override
    public void deleteRole(String clientUUID, String roleName) {
        log.debug("Keycloak: Deleting role {} from client {}", roleName, clientUUID);
        keycloakClient.deleteRole(clientUUID, roleName);
    }
}

