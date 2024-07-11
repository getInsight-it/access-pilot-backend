package it.getinsight.module.keycloak.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonValue;

import java.util.ArrayList;
import java.util.List;


public class AbstractPolicyRepresentationDTO {
    @JsonProperty("id")
    private String id = null;

    @JsonProperty("type")
    private String type = null;
    @JsonProperty("decisionStrategy")
    private DecisionStrategyEnum decisionStrategy = null;
    @JsonProperty("logic")
    private LogicEnum logic = null;
    @JsonProperty("name")
    private String name = null;
    @JsonProperty("description")
    private String description = null;
    @JsonProperty("policies")
    private List<String> policies = null;
    @JsonProperty("resources")
    private List<String> resources = null;
    @JsonProperty("scopes")
    private List<String> scopes = null;
    @JsonProperty("owner")
    private String owner = null;
    @JsonProperty("resourcesData")
    private List<ResourceRepresentationDTO> resourcesData = null;
    @JsonProperty("scopesData")
    private List<ScopeRepresentationDTO> scopesData = null;

    public AbstractPolicyRepresentationDTO id(String id) {
        this.id = id;
        return this;
    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public AbstractPolicyRepresentationDTO type(String type) {
        this.type = type;
        return this;
    }


    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public AbstractPolicyRepresentationDTO decisionStrategy(DecisionStrategyEnum decisionStrategy) {
        this.decisionStrategy = decisionStrategy;
        return this;
    }


    public DecisionStrategyEnum getDecisionStrategy() {
        return decisionStrategy;
    }

    public void setDecisionStrategy(DecisionStrategyEnum decisionStrategy) {
        this.decisionStrategy = decisionStrategy;
    }

    public AbstractPolicyRepresentationDTO logic(LogicEnum logic) {
        this.logic = logic;
        return this;
    }


    public LogicEnum getLogic() {
        return logic;
    }

    public void setLogic(LogicEnum logic) {
        this.logic = logic;
    }

    public AbstractPolicyRepresentationDTO name(String name) {
        this.name = name;
        return this;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public AbstractPolicyRepresentationDTO description(String description) {
        this.description = description;
        return this;
    }


    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public AbstractPolicyRepresentationDTO policies(List<String> policies) {
        this.policies = policies;
        return this;
    }

    public AbstractPolicyRepresentationDTO addPoliciesItem(String policiesItem) {
        if (this.policies == null) {
            this.policies = new ArrayList<>();
        }
        this.policies.add(policiesItem);
        return this;
    }


    public List<String> getPolicies() {
        return policies;
    }

    public void setPolicies(List<String> policies) {
        this.policies = policies;
    }

    public AbstractPolicyRepresentationDTO resources(List<String> resources) {
        this.resources = resources;
        return this;
    }

    public AbstractPolicyRepresentationDTO addResourcesItem(String resourcesItem) {
        if (this.resources == null) {
            this.resources = new ArrayList<>();
        }
        this.resources.add(resourcesItem);
        return this;
    }


    public List<String> getResources() {
        return resources;
    }

    public void setResources(List<String> resources) {
        this.resources = resources;
    }

    public AbstractPolicyRepresentationDTO scopes(List<String> scopes) {
        this.scopes = scopes;
        return this;
    }

    public AbstractPolicyRepresentationDTO addScopesItem(String scopesItem) {
        if (this.scopes == null) {
            this.scopes = new ArrayList<>();
        }
        this.scopes.add(scopesItem);
        return this;
    }


    public List<String> getScopes() {
        return scopes;
    }

    public void setScopes(List<String> scopes) {
        this.scopes = scopes;
    }

    public AbstractPolicyRepresentationDTO owner(String owner) {
        this.owner = owner;
        return this;
    }


    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public AbstractPolicyRepresentationDTO resourcesData(List<ResourceRepresentationDTO> resourcesData) {
        this.resourcesData = resourcesData;
        return this;
    }

    public AbstractPolicyRepresentationDTO addResourcesDataItem(ResourceRepresentationDTO resourcesDataItem) {
        if (this.resourcesData == null) {
            this.resourcesData = new ArrayList<>();
        }
        this.resourcesData.add(resourcesDataItem);
        return this;
    }


    public List<ResourceRepresentationDTO> getResourcesData() {
        return resourcesData;
    }

    public void setResourcesData(List<ResourceRepresentationDTO> resourcesData) {
        this.resourcesData = resourcesData;
    }

    public AbstractPolicyRepresentationDTO scopesData(List<ScopeRepresentationDTO> scopesData) {
        this.scopesData = scopesData;
        return this;
    }

    public AbstractPolicyRepresentationDTO addScopesDataItem(ScopeRepresentationDTO scopesDataItem) {
        if (this.scopesData == null) {
            this.scopesData = new ArrayList<>();
        }
        this.scopesData.add(scopesDataItem);
        return this;
    }


    public List<ScopeRepresentationDTO> getScopesData() {
        return scopesData;
    }

    public void setScopesData(List<ScopeRepresentationDTO> scopesData) {
        this.scopesData = scopesData;
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


    public enum LogicEnum {
        POSITIVE_0_("POSITIVE(0)"),

        NEGATIVE_1_("NEGATIVE(1)");

        private final String value;

        LogicEnum(String value) {
            this.value = value;
        }

        @JsonCreator
        public static LogicEnum fromValue(String text) {
            for (LogicEnum b : LogicEnum.values()) {
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

