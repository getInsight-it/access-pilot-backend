package it.getinsight.module.keycloak.client;


import it.getinsight.config.FeignConfiguration;
import it.getinsight.module.keycloak.dto.ClientRepresentationDTO;
import it.getinsight.module.keycloak.dto.RoleRepresentationDTO;
import it.getinsight.module.keycloak.dto.UserRepresentationDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@FeignClient(name = "keycloakClient", url = "http://localhost:9080/admin/realms/accesspilot", configuration = FeignConfiguration.FeignConfigurationToken.class)
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
    ClientRepresentationDTO getClient(@PathVariable String clientUUID);

    @GetMapping("/clients")
    List<ClientRepresentationDTO> getClients();

    @GetMapping("/clients")
    List<ClientRepresentationDTO> getClientsByClientId(@RequestParam String clientId);


    @PutMapping("/clients/{clientUUID}")
    void updateClient(@PathVariable String clientUUID, @RequestBody ClientRepresentationDTO clientRepresentationDTO);

}
