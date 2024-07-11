package it.getinsight.module.keycloak.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Map;
import java.util.Objects;


public class ComponentRepresentationDTO {
    @JsonProperty("id")
    private String id = null;

    @JsonProperty("name")
    private String name = null;

    @JsonProperty("providerId")
    private String providerId = null;

    @JsonProperty("providerType")
    private String providerType = null;

    @JsonProperty("parentId")
    private String parentId = null;

    @JsonProperty("subType")
    private String subType = null;

    @JsonProperty("config")
    private Map<String, String> config = null;

    public ComponentRepresentationDTO id(String id) {
        this.id = id;
        return this;
    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public ComponentRepresentationDTO name(String name) {
        this.name = name;
        return this;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ComponentRepresentationDTO providerId(String providerId) {
        this.providerId = providerId;
        return this;
    }


    public String getProviderId() {
        return providerId;
    }

    public void setProviderId(String providerId) {
        this.providerId = providerId;
    }

    public ComponentRepresentationDTO providerType(String providerType) {
        this.providerType = providerType;
        return this;
    }


    public String getProviderType() {
        return providerType;
    }

    public void setProviderType(String providerType) {
        this.providerType = providerType;
    }

    public ComponentRepresentationDTO parentId(String parentId) {
        this.parentId = parentId;
        return this;
    }


    public String getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }

    public ComponentRepresentationDTO subType(String subType) {
        this.subType = subType;
        return this;
    }


    public String getSubType() {
        return subType;
    }

    public void setSubType(String subType) {
        this.subType = subType;
    }

    public ComponentRepresentationDTO config(Map<String, String> config) {
        this.config = config;
        return this;
    }

    public ComponentRepresentationDTO putConfigItem(String key, String configItem) {
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
        ComponentRepresentationDTO componentRepresentation = (ComponentRepresentationDTO) o;
        return Objects.equals(this.id, componentRepresentation.id) &&
                Objects.equals(this.name, componentRepresentation.name) &&
                Objects.equals(this.providerId, componentRepresentation.providerId) &&
                Objects.equals(this.providerType, componentRepresentation.providerType) &&
                Objects.equals(this.parentId, componentRepresentation.parentId) &&
                Objects.equals(this.subType, componentRepresentation.subType) &&
                Objects.equals(this.config, componentRepresentation.config);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, providerId, providerType, parentId, subType, config);
    }

    @Override
    public String toString() {

        String sb = "class ComponentRepresentationDTO {\n" +
                "    id: " + toIndentedString(id) + "\n" +
                "    name: " + toIndentedString(name) + "\n" +
                "    providerId: " + toIndentedString(providerId) + "\n" +
                "    providerType: " + toIndentedString(providerType) + "\n" +
                "    parentId: " + toIndentedString(parentId) + "\n" +
                "    subType: " + toIndentedString(subType) + "\n" +
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

