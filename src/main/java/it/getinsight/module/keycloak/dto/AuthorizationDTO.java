package it.getinsight.module.keycloak.dto;

import java.util.List;

public record AuthorizationDTO(List<PermissionDTO> permissions) {
}
