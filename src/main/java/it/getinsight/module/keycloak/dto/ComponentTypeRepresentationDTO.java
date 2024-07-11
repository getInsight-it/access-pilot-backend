package it.getinsight.module.keycloak.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;


public class ComponentTypeRepresentationDTO {
    @JsonProperty("id")
    private String id = null;

    @JsonProperty("helpText")
    private String helpText = null;

    @JsonProperty("properties")
    private List<ConfigPropertyRepresentationDTO> properties = null;

    @JsonProperty("metadata")
    private Map<String, Object> metadata = null;

    public ComponentTypeRepresentationDTO id(String id) {
        this.id = id;
        return this;
    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public ComponentTypeRepresentationDTO helpText(String helpText) {
        this.helpText = helpText;
        return this;
    }


    public String getHelpText() {
        return helpText;
    }

    public void setHelpText(String helpText) {
        this.helpText = helpText;
    }

    public ComponentTypeRepresentationDTO properties(List<ConfigPropertyRepresentationDTO> properties) {
        this.properties = properties;
        return this;
    }

    public ComponentTypeRepresentationDTO addPropertiesItem(ConfigPropertyRepresentationDTO propertiesItem) {
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

    public ComponentTypeRepresentationDTO metadata(Map<String, Object> metadata) {
        this.metadata = metadata;
        return this;
    }

    public ComponentTypeRepresentationDTO putMetadataItem(String key, Object metadataItem) {
        if (this.metadata == null) {
            this.metadata = null;
        }
        this.metadata.put(key, metadataItem);
        return this;
    }


    public Map<String, Object> getMetadata() {
        return metadata;
    }

    public void setMetadata(Map<String, Object> metadata) {
        this.metadata = metadata;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ComponentTypeRepresentationDTO componentTypeRepresentation = (ComponentTypeRepresentationDTO) o;
        return Objects.equals(this.id, componentTypeRepresentation.id) &&
                Objects.equals(this.helpText, componentTypeRepresentation.helpText) &&
                Objects.equals(this.properties, componentTypeRepresentation.properties) &&
                Objects.equals(this.metadata, componentTypeRepresentation.metadata);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, helpText, properties, metadata);
    }

    @Override
    public String toString() {

        String sb = "class ComponentTypeRepresentationDTO {\n" +
                "    id: " + toIndentedString(id) + "\n" +
                "    helpText: " + toIndentedString(helpText) + "\n" +
                "    properties: " + toIndentedString(properties) + "\n" +
                "    metadata: " + toIndentedString(metadata) + "\n" +
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

