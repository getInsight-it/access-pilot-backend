package it.getinsight.module.keycloak.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public record ClientPolicyExecutorRepresentationDTO(
    @JsonProperty("executorProviderId") String executorProviderId,
    @JsonProperty("configuracao") Object _configuration
) {
}
