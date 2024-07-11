package it.getinsight.module.keycloak.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Map;
import java.util.Objects;


public class AccessTokenResponseDTO {
    @JsonProperty("scope")
    private String scope = null;

    @JsonProperty("token")
    private String token = null;

    @JsonProperty("expiresIn")
    private Long expiresIn = null;

    @JsonProperty("refreshExpiresIn")
    private Long refreshExpiresIn = null;

    @JsonProperty("refreshToken")
    private String refreshToken = null;

    @JsonProperty("tokenType")
    private String tokenType = null;

    @JsonProperty("idToken")
    private String idToken = null;

    @JsonProperty("notBeforePolicy")
    private Integer notBeforePolicy = null;

    @JsonProperty("sessionState")
    private String sessionState = null;

    @JsonProperty("otherClaims")
    private Map<String, Object> otherClaims = null;

    @JsonProperty("error")
    private String error = null;

    @JsonProperty("errorDescription")
    private String errorDescription = null;

    @JsonProperty("errorUri")
    private String errorUri = null;

    public AccessTokenResponseDTO scope(String scope) {
        this.scope = scope;
        return this;
    }


    public String getScope() {
        return scope;
    }

    public void setScope(String scope) {
        this.scope = scope;
    }

    public AccessTokenResponseDTO token(String token) {
        this.token = token;
        return this;
    }


    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public AccessTokenResponseDTO expiresIn(Long expiresIn) {
        this.expiresIn = expiresIn;
        return this;
    }


    public Long getExpiresIn() {
        return expiresIn;
    }

    public void setExpiresIn(Long expiresIn) {
        this.expiresIn = expiresIn;
    }

    public AccessTokenResponseDTO refreshExpiresIn(Long refreshExpiresIn) {
        this.refreshExpiresIn = refreshExpiresIn;
        return this;
    }


    public Long getRefreshExpiresIn() {
        return refreshExpiresIn;
    }

    public void setRefreshExpiresIn(Long refreshExpiresIn) {
        this.refreshExpiresIn = refreshExpiresIn;
    }

    public AccessTokenResponseDTO refreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
        return this;
    }


    public String getRefreshToken() {
        return refreshToken;
    }

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }

    public AccessTokenResponseDTO tokenType(String tokenType) {
        this.tokenType = tokenType;
        return this;
    }


    public String getTokenType() {
        return tokenType;
    }

    public void setTokenType(String tokenType) {
        this.tokenType = tokenType;
    }

    public AccessTokenResponseDTO idToken(String idToken) {
        this.idToken = idToken;
        return this;
    }


    public String getIdToken() {
        return idToken;
    }

    public void setIdToken(String idToken) {
        this.idToken = idToken;
    }

    public AccessTokenResponseDTO notBeforePolicy(Integer notBeforePolicy) {
        this.notBeforePolicy = notBeforePolicy;
        return this;
    }


    public Integer getNotBeforePolicy() {
        return notBeforePolicy;
    }

    public void setNotBeforePolicy(Integer notBeforePolicy) {
        this.notBeforePolicy = notBeforePolicy;
    }

    public AccessTokenResponseDTO sessionState(String sessionState) {
        this.sessionState = sessionState;
        return this;
    }


    public String getSessionState() {
        return sessionState;
    }

    public void setSessionState(String sessionState) {
        this.sessionState = sessionState;
    }

    public AccessTokenResponseDTO otherClaims(Map<String, Object> otherClaims) {
        this.otherClaims = otherClaims;
        return this;
    }

    public AccessTokenResponseDTO putOtherClaimsItem(String key, Object otherClaimsItem) {
        if (this.otherClaims == null) {
            this.otherClaims = null;
        }
        this.otherClaims.put(key, otherClaimsItem);
        return this;
    }


    public Map<String, Object> getOtherClaims() {
        return otherClaims;
    }

    public void setOtherClaims(Map<String, Object> otherClaims) {
        this.otherClaims = otherClaims;
    }

    public AccessTokenResponseDTO error(String error) {
        this.error = error;
        return this;
    }


    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public AccessTokenResponseDTO errorDescription(String errorDescription) {
        this.errorDescription = errorDescription;
        return this;
    }


    public String getErrorDescription() {
        return errorDescription;
    }

    public void setErrorDescription(String errorDescription) {
        this.errorDescription = errorDescription;
    }

    public AccessTokenResponseDTO errorUri(String errorUri) {
        this.errorUri = errorUri;
        return this;
    }


    public String getErrorUri() {
        return errorUri;
    }

    public void setErrorUri(String errorUri) {
        this.errorUri = errorUri;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        AccessTokenResponseDTO accessTokenResponse = (AccessTokenResponseDTO) o;
        return Objects.equals(this.scope, accessTokenResponse.scope) &&
                Objects.equals(this.token, accessTokenResponse.token) &&
                Objects.equals(this.expiresIn, accessTokenResponse.expiresIn) &&
                Objects.equals(this.refreshExpiresIn, accessTokenResponse.refreshExpiresIn) &&
                Objects.equals(this.refreshToken, accessTokenResponse.refreshToken) &&
                Objects.equals(this.tokenType, accessTokenResponse.tokenType) &&
                Objects.equals(this.idToken, accessTokenResponse.idToken) &&
                Objects.equals(this.notBeforePolicy, accessTokenResponse.notBeforePolicy) &&
                Objects.equals(this.sessionState, accessTokenResponse.sessionState) &&
                Objects.equals(this.otherClaims, accessTokenResponse.otherClaims) &&
                Objects.equals(this.error, accessTokenResponse.error) &&
                Objects.equals(this.errorDescription, accessTokenResponse.errorDescription) &&
                Objects.equals(this.errorUri, accessTokenResponse.errorUri);
    }

    @Override
    public int hashCode() {
        return Objects.hash(scope, token, expiresIn, refreshExpiresIn, refreshToken, tokenType, idToken, notBeforePolicy, sessionState, otherClaims, error, errorDescription, errorUri);
    }

    @Override
    public String toString() {

        String sb = "class AccessTokenResponseDTO {\n" +
                "    scope: " + toIndentedString(scope) + "\n" +
                "    token: " + toIndentedString(token) + "\n" +
                "    expiresIn: " + toIndentedString(expiresIn) + "\n" +
                "    refreshExpiresIn: " + toIndentedString(refreshExpiresIn) + "\n" +
                "    refreshToken: " + toIndentedString(refreshToken) + "\n" +
                "    tokenType: " + toIndentedString(tokenType) + "\n" +
                "    idToken: " + toIndentedString(idToken) + "\n" +
                "    notBeforePolicy: " + toIndentedString(notBeforePolicy) + "\n" +
                "    sessionState: " + toIndentedString(sessionState) + "\n" +
                "    otherClaims: " + toIndentedString(otherClaims) + "\n" +
                "    error: " + toIndentedString(error) + "\n" +
                "    errorDescription: " + toIndentedString(errorDescription) + "\n" +
                "    errorUri: " + toIndentedString(errorUri) + "\n" +
                "}";
        return sb;
    }


    private String toIndentedString(Object o) {
        if (o == null) {
            return "null";
        }
        return o.toString().replace("\n", "\n    ");
    }
}

