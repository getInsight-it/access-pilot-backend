package it.getinsight.module.keycloak.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public record FederatedIdentityRepresentationDTO(
    @JsonProperty("identityProvider") String identityProvider,
    @JsonProperty("userId") String userId,
    @JsonProperty("userName") String userName
) {}
