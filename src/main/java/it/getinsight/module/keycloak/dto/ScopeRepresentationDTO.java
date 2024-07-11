package it.getinsight.module.keycloak.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


public class ScopeRepresentationDTO {
    @JsonProperty("name")
    private String name = null;

    @JsonProperty("displayName")
    private String displayName = null;

    @JsonProperty("iconUri")
    private String iconUri = null;

    @JsonProperty("id")
    private String id = null;

    @JsonProperty("policies")
    private List<PolicyRepresentationDTO> policies = null;

    @JsonProperty("resources")
    private List<ResourceRepresentationDTO> resources = null;

    public ScopeRepresentationDTO name(String name) {
        this.name = name;
        return this;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ScopeRepresentationDTO displayName(String displayName) {
        this.displayName = displayName;
        return this;
    }


    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public ScopeRepresentationDTO iconUri(String iconUri) {
        this.iconUri = iconUri;
        return this;
    }


    public String getIconUri() {
        return iconUri;
    }

    public void setIconUri(String iconUri) {
        this.iconUri = iconUri;
    }

    public ScopeRepresentationDTO id(String id) {
        this.id = id;
        return this;
    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public ScopeRepresentationDTO policies(List<PolicyRepresentationDTO> policies) {
        this.policies = policies;
        return this;
    }

    public ScopeRepresentationDTO addPoliciesItem(PolicyRepresentationDTO policiesItem) {
        if (this.policies == null) {
            this.policies = new ArrayList<PolicyRepresentationDTO>();
        }
        this.policies.add(policiesItem);
        return this;
    }


    public List<PolicyRepresentationDTO> getPolicies() {
        return policies;
    }

    public void setPolicies(List<PolicyRepresentationDTO> policies) {
        this.policies = policies;
    }

    public ScopeRepresentationDTO resources(List<ResourceRepresentationDTO> resources) {
        this.resources = resources;
        return this;
    }

    public ScopeRepresentationDTO addResourcesItem(ResourceRepresentationDTO resourcesItem) {
        if (this.resources == null) {
            this.resources = new ArrayList<ResourceRepresentationDTO>();
        }
        this.resources.add(resourcesItem);
        return this;
    }


    public List<ResourceRepresentationDTO> getResources() {
        return resources;
    }

    public void setResources(List<ResourceRepresentationDTO> resources) {
        this.resources = resources;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ScopeRepresentationDTO scopeRepresentation = (ScopeRepresentationDTO) o;
        return Objects.equals(this.name, scopeRepresentation.name) &&
                Objects.equals(this.displayName, scopeRepresentation.displayName) &&
                Objects.equals(this.iconUri, scopeRepresentation.iconUri) &&
                Objects.equals(this.id, scopeRepresentation.id) &&
                Objects.equals(this.policies, scopeRepresentation.policies) &&
                Objects.equals(this.resources, scopeRepresentation.resources);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, displayName, iconUri, id, policies, resources);
    }

    @Override
    public String toString() {

        String sb = "class ScopeRepresentationDTO {\n" +
                "    name: " + toIndentedString(name) + "\n" +
                "    displayName: " + toIndentedString(displayName) + "\n" +
                "    iconUri: " + toIndentedString(iconUri) + "\n" +
                "    id: " + toIndentedString(id) + "\n" +
                "    policies: " + toIndentedString(policies) + "\n" +
                "    resources: " + toIndentedString(resources) + "\n" +
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

