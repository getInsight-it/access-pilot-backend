package it.getinsight.module.keycloak.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Map;

public record IdentityProviderRepresentationDTO(
    @JsonProperty("internalId") String internalId,
    @JsonProperty("alias") String alias,
    @JsonProperty("providerId") String providerId,
    @JsonProperty("config") Map<String, String> config,
    @JsonProperty("enabled") Boolean enabled,
    @JsonProperty("linkOnly") Boolean linkOnly,
    @JsonProperty("updateProfileFirstLoginMode") String updateProfileFirstLoginMode,
    @JsonProperty("authenticateByDefault") Boolean authenticateByDefault,
    @JsonProperty("firstBrokerLoginFlowAlias") String firstBrokerLoginFlowAlias,
    @JsonProperty("postBrokerLoginFlowAlias") String postBrokerLoginFlowAlias,
    @JsonProperty("storeToken") Boolean storeToken,
    @JsonProperty("addReadTokenRoleOnCreate") Boolean addReadTokenRoleOnCreate,
    @JsonProperty("trustEmail") Boolean trustEmail,
    @JsonProperty("displayName") String displayName
) {
}
