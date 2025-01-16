package it.getinsight.module.keycloak.dto;

import java.util.List;

public record RolePolicyRepresentationDTO(String type, List<RoleRepresentationDTO> roles) {
}
