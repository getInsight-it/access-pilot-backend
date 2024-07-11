package it.getinsight.module.keycloak.dto;


import com.fasterxml.jackson.annotation.JsonProperty;

public record TokenRequestDTO(
    @JsonProperty("client_id") String client_id,
    @JsonProperty("client_secret") String client_secret,
    @JsonProperty("grant_type") String grant_type) {

    public TokenRequestDTO(String clientId, String clientSecret) {
        this(clientId, clientSecret, "client_credentials");
    }
}
