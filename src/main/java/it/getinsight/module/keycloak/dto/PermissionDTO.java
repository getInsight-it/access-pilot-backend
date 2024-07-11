package it.getinsight.module.keycloak.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;


public class PermissionDTO {
    @JsonProperty("resourceId")
    private String resourceId = null;

    @JsonProperty("resourceName")
    private String resourceName = null;

    @JsonProperty("scopes")
    private List<String> scopes = null;

    @JsonProperty("claims")
    private Map<String, List<String>> claims = null;

    public PermissionDTO resourceId(String resourceId) {
        this.resourceId = resourceId;
        return this;
    }


    public String getResourceId() {
        return resourceId;
    }

    public void setResourceId(String resourceId) {
        this.resourceId = resourceId;
    }

    public PermissionDTO resourceName(String resourceName) {
        this.resourceName = resourceName;
        return this;
    }


    public String getResourceName() {
        return resourceName;
    }

    public void setResourceName(String resourceName) {
        this.resourceName = resourceName;
    }

    public PermissionDTO scopes(List<String> scopes) {
        this.scopes = scopes;
        return this;
    }

    public PermissionDTO addScopesItem(String scopesItem) {
        if (this.scopes == null) {
            this.scopes = new ArrayList<String>();
        }
        this.scopes.add(scopesItem);
        return this;
    }


    public List<String> getScopes() {
        return scopes;
    }

    public void setScopes(List<String> scopes) {
        this.scopes = scopes;
    }

    public PermissionDTO claims(Map<String, List<String>> claims) {
        this.claims = claims;
        return this;
    }

    public PermissionDTO putClaimsItem(String key, List<String> claimsItem) {
        if (this.claims == null) {
            this.claims = null;
        }
        this.claims.put(key, claimsItem);
        return this;
    }


    public Map<String, List<String>> getClaims() {
        return claims;
    }

    public void setClaims(Map<String, List<String>> claims) {
        this.claims = claims;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        PermissionDTO permission = (PermissionDTO) o;
        return Objects.equals(this.resourceId, permission.resourceId) &&
                Objects.equals(this.resourceName, permission.resourceName) &&
                Objects.equals(this.scopes, permission.scopes) &&
                Objects.equals(this.claims, permission.claims);
    }

    @Override
    public int hashCode() {
        return Objects.hash(resourceId, resourceName, scopes, claims);
    }

    @Override
    public String toString() {

        String sb = "class PermissionDTO {\n" +
                "    resourceId: " + toIndentedString(resourceId) + "\n" +
                "    resourceName: " + toIndentedString(resourceName) + "\n" +
                "    scopes: " + toIndentedString(scopes) + "\n" +
                "    claims: " + toIndentedString(claims) + "\n" +
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

