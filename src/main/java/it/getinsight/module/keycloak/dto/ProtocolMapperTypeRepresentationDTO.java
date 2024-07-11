package it.getinsight.module.keycloak.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


public class ProtocolMapperTypeRepresentationDTO {
    @JsonProperty("id")
    private String id = null;

    @JsonProperty("name")
    private String name = null;

    @JsonProperty("category")
    private String category = null;

    @JsonProperty("helpText")
    private String helpText = null;

    @JsonProperty("priority")
    private Integer priority = null;

    @JsonProperty("properties")
    private List<ConfigPropertyRepresentationDTO> properties = null;

    public ProtocolMapperTypeRepresentationDTO id(String id) {
        this.id = id;
        return this;
    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public ProtocolMapperTypeRepresentationDTO name(String name) {
        this.name = name;
        return this;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ProtocolMapperTypeRepresentationDTO category(String category) {
        this.category = category;
        return this;
    }


    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public ProtocolMapperTypeRepresentationDTO helpText(String helpText) {
        this.helpText = helpText;
        return this;
    }


    public String getHelpText() {
        return helpText;
    }

    public void setHelpText(String helpText) {
        this.helpText = helpText;
    }

    public ProtocolMapperTypeRepresentationDTO priority(Integer priority) {
        this.priority = priority;
        return this;
    }


    public Integer getPriority() {
        return priority;
    }

    public void setPriority(Integer priority) {
        this.priority = priority;
    }

    public ProtocolMapperTypeRepresentationDTO properties(List<ConfigPropertyRepresentationDTO> properties) {
        this.properties = properties;
        return this;
    }

    public ProtocolMapperTypeRepresentationDTO addPropertiesItem(ConfigPropertyRepresentationDTO propertiesItem) {
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
        ProtocolMapperTypeRepresentationDTO protocolMapperTypeRepresentation = (ProtocolMapperTypeRepresentationDTO) o;
        return Objects.equals(this.id, protocolMapperTypeRepresentation.id) &&
                Objects.equals(this.name, protocolMapperTypeRepresentation.name) &&
                Objects.equals(this.category, protocolMapperTypeRepresentation.category) &&
                Objects.equals(this.helpText, protocolMapperTypeRepresentation.helpText) &&
                Objects.equals(this.priority, protocolMapperTypeRepresentation.priority) &&
                Objects.equals(this.properties, protocolMapperTypeRepresentation.properties);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, category, helpText, priority, properties);
    }

    @Override
    public String toString() {

        String sb = "class ProtocolMapperTypeRepresentationDTO {\n" +
                "    id: " + toIndentedString(id) + "\n" +
                "    name: " + toIndentedString(name) + "\n" +
                "    category: " + toIndentedString(category) + "\n" +
                "    helpText: " + toIndentedString(helpText) + "\n" +
                "    priority: " + toIndentedString(priority) + "\n" +
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

