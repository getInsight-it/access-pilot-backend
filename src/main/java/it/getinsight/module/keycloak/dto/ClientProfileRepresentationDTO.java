package it.getinsight.module.keycloak.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public record ClientProfileRepresentationDTO(
    @JsonProperty("name") String name,
    @JsonProperty("description") String description,
    @JsonProperty("executors") List<ClientPolicyExecutorRepresentationDTO> executors
) {
}
