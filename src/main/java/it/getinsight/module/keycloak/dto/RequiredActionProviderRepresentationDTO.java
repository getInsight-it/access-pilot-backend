package it.getinsight.module.keycloak.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Map;
import java.util.Objects;


public class RequiredActionProviderRepresentationDTO {
    @JsonProperty("alias")
    private String alias = null;

    @JsonProperty("name")
    private String name = null;

    @JsonProperty("enabled")
    private Boolean enabled = null;

    @JsonProperty("defaultAction")
    private Boolean defaultAction = null;

    @JsonProperty("providerId")
    private String providerId = null;

    @JsonProperty("priority")
    private Integer priority = null;

    @JsonProperty("config")
    private Map<String, String> config = null;

    public RequiredActionProviderRepresentationDTO alias(String alias) {
        this.alias = alias;
        return this;
    }


    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

    public RequiredActionProviderRepresentationDTO name(String name) {
        this.name = name;
        return this;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public RequiredActionProviderRepresentationDTO enabled(Boolean enabled) {
        this.enabled = enabled;
        return this;
    }


    public Boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(Boolean enabled) {
        this.enabled = enabled;
    }

    public RequiredActionProviderRepresentationDTO defaultAction(Boolean defaultAction) {
        this.defaultAction = defaultAction;
        return this;
    }


    public Boolean isDefaultAction() {
        return defaultAction;
    }

    public void setDefaultAction(Boolean defaultAction) {
        this.defaultAction = defaultAction;
    }

    public RequiredActionProviderRepresentationDTO providerId(String providerId) {
        this.providerId = providerId;
        return this;
    }


    public String getProviderId() {
        return providerId;
    }

    public void setProviderId(String providerId) {
        this.providerId = providerId;
    }

    public RequiredActionProviderRepresentationDTO priority(Integer priority) {
        this.priority = priority;
        return this;
    }


    public Integer getPriority() {
        return priority;
    }

    public void setPriority(Integer priority) {
        this.priority = priority;
    }

    public RequiredActionProviderRepresentationDTO config(Map<String, String> config) {
        this.config = config;
        return this;
    }

    public RequiredActionProviderRepresentationDTO putConfigItem(String key, String configItem) {
        if (this.config == null) {
            this.config = null;
        }
        this.config.put(key, configItem);
        return this;
    }


    public Map<String, String> getConfig() {
        return config;
    }

    public void setConfig(Map<String, String> config) {
        this.config = config;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        RequiredActionProviderRepresentationDTO requiredActionProviderRepresentation = (RequiredActionProviderRepresentationDTO) o;
        return Objects.equals(this.alias, requiredActionProviderRepresentation.alias) &&
                Objects.equals(this.name, requiredActionProviderRepresentation.name) &&
                Objects.equals(this.enabled, requiredActionProviderRepresentation.enabled) &&
                Objects.equals(this.defaultAction, requiredActionProviderRepresentation.defaultAction) &&
                Objects.equals(this.providerId, requiredActionProviderRepresentation.providerId) &&
                Objects.equals(this.priority, requiredActionProviderRepresentation.priority) &&
                Objects.equals(this.config, requiredActionProviderRepresentation.config);
    }

    @Override
    public int hashCode() {
        return Objects.hash(alias, name, enabled, defaultAction, providerId, priority, config);
    }

    @Override
    public String toString() {

        String sb = "class RequiredActionProviderRepresentationDTO {\n" +
                "    alias: " + toIndentedString(alias) + "\n" +
                "    name: " + toIndentedString(name) + "\n" +
                "    enabled: " + toIndentedString(enabled) + "\n" +
                "    defaultAction: " + toIndentedString(defaultAction) + "\n" +
                "    providerId: " + toIndentedString(providerId) + "\n" +
                "    priority: " + toIndentedString(priority) + "\n" +
                "    config: " + toIndentedString(config) + "\n" +
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

