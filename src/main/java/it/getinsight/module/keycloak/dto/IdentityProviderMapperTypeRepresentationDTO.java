package it.getinsight.module.keycloak.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


public class IdentityProviderMapperTypeRepresentationDTO {
    @JsonProperty("id")
    private String id = null;

    @JsonProperty("name")
    private String name = null;

    @JsonProperty("category")
    private String category = null;

    @JsonProperty("helpText")
    private String helpText = null;

    @JsonProperty("properties")
    private List<ConfigPropertyRepresentationDTO> properties = null;

    public IdentityProviderMapperTypeRepresentationDTO id(String id) {
        this.id = id;
        return this;
    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public IdentityProviderMapperTypeRepresentationDTO name(String name) {
        this.name = name;
        return this;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public IdentityProviderMapperTypeRepresentationDTO category(String category) {
        this.category = category;
        return this;
    }


    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public IdentityProviderMapperTypeRepresentationDTO helpText(String helpText) {
        this.helpText = helpText;
        return this;
    }


    public String getHelpText() {
        return helpText;
    }

    public void setHelpText(String helpText) {
        this.helpText = helpText;
    }

    public IdentityProviderMapperTypeRepresentationDTO properties(List<ConfigPropertyRepresentationDTO> properties) {
        this.properties = properties;
        return this;
    }

    public IdentityProviderMapperTypeRepresentationDTO addPropertiesItem(ConfigPropertyRepresentationDTO propertiesItem) {
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
        IdentityProviderMapperTypeRepresentationDTO identityProviderMapperTypeRepresentation = (IdentityProviderMapperTypeRepresentationDTO) o;
        return Objects.equals(this.id, identityProviderMapperTypeRepresentation.id) &&
                Objects.equals(this.name, identityProviderMapperTypeRepresentation.name) &&
                Objects.equals(this.category, identityProviderMapperTypeRepresentation.category) &&
                Objects.equals(this.helpText, identityProviderMapperTypeRepresentation.helpText) &&
                Objects.equals(this.properties, identityProviderMapperTypeRepresentation.properties);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, category, helpText, properties);
    }

    @Override
    public String toString() {

        String sb = "class IdentityProviderMapperTypeRepresentationDTO {\n" +
                "    id: " + toIndentedString(id) + "\n" +
                "    name: " + toIndentedString(name) + "\n" +
                "    category: " + toIndentedString(category) + "\n" +
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

