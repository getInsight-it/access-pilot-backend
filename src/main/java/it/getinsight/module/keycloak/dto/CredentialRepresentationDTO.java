package it.getinsight.module.keycloak.dto;

import java.util.Map;

public record CredentialRepresentationDTO(String id, String type, String userLabel, String secretData,
                                          String credentialData, Integer priority, Long createdDate, String value,
                                          Boolean temporary, String device, String hashedSaltedValue, String salt,
                                          Integer hashIterations, Integer counter, String algorithm, Integer digits,
                                          Integer period, Map<String, String> config) {
}
