package it.getinsight.module.keycloak.dto;

import java.util.List;

public record ClientMappingsRepresentationDTO(String id, String client, List<RoleRepresentationDTO> mappings) {
}
