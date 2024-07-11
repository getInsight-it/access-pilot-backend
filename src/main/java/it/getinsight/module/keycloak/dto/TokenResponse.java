package it.getinsight.module.keycloak.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public record TokenResponse(
    @JsonProperty("access_token")
    String accessToken,
    @JsonProperty("expires_in")
    int expiresIn,
    @JsonProperty("refresh_expires_in")
    int refreshExpiresIn,
    @JsonProperty("token_type")
    String tokenType,
    @JsonProperty("not-before-policy")
    int notBeforePolicy,
    @JsonProperty("scope")
    String scope
) {}
