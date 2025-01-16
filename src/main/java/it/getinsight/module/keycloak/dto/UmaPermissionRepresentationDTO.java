package it.getinsight.module.keycloak.dto;

import java.util.List;

public record UmaPermissionRepresentationDTO(String type, List<RoleRepresentationDTO> roles,
                                             List<GroupRepresentationDTO> groups, List<ClientRepresentationDTO> clients,
                                             List<UserRepresentationDTO> users, String condition) {
}
