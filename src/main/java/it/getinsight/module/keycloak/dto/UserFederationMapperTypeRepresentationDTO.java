package it.getinsight.module.keycloak.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;


public class UserFederationMapperTypeRepresentationDTO {
    @JsonProperty("id")
    private String id = null;

    @JsonProperty("name")
    private String name = null;

    @JsonProperty("category")
    private String category = null;

    @JsonProperty("helpText")
    private String helpText = null;

    @JsonProperty("syncConfig")
    private UserFederationMapperSyncConfigRepresentationDTO syncConfig = null;

    @JsonProperty("properties")
    private List<ConfigPropertyRepresentationDTO> properties = null;

    @JsonProperty("defaultConfig")
    private Map<String, String> defaultConfig = null;

    public UserFederationMapperTypeRepresentationDTO id(String id) {
        this.id = id;
        return this;
    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public UserFederationMapperTypeRepresentationDTO name(String name) {
        this.name = name;
        return this;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public UserFederationMapperTypeRepresentationDTO category(String category) {
        this.category = category;
        return this;
    }


    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public UserFederationMapperTypeRepresentationDTO helpText(String helpText) {
        this.helpText = helpText;
        return this;
    }


    public String getHelpText() {
        return helpText;
    }

    public void setHelpText(String helpText) {
        this.helpText = helpText;
    }

    public UserFederationMapperTypeRepresentationDTO syncConfig(UserFederationMapperSyncConfigRepresentationDTO syncConfig) {
        this.syncConfig = syncConfig;
        return this;
    }


    public UserFederationMapperSyncConfigRepresentationDTO getSyncConfig() {
        return syncConfig;
    }

    public void setSyncConfig(UserFederationMapperSyncConfigRepresentationDTO syncConfig) {
        this.syncConfig = syncConfig;
    }

    public UserFederationMapperTypeRepresentationDTO properties(List<ConfigPropertyRepresentationDTO> properties) {
        this.properties = properties;
        return this;
    }

    public UserFederationMapperTypeRepresentationDTO addPropertiesItem(ConfigPropertyRepresentationDTO propertiesItem) {
        if (this.properties == null) {
            this.properties = new ArrayList<ConfigPropertyRepresentationDTO>();
        }
        this.properties.add(propertiesItem);
        return this;
    }


    public List<ConfigPropertyRepresentationDTO> getProperties() {
        return properties;
    }

    public void setProperties(List<ConfigPropertyRepresentationDTO> properties) {
        this.properties = properties;
    }

    public UserFederationMapperTypeRepresentationDTO defaultConfig(Map<String, String> defaultConfig) {
        this.defaultConfig = defaultConfig;
        return this;
    }

    public UserFederationMapperTypeRepresentationDTO putDefaultConfigItem(String key, String defaultConfigItem) {
        if (this.defaultConfig == null) {
            this.defaultConfig = null;
        }
        this.defaultConfig.put(key, defaultConfigItem);
        return this;
    }


    public Map<String, String> getDefaultConfig() {
        return defaultConfig;
    }

    public void setDefaultConfig(Map<String, String> defaultConfig) {
        this.defaultConfig = defaultConfig;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        UserFederationMapperTypeRepresentationDTO userFederationMapperTypeRepresentation = (UserFederationMapperTypeRepresentationDTO) o;
        return Objects.equals(this.id, userFederationMapperTypeRepresentation.id) &&
                Objects.equals(this.name, userFederationMapperTypeRepresentation.name) &&
                Objects.equals(this.category, userFederationMapperTypeRepresentation.category) &&
                Objects.equals(this.helpText, userFederationMapperTypeRepresentation.helpText) &&
                Objects.equals(this.syncConfig, userFederationMapperTypeRepresentation.syncConfig) &&
                Objects.equals(this.properties, userFederationMapperTypeRepresentation.properties) &&
                Objects.equals(this.defaultConfig, userFederationMapperTypeRepresentation.defaultConfig);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, category, helpText, syncConfig, properties, defaultConfig);
    }

    @Override
    public String toString() {

        String sb = "class UserFederationMapperTypeRepresentationDTO {\n" +
                "    id: " + toIndentedString(id) + "\n" +
                "    name: " + toIndentedString(name) + "\n" +
                "    category: " + toIndentedString(category) + "\n" +
                "    helpText: " + toIndentedString(helpText) + "\n" +
                "    syncConfig: " + toIndentedString(syncConfig) + "\n" +
                "    properties: " + toIndentedString(properties) + "\n" +
                "    defaultConfig: " + toIndentedString(defaultConfig) + "\n" +
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

