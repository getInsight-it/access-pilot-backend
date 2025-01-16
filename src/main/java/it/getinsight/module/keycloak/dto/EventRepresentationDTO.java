package it.getinsight.module.keycloak.dto;

import java.util.Map;

public record EventRepresentationDTO(Long time, String type, String realmId, String clientId, String userId,
                                     String sessionId, String ipAddress, String error, Map<String, String> details) {
}
