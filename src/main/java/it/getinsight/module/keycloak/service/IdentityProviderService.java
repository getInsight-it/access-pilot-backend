package it.getinsight.module.keycloak.service;

import it.getinsight.module.keycloak.dto.ClientRepresentationDTO;
import it.getinsight.module.keycloak.dto.RoleRepresentationDTO;
import it.getinsight.module.keycloak.dto.UserRepresentationDTO;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public interface IdentityProviderService {


    List<UserRepresentationDTO> getUsersByClientUUIDAndRoleName(String clientUUID, String roleName);

    List<UserRepresentationDTO> getUsers(Map<String, String> searchCriteria);

    void updateUser(String userId, UserRepresentationDTO userData);

    List<UserRepresentationDTO> getAllUsersByClient(String clientUUID);


    RoleRepresentationDTO getRoleByNameAndClientUUID(String roleName, String clientUUID);

    void assignRoles(String userId, String clientUUID, List<RoleRepresentationDTO> roleNames);

    void removeRoles(String userId, String clientUUID, List<RoleRepresentationDTO> roles);

    void logoutUser(String userId);

    List<RoleRepresentationDTO> getUserRoles(String userId);


    boolean hasRole(String userId, String roleName);

    List<RoleRepresentationDTO> getAllRolesByClient(String clientUUID);

    List<RoleRepresentationDTO> getRolesByClientUUID(String clientUUID);

    RoleRepresentationDTO getRole(String clientUUID, String roleName);

    void createRole(String clientUUID, RoleRepresentationDTO role);

    void updateRole(String clientUUID, String roleName, RoleRepresentationDTO role);

    void deleteRole(String clientUUID, String roleName);

    List<ClientRepresentationDTO> getClients();

    ClientRepresentationDTO getClientByUUID(String clientUUID);

    List<ClientRepresentationDTO> getClientsByClientId(String clientId);

    void createClient(ClientRepresentationDTO client);

    void updateClient(String clientUUID, ClientRepresentationDTO client);
}

