package it.getinsight.module.keycloak.dto;

import java.util.Map;

public record RequiredActionProviderRepresentationDTO(String alias, String name, Boolean enabled, Boolean defaultAction,
                                                      String providerId, Integer priority, Map<String, String> config) {
}
