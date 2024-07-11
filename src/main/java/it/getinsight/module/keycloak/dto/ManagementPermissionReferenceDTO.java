package it.getinsight.module.keycloak.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Map;
import java.util.Objects;


public class ManagementPermissionReferenceDTO {
    @JsonProperty("enabled")
    private Boolean enabled = null;

    @JsonProperty("resource")
    private String resource = null;

    @JsonProperty("scopePermissions")
    private Map<String, String> scopePermissions = null;

    public ManagementPermissionReferenceDTO enabled(Boolean enabled) {
        this.enabled = enabled;
        return this;
    }


    public Boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(Boolean enabled) {
        this.enabled = enabled;
    }

    public ManagementPermissionReferenceDTO resource(String resource) {
        this.resource = resource;
        return this;
    }


    public String getResource() {
        return resource;
    }

    public void setResource(String resource) {
        this.resource = resource;
    }

    public ManagementPermissionReferenceDTO scopePermissions(Map<String, String> scopePermissions) {
        this.scopePermissions = scopePermissions;
        return this;
    }

    public ManagementPermissionReferenceDTO putScopePermissionsItem(String key, String scopePermissionsItem) {
        if (this.scopePermissions == null) {
            this.scopePermissions = null;
        }
        this.scopePermissions.put(key, scopePermissionsItem);
        return this;
    }


    public Map<String, String> getScopePermissions() {
        return scopePermissions;
    }

    public void setScopePermissions(Map<String, String> scopePermissions) {
        this.scopePermissions = scopePermissions;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ManagementPermissionReferenceDTO managementPermissionReference = (ManagementPermissionReferenceDTO) o;
        return Objects.equals(this.enabled, managementPermissionReference.enabled) &&
                Objects.equals(this.resource, managementPermissionReference.resource) &&
                Objects.equals(this.scopePermissions, managementPermissionReference.scopePermissions);
    }

    @Override
    public int hashCode() {
        return Objects.hash(enabled, resource, scopePermissions);
    }

    @Override
    public String toString() {

        String sb = "class ManagementPermissionReferenceDTO {\n" +
                "    enabled: " + toIndentedString(enabled) + "\n" +
                "    resource: " + toIndentedString(resource) + "\n" +
                "    scopePermissions: " + toIndentedString(scopePermissions) + "\n" +
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

