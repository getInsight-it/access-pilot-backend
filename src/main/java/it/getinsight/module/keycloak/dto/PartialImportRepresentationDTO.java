package it.getinsight.module.keycloak.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


public class PartialImportRepresentationDTO {
    @JsonProperty("ifResourceExists")
    private String ifResourceExists = null;

    @JsonProperty("policy")
    private Object policy = null;

    @JsonProperty("users")
    private List<UserRepresentationDTO> users = null;

    @JsonProperty("clients")
    private List<ClientRepresentationDTO> clients = null;

    @JsonProperty("groups")
    private List<GroupRepresentationDTO> groups = null;

    @JsonProperty("identityProviders")
    private List<IdentityProviderRepresentationDTO> identityProviders = null;

    @JsonProperty("identityProviderMappers")
    private List<IdentityProviderMapperRepresentationDTO> identityProviderMappers = null;

    @JsonProperty("roles")
    private RolesRepresentationDTO roles = null;

    public PartialImportRepresentationDTO ifResourceExists(String ifResourceExists) {
        this.ifResourceExists = ifResourceExists;
        return this;
    }


    public String getIfResourceExists() {
        return ifResourceExists;
    }

    public void setIfResourceExists(String ifResourceExists) {
        this.ifResourceExists = ifResourceExists;
    }

    public PartialImportRepresentationDTO policy(Object policy) {
        this.policy = policy;
        return this;
    }


    public Object getPolicy() {
        return policy;
    }

    public void setPolicy(Object policy) {
        this.policy = policy;
    }

    public PartialImportRepresentationDTO users(List<UserRepresentationDTO> users) {
        this.users = users;
        return this;
    }

    public PartialImportRepresentationDTO addUsersItem(UserRepresentationDTO usersItem) {
        if (this.users == null) {
            this.users = new ArrayList<UserRepresentationDTO>();
        }
        this.users.add(usersItem);
        return this;
    }


    public List<UserRepresentationDTO> getUsers() {
        return users;
    }

    public void setUsers(List<UserRepresentationDTO> users) {
        this.users = users;
    }

    public PartialImportRepresentationDTO clients(List<ClientRepresentationDTO> clients) {
        this.clients = clients;
        return this;
    }

    public PartialImportRepresentationDTO addClientsItem(ClientRepresentationDTO clientsItem) {
        if (this.clients == null) {
            this.clients = new ArrayList<ClientRepresentationDTO>();
        }
        this.clients.add(clientsItem);
        return this;
    }


    public List<ClientRepresentationDTO> getClients() {
        return clients;
    }

    public void setClients(List<ClientRepresentationDTO> clients) {
        this.clients = clients;
    }

    public PartialImportRepresentationDTO groups(List<GroupRepresentationDTO> groups) {
        this.groups = groups;
        return this;
    }

    public PartialImportRepresentationDTO addGroupsItem(GroupRepresentationDTO groupsItem) {
        if (this.groups == null) {
            this.groups = new ArrayList<GroupRepresentationDTO>();
        }
        this.groups.add(groupsItem);
        return this;
    }


    public List<GroupRepresentationDTO> getGroups() {
        return groups;
    }

    public void setGroups(List<GroupRepresentationDTO> groups) {
        this.groups = groups;
    }

    public PartialImportRepresentationDTO identityProviders(List<IdentityProviderRepresentationDTO> identityProviders) {
        this.identityProviders = identityProviders;
        return this;
    }

    public PartialImportRepresentationDTO addIdentityProvidersItem(IdentityProviderRepresentationDTO identityProvidersItem) {
        if (this.identityProviders == null) {
            this.identityProviders = new ArrayList<IdentityProviderRepresentationDTO>();
        }
        this.identityProviders.add(identityProvidersItem);
        return this;
    }


    public List<IdentityProviderRepresentationDTO> getIdentityProviders() {
        return identityProviders;
    }

    public void setIdentityProviders(List<IdentityProviderRepresentationDTO> identityProviders) {
        this.identityProviders = identityProviders;
    }

    public PartialImportRepresentationDTO identityProviderMappers(List<IdentityProviderMapperRepresentationDTO> identityProviderMappers) {
        this.identityProviderMappers = identityProviderMappers;
        return this;
    }

    public PartialImportRepresentationDTO addIdentityProviderMappersItem(IdentityProviderMapperRepresentationDTO identityProviderMappersItem) {
        if (this.identityProviderMappers == null) {
            this.identityProviderMappers = new ArrayList<IdentityProviderMapperRepresentationDTO>();
        }
        this.identityProviderMappers.add(identityProviderMappersItem);
        return this;
    }


    public List<IdentityProviderMapperRepresentationDTO> getIdentityProviderMappers() {
        return identityProviderMappers;
    }

    public void setIdentityProviderMappers(List<IdentityProviderMapperRepresentationDTO> identityProviderMappers) {
        this.identityProviderMappers = identityProviderMappers;
    }

    public PartialImportRepresentationDTO roles(RolesRepresentationDTO roles) {
        this.roles = roles;
        return this;
    }


    public RolesRepresentationDTO getRoles() {
        return roles;
    }

    public void setRoles(RolesRepresentationDTO roles) {
        this.roles = roles;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        PartialImportRepresentationDTO partialImportRepresentation = (PartialImportRepresentationDTO) o;
        return Objects.equals(this.ifResourceExists, partialImportRepresentation.ifResourceExists) &&
                Objects.equals(this.policy, partialImportRepresentation.policy) &&
                Objects.equals(this.users, partialImportRepresentation.users) &&
                Objects.equals(this.clients, partialImportRepresentation.clients) &&
                Objects.equals(this.groups, partialImportRepresentation.groups) &&
                Objects.equals(this.identityProviders, partialImportRepresentation.identityProviders) &&
                Objects.equals(this.identityProviderMappers, partialImportRepresentation.identityProviderMappers) &&
                Objects.equals(this.roles, partialImportRepresentation.roles);
    }

    @Override
    public int hashCode() {
        return Objects.hash(ifResourceExists, policy, users, clients, groups, identityProviders, identityProviderMappers, roles);
    }

    @Override
    public String toString() {

        String sb = "class PartialImportRepresentationDTO {\n" +
                "    ifResourceExists: " + toIndentedString(ifResourceExists) + "\n" +
                "    policy: " + toIndentedString(policy) + "\n" +
                "    users: " + toIndentedString(users) + "\n" +
                "    clients: " + toIndentedString(clients) + "\n" +
                "    groups: " + toIndentedString(groups) + "\n" +
                "    identityProviders: " + toIndentedString(identityProviders) + "\n" +
                "    identityProviderMappers: " + toIndentedString(identityProviderMappers) + "\n" +
                "    roles: " + toIndentedString(roles) + "\n" +
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

