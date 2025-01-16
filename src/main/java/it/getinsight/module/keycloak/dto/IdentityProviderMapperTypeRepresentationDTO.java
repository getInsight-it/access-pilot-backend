package it.getinsight.module.keycloak.dto;

import java.util.List;

public record IdentityProviderMapperTypeRepresentationDTO(String id, String name, String category, String helpText,
                                                          List<ConfigPropertyRepresentationDTO> properties) {
}
