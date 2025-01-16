package it.getinsight.module.keycloak.dto;

public record PasswordPolicyTypeRepresentationDTO(String id, String displayName, String configType, String defaultValue,
                                                  Boolean multipleSupported) {
}
