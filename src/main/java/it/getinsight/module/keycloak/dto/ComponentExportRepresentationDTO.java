package it.getinsight.module.keycloak.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Map;
import java.util.Objects;


public class ComponentExportRepresentationDTO {
    @JsonProperty("id")
    private String id = null;

    @JsonProperty("name")
    private String name = null;

    @JsonProperty("providerId")
    private String providerId = null;

    @JsonProperty("subType")
    private String subType = null;

    @JsonProperty("config")
    private Map<String, String> config = null;

    @JsonProperty("subComponents")
    private Map<String, ComponentExportRepresentationDTO> subComponents = null;

    public ComponentExportRepresentationDTO id(String id) {
        this.id = id;
        return this;
    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public ComponentExportRepresentationDTO name(String name) {
        this.name = name;
        return this;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ComponentExportRepresentationDTO providerId(String providerId) {
        this.providerId = providerId;
        return this;
    }


    public String getProviderId() {
        return providerId;
    }

    public void setProviderId(String providerId) {
        this.providerId = providerId;
    }

    public ComponentExportRepresentationDTO subType(String subType) {
        this.subType = subType;
        return this;
    }


    public String getSubType() {
        return subType;
    }

    public void setSubType(String subType) {
        this.subType = subType;
    }

    public ComponentExportRepresentationDTO config(Map<String, String> config) {
        this.config = config;
        return this;
    }

    public ComponentExportRepresentationDTO putConfigItem(String key, String configItem) {
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

    public ComponentExportRepresentationDTO subComponents(Map<String, ComponentExportRepresentationDTO> subComponents) {
        this.subComponents = subComponents;
        return this;
    }

    public ComponentExportRepresentationDTO putSubComponentsItem(String key, ComponentExportRepresentationDTO subComponentsItem) {
        if (this.subComponents == null) {
            this.subComponents = null;
        }
        this.subComponents.put(key, subComponentsItem);
        return this;
    }


    public Map<String, ComponentExportRepresentationDTO> getSubComponents() {
        return subComponents;
    }

    public void setSubComponents(Map<String, ComponentExportRepresentationDTO> subComponents) {
        this.subComponents = subComponents;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ComponentExportRepresentationDTO componentExportRepresentation = (ComponentExportRepresentationDTO) o;
        return Objects.equals(this.id, componentExportRepresentation.id) &&
                Objects.equals(this.name, componentExportRepresentation.name) &&
                Objects.equals(this.providerId, componentExportRepresentation.providerId) &&
                Objects.equals(this.subType, componentExportRepresentation.subType) &&
                Objects.equals(this.config, componentExportRepresentation.config) &&
                Objects.equals(this.subComponents, componentExportRepresentation.subComponents);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, providerId, subType, config, subComponents);
    }

    @Override
    public String toString() {

        String sb = "class ComponentExportRepresentationDTO {\n" +
                "    id: " + toIndentedString(id) + "\n" +
                "    name: " + toIndentedString(name) + "\n" +
                "    providerId: " + toIndentedString(providerId) + "\n" +
                "    subType: " + toIndentedString(subType) + "\n" +
                "    config: " + toIndentedString(config) + "\n" +
                "    subComponents: " + toIndentedString(subComponents) + "\n" +
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

