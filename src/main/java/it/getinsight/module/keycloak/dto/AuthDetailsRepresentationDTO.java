package it.getinsight.module.keycloak.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Objects;


public class AuthDetailsRepresentationDTO {
    @JsonProperty("realmId")
    private String realmId = null;

    @JsonProperty("clientId")
    private String clientId = null;

    @JsonProperty("userId")
    private String userId = null;

    @JsonProperty("ipAddress")
    private String ipAddress = null;

    public AuthDetailsRepresentationDTO realmId(String realmId) {
        this.realmId = realmId;
        return this;
    }


    public String getRealmId() {
        return realmId;
    }

    public void setRealmId(String realmId) {
        this.realmId = realmId;
    }

    public AuthDetailsRepresentationDTO clientId(String clientId) {
        this.clientId = clientId;
        return this;
    }


    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public AuthDetailsRepresentationDTO userId(String userId) {
        this.userId = userId;
        return this;
    }


    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public AuthDetailsRepresentationDTO ipAddress(String ipAddress) {
        this.ipAddress = ipAddress;
        return this;
    }


    public String getIpAddress() {
        return ipAddress;
    }

    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        AuthDetailsRepresentationDTO authDetailsRepresentation = (AuthDetailsRepresentationDTO) o;
        return Objects.equals(this.realmId, authDetailsRepresentation.realmId) &&
                Objects.equals(this.clientId, authDetailsRepresentation.clientId) &&
                Objects.equals(this.userId, authDetailsRepresentation.userId) &&
                Objects.equals(this.ipAddress, authDetailsRepresentation.ipAddress);
    }

    @Override
    public int hashCode() {
        return Objects.hash(realmId, clientId, userId, ipAddress);
    }

    @Override
    public String toString() {

        String sb = "class AuthDetailsRepresentationDTO {\n" +
                "    realmId: " + toIndentedString(realmId) + "\n" +
                "    clientId: " + toIndentedString(clientId) + "\n" +
                "    userId: " + toIndentedString(userId) + "\n" +
                "    ipAddress: " + toIndentedString(ipAddress) + "\n" +
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

