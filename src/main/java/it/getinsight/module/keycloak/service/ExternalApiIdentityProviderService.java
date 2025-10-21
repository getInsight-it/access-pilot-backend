package it.getinsight.module.keycloak.service;

import it.getinsight.module.keycloak.dto.ClientRepresentationDTO;
import it.getinsight.module.keycloak.dto.RoleRepresentationDTO;
import it.getinsight.module.keycloak.dto.UserRepresentationDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
@ConditionalOnProperty(name = "identity.provider", havingValue = "external-api")
@RequiredArgsConstructor
@Slf4j
public class ExternalApiIdentityProviderService implements IdentityProviderService {


    @Override
    public List<UserRepresentationDTO> getUsersByClientUUIDAndRoleName(String clientUUID, String roleName) {
        log.debug("External API: Getting users by client UUID {} and role {}", clientUUID, roleName);
        throw new UnsupportedOperationException("External API provider not yet implemented");
    }

    @Override
    public RoleRepresentationDTO getRoleByNameAndClientUUID(String roleName, String clientUUID) {
        log.debug("External API: Getting role {} for client {}", roleName, clientUUID);
        throw new UnsupportedOperationException("External API provider not yet implemented");
    }

    @Override
    public void assignRoles(String userId, String clientUUID, List<RoleRepresentationDTO> roleNames) {
        log.debug("External API: Assigning roles {} at client {} to user {}", roleNames, clientUUID, userId);
        throw new UnsupportedOperationException("External API provider not yet implemented");
    }

    @Override
    public List<UserRepresentationDTO> getUsers(Map<String, String> searchCriteria) {
        log.debug("External API: Getting users with criteria: {}", searchCriteria);
        throw new UnsupportedOperationException("External API provider not yet implemented");
    }

    @Override
    public void updateUser(String userId, UserRepresentationDTO userData) {
        log.debug("External API: Updating user {} with data: {}", userId, userData);
        throw new UnsupportedOperationException("External API provider not yet implemented");
    }

    @Override
    public List<RoleRepresentationDTO> getUserRoles(String userId) {
        log.debug("External API: Getting roles for user {}", userId);
        throw new UnsupportedOperationException("External API provider not yet implemented");
    }

    @Override
    public boolean hasRole(String userId, String roleName) {
        log.debug("External API: Checking if user {} has role {}", userId, roleName);
        throw new UnsupportedOperationException("External API provider not yet implemented");
    }

    @Override
    public List<UserRepresentationDTO> getAllUsersByClient(String clientUUID) {
        log.debug("External API: Getting all users for client {}", clientUUID);
        throw new UnsupportedOperationException("External API provider not yet implemented");
    }

    @Override
    public List<RoleRepresentationDTO> getAllRolesByClient(String clientUUID) {
        log.debug("External API: Getting all roles for client {}", clientUUID);
        throw new UnsupportedOperationException("External API provider not yet implemented");
    }


    @Override
    public List<ClientRepresentationDTO> getClients() {
        throw new UnsupportedOperationException("External API provider not yet implemented");
    }

    @Override
    public List<RoleRepresentationDTO> getRolesByClientUUID(String clientUUID) {
        throw new UnsupportedOperationException("External API provider not yet implemented");
    }

    @Override
    public ClientRepresentationDTO getClientByUUID(String clientUUID) {
        throw new UnsupportedOperationException("External API provider not yet implemented");
    }

    @Override
    public List<ClientRepresentationDTO> getClientsByClientId(String clientId) {
        throw new UnsupportedOperationException("External API provider not yet implemented");
    }

    @Override
    public RoleRepresentationDTO getRole(String clientUUID, String roleName) {
        throw new UnsupportedOperationException("External API provider not yet implemented");
    }

    @Override
    public void createClient(ClientRepresentationDTO client) {
        throw new UnsupportedOperationException("External API provider not yet implemented");
    }

    @Override
    public void updateClient(String clientUUID, ClientRepresentationDTO client) {
        throw new UnsupportedOperationException("External API provider not yet implemented");
    }

    @Override
    public void createRole(String clientUUID, RoleRepresentationDTO role) {
        throw new UnsupportedOperationException("External API provider not yet implemented");
    }

    @Override
    public void updateRole(String clientUUID, String roleName, RoleRepresentationDTO role) {
        throw new UnsupportedOperationException("External API provider not yet implemented");
    }

    @Override
    public void deleteRole(String clientUUID, String roleName) {
        throw new UnsupportedOperationException("External API provider not yet implemented");
    }
}

