package it.getinsight.module.keycloak.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Map;
import java.util.Objects;


public class IdentityProviderRepresentationDTO {
    @JsonProperty("internalId")
    private String internalId = null;

    @JsonProperty("alias")
    private String alias = null;

    @JsonProperty("providerId")
    private String providerId = null;

    @JsonProperty("config")
    private Map<String, String> config = null;

    @JsonProperty("enabled")
    private Boolean enabled = null;

    @JsonProperty("linkOnly")
    private Boolean linkOnly = null;

    @JsonProperty("updateProfileFirstLoginMode")
    private String updateProfileFirstLoginMode = null;

    @JsonProperty("authenticateByDefault")
    private Boolean authenticateByDefault = null;

    @JsonProperty("firstBrokerLoginFlowAlias")
    private String firstBrokerLoginFlowAlias = null;

    @JsonProperty("postBrokerLoginFlowAlias")
    private String postBrokerLoginFlowAlias = null;

    @JsonProperty("storeToken")
    private Boolean storeToken = null;

    @JsonProperty("addReadTokenRoleOnCreate")
    private Boolean addReadTokenRoleOnCreate = null;

    @JsonProperty("trustEmail")
    private Boolean trustEmail = null;

    @JsonProperty("displayName")
    private String displayName = null;

    public IdentityProviderRepresentationDTO internalId(String internalId) {
        this.internalId = internalId;
        return this;
    }


    public String getInternalId() {
        return internalId;
    }

    public void setInternalId(String internalId) {
        this.internalId = internalId;
    }

    public IdentityProviderRepresentationDTO alias(String alias) {
        this.alias = alias;
        return this;
    }


    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

    public IdentityProviderRepresentationDTO providerId(String providerId) {
        this.providerId = providerId;
        return this;
    }


    public String getProviderId() {
        return providerId;
    }

    public void setProviderId(String providerId) {
        this.providerId = providerId;
    }

    public IdentityProviderRepresentationDTO config(Map<String, String> config) {
        this.config = config;
        return this;
    }

    public IdentityProviderRepresentationDTO putConfigItem(String key, String configItem) {
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

    public IdentityProviderRepresentationDTO enabled(Boolean enabled) {
        this.enabled = enabled;
        return this;
    }


    public Boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(Boolean enabled) {
        this.enabled = enabled;
    }

    public IdentityProviderRepresentationDTO linkOnly(Boolean linkOnly) {
        this.linkOnly = linkOnly;
        return this;
    }


    public Boolean isLinkOnly() {
        return linkOnly;
    }

    public void setLinkOnly(Boolean linkOnly) {
        this.linkOnly = linkOnly;
    }

    public IdentityProviderRepresentationDTO updateProfileFirstLoginMode(String updateProfileFirstLoginMode) {
        this.updateProfileFirstLoginMode = updateProfileFirstLoginMode;
        return this;
    }


    public String getUpdateProfileFirstLoginMode() {
        return updateProfileFirstLoginMode;
    }

    public void setUpdateProfileFirstLoginMode(String updateProfileFirstLoginMode) {
        this.updateProfileFirstLoginMode = updateProfileFirstLoginMode;
    }

    public IdentityProviderRepresentationDTO authenticateByDefault(Boolean authenticateByDefault) {
        this.authenticateByDefault = authenticateByDefault;
        return this;
    }


    public Boolean isAuthenticateByDefault() {
        return authenticateByDefault;
    }

    public void setAuthenticateByDefault(Boolean authenticateByDefault) {
        this.authenticateByDefault = authenticateByDefault;
    }

    public IdentityProviderRepresentationDTO firstBrokerLoginFlowAlias(String firstBrokerLoginFlowAlias) {
        this.firstBrokerLoginFlowAlias = firstBrokerLoginFlowAlias;
        return this;
    }


    public String getFirstBrokerLoginFlowAlias() {
        return firstBrokerLoginFlowAlias;
    }

    public void setFirstBrokerLoginFlowAlias(String firstBrokerLoginFlowAlias) {
        this.firstBrokerLoginFlowAlias = firstBrokerLoginFlowAlias;
    }

    public IdentityProviderRepresentationDTO postBrokerLoginFlowAlias(String postBrokerLoginFlowAlias) {
        this.postBrokerLoginFlowAlias = postBrokerLoginFlowAlias;
        return this;
    }


    public String getPostBrokerLoginFlowAlias() {
        return postBrokerLoginFlowAlias;
    }

    public void setPostBrokerLoginFlowAlias(String postBrokerLoginFlowAlias) {
        this.postBrokerLoginFlowAlias = postBrokerLoginFlowAlias;
    }

    public IdentityProviderRepresentationDTO storeToken(Boolean storeToken) {
        this.storeToken = storeToken;
        return this;
    }


    public Boolean isStoreToken() {
        return storeToken;
    }

    public void setStoreToken(Boolean storeToken) {
        this.storeToken = storeToken;
    }

    public IdentityProviderRepresentationDTO addReadTokenRoleOnCreate(Boolean addReadTokenRoleOnCreate) {
        this.addReadTokenRoleOnCreate = addReadTokenRoleOnCreate;
        return this;
    }


    public Boolean isAddReadTokenRoleOnCreate() {
        return addReadTokenRoleOnCreate;
    }

    public void setAddReadTokenRoleOnCreate(Boolean addReadTokenRoleOnCreate) {
        this.addReadTokenRoleOnCreate = addReadTokenRoleOnCreate;
    }

    public IdentityProviderRepresentationDTO trustEmail(Boolean trustEmail) {
        this.trustEmail = trustEmail;
        return this;
    }


    public Boolean isTrustEmail() {
        return trustEmail;
    }

    public void setTrustEmail(Boolean trustEmail) {
        this.trustEmail = trustEmail;
    }

    public IdentityProviderRepresentationDTO displayName(String displayName) {
        this.displayName = displayName;
        return this;
    }


    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        IdentityProviderRepresentationDTO identityProviderRepresentation = (IdentityProviderRepresentationDTO) o;
        return Objects.equals(this.internalId, identityProviderRepresentation.internalId) &&
                Objects.equals(this.alias, identityProviderRepresentation.alias) &&
                Objects.equals(this.providerId, identityProviderRepresentation.providerId) &&
                Objects.equals(this.config, identityProviderRepresentation.config) &&
                Objects.equals(this.enabled, identityProviderRepresentation.enabled) &&
                Objects.equals(this.linkOnly, identityProviderRepresentation.linkOnly) &&
                Objects.equals(this.updateProfileFirstLoginMode, identityProviderRepresentation.updateProfileFirstLoginMode) &&
                Objects.equals(this.authenticateByDefault, identityProviderRepresentation.authenticateByDefault) &&
                Objects.equals(this.firstBrokerLoginFlowAlias, identityProviderRepresentation.firstBrokerLoginFlowAlias) &&
                Objects.equals(this.postBrokerLoginFlowAlias, identityProviderRepresentation.postBrokerLoginFlowAlias) &&
                Objects.equals(this.storeToken, identityProviderRepresentation.storeToken) &&
                Objects.equals(this.addReadTokenRoleOnCreate, identityProviderRepresentation.addReadTokenRoleOnCreate) &&
                Objects.equals(this.trustEmail, identityProviderRepresentation.trustEmail) &&
                Objects.equals(this.displayName, identityProviderRepresentation.displayName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(internalId, alias, providerId, config, enabled, linkOnly, updateProfileFirstLoginMode, authenticateByDefault, firstBrokerLoginFlowAlias, postBrokerLoginFlowAlias, storeToken, addReadTokenRoleOnCreate, trustEmail, displayName);
    }

    @Override
    public String toString() {

        String sb = "class IdentityProviderRepresentationDTO {\n" +
                "    internalId: " + toIndentedString(internalId) + "\n" +
                "    alias: " + toIndentedString(alias) + "\n" +
                "    providerId: " + toIndentedString(providerId) + "\n" +
                "    config: " + toIndentedString(config) + "\n" +
                "    enabled: " + toIndentedString(enabled) + "\n" +
                "    linkOnly: " + toIndentedString(linkOnly) + "\n" +
                "    updateProfileFirstLoginMode: " + toIndentedString(updateProfileFirstLoginMode) + "\n" +
                "    authenticateByDefault: " + toIndentedString(authenticateByDefault) + "\n" +
                "    firstBrokerLoginFlowAlias: " + toIndentedString(firstBrokerLoginFlowAlias) + "\n" +
                "    postBrokerLoginFlowAlias: " + toIndentedString(postBrokerLoginFlowAlias) + "\n" +
                "    storeToken: " + toIndentedString(storeToken) + "\n" +
                "    addReadTokenRoleOnCreate: " + toIndentedString(addReadTokenRoleOnCreate) + "\n" +
                "    trustEmail: " + toIndentedString(trustEmail) + "\n" +
                "    displayName: " + toIndentedString(displayName) + "\n" +
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

