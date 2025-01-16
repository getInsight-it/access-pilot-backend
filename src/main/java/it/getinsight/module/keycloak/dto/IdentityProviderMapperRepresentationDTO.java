package it.getinsight.module.keycloak.dto;

import java.util.Map;

public record IdentityProviderMapperRepresentationDTO(String id, String name, String identityProviderAlias,
                                                      String identityProviderMapper, Map<String, String> config) {
}
