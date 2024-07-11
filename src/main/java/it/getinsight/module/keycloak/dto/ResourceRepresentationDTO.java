package it.getinsight.module.keycloak.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;


public class ResourceRepresentationDTO {
    @JsonProperty("id")
    private String id = null;

    @JsonProperty("name")
    private String name = null;

    @JsonProperty("displayName")
    private String displayName = null;

    @JsonProperty("uris")
    private List<String> uris = null;

    @JsonProperty("type")
    private String type = null;

    @JsonProperty("scopes")
    private List<ScopeRepresentationDTO> scopes = null;

    @JsonProperty("iconUri")
    private String iconUri = null;

    @JsonProperty("owner")
    private ResourceOwnerRepresentationDTO owner = null;

    @JsonProperty("ownerManagedAccess")
    private Boolean ownerManagedAccess = null;

    @JsonProperty("attributes")
    private Map<String, List<String>> attributes = null;

    public ResourceRepresentationDTO id(String id) {
        this.id = id;
        return this;
    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public ResourceRepresentationDTO name(String name) {
        this.name = name;
        return this;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ResourceRepresentationDTO displayName(String displayName) {
        this.displayName = displayName;
        return this;
    }


    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public ResourceRepresentationDTO uris(List<String> uris) {
        this.uris = uris;
        return this;
    }

    public ResourceRepresentationDTO addUrisItem(String urisItem) {
        if (this.uris == null) {
            this.uris = new ArrayList<String>();
        }
        this.uris.add(urisItem);
        return this;
    }


    public List<String> getUris() {
        return uris;
    }

    public void setUris(List<String> uris) {
        this.uris = uris;
    }

    public ResourceRepresentationDTO type(String type) {
        this.type = type;
        return this;
    }


    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public ResourceRepresentationDTO scopes(List<ScopeRepresentationDTO> scopes) {
        this.scopes = scopes;
        return this;
    }

    public ResourceRepresentationDTO addScopesItem(ScopeRepresentationDTO scopesItem) {
        if (this.scopes == null) {
            this.scopes = new ArrayList<ScopeRepresentationDTO>();
        }
        this.scopes.add(scopesItem);
        return this;
    }


    public List<ScopeRepresentationDTO> getScopes() {
        return scopes;
    }

    public void setScopes(List<ScopeRepresentationDTO> scopes) {
        this.scopes = scopes;
    }

    public ResourceRepresentationDTO iconUri(String iconUri) {
        this.iconUri = iconUri;
        return this;
    }


    public String getIconUri() {
        return iconUri;
    }

    public void setIconUri(String iconUri) {
        this.iconUri = iconUri;
    }

    public ResourceRepresentationDTO owner(ResourceOwnerRepresentationDTO owner) {
        this.owner = owner;
        return this;
    }


    public ResourceOwnerRepresentationDTO getOwner() {
        return owner;
    }

    public void setOwner(ResourceOwnerRepresentationDTO owner) {
        this.owner = owner;
    }

    public ResourceRepresentationDTO ownerManagedAccess(Boolean ownerManagedAccess) {
        this.ownerManagedAccess = ownerManagedAccess;
        return this;
    }


    public Boolean isOwnerManagedAccess() {
        return ownerManagedAccess;
    }

    public void setOwnerManagedAccess(Boolean ownerManagedAccess) {
        this.ownerManagedAccess = ownerManagedAccess;
    }

    public ResourceRepresentationDTO attributes(Map<String, List<String>> attributes) {
        this.attributes = attributes;
        return this;
    }

    public ResourceRepresentationDTO putAttributesItem(String key, List<String> attributesItem) {
        if (this.attributes == null) {
            this.attributes = null;
        }
        this.attributes.put(key, attributesItem);
        return this;
    }


    public Map<String, List<String>> getAttributes() {
        return attributes;
    }

    public void setAttributes(Map<String, List<String>> attributes) {
        this.attributes = attributes;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ResourceRepresentationDTO resourceRepresentation = (ResourceRepresentationDTO) o;
        return Objects.equals(this.id, resourceRepresentation.id) &&
                Objects.equals(this.name, resourceRepresentation.name) &&
                Objects.equals(this.displayName, resourceRepresentation.displayName) &&
                Objects.equals(this.uris, resourceRepresentation.uris) &&
                Objects.equals(this.type, resourceRepresentation.type) &&
                Objects.equals(this.scopes, resourceRepresentation.scopes) &&
                Objects.equals(this.iconUri, resourceRepresentation.iconUri) &&
                Objects.equals(this.owner, resourceRepresentation.owner) &&
                Objects.equals(this.ownerManagedAccess, resourceRepresentation.ownerManagedAccess) &&
                Objects.equals(this.attributes, resourceRepresentation.attributes);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, displayName, uris, type, scopes, iconUri, owner, ownerManagedAccess, attributes);
    }

    @Override
    public String toString() {

        String sb = "class ResourceRepresentationDTO {\n" +
                "    id: " + toIndentedString(id) + "\n" +
                "    name: " + toIndentedString(name) + "\n" +
                "    displayName: " + toIndentedString(displayName) + "\n" +
                "    uris: " + toIndentedString(uris) + "\n" +
                "    type: " + toIndentedString(type) + "\n" +
                "    scopes: " + toIndentedString(scopes) + "\n" +
                "    iconUri: " + toIndentedString(iconUri) + "\n" +
                "    owner: " + toIndentedString(owner) + "\n" +
                "    ownerManagedAccess: " + toIndentedString(ownerManagedAccess) + "\n" +
                "    attributes: " + toIndentedString(attributes) + "\n" +
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

