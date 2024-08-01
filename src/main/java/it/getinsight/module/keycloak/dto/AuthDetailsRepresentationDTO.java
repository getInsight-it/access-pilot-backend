package it.getinsight.module.keycloak.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public record AuthDetailsRepresentationDTO(
    @JsonProperty("realmId") String realmId,
    @JsonProperty("clientId") String clientId,
    @JsonProperty("userId") String userId,
    @JsonProperty("ipAddress") String ipAddress
) {
}
