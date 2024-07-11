package it.getinsight.module.keycloak.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


public class ScopeMappingRepresentationDTO {
    @JsonProperty("self")
    private String self = null;

    @JsonProperty("client")
    private String client = null;

    @JsonProperty("clientTemplate")
    private String clientTemplate = null;

    @JsonProperty("clientScope")
    private String clientScope = null;

    @JsonProperty("roles")
    private List<String> roles = null;

    public ScopeMappingRepresentationDTO self(String self) {
        this.self = self;
        return this;
    }


    public String getSelf() {
        return self;
    }

    public void setSelf(String self) {
        this.self = self;
    }

    public ScopeMappingRepresentationDTO client(String client) {
        this.client = client;
        return this;
    }


    public String getClient() {
        return client;
    }

    public void setClient(String client) {
        this.client = client;
    }

    public ScopeMappingRepresentationDTO clientTemplate(String clientTemplate) {
        this.clientTemplate = clientTemplate;
        return this;
    }


    public String getClientTemplate() {
        return clientTemplate;
    }

    public void setClientTemplate(String clientTemplate) {
        this.clientTemplate = clientTemplate;
    }

    public ScopeMappingRepresentationDTO clientScope(String clientScope) {
        this.clientScope = clientScope;
        return this;
    }


    public String getClientScope() {
        return clientScope;
    }

    public void setClientScope(String clientScope) {
        this.clientScope = clientScope;
    }

    public ScopeMappingRepresentationDTO roles(List<String> roles) {
        this.roles = roles;
        return this;
    }

    public ScopeMappingRepresentationDTO addRolesItem(String rolesItem) {
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


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ScopeMappingRepresentationDTO scopeMappingRepresentation = (ScopeMappingRepresentationDTO) o;
        return Objects.equals(this.self, scopeMappingRepresentation.self) &&
                Objects.equals(this.client, scopeMappingRepresentation.client) &&
                Objects.equals(this.clientTemplate, scopeMappingRepresentation.clientTemplate) &&
                Objects.equals(this.clientScope, scopeMappingRepresentation.clientScope) &&
                Objects.equals(this.roles, scopeMappingRepresentation.roles);
    }

    @Override
    public int hashCode() {
        return Objects.hash(self, client, clientTemplate, clientScope, roles);
    }

    @Override
    public String toString() {

        String sb = "class ScopeMappingRepresentationDTO {\n" +
                "    self: " + toIndentedString(self) + "\n" +
                "    client: " + toIndentedString(client) + "\n" +
                "    clientTemplate: " + toIndentedString(clientTemplate) + "\n" +
                "    clientScope: " + toIndentedString(clientScope) + "\n" +
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

