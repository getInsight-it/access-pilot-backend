package it.getinsight.module.keycloak.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;
import java.util.Map;
import java.util.Objects;


public class RoleRepresentationDTO {
    @JsonProperty("id")
    private String id = null;

    @JsonProperty("name")
    private String name = null;

    @JsonProperty("description")
    private String description = null;

    @JsonProperty("scopeParamRequired")
    private Boolean scopeParamRequired = null;

    @JsonProperty("composites")
    private CompositesDTO composites = null;

    @JsonProperty("composite")
    private Boolean composite = null;

    @JsonProperty("clientRole")
    private Boolean clientRole = null;

    @JsonProperty("containerId")
    private String containerId = null;

    @JsonProperty("attributes")
    private Map<String, List<String>> attributes = null;

    public RoleRepresentationDTO id(String id) {
        this.id = id;
        return this;
    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public RoleRepresentationDTO name(String name) {
        this.name = name;
        return this;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public RoleRepresentationDTO description(String description) {
        this.description = description;
        return this;
    }


    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public RoleRepresentationDTO scopeParamRequired(Boolean scopeParamRequired) {
        this.scopeParamRequired = scopeParamRequired;
        return this;
    }


    public Boolean isScopeParamRequired() {
        return scopeParamRequired;
    }

    public void setScopeParamRequired(Boolean scopeParamRequired) {
        this.scopeParamRequired = scopeParamRequired;
    }

    public RoleRepresentationDTO composites(CompositesDTO composites) {
        this.composites = composites;
        return this;
    }


    public CompositesDTO getComposites() {
        return composites;
    }

    public void setComposites(CompositesDTO composites) {
        this.composites = composites;
    }

    public RoleRepresentationDTO composite(Boolean composite) {
        this.composite = composite;
        return this;
    }


    public Boolean isComposite() {
        return composite;
    }

    public void setComposite(Boolean composite) {
        this.composite = composite;
    }

    public RoleRepresentationDTO clientRole(Boolean clientRole) {
        this.clientRole = clientRole;
        return this;
    }


    public Boolean isClientRole() {
        return clientRole;
    }

    public void setClientRole(Boolean clientRole) {
        this.clientRole = clientRole;
    }

    public RoleRepresentationDTO containerId(String containerId) {
        this.containerId = containerId;
        return this;
    }


    public String getContainerId() {
        return containerId;
    }

    public void setContainerId(String containerId) {
        this.containerId = containerId;
    }

    public RoleRepresentationDTO attributes(Map<String, List<String>> attributes) {
        this.attributes = attributes;
        return this;
    }

    public RoleRepresentationDTO putAttributesItem(String key, List<String> attributesItem) {
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
        RoleRepresentationDTO roleRepresentation = (RoleRepresentationDTO) o;
        return Objects.equals(this.id, roleRepresentation.id) &&
                Objects.equals(this.name, roleRepresentation.name) &&
                Objects.equals(this.description, roleRepresentation.description) &&
                Objects.equals(this.scopeParamRequired, roleRepresentation.scopeParamRequired) &&
                Objects.equals(this.composites, roleRepresentation.composites) &&
                Objects.equals(this.composite, roleRepresentation.composite) &&
                Objects.equals(this.clientRole, roleRepresentation.clientRole) &&
                Objects.equals(this.containerId, roleRepresentation.containerId) &&
                Objects.equals(this.attributes, roleRepresentation.attributes);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, description, scopeParamRequired, composites, composite, clientRole, containerId, attributes);
    }

    @Override
    public String toString() {

        String sb = "class RoleRepresentationDTO {\n" +
                "    id: " + toIndentedString(id) + "\n" +
                "    name: " + toIndentedString(name) + "\n" +
                "    description: " + toIndentedString(description) + "\n" +
                "    scopeParamRequired: " + toIndentedString(scopeParamRequired) + "\n" +
                "    composites: " + toIndentedString(composites) + "\n" +
                "    composite: " + toIndentedString(composite) + "\n" +
                "    clientRole: " + toIndentedString(clientRole) + "\n" +
                "    containerId: " + toIndentedString(containerId) + "\n" +
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

