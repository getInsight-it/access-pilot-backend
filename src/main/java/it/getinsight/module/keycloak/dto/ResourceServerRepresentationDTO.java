package it.getinsight.module.keycloak.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonValue;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


public class ResourceServerRepresentationDTO {
    @JsonProperty("id")
    private String id = null;

    @JsonProperty("clientId")
    private String clientId = null;

    @JsonProperty("name")
    private String name = null;

    @JsonProperty("allowRemoteResourceManagement")
    private Boolean allowRemoteResourceManagement = null;
    @JsonProperty("policyEnforcementMode")
    private PolicyEnforcementModeEnum policyEnforcementMode = null;
    @JsonProperty("resources")
    private List<ResourceRepresentationDTO> resources = null;
    @JsonProperty("policies")
    private List<PolicyRepresentationDTO> policies = null;
    @JsonProperty("scopes")
    private List<ScopeRepresentationDTO> scopes = null;
    @JsonProperty("decisionStrategy")
    private DecisionStrategyEnum decisionStrategy = null;

    public ResourceServerRepresentationDTO id(String id) {
        this.id = id;
        return this;
    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public ResourceServerRepresentationDTO clientId(String clientId) {
        this.clientId = clientId;
        return this;
    }


    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public ResourceServerRepresentationDTO name(String name) {
        this.name = name;
        return this;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ResourceServerRepresentationDTO allowRemoteResourceManagement(Boolean allowRemoteResourceManagement) {
        this.allowRemoteResourceManagement = allowRemoteResourceManagement;
        return this;
    }


    public Boolean isAllowRemoteResourceManagement() {
        return allowRemoteResourceManagement;
    }

    public void setAllowRemoteResourceManagement(Boolean allowRemoteResourceManagement) {
        this.allowRemoteResourceManagement = allowRemoteResourceManagement;
    }

    public ResourceServerRepresentationDTO policyEnforcementMode(PolicyEnforcementModeEnum policyEnforcementMode) {
        this.policyEnforcementMode = policyEnforcementMode;
        return this;
    }


    public PolicyEnforcementModeEnum getPolicyEnforcementMode() {
        return policyEnforcementMode;
    }

    public void setPolicyEnforcementMode(PolicyEnforcementModeEnum policyEnforcementMode) {
        this.policyEnforcementMode = policyEnforcementMode;
    }

    public ResourceServerRepresentationDTO resources(List<ResourceRepresentationDTO> resources) {
        this.resources = resources;
        return this;
    }

    public ResourceServerRepresentationDTO addResourcesItem(ResourceRepresentationDTO resourcesItem) {
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

    public ResourceServerRepresentationDTO policies(List<PolicyRepresentationDTO> policies) {
        this.policies = policies;
        return this;
    }

    public ResourceServerRepresentationDTO addPoliciesItem(PolicyRepresentationDTO policiesItem) {
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

    public ResourceServerRepresentationDTO scopes(List<ScopeRepresentationDTO> scopes) {
        this.scopes = scopes;
        return this;
    }

    public ResourceServerRepresentationDTO addScopesItem(ScopeRepresentationDTO scopesItem) {
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

    public ResourceServerRepresentationDTO decisionStrategy(DecisionStrategyEnum decisionStrategy) {
        this.decisionStrategy = decisionStrategy;
        return this;
    }


    public DecisionStrategyEnum getDecisionStrategy() {
        return decisionStrategy;
    }

    public void setDecisionStrategy(DecisionStrategyEnum decisionStrategy) {
        this.decisionStrategy = decisionStrategy;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ResourceServerRepresentationDTO resourceServerRepresentation = (ResourceServerRepresentationDTO) o;
        return Objects.equals(this.id, resourceServerRepresentation.id) &&
                Objects.equals(this.clientId, resourceServerRepresentation.clientId) &&
                Objects.equals(this.name, resourceServerRepresentation.name) &&
                Objects.equals(this.allowRemoteResourceManagement, resourceServerRepresentation.allowRemoteResourceManagement) &&
                Objects.equals(this.policyEnforcementMode, resourceServerRepresentation.policyEnforcementMode) &&
                Objects.equals(this.resources, resourceServerRepresentation.resources) &&
                Objects.equals(this.policies, resourceServerRepresentation.policies) &&
                Objects.equals(this.scopes, resourceServerRepresentation.scopes) &&
                Objects.equals(this.decisionStrategy, resourceServerRepresentation.decisionStrategy);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, clientId, name, allowRemoteResourceManagement, policyEnforcementMode, resources, policies, scopes, decisionStrategy);
    }

    @Override
    public String toString() {

        String sb = "class ResourceServerRepresentationDTO {\n" +
                "    id: " + toIndentedString(id) + "\n" +
                "    clientId: " + toIndentedString(clientId) + "\n" +
                "    name: " + toIndentedString(name) + "\n" +
                "    allowRemoteResourceManagement: " + toIndentedString(allowRemoteResourceManagement) + "\n" +
                "    policyEnforcementMode: " + toIndentedString(policyEnforcementMode) + "\n" +
                "    resources: " + toIndentedString(resources) + "\n" +
                "    policies: " + toIndentedString(policies) + "\n" +
                "    scopes: " + toIndentedString(scopes) + "\n" +
                "    decisionStrategy: " + toIndentedString(decisionStrategy) + "\n" +
                "}";
        return sb;
    }

    private String toIndentedString(Object o) {
        if (o == null) {
            return "null";
        }
        return o.toString().replace("\n", "\n    ");
    }

    public enum PolicyEnforcementModeEnum {
        ENFORCING_0_("ENFORCING(0)"),

        PERMISSIVE_1_("PERMISSIVE(1)"),

        DISABLED_2_("DISABLED(2)");

        private final String value;

        PolicyEnforcementModeEnum(String value) {
            this.value = value;
        }

        @JsonCreator
        public static PolicyEnforcementModeEnum fromValue(String text) {
            for (PolicyEnforcementModeEnum b : PolicyEnforcementModeEnum.values()) {
                if (String.valueOf(b.value).equals(text)) {
                    return b;
                }
            }
            return null;
        }

        @Override
        @JsonValue
        public String toString() {
            return String.valueOf(value);
        }
    }


    public enum DecisionStrategyEnum {
        AFFIRMATIVE_0_("AFFIRMATIVE(0)"),

        UNANIMOUS_1_("UNANIMOUS(1)"),

        CONSENSUS_2_("CONSENSUS(2)");

        private final String value;

        DecisionStrategyEnum(String value) {
            this.value = value;
        }

        @JsonCreator
        public static DecisionStrategyEnum fromValue(String text) {
            for (DecisionStrategyEnum b : DecisionStrategyEnum.values()) {
                if (String.valueOf(b.value).equals(text)) {
                    return b;
                }
            }
            return null;
        }

        @Override
        @JsonValue
        public String toString() {
            return String.valueOf(value);
        }
    }
}

