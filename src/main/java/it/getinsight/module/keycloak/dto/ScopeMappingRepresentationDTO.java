package it.getinsight.module.keycloak.dto;

import java.util.List;

public record ScopeMappingRepresentationDTO(String self, String client, String clientTemplate, String clientScope,
                                            List<RoleRepresentationDTO> roles) {
}
