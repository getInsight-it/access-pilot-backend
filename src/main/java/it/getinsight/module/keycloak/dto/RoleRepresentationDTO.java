package it.getinsight.module.keycloak.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;

import java.util.List;
import java.util.Map;

@Builder
public record RoleRepresentationDTO(
    @JsonProperty("id") String id,
    @JsonProperty("key") String name,
    @JsonProperty("description") String description,
    @JsonProperty("scopeParamRequired") Boolean scopeParamRequired,
    @JsonProperty("composites") CompositesDTO composites,
    @JsonProperty("composite") Boolean composite,
    @JsonProperty("clientRole") Boolean clientRole,
    @JsonProperty("containerId") String containerId,
    @JsonProperty("attributes") Map<String, List<String>> attributes
) {
}
