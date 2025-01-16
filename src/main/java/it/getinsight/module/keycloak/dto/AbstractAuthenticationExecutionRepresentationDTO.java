package it.getinsight.module.keycloak.dto;

public record AbstractAuthenticationExecutionRepresentationDTO(String authenticatorConfig, String authenticator,
                                                               String requirement, Integer priority,
                                                               Boolean autheticatorFlow, Boolean authenticatorFlow) {
}
