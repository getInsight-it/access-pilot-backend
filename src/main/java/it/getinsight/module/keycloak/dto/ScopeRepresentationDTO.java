package it.getinsight.module.keycloak.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public record ScopeRepresentationDTO(
    @JsonProperty("key") String name,
    @JsonProperty("displayName") String displayName,
    @JsonProperty("iconUri") String iconUri,
    @JsonProperty("id") String id,
    @JsonProperty("policies") List<PolicyRepresentationDTO> policies,
    @JsonProperty("resources") List<ResourceRepresentationDTO> resources
) {
}
