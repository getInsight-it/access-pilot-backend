package it.getinsight.module.keycloak.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public record AuthenticationExecutionRepresentationDTO(
    @JsonProperty("id") String id,
    @JsonProperty("flowId") String flowId,
    @JsonProperty("parentFlow") String parentFlow
) {
}
