package it.getinsight.module.keycloak.dto;

import java.util.List;

public record AuthenticationFlowRepresentationDTO(String id, String alias, String description, String providerId,
                                                  Boolean topLevel, Boolean builtIn,
                                                  List<AuthenticationExecutionRepresentationDTO> authenticationExecutions) {
}
