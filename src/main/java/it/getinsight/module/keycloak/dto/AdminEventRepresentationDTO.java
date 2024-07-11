package it.getinsight.module.keycloak.dto;


import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Objects;


public class AdminEventRepresentationDTO {
    @JsonProperty("time")
    private Long time = null;

    @JsonProperty("realmId")
    private String realmId = null;

    @JsonProperty("authDetails")
    private AuthDetailsRepresentationDTO authDetails = null;

    @JsonProperty("operationType")
    private String operationType = null;

    @JsonProperty("resourceType")
    private String resourceType = null;

    @JsonProperty("resourcePath")
    private String resourcePath = null;

    @JsonProperty("representation")
    private String representation = null;

    @JsonProperty("error")
    private String error = null;

    public AdminEventRepresentationDTO time(Long time) {
        this.time = time;
        return this;
    }


    public Long getTime() {
        return time;
    }

    public void setTime(Long time) {
        this.time = time;
    }

    public AdminEventRepresentationDTO realmId(String realmId) {
        this.realmId = realmId;
        return this;
    }


    public String getRealmId() {
        return realmId;
    }

    public void setRealmId(String realmId) {
        this.realmId = realmId;
    }

    public AdminEventRepresentationDTO authDetails(AuthDetailsRepresentationDTO authDetails) {
        this.authDetails = authDetails;
        return this;
    }


    public AuthDetailsRepresentationDTO getAuthDetails() {
        return authDetails;
    }

    public void setAuthDetails(AuthDetailsRepresentationDTO authDetails) {
        this.authDetails = authDetails;
    }

    public AdminEventRepresentationDTO operationType(String operationType) {
        this.operationType = operationType;
        return this;
    }


    public String getOperationType() {
        return operationType;
    }

    public void setOperationType(String operationType) {
        this.operationType = operationType;
    }

    public AdminEventRepresentationDTO resourceType(String resourceType) {
        this.resourceType = resourceType;
        return this;
    }


    public String getResourceType() {
        return resourceType;
    }

    public void setResourceType(String resourceType) {
        this.resourceType = resourceType;
    }

    public AdminEventRepresentationDTO resourcePath(String resourcePath) {
        this.resourcePath = resourcePath;
        return this;
    }


    public String getResourcePath() {
        return resourcePath;
    }

    public void setResourcePath(String resourcePath) {
        this.resourcePath = resourcePath;
    }

    public AdminEventRepresentationDTO representation(String representation) {
        this.representation = representation;
        return this;
    }


    public String getRepresentation() {
        return representation;
    }

    public void setRepresentation(String representation) {
        this.representation = representation;
    }

    public AdminEventRepresentationDTO error(String error) {
        this.error = error;
        return this;
    }


    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        AdminEventRepresentationDTO adminEventRepresentation = (AdminEventRepresentationDTO) o;
        return Objects.equals(this.time, adminEventRepresentation.time) &&
                Objects.equals(this.realmId, adminEventRepresentation.realmId) &&
                Objects.equals(this.authDetails, adminEventRepresentation.authDetails) &&
                Objects.equals(this.operationType, adminEventRepresentation.operationType) &&
                Objects.equals(this.resourceType, adminEventRepresentation.resourceType) &&
                Objects.equals(this.resourcePath, adminEventRepresentation.resourcePath) &&
                Objects.equals(this.representation, adminEventRepresentation.representation) &&
                Objects.equals(this.error, adminEventRepresentation.error);
    }

    @Override
    public int hashCode() {
        return Objects.hash(time, realmId, authDetails, operationType, resourceType, resourcePath, representation, error);
    }

    @Override
    public String toString() {

        String sb = "class AdminEventRepresentationDTO {\n" +
                "    time: " + toIndentedString(time) + "\n" +
                "    realmId: " + toIndentedString(realmId) + "\n" +
                "    authDetails: " + toIndentedString(authDetails) + "\n" +
                "    operationType: " + toIndentedString(operationType) + "\n" +
                "    resourceType: " + toIndentedString(resourceType) + "\n" +
                "    resourcePath: " + toIndentedString(resourcePath) + "\n" +
                "    representation: " + toIndentedString(representation) + "\n" +
                "    error: " + toIndentedString(error) + "\n" +
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

