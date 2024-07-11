package it.getinsight.module.keycloak.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Objects;


public class ScopePermissionRepresentationDTO {
    @JsonProperty("type")
    private String type = null;

    @JsonProperty("resourceType")
    private String resourceType = null;

    public ScopePermissionRepresentationDTO type(String type) {
        this.type = type;
        return this;
    }


    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public ScopePermissionRepresentationDTO resourceType(String resourceType) {
        this.resourceType = resourceType;
        return this;
    }


    public String getResourceType() {
        return resourceType;
    }

    public void setResourceType(String resourceType) {
        this.resourceType = resourceType;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ScopePermissionRepresentationDTO scopePermissionRepresentation = (ScopePermissionRepresentationDTO) o;
        return Objects.equals(this.type, scopePermissionRepresentation.type) &&
                Objects.equals(this.resourceType, scopePermissionRepresentation.resourceType);
    }

    @Override
    public int hashCode() {
        return Objects.hash(type, resourceType);
    }

    @Override
    public String toString() {

        String sb = "class ScopePermissionRepresentationDTO {\n" +
                "    type: " + toIndentedString(type) + "\n" +
                "    resourceType: " + toIndentedString(resourceType) + "\n" +
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

