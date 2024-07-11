package it.getinsight.module.keycloak.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Objects;


public class PermissionTicketRepresentationDTO {
    @JsonProperty("id")
    private String id = null;

    @JsonProperty("owner")
    private String owner = null;

    @JsonProperty("resource")
    private String resource = null;

    @JsonProperty("scope")
    private String scope = null;

    @JsonProperty("granted")
    private Boolean granted = null;

    @JsonProperty("scopeName")
    private String scopeName = null;

    @JsonProperty("resourceName")
    private String resourceName = null;

    @JsonProperty("requesterName")
    private String requesterName = null;

    @JsonProperty("requester")
    private String requester = null;

    @JsonProperty("ownerName")
    private String ownerName = null;

    public PermissionTicketRepresentationDTO id(String id) {
        this.id = id;
        return this;
    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public PermissionTicketRepresentationDTO owner(String owner) {
        this.owner = owner;
        return this;
    }


    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public PermissionTicketRepresentationDTO resource(String resource) {
        this.resource = resource;
        return this;
    }


    public String getResource() {
        return resource;
    }

    public void setResource(String resource) {
        this.resource = resource;
    }

    public PermissionTicketRepresentationDTO scope(String scope) {
        this.scope = scope;
        return this;
    }


    public String getScope() {
        return scope;
    }

    public void setScope(String scope) {
        this.scope = scope;
    }

    public PermissionTicketRepresentationDTO granted(Boolean granted) {
        this.granted = granted;
        return this;
    }


    public Boolean isGranted() {
        return granted;
    }

    public void setGranted(Boolean granted) {
        this.granted = granted;
    }

    public PermissionTicketRepresentationDTO scopeName(String scopeName) {
        this.scopeName = scopeName;
        return this;
    }


    public String getScopeName() {
        return scopeName;
    }

    public void setScopeName(String scopeName) {
        this.scopeName = scopeName;
    }

    public PermissionTicketRepresentationDTO resourceName(String resourceName) {
        this.resourceName = resourceName;
        return this;
    }


    public String getResourceName() {
        return resourceName;
    }

    public void setResourceName(String resourceName) {
        this.resourceName = resourceName;
    }

    public PermissionTicketRepresentationDTO requesterName(String requesterName) {
        this.requesterName = requesterName;
        return this;
    }


    public String getRequesterName() {
        return requesterName;
    }

    public void setRequesterName(String requesterName) {
        this.requesterName = requesterName;
    }

    public PermissionTicketRepresentationDTO requester(String requester) {
        this.requester = requester;
        return this;
    }


    public String getRequester() {
        return requester;
    }

    public void setRequester(String requester) {
        this.requester = requester;
    }

    public PermissionTicketRepresentationDTO ownerName(String ownerName) {
        this.ownerName = ownerName;
        return this;
    }


    public String getOwnerName() {
        return ownerName;
    }

    public void setOwnerName(String ownerName) {
        this.ownerName = ownerName;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        PermissionTicketRepresentationDTO permissionTicketRepresentation = (PermissionTicketRepresentationDTO) o;
        return Objects.equals(this.id, permissionTicketRepresentation.id) &&
                Objects.equals(this.owner, permissionTicketRepresentation.owner) &&
                Objects.equals(this.resource, permissionTicketRepresentation.resource) &&
                Objects.equals(this.scope, permissionTicketRepresentation.scope) &&
                Objects.equals(this.granted, permissionTicketRepresentation.granted) &&
                Objects.equals(this.scopeName, permissionTicketRepresentation.scopeName) &&
                Objects.equals(this.resourceName, permissionTicketRepresentation.resourceName) &&
                Objects.equals(this.requesterName, permissionTicketRepresentation.requesterName) &&
                Objects.equals(this.requester, permissionTicketRepresentation.requester) &&
                Objects.equals(this.ownerName, permissionTicketRepresentation.ownerName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, owner, resource, scope, granted, scopeName, resourceName, requesterName, requester, ownerName);
    }

    @Override
    public String toString() {

        String sb = "class PermissionTicketRepresentationDTO {\n" +
                "    id: " + toIndentedString(id) + "\n" +
                "    owner: " + toIndentedString(owner) + "\n" +
                "    resource: " + toIndentedString(resource) + "\n" +
                "    scope: " + toIndentedString(scope) + "\n" +
                "    granted: " + toIndentedString(granted) + "\n" +
                "    scopeName: " + toIndentedString(scopeName) + "\n" +
                "    resourceName: " + toIndentedString(resourceName) + "\n" +
                "    requesterName: " + toIndentedString(requesterName) + "\n" +
                "    requester: " + toIndentedString(requester) + "\n" +
                "    ownerName: " + toIndentedString(ownerName) + "\n" +
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

