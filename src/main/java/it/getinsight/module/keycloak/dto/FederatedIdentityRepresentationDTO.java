package it.getinsight.module.keycloak.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Objects;


public class FederatedIdentityRepresentationDTO {
    @JsonProperty("identityProvider")
    private String identityProvider = null;

    @JsonProperty("userId")
    private String userId = null;

    @JsonProperty("userName")
    private String userName = null;

    public FederatedIdentityRepresentationDTO identityProvider(String identityProvider) {
        this.identityProvider = identityProvider;
        return this;
    }


    public String getIdentityProvider() {
        return identityProvider;
    }

    public void setIdentityProvider(String identityProvider) {
        this.identityProvider = identityProvider;
    }

    public FederatedIdentityRepresentationDTO userId(String userId) {
        this.userId = userId;
        return this;
    }


    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public FederatedIdentityRepresentationDTO userName(String userName) {
        this.userName = userName;
        return this;
    }


    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        FederatedIdentityRepresentationDTO federatedIdentityRepresentation = (FederatedIdentityRepresentationDTO) o;
        return Objects.equals(this.identityProvider, federatedIdentityRepresentation.identityProvider) &&
                Objects.equals(this.userId, federatedIdentityRepresentation.userId) &&
                Objects.equals(this.userName, federatedIdentityRepresentation.userName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(identityProvider, userId, userName);
    }

    @Override
    public String toString() {

        String sb = "class FederatedIdentityRepresentationDTO {\n" +
                "    identityProvider: " + toIndentedString(identityProvider) + "\n" +
                "    userId: " + toIndentedString(userId) + "\n" +
                "    userName: " + toIndentedString(userName) + "\n" +
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

