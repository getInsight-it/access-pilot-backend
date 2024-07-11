package it.getinsight.module.keycloak.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Objects;


public class OAuth2DeviceAuthorizationResponseDTO {
    @JsonProperty("deviceCode")
    private String deviceCode = null;

    @JsonProperty("userCode")
    private String userCode = null;

    @JsonProperty("verificationUri")
    private String verificationUri = null;

    @JsonProperty("verificationUriComplete")
    private String verificationUriComplete = null;

    @JsonProperty("expiresIn")
    private Long expiresIn = null;

    @JsonProperty("interval")
    private Long interval = null;

    public OAuth2DeviceAuthorizationResponseDTO deviceCode(String deviceCode) {
        this.deviceCode = deviceCode;
        return this;
    }


    public String getDeviceCode() {
        return deviceCode;
    }

    public void setDeviceCode(String deviceCode) {
        this.deviceCode = deviceCode;
    }

    public OAuth2DeviceAuthorizationResponseDTO userCode(String userCode) {
        this.userCode = userCode;
        return this;
    }


    public String getUserCode() {
        return userCode;
    }

    public void setUserCode(String userCode) {
        this.userCode = userCode;
    }

    public OAuth2DeviceAuthorizationResponseDTO verificationUri(String verificationUri) {
        this.verificationUri = verificationUri;
        return this;
    }


    public String getVerificationUri() {
        return verificationUri;
    }

    public void setVerificationUri(String verificationUri) {
        this.verificationUri = verificationUri;
    }

    public OAuth2DeviceAuthorizationResponseDTO verificationUriComplete(String verificationUriComplete) {
        this.verificationUriComplete = verificationUriComplete;
        return this;
    }


    public String getVerificationUriComplete() {
        return verificationUriComplete;
    }

    public void setVerificationUriComplete(String verificationUriComplete) {
        this.verificationUriComplete = verificationUriComplete;
    }

    public OAuth2DeviceAuthorizationResponseDTO expiresIn(Long expiresIn) {
        this.expiresIn = expiresIn;
        return this;
    }


    public Long getExpiresIn() {
        return expiresIn;
    }

    public void setExpiresIn(Long expiresIn) {
        this.expiresIn = expiresIn;
    }

    public OAuth2DeviceAuthorizationResponseDTO interval(Long interval) {
        this.interval = interval;
        return this;
    }


    public Long getInterval() {
        return interval;
    }

    public void setInterval(Long interval) {
        this.interval = interval;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        OAuth2DeviceAuthorizationResponseDTO oauth2DeviceAuthorizationResponse = (OAuth2DeviceAuthorizationResponseDTO) o;
        return Objects.equals(this.deviceCode, oauth2DeviceAuthorizationResponse.deviceCode) &&
                Objects.equals(this.userCode, oauth2DeviceAuthorizationResponse.userCode) &&
                Objects.equals(this.verificationUri, oauth2DeviceAuthorizationResponse.verificationUri) &&
                Objects.equals(this.verificationUriComplete, oauth2DeviceAuthorizationResponse.verificationUriComplete) &&
                Objects.equals(this.expiresIn, oauth2DeviceAuthorizationResponse.expiresIn) &&
                Objects.equals(this.interval, oauth2DeviceAuthorizationResponse.interval);
    }

    @Override
    public int hashCode() {
        return Objects.hash(deviceCode, userCode, verificationUri, verificationUriComplete, expiresIn, interval);
    }

    @Override
    public String toString() {

        String sb = "class OAuth2DeviceAuthorizationResponseDTO {\n" +
                "    deviceCode: " + toIndentedString(deviceCode) + "\n" +
                "    userCode: " + toIndentedString(userCode) + "\n" +
                "    verificationUri: " + toIndentedString(verificationUri) + "\n" +
                "    verificationUriComplete: " + toIndentedString(verificationUriComplete) + "\n" +
                "    expiresIn: " + toIndentedString(expiresIn) + "\n" +
                "    interval: " + toIndentedString(interval) + "\n" +
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

