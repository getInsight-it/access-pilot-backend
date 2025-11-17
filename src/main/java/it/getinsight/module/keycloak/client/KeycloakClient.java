package it.getinsight.module.keycloak.client;


import io.github.resilience4j.retry.annotation.Retry;
import it.getinsight.config.FeignConfiguration;
import it.getinsight.module.keycloak.dto.ClientRepresentationDTO;
import it.getinsight.module.keycloak.dto.RoleRepresentationDTO;
import it.getinsight.module.keycloak.dto.UserRepresentationDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@FeignClient(name = "keycloakClient", url = "${feign.client.keycloak.url}", configuration = FeignConfiguration.FeignConfigurationToken.class)
public interface KeycloakClient {

    @GetMapping("/users/{id}")
    @Retry(name = "keycloak")
    UserRepresentationDTO getUsers(@PathVariable String id);

    @GetMapping("/clients/{clientUUID}/roles/{roleName}/users")
    @Retry(name = "keycloak")
    List<UserRepresentationDTO> getUsersByClientUUIDAndRoleName(@PathVariable String clientUUID,@PathVariable String roleName);

    @PostMapping("/users/{id}/role-mappings/clients/{clientUUID}")
    void assignRoles(@PathVariable String id, @PathVariable String clientUUID, @RequestBody List<RoleRepresentationDTO> roles);

    @DeleteMapping("/users/{id}/role-mappings/clients/{clientUUID}")
    void removeRoles(@PathVariable String id, @PathVariable String clientUUID, @RequestBody List<RoleRepresentationDTO> roles);

    @PostMapping("/users/{id}/logout")
    void logoutUser(@PathVariable String id);

    @GetMapping("/clients/{clientUUID}/roles/{roleName}")
    @Retry(name = "keycloak")
    RoleRepresentationDTO getRole(@PathVariable String clientUUID, @PathVariable String roleName);

    @GetMapping("/clients/{clientUUID}/roles/{roleName}")
    @Retry(name = "keycloak")
    RoleRepresentationDTO getRoleByNameAndClientUUID(@PathVariable String roleName, @PathVariable String clientUUID);

    @GetMapping("/clients/{clientUUID}/roles")
    @Retry(name = "keycloak")
    List<RoleRepresentationDTO> getRolesByClientUUID(@PathVariable String clientUUID);


    @GetMapping("/clients/{clientUUID}")
    @Retry(name = "keycloak")
    ClientRepresentationDTO getClientByClientUUID(@PathVariable String clientUUID);

    @GetMapping("/clients")
    @Retry(name = "keycloak")
    List<ClientRepresentationDTO> getClients();

    @GetMapping("/clients")
    @Retry(name = "keycloak")
    List<ClientRepresentationDTO> getClientsByClientId(@RequestParam String clientId);

    @GetMapping("/users/count")
    @Retry(name = "keycloak")
    Long getTotalUsersByEnabled(@RequestParam Boolean enabled);

    @GetMapping("/users/{id}/role-mappings")
    @Retry(name = "keycloak")
    Map<String, List<RoleRepresentationDTO>> getUserRoles(@PathVariable("id") String userId);

    @PutMapping("/users/{id}")
    void updateUser(@PathVariable String id, @RequestBody UserRepresentationDTO userRepresentationDTO);

    @PutMapping("/clients/{clientUUID}")
    void updateClient(@PathVariable String clientUUID, @RequestBody ClientRepresentationDTO clientRepresentationDTO);

    @PostMapping("/clients")
    void createClient(@RequestBody ClientRepresentationDTO clientRepresentationDTO);

    @PostMapping("/clients/{clientUUID}/roles")
    void createRole(@PathVariable String clientUUID, @RequestBody RoleRepresentationDTO roleRepresentationDTO);

    @PutMapping("/clients/{clientUUID}/roles/{roleName}")
    void updateRole(@PathVariable String clientUUID, @PathVariable String roleName, @RequestBody RoleRepresentationDTO roleRepresentationDTO);

    @DeleteMapping("/clients/{clientUUID}/roles/{roleName}")
    void deleteRole(@PathVariable String clientUUID, @PathVariable String roleName);

}
