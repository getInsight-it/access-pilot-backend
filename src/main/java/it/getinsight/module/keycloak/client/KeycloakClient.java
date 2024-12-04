package it.getinsight.module.keycloak.client;


import it.getinsight.config.FeignConfiguration;
import it.getinsight.module.keycloak.dto.ClientRepresentationDTO;
import it.getinsight.module.keycloak.dto.RoleRepresentationDTO;
import it.getinsight.module.keycloak.dto.UserRepresentationDTO;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@FeignClient(name = "keycloakClient", url = "${feign.client.keycloak.url}", configuration = FeignConfiguration.FeignConfigurationToken.class)
public interface KeycloakClient {

    @GetMapping("/users/{id}")
    UserRepresentationDTO getUsers(@PathVariable String id);

    @GetMapping("/clients/{clientUUID}/roles/{roleName}/users")
    List<UserRepresentationDTO> getUsersByClientUUIDAndRoleName(@PathVariable String clientUUID,@PathVariable String roleName);

    @PostMapping("/users/{id}/role-mappings/clients/{clientUUID}")
    void assignRoles(@PathVariable String id, @PathVariable String clientUUID, @RequestBody List<RoleRepresentationDTO> roles);

    @GetMapping("/clients/{clientUUID}/roles/{roleName}")
    RoleRepresentationDTO getRole(@PathVariable String clientUUID, @PathVariable String roleName);

    @GetMapping("/clients/{clientUUID}/roles/{roleName}")
    RoleRepresentationDTO getRoleByNameAndClientUUID(@PathVariable String roleName, @PathVariable String clientUUID);

    @GetMapping("/clients/{clientUUID}/roles")
    List<RoleRepresentationDTO> getRolesByClientUUID(@PathVariable String clientUUID);


    @GetMapping("/clients/{clientUUID}")
    @Cacheable(value = "getClientByClientUUID", key = "#clientUUID")
    ClientRepresentationDTO getClientByClientUUID(@PathVariable String clientUUID);

    @GetMapping("/clients")
    List<ClientRepresentationDTO> getClients();

    @GetMapping("/clients")
    @Cacheable(value = "getClientsByClientId", key = "#clientId")
    List<ClientRepresentationDTO> getClientsByClientId(@RequestParam String clientId);

    @GetMapping("/users/count")
    Long getTotalUsersByEnabled(@RequestParam Boolean enabled);

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
