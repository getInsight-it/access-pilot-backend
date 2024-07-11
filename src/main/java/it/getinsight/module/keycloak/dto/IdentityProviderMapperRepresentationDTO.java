package it.getinsight.module.keycloak.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Map;
import java.util.Objects;


public class IdentityProviderMapperRepresentationDTO {
    @JsonProperty("id")
    private String id = null;

    @JsonProperty("name")
    private String name = null;

    @JsonProperty("identityProviderAlias")
    private String identityProviderAlias = null;

    @JsonProperty("identityProviderMapper")
    private String identityProviderMapper = null;

    @JsonProperty("config")
    private Map<String, String> config = null;

    public IdentityProviderMapperRepresentationDTO id(String id) {
        this.id = id;
        return this;
    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public IdentityProviderMapperRepresentationDTO name(String name) {
        this.name = name;
        return this;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public IdentityProviderMapperRepresentationDTO identityProviderAlias(String identityProviderAlias) {
        this.identityProviderAlias = identityProviderAlias;
        return this;
    }


    public String getIdentityProviderAlias() {
        return identityProviderAlias;
    }

    public void setIdentityProviderAlias(String identityProviderAlias) {
        this.identityProviderAlias = identityProviderAlias;
    }

    public IdentityProviderMapperRepresentationDTO identityProviderMapper(String identityProviderMapper) {
        this.identityProviderMapper = identityProviderMapper;
        return this;
    }


    public String getIdentityProviderMapper() {
        return identityProviderMapper;
    }

    public void setIdentityProviderMapper(String identityProviderMapper) {
        this.identityProviderMapper = identityProviderMapper;
    }

    public IdentityProviderMapperRepresentationDTO config(Map<String, String> config) {
        this.config = config;
        return this;
    }

    public IdentityProviderMapperRepresentationDTO putConfigItem(String key, String configItem) {
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
        IdentityProviderMapperRepresentationDTO identityProviderMapperRepresentation = (IdentityProviderMapperRepresentationDTO) o;
        return Objects.equals(this.id, identityProviderMapperRepresentation.id) &&
                Objects.equals(this.name, identityProviderMapperRepresentation.name) &&
                Objects.equals(this.identityProviderAlias, identityProviderMapperRepresentation.identityProviderAlias) &&
                Objects.equals(this.identityProviderMapper, identityProviderMapperRepresentation.identityProviderMapper) &&
                Objects.equals(this.config, identityProviderMapperRepresentation.config);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, identityProviderAlias, identityProviderMapper, config);
    }

    @Override
    public String toString() {

        String sb = "class IdentityProviderMapperRepresentationDTO {\n" +
                "    id: " + toIndentedString(id) + "\n" +
                "    name: " + toIndentedString(name) + "\n" +
                "    identityProviderAlias: " + toIndentedString(identityProviderAlias) + "\n" +
                "    identityProviderMapper: " + toIndentedString(identityProviderMapper) + "\n" +
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

