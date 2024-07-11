package it.getinsight.module.keycloak.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;


public class ClientRepresentationDTO {
    @JsonProperty("id")
    private String id = null;

    @JsonProperty("name")
    private String name = null;

    @JsonProperty("description")
    private String description = null;

    @JsonProperty("clientId")
    private String clientId = null;

    @JsonProperty("enabled")
    private Boolean enabled = null;

    @JsonProperty("alwaysDisplayInConsole")
    private Boolean alwaysDisplayInConsole = null;

    @JsonProperty("surrogateAuthRequired")
    private Boolean surrogateAuthRequired = null;

    @JsonProperty("rootUrl")
    private String rootUrl = null;

    @JsonProperty("adminUrl")
    private String adminUrl = null;

    @JsonProperty("baseUrl")
    private String baseUrl = null;

    @JsonProperty("clientAuthenticatorType")
    private String clientAuthenticatorType = null;

    @JsonProperty("secret")
    private String secret = null;

    @JsonProperty("registrationAccessToken")
    private String registrationAccessToken = null;

    @JsonProperty("redirectUris")
    private List<String> redirectUris = null;

    @JsonProperty("webOrigins")
    private List<String> webOrigins = null;

    @JsonProperty("defaultRoles")
    private List<String> defaultRoles = null;

    @JsonProperty("notBefore")
    private Integer notBefore = null;

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

    @JsonProperty("authorizationServicesEnabled")
    private Boolean authorizationServicesEnabled = null;

    @JsonProperty("directGrantsOnly")
    private Boolean directGrantsOnly = null;

    @JsonProperty("publicClient")
    private Boolean publicClient = null;

    @JsonProperty("fullScopeAllowed")
    private Boolean fullScopeAllowed = null;

    @JsonProperty("protocol")
    private String protocol = null;

    @JsonProperty("attributes")
    private Map<String, String> attributes = null;

    @JsonProperty("authenticationFlowBindingOverrides")
    private Map<String, String> authenticationFlowBindingOverrides = null;

    @JsonProperty("nodeReRegistrationTimeout")
    private Integer nodeReRegistrationTimeout = null;

    @JsonProperty("registeredNodes")
    private Map<String, Integer> registeredNodes = null;

    @JsonProperty("frontchannelLogout")
    private Boolean frontchannelLogout = null;

    @JsonProperty("protocolMappers")
    private List<ProtocolMapperRepresentationDTO> protocolMappers = null;

    @JsonProperty("clientTemplate")
    private String clientTemplate = null;

    @JsonProperty("useTemplateConfig")
    private Boolean useTemplateConfig = null;

    @JsonProperty("useTemplateScope")
    private Boolean useTemplateScope = null;

    @JsonProperty("useTemplateMappers")
    private Boolean useTemplateMappers = null;

    @JsonProperty("defaultClientScopes")
    private List<String> defaultClientScopes = null;

    @JsonProperty("optionalClientScopes")
    private List<String> optionalClientScopes = null;

    @JsonProperty("authorizationSettings")
    private ResourceServerRepresentationDTO authorizationSettings = null;

    @JsonProperty("access")
    private Map<String, Boolean> access = null;

    @JsonProperty("origin")
    private String origin = null;

    public ClientRepresentationDTO id(String id) {
        this.id = id;
        return this;
    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public ClientRepresentationDTO name(String name) {
        this.name = name;
        return this;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ClientRepresentationDTO description(String description) {
        this.description = description;
        return this;
    }


    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public ClientRepresentationDTO clientId(String clientId) {
        this.clientId = clientId;
        return this;
    }


    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public ClientRepresentationDTO enabled(Boolean enabled) {
        this.enabled = enabled;
        return this;
    }


    public Boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(Boolean enabled) {
        this.enabled = enabled;
    }

    public ClientRepresentationDTO alwaysDisplayInConsole(Boolean alwaysDisplayInConsole) {
        this.alwaysDisplayInConsole = alwaysDisplayInConsole;
        return this;
    }


    public Boolean isAlwaysDisplayInConsole() {
        return alwaysDisplayInConsole;
    }

    public void setAlwaysDisplayInConsole(Boolean alwaysDisplayInConsole) {
        this.alwaysDisplayInConsole = alwaysDisplayInConsole;
    }

    public ClientRepresentationDTO surrogateAuthRequired(Boolean surrogateAuthRequired) {
        this.surrogateAuthRequired = surrogateAuthRequired;
        return this;
    }


    public Boolean isSurrogateAuthRequired() {
        return surrogateAuthRequired;
    }

    public void setSurrogateAuthRequired(Boolean surrogateAuthRequired) {
        this.surrogateAuthRequired = surrogateAuthRequired;
    }

    public ClientRepresentationDTO rootUrl(String rootUrl) {
        this.rootUrl = rootUrl;
        return this;
    }


    public String getRootUrl() {
        return rootUrl;
    }

    public void setRootUrl(String rootUrl) {
        this.rootUrl = rootUrl;
    }

    public ClientRepresentationDTO adminUrl(String adminUrl) {
        this.adminUrl = adminUrl;
        return this;
    }


    public String getAdminUrl() {
        return adminUrl;
    }

    public void setAdminUrl(String adminUrl) {
        this.adminUrl = adminUrl;
    }

    public ClientRepresentationDTO baseUrl(String baseUrl) {
        this.baseUrl = baseUrl;
        return this;
    }


    public String getBaseUrl() {
        return baseUrl;
    }

    public void setBaseUrl(String baseUrl) {
        this.baseUrl = baseUrl;
    }

    public ClientRepresentationDTO clientAuthenticatorType(String clientAuthenticatorType) {
        this.clientAuthenticatorType = clientAuthenticatorType;
        return this;
    }


    public String getClientAuthenticatorType() {
        return clientAuthenticatorType;
    }

    public void setClientAuthenticatorType(String clientAuthenticatorType) {
        this.clientAuthenticatorType = clientAuthenticatorType;
    }

    public ClientRepresentationDTO secret(String secret) {
        this.secret = secret;
        return this;
    }


    public String getSecret() {
        return secret;
    }

    public void setSecret(String secret) {
        this.secret = secret;
    }

    public ClientRepresentationDTO registrationAccessToken(String registrationAccessToken) {
        this.registrationAccessToken = registrationAccessToken;
        return this;
    }


    public String getRegistrationAccessToken() {
        return registrationAccessToken;
    }

    public void setRegistrationAccessToken(String registrationAccessToken) {
        this.registrationAccessToken = registrationAccessToken;
    }

    public ClientRepresentationDTO redirectUris(List<String> redirectUris) {
        this.redirectUris = redirectUris;
        return this;
    }

    public ClientRepresentationDTO addRedirectUrisItem(String redirectUrisItem) {
        if (this.redirectUris == null) {
            this.redirectUris = new ArrayList<String>();
        }
        this.redirectUris.add(redirectUrisItem);
        return this;
    }


    public List<String> getRedirectUris() {
        return redirectUris;
    }

    public void setRedirectUris(List<String> redirectUris) {
        this.redirectUris = redirectUris;
    }

    public ClientRepresentationDTO webOrigins(List<String> webOrigins) {
        this.webOrigins = webOrigins;
        return this;
    }

    public ClientRepresentationDTO addWebOriginsItem(String webOriginsItem) {
        if (this.webOrigins == null) {
            this.webOrigins = new ArrayList<String>();
        }
        this.webOrigins.add(webOriginsItem);
        return this;
    }


    public List<String> getWebOrigins() {
        return webOrigins;
    }

    public void setWebOrigins(List<String> webOrigins) {
        this.webOrigins = webOrigins;
    }

    public ClientRepresentationDTO defaultRoles(List<String> defaultRoles) {
        this.defaultRoles = defaultRoles;
        return this;
    }

    public ClientRepresentationDTO addDefaultRolesItem(String defaultRolesItem) {
        if (this.defaultRoles == null) {
            this.defaultRoles = new ArrayList<String>();
        }
        this.defaultRoles.add(defaultRolesItem);
        return this;
    }


    public List<String> getDefaultRoles() {
        return defaultRoles;
    }

    public void setDefaultRoles(List<String> defaultRoles) {
        this.defaultRoles = defaultRoles;
    }

    public ClientRepresentationDTO notBefore(Integer notBefore) {
        this.notBefore = notBefore;
        return this;
    }


    public Integer getNotBefore() {
        return notBefore;
    }

    public void setNotBefore(Integer notBefore) {
        this.notBefore = notBefore;
    }

    public ClientRepresentationDTO bearerOnly(Boolean bearerOnly) {
        this.bearerOnly = bearerOnly;
        return this;
    }


    public Boolean isBearerOnly() {
        return bearerOnly;
    }

    public void setBearerOnly(Boolean bearerOnly) {
        this.bearerOnly = bearerOnly;
    }

    public ClientRepresentationDTO consentRequired(Boolean consentRequired) {
        this.consentRequired = consentRequired;
        return this;
    }


    public Boolean isConsentRequired() {
        return consentRequired;
    }

    public void setConsentRequired(Boolean consentRequired) {
        this.consentRequired = consentRequired;
    }

    public ClientRepresentationDTO standardFlowEnabled(Boolean standardFlowEnabled) {
        this.standardFlowEnabled = standardFlowEnabled;
        return this;
    }


    public Boolean isStandardFlowEnabled() {
        return standardFlowEnabled;
    }

    public void setStandardFlowEnabled(Boolean standardFlowEnabled) {
        this.standardFlowEnabled = standardFlowEnabled;
    }

    public ClientRepresentationDTO implicitFlowEnabled(Boolean implicitFlowEnabled) {
        this.implicitFlowEnabled = implicitFlowEnabled;
        return this;
    }


    public Boolean isImplicitFlowEnabled() {
        return implicitFlowEnabled;
    }

    public void setImplicitFlowEnabled(Boolean implicitFlowEnabled) {
        this.implicitFlowEnabled = implicitFlowEnabled;
    }

    public ClientRepresentationDTO directAccessGrantsEnabled(Boolean directAccessGrantsEnabled) {
        this.directAccessGrantsEnabled = directAccessGrantsEnabled;
        return this;
    }


    public Boolean isDirectAccessGrantsEnabled() {
        return directAccessGrantsEnabled;
    }

    public void setDirectAccessGrantsEnabled(Boolean directAccessGrantsEnabled) {
        this.directAccessGrantsEnabled = directAccessGrantsEnabled;
    }

    public ClientRepresentationDTO serviceAccountsEnabled(Boolean serviceAccountsEnabled) {
        this.serviceAccountsEnabled = serviceAccountsEnabled;
        return this;
    }


    public Boolean isServiceAccountsEnabled() {
        return serviceAccountsEnabled;
    }

    public void setServiceAccountsEnabled(Boolean serviceAccountsEnabled) {
        this.serviceAccountsEnabled = serviceAccountsEnabled;
    }

    public ClientRepresentationDTO authorizationServicesEnabled(Boolean authorizationServicesEnabled) {
        this.authorizationServicesEnabled = authorizationServicesEnabled;
        return this;
    }


    public Boolean isAuthorizationServicesEnabled() {
        return authorizationServicesEnabled;
    }

    public void setAuthorizationServicesEnabled(Boolean authorizationServicesEnabled) {
        this.authorizationServicesEnabled = authorizationServicesEnabled;
    }

    public ClientRepresentationDTO directGrantsOnly(Boolean directGrantsOnly) {
        this.directGrantsOnly = directGrantsOnly;
        return this;
    }


    public Boolean isDirectGrantsOnly() {
        return directGrantsOnly;
    }

    public void setDirectGrantsOnly(Boolean directGrantsOnly) {
        this.directGrantsOnly = directGrantsOnly;
    }

    public ClientRepresentationDTO publicClient(Boolean publicClient) {
        this.publicClient = publicClient;
        return this;
    }


    public Boolean isPublicClient() {
        return publicClient;
    }

    public void setPublicClient(Boolean publicClient) {
        this.publicClient = publicClient;
    }

    public ClientRepresentationDTO fullScopeAllowed(Boolean fullScopeAllowed) {
        this.fullScopeAllowed = fullScopeAllowed;
        return this;
    }


    public Boolean isFullScopeAllowed() {
        return fullScopeAllowed;
    }

    public void setFullScopeAllowed(Boolean fullScopeAllowed) {
        this.fullScopeAllowed = fullScopeAllowed;
    }

    public ClientRepresentationDTO protocol(String protocol) {
        this.protocol = protocol;
        return this;
    }


    public String getProtocol() {
        return protocol;
    }

    public void setProtocol(String protocol) {
        this.protocol = protocol;
    }

    public ClientRepresentationDTO attributes(Map<String, String> attributes) {
        this.attributes = attributes;
        return this;
    }

    public ClientRepresentationDTO putAttributesItem(String key, String attributesItem) {
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

    public ClientRepresentationDTO authenticationFlowBindingOverrides(Map<String, String> authenticationFlowBindingOverrides) {
        this.authenticationFlowBindingOverrides = authenticationFlowBindingOverrides;
        return this;
    }

    public ClientRepresentationDTO putAuthenticationFlowBindingOverridesItem(String key, String authenticationFlowBindingOverridesItem) {
        if (this.authenticationFlowBindingOverrides == null) {
            this.authenticationFlowBindingOverrides = null;
        }
        this.authenticationFlowBindingOverrides.put(key, authenticationFlowBindingOverridesItem);
        return this;
    }


    public Map<String, String> getAuthenticationFlowBindingOverrides() {
        return authenticationFlowBindingOverrides;
    }

    public void setAuthenticationFlowBindingOverrides(Map<String, String> authenticationFlowBindingOverrides) {
        this.authenticationFlowBindingOverrides = authenticationFlowBindingOverrides;
    }

    public ClientRepresentationDTO nodeReRegistrationTimeout(Integer nodeReRegistrationTimeout) {
        this.nodeReRegistrationTimeout = nodeReRegistrationTimeout;
        return this;
    }


    public Integer getNodeReRegistrationTimeout() {
        return nodeReRegistrationTimeout;
    }

    public void setNodeReRegistrationTimeout(Integer nodeReRegistrationTimeout) {
        this.nodeReRegistrationTimeout = nodeReRegistrationTimeout;
    }

    public ClientRepresentationDTO registeredNodes(Map<String, Integer> registeredNodes) {
        this.registeredNodes = registeredNodes;
        return this;
    }

    public ClientRepresentationDTO putRegisteredNodesItem(String key, Integer registeredNodesItem) {
        if (this.registeredNodes == null) {
            this.registeredNodes = null;
        }
        this.registeredNodes.put(key, registeredNodesItem);
        return this;
    }


    public Map<String, Integer> getRegisteredNodes() {
        return registeredNodes;
    }

    public void setRegisteredNodes(Map<String, Integer> registeredNodes) {
        this.registeredNodes = registeredNodes;
    }

    public ClientRepresentationDTO frontchannelLogout(Boolean frontchannelLogout) {
        this.frontchannelLogout = frontchannelLogout;
        return this;
    }


    public Boolean isFrontchannelLogout() {
        return frontchannelLogout;
    }

    public void setFrontchannelLogout(Boolean frontchannelLogout) {
        this.frontchannelLogout = frontchannelLogout;
    }

    public ClientRepresentationDTO protocolMappers(List<ProtocolMapperRepresentationDTO> protocolMappers) {
        this.protocolMappers = protocolMappers;
        return this;
    }

    public ClientRepresentationDTO addProtocolMappersItem(ProtocolMapperRepresentationDTO protocolMappersItem) {
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

    public ClientRepresentationDTO clientTemplate(String clientTemplate) {
        this.clientTemplate = clientTemplate;
        return this;
    }


    public String getClientTemplate() {
        return clientTemplate;
    }

    public void setClientTemplate(String clientTemplate) {
        this.clientTemplate = clientTemplate;
    }

    public ClientRepresentationDTO useTemplateConfig(Boolean useTemplateConfig) {
        this.useTemplateConfig = useTemplateConfig;
        return this;
    }


    public Boolean isUseTemplateConfig() {
        return useTemplateConfig;
    }

    public void setUseTemplateConfig(Boolean useTemplateConfig) {
        this.useTemplateConfig = useTemplateConfig;
    }

    public ClientRepresentationDTO useTemplateScope(Boolean useTemplateScope) {
        this.useTemplateScope = useTemplateScope;
        return this;
    }


    public Boolean isUseTemplateScope() {
        return useTemplateScope;
    }

    public void setUseTemplateScope(Boolean useTemplateScope) {
        this.useTemplateScope = useTemplateScope;
    }

    public ClientRepresentationDTO useTemplateMappers(Boolean useTemplateMappers) {
        this.useTemplateMappers = useTemplateMappers;
        return this;
    }


    public Boolean isUseTemplateMappers() {
        return useTemplateMappers;
    }

    public void setUseTemplateMappers(Boolean useTemplateMappers) {
        this.useTemplateMappers = useTemplateMappers;
    }

    public ClientRepresentationDTO defaultClientScopes(List<String> defaultClientScopes) {
        this.defaultClientScopes = defaultClientScopes;
        return this;
    }

    public ClientRepresentationDTO addDefaultClientScopesItem(String defaultClientScopesItem) {
        if (this.defaultClientScopes == null) {
            this.defaultClientScopes = new ArrayList<String>();
        }
        this.defaultClientScopes.add(defaultClientScopesItem);
        return this;
    }


    public List<String> getDefaultClientScopes() {
        return defaultClientScopes;
    }

    public void setDefaultClientScopes(List<String> defaultClientScopes) {
        this.defaultClientScopes = defaultClientScopes;
    }

    public ClientRepresentationDTO optionalClientScopes(List<String> optionalClientScopes) {
        this.optionalClientScopes = optionalClientScopes;
        return this;
    }

    public ClientRepresentationDTO addOptionalClientScopesItem(String optionalClientScopesItem) {
        if (this.optionalClientScopes == null) {
            this.optionalClientScopes = new ArrayList<String>();
        }
        this.optionalClientScopes.add(optionalClientScopesItem);
        return this;
    }


    public List<String> getOptionalClientScopes() {
        return optionalClientScopes;
    }

    public void setOptionalClientScopes(List<String> optionalClientScopes) {
        this.optionalClientScopes = optionalClientScopes;
    }

    public ClientRepresentationDTO authorizationSettings(ResourceServerRepresentationDTO authorizationSettings) {
        this.authorizationSettings = authorizationSettings;
        return this;
    }


    public ResourceServerRepresentationDTO getAuthorizationSettings() {
        return authorizationSettings;
    }

    public void setAuthorizationSettings(ResourceServerRepresentationDTO authorizationSettings) {
        this.authorizationSettings = authorizationSettings;
    }

    public ClientRepresentationDTO access(Map<String, Boolean> access) {
        this.access = access;
        return this;
    }

    public ClientRepresentationDTO putAccessItem(String key, Boolean accessItem) {
        if (this.access == null) {
            this.access = null;
        }
        this.access.put(key, accessItem);
        return this;
    }


    public Map<String, Boolean> getAccess() {
        return access;
    }

    public void setAccess(Map<String, Boolean> access) {
        this.access = access;
    }

    public ClientRepresentationDTO origin(String origin) {
        this.origin = origin;
        return this;
    }


    public String getOrigin() {
        return origin;
    }

    public void setOrigin(String origin) {
        this.origin = origin;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ClientRepresentationDTO clientRepresentation = (ClientRepresentationDTO) o;
        return Objects.equals(this.id, clientRepresentation.id) &&
                Objects.equals(this.name, clientRepresentation.name) &&
                Objects.equals(this.description, clientRepresentation.description) &&
                Objects.equals(this.clientId, clientRepresentation.clientId) &&
                Objects.equals(this.enabled, clientRepresentation.enabled) &&
                Objects.equals(this.alwaysDisplayInConsole, clientRepresentation.alwaysDisplayInConsole) &&
                Objects.equals(this.surrogateAuthRequired, clientRepresentation.surrogateAuthRequired) &&
                Objects.equals(this.rootUrl, clientRepresentation.rootUrl) &&
                Objects.equals(this.adminUrl, clientRepresentation.adminUrl) &&
                Objects.equals(this.baseUrl, clientRepresentation.baseUrl) &&
                Objects.equals(this.clientAuthenticatorType, clientRepresentation.clientAuthenticatorType) &&
                Objects.equals(this.secret, clientRepresentation.secret) &&
                Objects.equals(this.registrationAccessToken, clientRepresentation.registrationAccessToken) &&
                Objects.equals(this.redirectUris, clientRepresentation.redirectUris) &&
                Objects.equals(this.webOrigins, clientRepresentation.webOrigins) &&
                Objects.equals(this.defaultRoles, clientRepresentation.defaultRoles) &&
                Objects.equals(this.notBefore, clientRepresentation.notBefore) &&
                Objects.equals(this.bearerOnly, clientRepresentation.bearerOnly) &&
                Objects.equals(this.consentRequired, clientRepresentation.consentRequired) &&
                Objects.equals(this.standardFlowEnabled, clientRepresentation.standardFlowEnabled) &&
                Objects.equals(this.implicitFlowEnabled, clientRepresentation.implicitFlowEnabled) &&
                Objects.equals(this.directAccessGrantsEnabled, clientRepresentation.directAccessGrantsEnabled) &&
                Objects.equals(this.serviceAccountsEnabled, clientRepresentation.serviceAccountsEnabled) &&
                Objects.equals(this.authorizationServicesEnabled, clientRepresentation.authorizationServicesEnabled) &&
                Objects.equals(this.directGrantsOnly, clientRepresentation.directGrantsOnly) &&
                Objects.equals(this.publicClient, clientRepresentation.publicClient) &&
                Objects.equals(this.fullScopeAllowed, clientRepresentation.fullScopeAllowed) &&
                Objects.equals(this.protocol, clientRepresentation.protocol) &&
                Objects.equals(this.attributes, clientRepresentation.attributes) &&
                Objects.equals(this.authenticationFlowBindingOverrides, clientRepresentation.authenticationFlowBindingOverrides) &&
                Objects.equals(this.nodeReRegistrationTimeout, clientRepresentation.nodeReRegistrationTimeout) &&
                Objects.equals(this.registeredNodes, clientRepresentation.registeredNodes) &&
                Objects.equals(this.frontchannelLogout, clientRepresentation.frontchannelLogout) &&
                Objects.equals(this.protocolMappers, clientRepresentation.protocolMappers) &&
                Objects.equals(this.clientTemplate, clientRepresentation.clientTemplate) &&
                Objects.equals(this.useTemplateConfig, clientRepresentation.useTemplateConfig) &&
                Objects.equals(this.useTemplateScope, clientRepresentation.useTemplateScope) &&
                Objects.equals(this.useTemplateMappers, clientRepresentation.useTemplateMappers) &&
                Objects.equals(this.defaultClientScopes, clientRepresentation.defaultClientScopes) &&
                Objects.equals(this.optionalClientScopes, clientRepresentation.optionalClientScopes) &&
                Objects.equals(this.authorizationSettings, clientRepresentation.authorizationSettings) &&
                Objects.equals(this.access, clientRepresentation.access) &&
                Objects.equals(this.origin, clientRepresentation.origin);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, description, clientId, enabled, alwaysDisplayInConsole, surrogateAuthRequired, rootUrl, adminUrl, baseUrl, clientAuthenticatorType, secret, registrationAccessToken, redirectUris, webOrigins, defaultRoles, notBefore, bearerOnly, consentRequired, standardFlowEnabled, implicitFlowEnabled, directAccessGrantsEnabled, serviceAccountsEnabled, authorizationServicesEnabled, directGrantsOnly, publicClient, fullScopeAllowed, protocol, attributes, authenticationFlowBindingOverrides, nodeReRegistrationTimeout, registeredNodes, frontchannelLogout, protocolMappers, clientTemplate, useTemplateConfig, useTemplateScope, useTemplateMappers, defaultClientScopes, optionalClientScopes, authorizationSettings, access, origin);
    }

    @Override
    public String toString() {

        String sb = "class ClientRepresentationDTO {\n" +
                "    id: " + toIndentedString(id) + "\n" +
                "    name: " + toIndentedString(name) + "\n" +
                "    description: " + toIndentedString(description) + "\n" +
                "    clientId: " + toIndentedString(clientId) + "\n" +
                "    enabled: " + toIndentedString(enabled) + "\n" +
                "    alwaysDisplayInConsole: " + toIndentedString(alwaysDisplayInConsole) + "\n" +
                "    surrogateAuthRequired: " + toIndentedString(surrogateAuthRequired) + "\n" +
                "    rootUrl: " + toIndentedString(rootUrl) + "\n" +
                "    adminUrl: " + toIndentedString(adminUrl) + "\n" +
                "    baseUrl: " + toIndentedString(baseUrl) + "\n" +
                "    clientAuthenticatorType: " + toIndentedString(clientAuthenticatorType) + "\n" +
                "    secret: " + toIndentedString(secret) + "\n" +
                "    registrationAccessToken: " + toIndentedString(registrationAccessToken) + "\n" +
                "    redirectUris: " + toIndentedString(redirectUris) + "\n" +
                "    webOrigins: " + toIndentedString(webOrigins) + "\n" +
                "    defaultRoles: " + toIndentedString(defaultRoles) + "\n" +
                "    notBefore: " + toIndentedString(notBefore) + "\n" +
                "    bearerOnly: " + toIndentedString(bearerOnly) + "\n" +
                "    consentRequired: " + toIndentedString(consentRequired) + "\n" +
                "    standardFlowEnabled: " + toIndentedString(standardFlowEnabled) + "\n" +
                "    implicitFlowEnabled: " + toIndentedString(implicitFlowEnabled) + "\n" +
                "    directAccessGrantsEnabled: " + toIndentedString(directAccessGrantsEnabled) + "\n" +
                "    serviceAccountsEnabled: " + toIndentedString(serviceAccountsEnabled) + "\n" +
                "    authorizationServicesEnabled: " + toIndentedString(authorizationServicesEnabled) + "\n" +
                "    directGrantsOnly: " + toIndentedString(directGrantsOnly) + "\n" +
                "    publicClient: " + toIndentedString(publicClient) + "\n" +
                "    fullScopeAllowed: " + toIndentedString(fullScopeAllowed) + "\n" +
                "    protocol: " + toIndentedString(protocol) + "\n" +
                "    attributes: " + toIndentedString(attributes) + "\n" +
                "    authenticationFlowBindingOverrides: " + toIndentedString(authenticationFlowBindingOverrides) + "\n" +
                "    nodeReRegistrationTimeout: " + toIndentedString(nodeReRegistrationTimeout) + "\n" +
                "    registeredNodes: " + toIndentedString(registeredNodes) + "\n" +
                "    frontchannelLogout: " + toIndentedString(frontchannelLogout) + "\n" +
                "    protocolMappers: " + toIndentedString(protocolMappers) + "\n" +
                "    clientTemplate: " + toIndentedString(clientTemplate) + "\n" +
                "    useTemplateConfig: " + toIndentedString(useTemplateConfig) + "\n" +
                "    useTemplateScope: " + toIndentedString(useTemplateScope) + "\n" +
                "    useTemplateMappers: " + toIndentedString(useTemplateMappers) + "\n" +
                "    defaultClientScopes: " + toIndentedString(defaultClientScopes) + "\n" +
                "    optionalClientScopes: " + toIndentedString(optionalClientScopes) + "\n" +
                "    authorizationSettings: " + toIndentedString(authorizationSettings) + "\n" +
                "    access: " + toIndentedString(access) + "\n" +
                "    origin: " + toIndentedString(origin) + "\n" +
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

