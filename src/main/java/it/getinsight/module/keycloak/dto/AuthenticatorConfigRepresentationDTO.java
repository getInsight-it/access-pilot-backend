package it.getinsight.module.keycloak.dto;

import java.util.Map;

public record AuthenticatorConfigRepresentationDTO(String id, String alias, Map<String, String> config) {
}
