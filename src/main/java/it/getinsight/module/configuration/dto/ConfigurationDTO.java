package it.getinsight.module.configuration.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.io.Serial;
import java.io.Serializable;

public record ConfigurationDTO(
    Long id,
    @NotBlank String name,
    @NotBlank String description,
    String icon,
    @NotBlank @JsonProperty("authority") String authority,
    @NotBlank @JsonProperty("redirect_url") String redirectUrl,
    @NotBlank @JsonProperty("client_id") String clientId,
    @NotBlank @JsonProperty("response_type") String responseType,
    @NotBlank String scope,
    @NotBlank @JsonProperty("post_logout_redirect_uri") String postLogoutRedirectUri,
    @NotNull  @JsonProperty("start_checksession") Boolean startChecksession,
    @NotNull  @JsonProperty("silent_renew") Boolean silentRenew,
    @NotBlank @JsonProperty("startup_route") String startupRoute,
    @NotBlank @JsonProperty("forbidden_route") String forbiddenRoute,
    @NotBlank @JsonProperty("unauthorized_route") String unauthorizedRoute,
    @NotNull @JsonProperty("log_level") Integer logLevel,
    @NotNull @JsonProperty("max_id_token_iat_offset_allowed_in_seconds") Integer maxIdTokenIatOffsetAllowedInSeconds,
    @NotNull @JsonProperty("history_cleanup_off") Boolean historyCleanupOff
) implements Serializable {
    @Serial
    private static final long serialVersionUID = -6731357049354425215L;
}
