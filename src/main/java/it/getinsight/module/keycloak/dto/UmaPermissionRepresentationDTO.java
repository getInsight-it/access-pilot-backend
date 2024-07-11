package it.getinsight.module.keycloak.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


public class UmaPermissionRepresentationDTO {
    @JsonProperty("type")
    private String type = null;

    @JsonProperty("roles")
    private List<String> roles = null;

    @JsonProperty("groups")
    private List<String> groups = null;

    @JsonProperty("clients")
    private List<String> clients = null;

    @JsonProperty("users")
    private List<String> users = null;

    @JsonProperty("condition")
    private String condition = null;

    public UmaPermissionRepresentationDTO type(String type) {
        this.type = type;
        return this;
    }


    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public UmaPermissionRepresentationDTO roles(List<String> roles) {
        this.roles = roles;
        return this;
    }

    public UmaPermissionRepresentationDTO addRolesItem(String rolesItem) {
        if (this.roles == null) {
            this.roles = new ArrayList<String>();
        }
        this.roles.add(rolesItem);
        return this;
    }


    public List<String> getRoles() {
        return roles;
    }

    public void setRoles(List<String> roles) {
        this.roles = roles;
    }

    public UmaPermissionRepresentationDTO groups(List<String> groups) {
        this.groups = groups;
        return this;
    }

    public UmaPermissionRepresentationDTO addGroupsItem(String groupsItem) {
        if (this.groups == null) {
            this.groups = new ArrayList<String>();
        }
        this.groups.add(groupsItem);
        return this;
    }


    public List<String> getGroups() {
        return groups;
    }

    public void setGroups(List<String> groups) {
        this.groups = groups;
    }

    public UmaPermissionRepresentationDTO clients(List<String> clients) {
        this.clients = clients;
        return this;
    }

    public UmaPermissionRepresentationDTO addClientsItem(String clientsItem) {
        if (this.clients == null) {
            this.clients = new ArrayList<String>();
        }
        this.clients.add(clientsItem);
        return this;
    }


    public List<String> getClients() {
        return clients;
    }

    public void setClients(List<String> clients) {
        this.clients = clients;
    }

    public UmaPermissionRepresentationDTO users(List<String> users) {
        this.users = users;
        return this;
    }

    public UmaPermissionRepresentationDTO addUsersItem(String usersItem) {
        if (this.users == null) {
            this.users = new ArrayList<String>();
        }
        this.users.add(usersItem);
        return this;
    }


    public List<String> getUsers() {
        return users;
    }

    public void setUsers(List<String> users) {
        this.users = users;
    }

    public UmaPermissionRepresentationDTO condition(String condition) {
        this.condition = condition;
        return this;
    }


    public String getCondition() {
        return condition;
    }

    public void setCondition(String condition) {
        this.condition = condition;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        UmaPermissionRepresentationDTO umaPermissionRepresentation = (UmaPermissionRepresentationDTO) o;
        return Objects.equals(this.type, umaPermissionRepresentation.type) &&
                Objects.equals(this.roles, umaPermissionRepresentation.roles) &&
                Objects.equals(this.groups, umaPermissionRepresentation.groups) &&
                Objects.equals(this.clients, umaPermissionRepresentation.clients) &&
                Objects.equals(this.users, umaPermissionRepresentation.users) &&
                Objects.equals(this.condition, umaPermissionRepresentation.condition);
    }

    @Override
    public int hashCode() {
        return Objects.hash(type, roles, groups, clients, users, condition);
    }

    @Override
    public String toString() {

        String sb = "class UmaPermissionRepresentationDTO {\n" +
                "    type: " + toIndentedString(type) + "\n" +
                "    roles: " + toIndentedString(roles) + "\n" +
                "    groups: " + toIndentedString(groups) + "\n" +
                "    clients: " + toIndentedString(clients) + "\n" +
                "    users: " + toIndentedString(users) + "\n" +
                "    condition: " + toIndentedString(condition) + "\n" +
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

