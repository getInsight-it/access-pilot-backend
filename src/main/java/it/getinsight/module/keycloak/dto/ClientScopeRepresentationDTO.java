package it.getinsight.module.keycloak.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;


public class ClientScopeRepresentationDTO {
    @JsonProperty("id")
    private String id = null;

    @JsonProperty("name")
    private String name = null;

    @JsonProperty("description")
    private String description = null;

    @JsonProperty("protocolMappers")
    private List<ProtocolMapperRepresentationDTO> protocolMappers = null;

    @JsonProperty("protocol")
    private String protocol = null;

    @JsonProperty("attributes")
    private Map<String, String> attributes = null;

    public ClientScopeRepresentationDTO id(String id) {
        this.id = id;
        return this;
    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public ClientScopeRepresentationDTO name(String name) {
        this.name = name;
        return this;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ClientScopeRepresentationDTO description(String description) {
        this.description = description;
        return this;
    }


    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public ClientScopeRepresentationDTO protocolMappers(List<ProtocolMapperRepresentationDTO> protocolMappers) {
        this.protocolMappers = protocolMappers;
        return this;
    }

    public ClientScopeRepresentationDTO addProtocolMappersItem(ProtocolMapperRepresentationDTO protocolMappersItem) {
        if (this.protocolMappers == null) {
            this.protocolMappers = new ArrayList<ProtocolMapperRepresentationDTO>();
        }
        this.protocolMappers.add(protocolMappersItem);
        return this;
    }


    public List<ProtocolMapperRepresentationDTO> getProtocolMappers() {
        return protocolMappers;
    }

    public void setProtocolMappers(List<ProtocolMapperRepresentationDTO> protocolMappers) {
        this.protocolMappers = protocolMappers;
    }

    public ClientScopeRepresentationDTO protocol(String protocol) {
        this.protocol = protocol;
        return this;
    }


    public String getProtocol() {
        return protocol;
    }

    public void setProtocol(String protocol) {
        this.protocol = protocol;
    }

    public ClientScopeRepresentationDTO attributes(Map<String, String> attributes) {
        this.attributes = attributes;
        return this;
    }

    public ClientScopeRepresentationDTO putAttributesItem(String key, String attributesItem) {
        if (this.attributes == null) {
            this.attributes = null;
        }
        this.attributes.put(key, attributesItem);
        return this;
    }


    public Map<String, String> getAttributes() {
        return attributes;
    }

    public void setAttributes(Map<String, String> attributes) {
        this.attributes = attributes;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ClientScopeRepresentationDTO clientScopeRepresentation = (ClientScopeRepresentationDTO) o;
        return Objects.equals(this.id, clientScopeRepresentation.id) &&
                Objects.equals(this.name, clientScopeRepresentation.name) &&
                Objects.equals(this.description, clientScopeRepresentation.description) &&
                Objects.equals(this.protocolMappers, clientScopeRepresentation.protocolMappers) &&
                Objects.equals(this.protocol, clientScopeRepresentation.protocol) &&
                Objects.equals(this.attributes, clientScopeRepresentation.attributes);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, description, protocolMappers, protocol, attributes);
    }

    @Override
    public String toString() {

        String sb = "class ClientScopeRepresentationDTO {\n" +
                "    id: " + toIndentedString(id) + "\n" +
                "    name: " + toIndentedString(name) + "\n" +
                "    description: " + toIndentedString(description) + "\n" +
                "    protocolMappers: " + toIndentedString(protocolMappers) + "\n" +
                "    protocol: " + toIndentedString(protocol) + "\n" +
                "    attributes: " + toIndentedString(attributes) + "\n" +
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

