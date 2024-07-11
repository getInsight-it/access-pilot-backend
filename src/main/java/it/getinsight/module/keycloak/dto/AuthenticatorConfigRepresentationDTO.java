package it.getinsight.module.keycloak.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Map;
import java.util.Objects;


public class AuthenticatorConfigRepresentationDTO {
    @JsonProperty("id")
    private String id = null;

    @JsonProperty("alias")
    private String alias = null;

    @JsonProperty("config")
    private Map<String, String> config = null;

    public AuthenticatorConfigRepresentationDTO id(String id) {
        this.id = id;
        return this;
    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public AuthenticatorConfigRepresentationDTO alias(String alias) {
        this.alias = alias;
        return this;
    }


    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

    public AuthenticatorConfigRepresentationDTO config(Map<String, String> config) {
        this.config = config;
        return this;
    }

    public AuthenticatorConfigRepresentationDTO putConfigItem(String key, String configItem) {
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
        AuthenticatorConfigRepresentationDTO authenticatorConfigRepresentation = (AuthenticatorConfigRepresentationDTO) o;
        return Objects.equals(this.id, authenticatorConfigRepresentation.id) &&
                Objects.equals(this.alias, authenticatorConfigRepresentation.alias) &&
                Objects.equals(this.config, authenticatorConfigRepresentation.config);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, alias, config);
    }

    @Override
    public String toString() {

        String sb = "class AuthenticatorConfigRepresentationDTO {\n" +
                "    id: " + toIndentedString(id) + "\n" +
                "    alias: " + toIndentedString(alias) + "\n" +
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

