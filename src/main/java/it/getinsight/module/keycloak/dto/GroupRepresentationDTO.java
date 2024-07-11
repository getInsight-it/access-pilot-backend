package it.getinsight.module.keycloak.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;


public class GroupRepresentationDTO {
    @JsonProperty("id")
    private String id = null;

    @JsonProperty("name")
    private String name = null;

    @JsonProperty("path")
    private String path = null;

    @JsonProperty("realmRoles")
    private List<String> realmRoles = null;

    @JsonProperty("clientRoles")
    private Map<String, List<String>> clientRoles = null;

    @JsonProperty("attributes")
    private Map<String, List<String>> attributes = null;

    @JsonProperty("subGroups")
    private List<GroupRepresentationDTO> subGroups = null;

    @JsonProperty("access")
    private Map<String, Boolean> access = null;

    public GroupRepresentationDTO id(String id) {
        this.id = id;
        return this;
    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public GroupRepresentationDTO name(String name) {
        this.name = name;
        return this;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public GroupRepresentationDTO path(String path) {
        this.path = path;
        return this;
    }


    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public GroupRepresentationDTO realmRoles(List<String> realmRoles) {
        this.realmRoles = realmRoles;
        return this;
    }

    public GroupRepresentationDTO addRealmRolesItem(String realmRolesItem) {
        if (this.realmRoles == null) {
            this.realmRoles = new ArrayList<String>();
        }
        this.realmRoles.add(realmRolesItem);
        return this;
    }


    public List<String> getRealmRoles() {
        return realmRoles;
    }

    public void setRealmRoles(List<String> realmRoles) {
        this.realmRoles = realmRoles;
    }

    public GroupRepresentationDTO clientRoles(Map<String, List<String>> clientRoles) {
        this.clientRoles = clientRoles;
        return this;
    }

    public GroupRepresentationDTO putClientRolesItem(String key, List<String> clientRolesItem) {
        if (this.clientRoles == null) {
            this.clientRoles = null;
        }
        this.clientRoles.put(key, clientRolesItem);
        return this;
    }


    public Map<String, List<String>> getClientRoles() {
        return clientRoles;
    }

    public void setClientRoles(Map<String, List<String>> clientRoles) {
        this.clientRoles = clientRoles;
    }

    public GroupRepresentationDTO attributes(Map<String, List<String>> attributes) {
        this.attributes = attributes;
        return this;
    }

    public GroupRepresentationDTO putAttributesItem(String key, List<String> attributesItem) {
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

    public GroupRepresentationDTO subGroups(List<GroupRepresentationDTO> subGroups) {
        this.subGroups = subGroups;
        return this;
    }

    public GroupRepresentationDTO addSubGroupsItem(GroupRepresentationDTO subGroupsItem) {
        if (this.subGroups == null) {
            this.subGroups = new ArrayList<GroupRepresentationDTO>();
        }
        this.subGroups.add(subGroupsItem);
        return this;
    }


    public List<GroupRepresentationDTO> getSubGroups() {
        return subGroups;
    }

    public void setSubGroups(List<GroupRepresentationDTO> subGroups) {
        this.subGroups = subGroups;
    }

    public GroupRepresentationDTO access(Map<String, Boolean> access) {
        this.access = access;
        return this;
    }

    public GroupRepresentationDTO putAccessItem(String key, Boolean accessItem) {
        if (this.access == null) {
            this.access = null;
        }
        this.access.put(key, accessItem);
        return this;
    }


    public Map<String, Boolean> getAccess() {
        return access;
    }

    public void setAccess(Map<String, Boolean> access) {
        this.access = access;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        GroupRepresentationDTO groupRepresentation = (GroupRepresentationDTO) o;
        return Objects.equals(this.id, groupRepresentation.id) &&
                Objects.equals(this.name, groupRepresentation.name) &&
                Objects.equals(this.path, groupRepresentation.path) &&
                Objects.equals(this.realmRoles, groupRepresentation.realmRoles) &&
                Objects.equals(this.clientRoles, groupRepresentation.clientRoles) &&
                Objects.equals(this.attributes, groupRepresentation.attributes) &&
                Objects.equals(this.subGroups, groupRepresentation.subGroups) &&
                Objects.equals(this.access, groupRepresentation.access);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, path, realmRoles, clientRoles, attributes, subGroups, access);
    }

    @Override
    public String toString() {

        String sb = "class GroupRepresentationDTO {\n" +
                "    id: " + toIndentedString(id) + "\n" +
                "    name: " + toIndentedString(name) + "\n" +
                "    path: " + toIndentedString(path) + "\n" +
                "    realmRoles: " + toIndentedString(realmRoles) + "\n" +
                "    clientRoles: " + toIndentedString(clientRoles) + "\n" +
                "    attributes: " + toIndentedString(attributes) + "\n" +
                "    subGroups: " + toIndentedString(subGroups) + "\n" +
                "    access: " + toIndentedString(access) + "\n" +
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

