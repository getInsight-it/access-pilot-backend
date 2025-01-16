package it.getinsight.module.keycloak.dto;

import java.util.List;

public record AuthenticationExecutionInfoRepresentationDTO(String id, String displayName, String alias,
                                                           String description, String requirement,
                                                           List<String> requirementChoices, Boolean configurable,
                                                           String providerId, String authenticationConfig,
                                                           Boolean authenticationFlow, Integer level, Integer index,
                                                           String flowId) {
}
