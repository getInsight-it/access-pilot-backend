package it.getinsight.module.keycloak.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Map;
import java.util.Objects;


public class UserFederationMapperRepresentationDTO {
    @JsonProperty("id")
    private String id = null;

    @JsonProperty("name")
    private String name = null;

    @JsonProperty("federationProviderDisplayName")
    private String federationProviderDisplayName = null;

    @JsonProperty("federationMapperType")
    private String federationMapperType = null;

    @JsonProperty("config")
    private Map<String, String> config = null;

    public UserFederationMapperRepresentationDTO id(String id) {
        this.id = id;
        return this;
    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public UserFederationMapperRepresentationDTO name(String name) {
        this.name = name;
        return this;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public UserFederationMapperRepresentationDTO federationProviderDisplayName(String federationProviderDisplayName) {
        this.federationProviderDisplayName = federationProviderDisplayName;
        return this;
    }


    public String getFederationProviderDisplayName() {
        return federationProviderDisplayName;
    }

    public void setFederationProviderDisplayName(String federationProviderDisplayName) {
        this.federationProviderDisplayName = federationProviderDisplayName;
    }

    public UserFederationMapperRepresentationDTO federationMapperType(String federationMapperType) {
        this.federationMapperType = federationMapperType;
        return this;
    }


    public String getFederationMapperType() {
        return federationMapperType;
    }

    public void setFederationMapperType(String federationMapperType) {
        this.federationMapperType = federationMapperType;
    }

    public UserFederationMapperRepresentationDTO config(Map<String, String> config) {
        this.config = config;
        return this;
    }

    public UserFederationMapperRepresentationDTO putConfigItem(String key, String configItem) {
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
        UserFederationMapperRepresentationDTO userFederationMapperRepresentation = (UserFederationMapperRepresentationDTO) o;
        return Objects.equals(this.id, userFederationMapperRepresentation.id) &&
                Objects.equals(this.name, userFederationMapperRepresentation.name) &&
                Objects.equals(this.federationProviderDisplayName, userFederationMapperRepresentation.federationProviderDisplayName) &&
                Objects.equals(this.federationMapperType, userFederationMapperRepresentation.federationMapperType) &&
                Objects.equals(this.config, userFederationMapperRepresentation.config);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, federationProviderDisplayName, federationMapperType, config);
    }

    @Override
    public String toString() {

        String sb = "class UserFederationMapperRepresentationDTO {\n" +
                "    id: " + toIndentedString(id) + "\n" +
                "    name: " + toIndentedString(name) + "\n" +
                "    federationProviderDisplayName: " + toIndentedString(federationProviderDisplayName) + "\n" +
                "    federationMapperType: " + toIndentedString(federationMapperType) + "\n" +
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

