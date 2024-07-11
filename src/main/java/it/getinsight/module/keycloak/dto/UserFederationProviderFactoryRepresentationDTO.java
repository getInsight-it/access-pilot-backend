package it.getinsight.module.keycloak.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


public class UserFederationProviderFactoryRepresentationDTO {
    @JsonProperty("id")
    private String id = null;

    @JsonProperty("options")
    private List<String> options = null;

    @JsonProperty("helpText")
    private String helpText = null;

    @JsonProperty("properties")
    private List<ConfigPropertyRepresentationDTO> properties = null;

    public UserFederationProviderFactoryRepresentationDTO id(String id) {
        this.id = id;
        return this;
    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public UserFederationProviderFactoryRepresentationDTO options(List<String> options) {
        this.options = options;
        return this;
    }

    public UserFederationProviderFactoryRepresentationDTO addOptionsItem(String optionsItem) {
        if (this.options == null) {
            this.options = new ArrayList<String>();
        }
        this.options.add(optionsItem);
        return this;
    }


    public List<String> getOptions() {
        return options;
    }

    public void setOptions(List<String> options) {
        this.options = options;
    }

    public UserFederationProviderFactoryRepresentationDTO helpText(String helpText) {
        this.helpText = helpText;
        return this;
    }


    public String getHelpText() {
        return helpText;
    }

    public void setHelpText(String helpText) {
        this.helpText = helpText;
    }

    public UserFederationProviderFactoryRepresentationDTO properties(List<ConfigPropertyRepresentationDTO> properties) {
        this.properties = properties;
        return this;
    }

    public UserFederationProviderFactoryRepresentationDTO addPropertiesItem(ConfigPropertyRepresentationDTO propertiesItem) {
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
        UserFederationProviderFactoryRepresentationDTO userFederationProviderFactoryRepresentation = (UserFederationProviderFactoryRepresentationDTO) o;
        return Objects.equals(this.id, userFederationProviderFactoryRepresentation.id) &&
                Objects.equals(this.options, userFederationProviderFactoryRepresentation.options) &&
                Objects.equals(this.helpText, userFederationProviderFactoryRepresentation.helpText) &&
                Objects.equals(this.properties, userFederationProviderFactoryRepresentation.properties);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, options, helpText, properties);
    }

    @Override
    public String toString() {

        String sb = "class UserFederationProviderFactoryRepresentationDTO {\n" +
                "    id: " + toIndentedString(id) + "\n" +
                "    options: " + toIndentedString(options) + "\n" +
                "    helpText: " + toIndentedString(helpText) + "\n" +
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

