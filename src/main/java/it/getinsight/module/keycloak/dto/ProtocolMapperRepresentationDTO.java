package it.getinsight.module.keycloak.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Map;
import java.util.Objects;


public class ProtocolMapperRepresentationDTO {
    @JsonProperty("id")
    private String id = null;

    @JsonProperty("name")
    private String name = null;

    @JsonProperty("protocol")
    private String protocol = null;

    @JsonProperty("protocolMapper")
    private String protocolMapper = null;

    @JsonProperty("config")
    private Map<String, String> config = null;

    @JsonProperty("consentRequired")
    private Boolean consentRequired = null;

    @JsonProperty("consentText")
    private String consentText = null;

    public ProtocolMapperRepresentationDTO id(String id) {
        this.id = id;
        return this;
    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public ProtocolMapperRepresentationDTO name(String name) {
        this.name = name;
        return this;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ProtocolMapperRepresentationDTO protocol(String protocol) {
        this.protocol = protocol;
        return this;
    }


    public String getProtocol() {
        return protocol;
    }

    public void setProtocol(String protocol) {
        this.protocol = protocol;
    }

    public ProtocolMapperRepresentationDTO protocolMapper(String protocolMapper) {
        this.protocolMapper = protocolMapper;
        return this;
    }


    public String getProtocolMapper() {
        return protocolMapper;
    }

    public void setProtocolMapper(String protocolMapper) {
        this.protocolMapper = protocolMapper;
    }

    public ProtocolMapperRepresentationDTO config(Map<String, String> config) {
        this.config = config;
        return this;
    }

    public ProtocolMapperRepresentationDTO putConfigItem(String key, String configItem) {
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

    public ProtocolMapperRepresentationDTO consentRequired(Boolean consentRequired) {
        this.consentRequired = consentRequired;
        return this;
    }


    public Boolean isConsentRequired() {
        return consentRequired;
    }

    public void setConsentRequired(Boolean consentRequired) {
        this.consentRequired = consentRequired;
    }

    public ProtocolMapperRepresentationDTO consentText(String consentText) {
        this.consentText = consentText;
        return this;
    }


    public String getConsentText() {
        return consentText;
    }

    public void setConsentText(String consentText) {
        this.consentText = consentText;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ProtocolMapperRepresentationDTO protocolMapperRepresentation = (ProtocolMapperRepresentationDTO) o;
        return Objects.equals(this.id, protocolMapperRepresentation.id) &&
                Objects.equals(this.name, protocolMapperRepresentation.name) &&
                Objects.equals(this.protocol, protocolMapperRepresentation.protocol) &&
                Objects.equals(this.protocolMapper, protocolMapperRepresentation.protocolMapper) &&
                Objects.equals(this.config, protocolMapperRepresentation.config) &&
                Objects.equals(this.consentRequired, protocolMapperRepresentation.consentRequired) &&
                Objects.equals(this.consentText, protocolMapperRepresentation.consentText);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, protocol, protocolMapper, config, consentRequired, consentText);
    }

    @Override
    public String toString() {

        String sb = "class ProtocolMapperRepresentationDTO {\n" +
                "    id: " + toIndentedString(id) + "\n" +
                "    name: " + toIndentedString(name) + "\n" +
                "    protocol: " + toIndentedString(protocol) + "\n" +
                "    protocolMapper: " + toIndentedString(protocolMapper) + "\n" +
                "    config: " + toIndentedString(config) + "\n" +
                "    consentRequired: " + toIndentedString(consentRequired) + "\n" +
                "    consentText: " + toIndentedString(consentText) + "\n" +
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

