package it.getinsight.module.keycloak.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


public class AuthenticatorConfigInfoRepresentationDTO {
    @JsonProperty("name")
    private String name = null;

    @JsonProperty("helpText")
    private String helpText = null;

    @JsonProperty("providerId")
    private String providerId = null;

    @JsonProperty("properties")
    private List<ConfigPropertyRepresentationDTO> properties = null;

    public AuthenticatorConfigInfoRepresentationDTO name(String name) {
        this.name = name;
        return this;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public AuthenticatorConfigInfoRepresentationDTO helpText(String helpText) {
        this.helpText = helpText;
        return this;
    }


    public String getHelpText() {
        return helpText;
    }

    public void setHelpText(String helpText) {
        this.helpText = helpText;
    }

    public AuthenticatorConfigInfoRepresentationDTO providerId(String providerId) {
        this.providerId = providerId;
        return this;
    }


    public String getProviderId() {
        return providerId;
    }

    public void setProviderId(String providerId) {
        this.providerId = providerId;
    }

    public AuthenticatorConfigInfoRepresentationDTO properties(List<ConfigPropertyRepresentationDTO> properties) {
        this.properties = properties;
        return this;
    }

    public AuthenticatorConfigInfoRepresentationDTO addPropertiesItem(ConfigPropertyRepresentationDTO propertiesItem) {
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


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        AuthenticatorConfigInfoRepresentationDTO authenticatorConfigInfoRepresentation = (AuthenticatorConfigInfoRepresentationDTO) o;
        return Objects.equals(this.name, authenticatorConfigInfoRepresentation.name) &&
                Objects.equals(this.helpText, authenticatorConfigInfoRepresentation.helpText) &&
                Objects.equals(this.providerId, authenticatorConfigInfoRepresentation.providerId) &&
                Objects.equals(this.properties, authenticatorConfigInfoRepresentation.properties);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, helpText, providerId, properties);
    }

    @Override
    public String toString() {

        String sb = "class AuthenticatorConfigInfoRepresentationDTO {\n" +
                "    name: " + toIndentedString(name) + "\n" +
                "    helpText: " + toIndentedString(helpText) + "\n" +
                "    providerId: " + toIndentedString(providerId) + "\n" +
                "    properties: " + toIndentedString(properties) + "\n" +
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

