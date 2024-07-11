package it.getinsight.module.keycloak.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


public class AuthenticationFlowRepresentationDTO {
    @JsonProperty("id")
    private String id = null;

    @JsonProperty("alias")
    private String alias = null;

    @JsonProperty("description")
    private String description = null;

    @JsonProperty("providerId")
    private String providerId = null;

    @JsonProperty("topLevel")
    private Boolean topLevel = null;

    @JsonProperty("builtIn")
    private Boolean builtIn = null;

    @JsonProperty("authenticationExecutions")
    private List<AuthenticationExecutionExportRepresentationDTO> authenticationExecutions = null;

    public AuthenticationFlowRepresentationDTO id(String id) {
        this.id = id;
        return this;
    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public AuthenticationFlowRepresentationDTO alias(String alias) {
        this.alias = alias;
        return this;
    }


    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

    public AuthenticationFlowRepresentationDTO description(String description) {
        this.description = description;
        return this;
    }


    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public AuthenticationFlowRepresentationDTO providerId(String providerId) {
        this.providerId = providerId;
        return this;
    }


    public String getProviderId() {
        return providerId;
    }

    public void setProviderId(String providerId) {
        this.providerId = providerId;
    }

    public AuthenticationFlowRepresentationDTO topLevel(Boolean topLevel) {
        this.topLevel = topLevel;
        return this;
    }


    public Boolean isTopLevel() {
        return topLevel;
    }

    public void setTopLevel(Boolean topLevel) {
        this.topLevel = topLevel;
    }

    public AuthenticationFlowRepresentationDTO builtIn(Boolean builtIn) {
        this.builtIn = builtIn;
        return this;
    }


    public Boolean isBuiltIn() {
        return builtIn;
    }

    public void setBuiltIn(Boolean builtIn) {
        this.builtIn = builtIn;
    }

    public AuthenticationFlowRepresentationDTO authenticationExecutions(List<AuthenticationExecutionExportRepresentationDTO> authenticationExecutions) {
        this.authenticationExecutions = authenticationExecutions;
        return this;
    }

    public AuthenticationFlowRepresentationDTO addAuthenticationExecutionsItem(AuthenticationExecutionExportRepresentationDTO authenticationExecutionsItem) {
        if (this.authenticationExecutions == null) {
            this.authenticationExecutions = new ArrayList<AuthenticationExecutionExportRepresentationDTO>();
        }
        this.authenticationExecutions.add(authenticationExecutionsItem);
        return this;
    }


    public List<AuthenticationExecutionExportRepresentationDTO> getAuthenticationExecutions() {
        return authenticationExecutions;
    }

    public void setAuthenticationExecutions(List<AuthenticationExecutionExportRepresentationDTO> authenticationExecutions) {
        this.authenticationExecutions = authenticationExecutions;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        AuthenticationFlowRepresentationDTO authenticationFlowRepresentation = (AuthenticationFlowRepresentationDTO) o;
        return Objects.equals(this.id, authenticationFlowRepresentation.id) &&
                Objects.equals(this.alias, authenticationFlowRepresentation.alias) &&
                Objects.equals(this.description, authenticationFlowRepresentation.description) &&
                Objects.equals(this.providerId, authenticationFlowRepresentation.providerId) &&
                Objects.equals(this.topLevel, authenticationFlowRepresentation.topLevel) &&
                Objects.equals(this.builtIn, authenticationFlowRepresentation.builtIn) &&
                Objects.equals(this.authenticationExecutions, authenticationFlowRepresentation.authenticationExecutions);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, alias, description, providerId, topLevel, builtIn, authenticationExecutions);
    }

    @Override
    public String toString() {

        String sb = "class AuthenticationFlowRepresentationDTO {\n" +
                "    id: " + toIndentedString(id) + "\n" +
                "    alias: " + toIndentedString(alias) + "\n" +
                "    description: " + toIndentedString(description) + "\n" +
                "    providerId: " + toIndentedString(providerId) + "\n" +
                "    topLevel: " + toIndentedString(topLevel) + "\n" +
                "    builtIn: " + toIndentedString(builtIn) + "\n" +
                "    authenticationExecutions: " + toIndentedString(authenticationExecutions) + "\n" +
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

