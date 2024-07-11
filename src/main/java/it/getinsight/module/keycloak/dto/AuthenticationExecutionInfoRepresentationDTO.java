package it.getinsight.module.keycloak.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


public class AuthenticationExecutionInfoRepresentationDTO {
    @JsonProperty("id")
    private String id = null;

    @JsonProperty("displayName")
    private String displayName = null;

    @JsonProperty("alias")
    private String alias = null;

    @JsonProperty("description")
    private String description = null;

    @JsonProperty("requirement")
    private String requirement = null;

    @JsonProperty("requirementChoices")
    private List<String> requirementChoices = null;

    @JsonProperty("configurable")
    private Boolean configurable = null;

    @JsonProperty("providerId")
    private String providerId = null;

    @JsonProperty("authenticationConfig")
    private String authenticationConfig = null;

    @JsonProperty("authenticationFlow")
    private Boolean authenticationFlow = null;

    @JsonProperty("level")
    private Integer level = null;

    @JsonProperty("index")
    private Integer index = null;

    @JsonProperty("flowId")
    private String flowId = null;

    public AuthenticationExecutionInfoRepresentationDTO id(String id) {
        this.id = id;
        return this;
    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public AuthenticationExecutionInfoRepresentationDTO displayName(String displayName) {
        this.displayName = displayName;
        return this;
    }


    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public AuthenticationExecutionInfoRepresentationDTO alias(String alias) {
        this.alias = alias;
        return this;
    }


    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

    public AuthenticationExecutionInfoRepresentationDTO description(String description) {
        this.description = description;
        return this;
    }


    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public AuthenticationExecutionInfoRepresentationDTO requirement(String requirement) {
        this.requirement = requirement;
        return this;
    }


    public String getRequirement() {
        return requirement;
    }

    public void setRequirement(String requirement) {
        this.requirement = requirement;
    }

    public AuthenticationExecutionInfoRepresentationDTO requirementChoices(List<String> requirementChoices) {
        this.requirementChoices = requirementChoices;
        return this;
    }

    public AuthenticationExecutionInfoRepresentationDTO addRequirementChoicesItem(String requirementChoicesItem) {
        if (this.requirementChoices == null) {
            this.requirementChoices = new ArrayList<String>();
        }
        this.requirementChoices.add(requirementChoicesItem);
        return this;
    }


    public List<String> getRequirementChoices() {
        return requirementChoices;
    }

    public void setRequirementChoices(List<String> requirementChoices) {
        this.requirementChoices = requirementChoices;
    }

    public AuthenticationExecutionInfoRepresentationDTO configurable(Boolean configurable) {
        this.configurable = configurable;
        return this;
    }


    public Boolean isConfigurable() {
        return configurable;
    }

    public void setConfigurable(Boolean configurable) {
        this.configurable = configurable;
    }

    public AuthenticationExecutionInfoRepresentationDTO providerId(String providerId) {
        this.providerId = providerId;
        return this;
    }


    public String getProviderId() {
        return providerId;
    }

    public void setProviderId(String providerId) {
        this.providerId = providerId;
    }

    public AuthenticationExecutionInfoRepresentationDTO authenticationConfig(String authenticationConfig) {
        this.authenticationConfig = authenticationConfig;
        return this;
    }


    public String getAuthenticationConfig() {
        return authenticationConfig;
    }

    public void setAuthenticationConfig(String authenticationConfig) {
        this.authenticationConfig = authenticationConfig;
    }

    public AuthenticationExecutionInfoRepresentationDTO authenticationFlow(Boolean authenticationFlow) {
        this.authenticationFlow = authenticationFlow;
        return this;
    }


    public Boolean isAuthenticationFlow() {
        return authenticationFlow;
    }

    public void setAuthenticationFlow(Boolean authenticationFlow) {
        this.authenticationFlow = authenticationFlow;
    }

    public AuthenticationExecutionInfoRepresentationDTO level(Integer level) {
        this.level = level;
        return this;
    }


    public Integer getLevel() {
        return level;
    }

    public void setLevel(Integer level) {
        this.level = level;
    }

    public AuthenticationExecutionInfoRepresentationDTO index(Integer index) {
        this.index = index;
        return this;
    }


    public Integer getIndex() {
        return index;
    }

    public void setIndex(Integer index) {
        this.index = index;
    }

    public AuthenticationExecutionInfoRepresentationDTO flowId(String flowId) {
        this.flowId = flowId;
        return this;
    }


    public String getFlowId() {
        return flowId;
    }

    public void setFlowId(String flowId) {
        this.flowId = flowId;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        AuthenticationExecutionInfoRepresentationDTO authenticationExecutionInfoRepresentation = (AuthenticationExecutionInfoRepresentationDTO) o;
        return Objects.equals(this.id, authenticationExecutionInfoRepresentation.id) &&
                Objects.equals(this.displayName, authenticationExecutionInfoRepresentation.displayName) &&
                Objects.equals(this.alias, authenticationExecutionInfoRepresentation.alias) &&
                Objects.equals(this.description, authenticationExecutionInfoRepresentation.description) &&
                Objects.equals(this.requirement, authenticationExecutionInfoRepresentation.requirement) &&
                Objects.equals(this.requirementChoices, authenticationExecutionInfoRepresentation.requirementChoices) &&
                Objects.equals(this.configurable, authenticationExecutionInfoRepresentation.configurable) &&
                Objects.equals(this.providerId, authenticationExecutionInfoRepresentation.providerId) &&
                Objects.equals(this.authenticationConfig, authenticationExecutionInfoRepresentation.authenticationConfig) &&
                Objects.equals(this.authenticationFlow, authenticationExecutionInfoRepresentation.authenticationFlow) &&
                Objects.equals(this.level, authenticationExecutionInfoRepresentation.level) &&
                Objects.equals(this.index, authenticationExecutionInfoRepresentation.index) &&
                Objects.equals(this.flowId, authenticationExecutionInfoRepresentation.flowId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, displayName, alias, description, requirement, requirementChoices, configurable, providerId, authenticationConfig, authenticationFlow, level, index, flowId);
    }

    @Override
    public String toString() {

        String sb = "class AuthenticationExecutionInfoRepresentationDTO {\n" +
                "    id: " + toIndentedString(id) + "\n" +
                "    displayName: " + toIndentedString(displayName) + "\n" +
                "    alias: " + toIndentedString(alias) + "\n" +
                "    description: " + toIndentedString(description) + "\n" +
                "    requirement: " + toIndentedString(requirement) + "\n" +
                "    requirementChoices: " + toIndentedString(requirementChoices) + "\n" +
                "    configurable: " + toIndentedString(configurable) + "\n" +
                "    providerId: " + toIndentedString(providerId) + "\n" +
                "    authenticationConfig: " + toIndentedString(authenticationConfig) + "\n" +
                "    authenticationFlow: " + toIndentedString(authenticationFlow) + "\n" +
                "    level: " + toIndentedString(level) + "\n" +
                "    index: " + toIndentedString(index) + "\n" +
                "    flowId: " + toIndentedString(flowId) + "\n" +
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

