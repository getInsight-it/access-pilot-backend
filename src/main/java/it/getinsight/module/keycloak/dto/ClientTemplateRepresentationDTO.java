package it.getinsight.module.keycloak.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;


public class ClientTemplateRepresentationDTO {
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

    @JsonProperty("fullScopeAllowed")
    private Boolean fullScopeAllowed = null;

    @JsonProperty("bearerOnly")
    private Boolean bearerOnly = null;

    @JsonProperty("consentRequired")
    private Boolean consentRequired = null;

    @JsonProperty("standardFlowEnabled")
    private Boolean standardFlowEnabled = null;

    @JsonProperty("implicitFlowEnabled")
    private Boolean implicitFlowEnabled = null;

    @JsonProperty("directAccessGrantsEnabled")
    private Boolean directAccessGrantsEnabled = null;

    @JsonProperty("serviceAccountsEnabled")
    private Boolean serviceAccountsEnabled = null;

    @JsonProperty("publicClient")
    private Boolean publicClient = null;

    @JsonProperty("frontchannelLogout")
    private Boolean frontchannelLogout = null;

    @JsonProperty("attributes")
    private Map<String, String> attributes = null;

    public ClientTemplateRepresentationDTO id(String id) {
        this.id = id;
        return this;
    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public ClientTemplateRepresentationDTO name(String name) {
        this.name = name;
        return this;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ClientTemplateRepresentationDTO description(String description) {
        this.description = description;
        return this;
    }


    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public ClientTemplateRepresentationDTO protocolMappers(List<ProtocolMapperRepresentationDTO> protocolMappers) {
        this.protocolMappers = protocolMappers;
        return this;
    }

    public ClientTemplateRepresentationDTO addProtocolMappersItem(ProtocolMapperRepresentationDTO protocolMappersItem) {
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

    public ClientTemplateRepresentationDTO protocol(String protocol) {
        this.protocol = protocol;
        return this;
    }


    public String getProtocol() {
        return protocol;
    }

    public void setProtocol(String protocol) {
        this.protocol = protocol;
    }

    public ClientTemplateRepresentationDTO fullScopeAllowed(Boolean fullScopeAllowed) {
        this.fullScopeAllowed = fullScopeAllowed;
        return this;
    }


    public Boolean isFullScopeAllowed() {
        return fullScopeAllowed;
    }

    public void setFullScopeAllowed(Boolean fullScopeAllowed) {
        this.fullScopeAllowed = fullScopeAllowed;
    }

    public ClientTemplateRepresentationDTO bearerOnly(Boolean bearerOnly) {
        this.bearerOnly = bearerOnly;
        return this;
    }


    public Boolean isBearerOnly() {
        return bearerOnly;
    }

    public void setBearerOnly(Boolean bearerOnly) {
        this.bearerOnly = bearerOnly;
    }

    public ClientTemplateRepresentationDTO consentRequired(Boolean consentRequired) {
        this.consentRequired = consentRequired;
        return this;
    }


    public Boolean isConsentRequired() {
        return consentRequired;
    }

    public void setConsentRequired(Boolean consentRequired) {
        this.consentRequired = consentRequired;
    }

    public ClientTemplateRepresentationDTO standardFlowEnabled(Boolean standardFlowEnabled) {
        this.standardFlowEnabled = standardFlowEnabled;
        return this;
    }


    public Boolean isStandardFlowEnabled() {
        return standardFlowEnabled;
    }

    public void setStandardFlowEnabled(Boolean standardFlowEnabled) {
        this.standardFlowEnabled = standardFlowEnabled;
    }

    public ClientTemplateRepresentationDTO implicitFlowEnabled(Boolean implicitFlowEnabled) {
        this.implicitFlowEnabled = implicitFlowEnabled;
        return this;
    }


    public Boolean isImplicitFlowEnabled() {
        return implicitFlowEnabled;
    }

    public void setImplicitFlowEnabled(Boolean implicitFlowEnabled) {
        this.implicitFlowEnabled = implicitFlowEnabled;
    }

    public ClientTemplateRepresentationDTO directAccessGrantsEnabled(Boolean directAccessGrantsEnabled) {
        this.directAccessGrantsEnabled = directAccessGrantsEnabled;
        return this;
    }


    public Boolean isDirectAccessGrantsEnabled() {
        return directAccessGrantsEnabled;
    }

    public void setDirectAccessGrantsEnabled(Boolean directAccessGrantsEnabled) {
        this.directAccessGrantsEnabled = directAccessGrantsEnabled;
    }

    public ClientTemplateRepresentationDTO serviceAccountsEnabled(Boolean serviceAccountsEnabled) {
        this.serviceAccountsEnabled = serviceAccountsEnabled;
        return this;
    }


    public Boolean isServiceAccountsEnabled() {
        return serviceAccountsEnabled;
    }

    public void setServiceAccountsEnabled(Boolean serviceAccountsEnabled) {
        this.serviceAccountsEnabled = serviceAccountsEnabled;
    }

    public ClientTemplateRepresentationDTO publicClient(Boolean publicClient) {
        this.publicClient = publicClient;
        return this;
    }


    public Boolean isPublicClient() {
        return publicClient;
    }

    public void setPublicClient(Boolean publicClient) {
        this.publicClient = publicClient;
    }

    public ClientTemplateRepresentationDTO frontchannelLogout(Boolean frontchannelLogout) {
        this.frontchannelLogout = frontchannelLogout;
        return this;
    }


    public Boolean isFrontchannelLogout() {
        return frontchannelLogout;
    }

    public void setFrontchannelLogout(Boolean frontchannelLogout) {
        this.frontchannelLogout = frontchannelLogout;
    }

    public ClientTemplateRepresentationDTO attributes(Map<String, String> attributes) {
        this.attributes = attributes;
        return this;
    }

    public ClientTemplateRepresentationDTO putAttributesItem(String key, String attributesItem) {
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
        ClientTemplateRepresentationDTO clientTemplateRepresentation = (ClientTemplateRepresentationDTO) o;
        return Objects.equals(this.id, clientTemplateRepresentation.id) &&
                Objects.equals(this.name, clientTemplateRepresentation.name) &&
                Objects.equals(this.description, clientTemplateRepresentation.description) &&
                Objects.equals(this.protocolMappers, clientTemplateRepresentation.protocolMappers) &&
                Objects.equals(this.protocol, clientTemplateRepresentation.protocol) &&
                Objects.equals(this.fullScopeAllowed, clientTemplateRepresentation.fullScopeAllowed) &&
                Objects.equals(this.bearerOnly, clientTemplateRepresentation.bearerOnly) &&
                Objects.equals(this.consentRequired, clientTemplateRepresentation.consentRequired) &&
                Objects.equals(this.standardFlowEnabled, clientTemplateRepresentation.standardFlowEnabled) &&
                Objects.equals(this.implicitFlowEnabled, clientTemplateRepresentation.implicitFlowEnabled) &&
                Objects.equals(this.directAccessGrantsEnabled, clientTemplateRepresentation.directAccessGrantsEnabled) &&
                Objects.equals(this.serviceAccountsEnabled, clientTemplateRepresentation.serviceAccountsEnabled) &&
                Objects.equals(this.publicClient, clientTemplateRepresentation.publicClient) &&
                Objects.equals(this.frontchannelLogout, clientTemplateRepresentation.frontchannelLogout) &&
                Objects.equals(this.attributes, clientTemplateRepresentation.attributes);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, description, protocolMappers, protocol, fullScopeAllowed, bearerOnly, consentRequired, standardFlowEnabled, implicitFlowEnabled, directAccessGrantsEnabled, serviceAccountsEnabled, publicClient, frontchannelLogout, attributes);
    }

    @Override
    public String toString() {

        String sb = "class ClientTemplateRepresentationDTO {\n" +
                "    id: " + toIndentedString(id) + "\n" +
                "    name: " + toIndentedString(name) + "\n" +
                "    description: " + toIndentedString(description) + "\n" +
                "    protocolMappers: " + toIndentedString(protocolMappers) + "\n" +
                "    protocol: " + toIndentedString(protocol) + "\n" +
                "    fullScopeAllowed: " + toIndentedString(fullScopeAllowed) + "\n" +
                "    bearerOnly: " + toIndentedString(bearerOnly) + "\n" +
                "    consentRequired: " + toIndentedString(consentRequired) + "\n" +
                "    standardFlowEnabled: " + toIndentedString(standardFlowEnabled) + "\n" +
                "    implicitFlowEnabled: " + toIndentedString(implicitFlowEnabled) + "\n" +
                "    directAccessGrantsEnabled: " + toIndentedString(directAccessGrantsEnabled) + "\n" +
                "    serviceAccountsEnabled: " + toIndentedString(serviceAccountsEnabled) + "\n" +
                "    publicClient: " + toIndentedString(publicClient) + "\n" +
                "    frontchannelLogout: " + toIndentedString(frontchannelLogout) + "\n" +
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

