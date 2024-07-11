package it.getinsight.module.keycloak.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;


public class RealmRepresentationDTO {
    @JsonProperty("id")
    private String id = null;

    @JsonProperty("realm")
    private String realm = null;

    @JsonProperty("displayName")
    private String displayName = null;

    @JsonProperty("displayNameHtml")
    private String displayNameHtml = null;

    @JsonProperty("users")
    private List<UserRepresentationDTO> users = null;

    @JsonProperty("applications")
    private List<ApplicationRepresentationDTO> applications = null;

    @JsonProperty("clients")
    private List<ClientRepresentationDTO> clients = null;

    @JsonProperty("enabled")
    private Boolean enabled = null;

    @JsonProperty("sslRequired")
    private String sslRequired = null;

    @JsonProperty("defaultSignatureAlgorithm")
    private String defaultSignatureAlgorithm = null;

    @JsonProperty("revokeRefreshToken")
    private Boolean revokeRefreshToken = null;

    @JsonProperty("refreshTokenMaxReuse")
    private Integer refreshTokenMaxReuse = null;

    @JsonProperty("accessTokenLifespan")
    private Integer accessTokenLifespan = null;

    @JsonProperty("accessTokenLifespanForImplicitFlow")
    private Integer accessTokenLifespanForImplicitFlow = null;

    @JsonProperty("ssoSessionIdleTimeout")
    private Integer ssoSessionIdleTimeout = null;

    @JsonProperty("ssoSessionMaxLifespan")
    private Integer ssoSessionMaxLifespan = null;

    @JsonProperty("ssoSessionMaxLifespanRememberMe")
    private Integer ssoSessionMaxLifespanRememberMe = null;

    @JsonProperty("ssoSessionIdleTimeoutRememberMe")
    private Integer ssoSessionIdleTimeoutRememberMe = null;

    @JsonProperty("offlineSessionIdleTimeout")
    private Integer offlineSessionIdleTimeout = null;

    @JsonProperty("offlineSessionMaxLifespanEnabled")
    private Boolean offlineSessionMaxLifespanEnabled = null;

    @JsonProperty("offlineSessionMaxLifespan")
    private Integer offlineSessionMaxLifespan = null;

    @JsonProperty("clientSessionIdleTimeout")
    private Integer clientSessionIdleTimeout = null;

    @JsonProperty("clientSessionMaxLifespan")
    private Integer clientSessionMaxLifespan = null;

    @JsonProperty("clientOfflineSessionIdleTimeout")
    private Integer clientOfflineSessionIdleTimeout = null;

    @JsonProperty("clientOfflineSessionMaxLifespan")
    private Integer clientOfflineSessionMaxLifespan = null;

    @JsonProperty("scopeMappings")
    private List<ScopeMappingRepresentationDTO> scopeMappings = null;

    @JsonProperty("requiredCredentials")
    private List<String> requiredCredentials = null;

    @JsonProperty("passwordPolicy")
    private String passwordPolicy = null;

    @JsonProperty("accessCodeLifespan")
    private Integer accessCodeLifespan = null;

    @JsonProperty("accessCodeLifespanUserAction")
    private Integer accessCodeLifespanUserAction = null;

    @JsonProperty("accessCodeLifespanLogin")
    private Integer accessCodeLifespanLogin = null;

    @JsonProperty("actionTokenGeneratedByAdminLifespan")
    private Integer actionTokenGeneratedByAdminLifespan = null;

    @JsonProperty("oAuth2DeviceCodeLifespan")
    private Integer oAuth2DeviceCodeLifespan = null;

    @JsonProperty("oAuth2DevicePollingInterval")
    private Integer oAuth2DevicePollingInterval = null;

    @JsonProperty("actionTokenGeneratedByUserLifespan")
    private Integer actionTokenGeneratedByUserLifespan = null;

    @JsonProperty("defaultRoles")
    private List<String> defaultRoles = null;

    @JsonProperty("defaultRole")
    private RoleRepresentationDTO defaultRole = null;

    @JsonProperty("defaultGroups")
    private List<String> defaultGroups = null;

    @JsonProperty("privateKey")
    private String privateKey = null;

    @JsonProperty("publicKey")
    private String publicKey = null;

    @JsonProperty("certificate")
    private String certificate = null;

    @JsonProperty("codeSecret")
    private String codeSecret = null;

    @JsonProperty("passwordCredentialGrantAllowed")
    private Boolean passwordCredentialGrantAllowed = null;

    @JsonProperty("registrationAllowed")
    private Boolean registrationAllowed = null;

    @JsonProperty("registrationEmailAsUsername")
    private Boolean registrationEmailAsUsername = null;

    @JsonProperty("rememberMe")
    private Boolean rememberMe = null;

    @JsonProperty("verifyEmail")
    private Boolean verifyEmail = null;

    @JsonProperty("loginWithEmailAllowed")
    private Boolean loginWithEmailAllowed = null;

    @JsonProperty("duplicateEmailsAllowed")
    private Boolean duplicateEmailsAllowed = null;

    @JsonProperty("resetPasswordAllowed")
    private Boolean resetPasswordAllowed = null;

    @JsonProperty("editUsernameAllowed")
    private Boolean editUsernameAllowed = null;

    @JsonProperty("social")
    private Boolean social = null;

    @JsonProperty("updateProfileOnInitialSocialLogin")
    private Boolean updateProfileOnInitialSocialLogin = null;

    @JsonProperty("browserSecurityHeaders")
    private Map<String, String> browserSecurityHeaders = null;

    @JsonProperty("socialProviders")
    private Map<String, String> socialProviders = null;

    @JsonProperty("smtpServer")
    private Map<String, String> smtpServer = null;

    @JsonProperty("oauthClients")
    private List<OAuthClientRepresentationDTO> oauthClients = null;

    @JsonProperty("clientScopeMappings")
    private Map<String, List<ScopeMappingRepresentationDTO>> clientScopeMappings = null;

    @JsonProperty("applicationScopeMappings")
    private Map<String, List<ScopeMappingRepresentationDTO>> applicationScopeMappings = null;

    @JsonProperty("roles")
    private RolesRepresentationDTO roles = null;

    @JsonProperty("loginTheme")
    private String loginTheme = null;

    @JsonProperty("accountTheme")
    private String accountTheme = null;

    @JsonProperty("adminTheme")
    private String adminTheme = null;

    @JsonProperty("emailTheme")
    private String emailTheme = null;

    @JsonProperty("notBefore")
    private Integer notBefore = null;

    @JsonProperty("bruteForceProtected")
    private Boolean bruteForceProtected = null;

    @JsonProperty("permanentLockout")
    private Boolean permanentLockout = null;

    @JsonProperty("maxFailureWaitSeconds")
    private Integer maxFailureWaitSeconds = null;

    @JsonProperty("minimumQuickLoginWaitSeconds")
    private Integer minimumQuickLoginWaitSeconds = null;

    @JsonProperty("waitIncrementSeconds")
    private Integer waitIncrementSeconds = null;

    @JsonProperty("quickLoginCheckMilliSeconds")
    private Long quickLoginCheckMilliSeconds = null;

    @JsonProperty("maxDeltaTimeSeconds")
    private Integer maxDeltaTimeSeconds = null;

    @JsonProperty("failureFactor")
    private Integer failureFactor = null;

    @JsonProperty("eventsEnabled")
    private Boolean eventsEnabled = null;

    @JsonProperty("eventsExpiration")
    private Long eventsExpiration = null;

    @JsonProperty("eventsListeners")
    private List<String> eventsListeners = null;

    @JsonProperty("enabledEventTypes")
    private List<String> enabledEventTypes = null;

    @JsonProperty("adminEventsEnabled")
    private Boolean adminEventsEnabled = null;

    @JsonProperty("adminEventsDetailsEnabled")
    private Boolean adminEventsDetailsEnabled = null;

    @JsonProperty("userFederationProviders")
    private List<UserFederationProviderRepresentationDTO> userFederationProviders = null;

    @JsonProperty("userFederationMappers")
    private List<UserFederationMapperRepresentationDTO> userFederationMappers = null;

    @JsonProperty("identityProviders")
    private List<IdentityProviderRepresentationDTO> identityProviders = null;

    @JsonProperty("protocolMappers")
    private List<ProtocolMapperRepresentationDTO> protocolMappers = null;

    @JsonProperty("internationalizationEnabled")
    private Boolean internationalizationEnabled = null;

    @JsonProperty("supportedLocales")
    private List<String> supportedLocales = null;

    @JsonProperty("defaultLocale")
    private String defaultLocale = null;

    @JsonProperty("identityProviderMappers")
    private List<IdentityProviderMapperRepresentationDTO> identityProviderMappers = null;

    @JsonProperty("authenticationFlows")
    private List<AuthenticationFlowRepresentationDTO> authenticationFlows = null;

    @JsonProperty("authenticatorConfig")
    private List<AuthenticatorConfigRepresentationDTO> authenticatorConfig = null;

    @JsonProperty("requiredActions")
    private List<RequiredActionProviderRepresentationDTO> requiredActions = null;

    @JsonProperty("otpPolicyType")
    private String otpPolicyType = null;

    @JsonProperty("otpPolicyAlgorithm")
    private String otpPolicyAlgorithm = null;

    @JsonProperty("otpPolicyInitialCounter")
    private Integer otpPolicyInitialCounter = null;

    @JsonProperty("otpPolicyDigits")
    private Integer otpPolicyDigits = null;

    @JsonProperty("otpPolicyLookAheadWindow")
    private Integer otpPolicyLookAheadWindow = null;

    @JsonProperty("otpPolicyPeriod")
    private Integer otpPolicyPeriod = null;

    @JsonProperty("otpSupportedApplications")
    private List<String> otpSupportedApplications = null;

    @JsonProperty("webAuthnPolicyRpEntityName")
    private String webAuthnPolicyRpEntityName = null;

    @JsonProperty("webAuthnPolicySignatureAlgorithms")
    private List<String> webAuthnPolicySignatureAlgorithms = null;

    @JsonProperty("webAuthnPolicyRpId")
    private String webAuthnPolicyRpId = null;

    @JsonProperty("webAuthnPolicyAttestationConveyancePreference")
    private String webAuthnPolicyAttestationConveyancePreference = null;

    @JsonProperty("webAuthnPolicyAuthenticatorAttachment")
    private String webAuthnPolicyAuthenticatorAttachment = null;

    @JsonProperty("webAuthnPolicyRequireResidentKey")
    private String webAuthnPolicyRequireResidentKey = null;

    @JsonProperty("webAuthnPolicyUserVerificationRequirement")
    private String webAuthnPolicyUserVerificationRequirement = null;

    @JsonProperty("webAuthnPolicyCreateTimeout")
    private Integer webAuthnPolicyCreateTimeout = null;

    @JsonProperty("webAuthnPolicyAvoidSameAuthenticatorRegister")
    private Boolean webAuthnPolicyAvoidSameAuthenticatorRegister = null;

    @JsonProperty("webAuthnPolicyAcceptableAaguids")
    private List<String> webAuthnPolicyAcceptableAaguids = null;

    @JsonProperty("webAuthnPolicyPasswordlessRpEntityName")
    private String webAuthnPolicyPasswordlessRpEntityName = null;

    @JsonProperty("webAuthnPolicyPasswordlessSignatureAlgorithms")
    private List<String> webAuthnPolicyPasswordlessSignatureAlgorithms = null;

    @JsonProperty("webAuthnPolicyPasswordlessRpId")
    private String webAuthnPolicyPasswordlessRpId = null;

    @JsonProperty("webAuthnPolicyPasswordlessAttestationConveyancePreference")
    private String webAuthnPolicyPasswordlessAttestationConveyancePreference = null;

    @JsonProperty("webAuthnPolicyPasswordlessAuthenticatorAttachment")
    private String webAuthnPolicyPasswordlessAuthenticatorAttachment = null;

    @JsonProperty("webAuthnPolicyPasswordlessRequireResidentKey")
    private String webAuthnPolicyPasswordlessRequireResidentKey = null;

    @JsonProperty("webAuthnPolicyPasswordlessUserVerificationRequirement")
    private String webAuthnPolicyPasswordlessUserVerificationRequirement = null;

    @JsonProperty("webAuthnPolicyPasswordlessCreateTimeout")
    private Integer webAuthnPolicyPasswordlessCreateTimeout = null;

    @JsonProperty("webAuthnPolicyPasswordlessAvoidSameAuthenticatorRegister")
    private Boolean webAuthnPolicyPasswordlessAvoidSameAuthenticatorRegister = null;

    @JsonProperty("webAuthnPolicyPasswordlessAcceptableAaguids")
    private List<String> webAuthnPolicyPasswordlessAcceptableAaguids = null;

    @JsonProperty("browserFlow")
    private String browserFlow = null;

    @JsonProperty("registrationFlow")
    private String registrationFlow = null;

    @JsonProperty("directGrantFlow")
    private String directGrantFlow = null;

    @JsonProperty("resetCredentialsFlow")
    private String resetCredentialsFlow = null;

    @JsonProperty("clientAuthenticationFlow")
    private String clientAuthenticationFlow = null;

    @JsonProperty("dockerAuthenticationFlow")
    private String dockerAuthenticationFlow = null;

    @JsonProperty("keycloakVersion")
    private String keycloakVersion = null;

    @JsonProperty("groups")
    private List<GroupRepresentationDTO> groups = null;

    @JsonProperty("clientTemplates")
    private List<ClientTemplateRepresentationDTO> clientTemplates = null;

    @JsonProperty("clientScopes")
    private List<ClientScopeRepresentationDTO> clientScopes = null;

    @JsonProperty("defaultDefaultClientScopes")
    private List<String> defaultDefaultClientScopes = null;

    @JsonProperty("defaultOptionalClientScopes")
    private List<String> defaultOptionalClientScopes = null;

    @JsonProperty("components")
    private Map<String, ComponentExportRepresentationDTO> components = null;

    @JsonProperty("attributes")
    private Map<String, String> attributes = null;

    @JsonProperty("federatedUsers")
    private List<UserRepresentationDTO> federatedUsers = null;

    @JsonProperty("userManagedAccessAllowed")
    private Boolean userManagedAccessAllowed = null;

    public RealmRepresentationDTO id(String id) {
        this.id = id;
        return this;
    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public RealmRepresentationDTO realm(String realm) {
        this.realm = realm;
        return this;
    }


    public String getRealm() {
        return realm;
    }

    public void setRealm(String realm) {
        this.realm = realm;
    }

    public RealmRepresentationDTO displayName(String displayName) {
        this.displayName = displayName;
        return this;
    }


    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public RealmRepresentationDTO displayNameHtml(String displayNameHtml) {
        this.displayNameHtml = displayNameHtml;
        return this;
    }


    public String getDisplayNameHtml() {
        return displayNameHtml;
    }

    public void setDisplayNameHtml(String displayNameHtml) {
        this.displayNameHtml = displayNameHtml;
    }

    public RealmRepresentationDTO users(List<UserRepresentationDTO> users) {
        this.users = users;
        return this;
    }

    public RealmRepresentationDTO addUsersItem(UserRepresentationDTO usersItem) {
        if (this.users == null) {
            this.users = new ArrayList<UserRepresentationDTO>();
        }
        this.users.add(usersItem);
        return this;
    }


    public List<UserRepresentationDTO> getUsers() {
        return users;
    }

    public void setUsers(List<UserRepresentationDTO> users) {
        this.users = users;
    }

    public RealmRepresentationDTO applications(List<ApplicationRepresentationDTO> applications) {
        this.applications = applications;
        return this;
    }

    public RealmRepresentationDTO addApplicationsItem(ApplicationRepresentationDTO applicationsItem) {
        if (this.applications == null) {
            this.applications = new ArrayList<ApplicationRepresentationDTO>();
        }
        this.applications.add(applicationsItem);
        return this;
    }


    public List<ApplicationRepresentationDTO> getApplications() {
        return applications;
    }

    public void setApplications(List<ApplicationRepresentationDTO> applications) {
        this.applications = applications;
    }

    public RealmRepresentationDTO clients(List<ClientRepresentationDTO> clients) {
        this.clients = clients;
        return this;
    }

    public RealmRepresentationDTO addClientsItem(ClientRepresentationDTO clientsItem) {
        if (this.clients == null) {
            this.clients = new ArrayList<ClientRepresentationDTO>();
        }
        this.clients.add(clientsItem);
        return this;
    }


    public List<ClientRepresentationDTO> getClients() {
        return clients;
    }

    public void setClients(List<ClientRepresentationDTO> clients) {
        this.clients = clients;
    }

    public RealmRepresentationDTO enabled(Boolean enabled) {
        this.enabled = enabled;
        return this;
    }


    public Boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(Boolean enabled) {
        this.enabled = enabled;
    }

    public RealmRepresentationDTO sslRequired(String sslRequired) {
        this.sslRequired = sslRequired;
        return this;
    }


    public String getSslRequired() {
        return sslRequired;
    }

    public void setSslRequired(String sslRequired) {
        this.sslRequired = sslRequired;
    }

    public RealmRepresentationDTO defaultSignatureAlgorithm(String defaultSignatureAlgorithm) {
        this.defaultSignatureAlgorithm = defaultSignatureAlgorithm;
        return this;
    }


    public String getDefaultSignatureAlgorithm() {
        return defaultSignatureAlgorithm;
    }

    public void setDefaultSignatureAlgorithm(String defaultSignatureAlgorithm) {
        this.defaultSignatureAlgorithm = defaultSignatureAlgorithm;
    }

    public RealmRepresentationDTO revokeRefreshToken(Boolean revokeRefreshToken) {
        this.revokeRefreshToken = revokeRefreshToken;
        return this;
    }


    public Boolean isRevokeRefreshToken() {
        return revokeRefreshToken;
    }

    public void setRevokeRefreshToken(Boolean revokeRefreshToken) {
        this.revokeRefreshToken = revokeRefreshToken;
    }

    public RealmRepresentationDTO refreshTokenMaxReuse(Integer refreshTokenMaxReuse) {
        this.refreshTokenMaxReuse = refreshTokenMaxReuse;
        return this;
    }


    public Integer getRefreshTokenMaxReuse() {
        return refreshTokenMaxReuse;
    }

    public void setRefreshTokenMaxReuse(Integer refreshTokenMaxReuse) {
        this.refreshTokenMaxReuse = refreshTokenMaxReuse;
    }

    public RealmRepresentationDTO accessTokenLifespan(Integer accessTokenLifespan) {
        this.accessTokenLifespan = accessTokenLifespan;
        return this;
    }


    public Integer getAccessTokenLifespan() {
        return accessTokenLifespan;
    }

    public void setAccessTokenLifespan(Integer accessTokenLifespan) {
        this.accessTokenLifespan = accessTokenLifespan;
    }

    public RealmRepresentationDTO accessTokenLifespanForImplicitFlow(Integer accessTokenLifespanForImplicitFlow) {
        this.accessTokenLifespanForImplicitFlow = accessTokenLifespanForImplicitFlow;
        return this;
    }


    public Integer getAccessTokenLifespanForImplicitFlow() {
        return accessTokenLifespanForImplicitFlow;
    }

    public void setAccessTokenLifespanForImplicitFlow(Integer accessTokenLifespanForImplicitFlow) {
        this.accessTokenLifespanForImplicitFlow = accessTokenLifespanForImplicitFlow;
    }

    public RealmRepresentationDTO ssoSessionIdleTimeout(Integer ssoSessionIdleTimeout) {
        this.ssoSessionIdleTimeout = ssoSessionIdleTimeout;
        return this;
    }


    public Integer getSsoSessionIdleTimeout() {
        return ssoSessionIdleTimeout;
    }

    public void setSsoSessionIdleTimeout(Integer ssoSessionIdleTimeout) {
        this.ssoSessionIdleTimeout = ssoSessionIdleTimeout;
    }

    public RealmRepresentationDTO ssoSessionMaxLifespan(Integer ssoSessionMaxLifespan) {
        this.ssoSessionMaxLifespan = ssoSessionMaxLifespan;
        return this;
    }


    public Integer getSsoSessionMaxLifespan() {
        return ssoSessionMaxLifespan;
    }

    public void setSsoSessionMaxLifespan(Integer ssoSessionMaxLifespan) {
        this.ssoSessionMaxLifespan = ssoSessionMaxLifespan;
    }

    public RealmRepresentationDTO ssoSessionMaxLifespanRememberMe(Integer ssoSessionMaxLifespanRememberMe) {
        this.ssoSessionMaxLifespanRememberMe = ssoSessionMaxLifespanRememberMe;
        return this;
    }


    public Integer getSsoSessionMaxLifespanRememberMe() {
        return ssoSessionMaxLifespanRememberMe;
    }

    public void setSsoSessionMaxLifespanRememberMe(Integer ssoSessionMaxLifespanRememberMe) {
        this.ssoSessionMaxLifespanRememberMe = ssoSessionMaxLifespanRememberMe;
    }

    public RealmRepresentationDTO ssoSessionIdleTimeoutRememberMe(Integer ssoSessionIdleTimeoutRememberMe) {
        this.ssoSessionIdleTimeoutRememberMe = ssoSessionIdleTimeoutRememberMe;
        return this;
    }


    public Integer getSsoSessionIdleTimeoutRememberMe() {
        return ssoSessionIdleTimeoutRememberMe;
    }

    public void setSsoSessionIdleTimeoutRememberMe(Integer ssoSessionIdleTimeoutRememberMe) {
        this.ssoSessionIdleTimeoutRememberMe = ssoSessionIdleTimeoutRememberMe;
    }

    public RealmRepresentationDTO offlineSessionIdleTimeout(Integer offlineSessionIdleTimeout) {
        this.offlineSessionIdleTimeout = offlineSessionIdleTimeout;
        return this;
    }


    public Integer getOfflineSessionIdleTimeout() {
        return offlineSessionIdleTimeout;
    }

    public void setOfflineSessionIdleTimeout(Integer offlineSessionIdleTimeout) {
        this.offlineSessionIdleTimeout = offlineSessionIdleTimeout;
    }

    public RealmRepresentationDTO offlineSessionMaxLifespanEnabled(Boolean offlineSessionMaxLifespanEnabled) {
        this.offlineSessionMaxLifespanEnabled = offlineSessionMaxLifespanEnabled;
        return this;
    }


    public Boolean isOfflineSessionMaxLifespanEnabled() {
        return offlineSessionMaxLifespanEnabled;
    }

    public void setOfflineSessionMaxLifespanEnabled(Boolean offlineSessionMaxLifespanEnabled) {
        this.offlineSessionMaxLifespanEnabled = offlineSessionMaxLifespanEnabled;
    }

    public RealmRepresentationDTO offlineSessionMaxLifespan(Integer offlineSessionMaxLifespan) {
        this.offlineSessionMaxLifespan = offlineSessionMaxLifespan;
        return this;
    }


    public Integer getOfflineSessionMaxLifespan() {
        return offlineSessionMaxLifespan;
    }

    public void setOfflineSessionMaxLifespan(Integer offlineSessionMaxLifespan) {
        this.offlineSessionMaxLifespan = offlineSessionMaxLifespan;
    }

    public RealmRepresentationDTO clientSessionIdleTimeout(Integer clientSessionIdleTimeout) {
        this.clientSessionIdleTimeout = clientSessionIdleTimeout;
        return this;
    }


    public Integer getClientSessionIdleTimeout() {
        return clientSessionIdleTimeout;
    }

    public void setClientSessionIdleTimeout(Integer clientSessionIdleTimeout) {
        this.clientSessionIdleTimeout = clientSessionIdleTimeout;
    }

    public RealmRepresentationDTO clientSessionMaxLifespan(Integer clientSessionMaxLifespan) {
        this.clientSessionMaxLifespan = clientSessionMaxLifespan;
        return this;
    }


    public Integer getClientSessionMaxLifespan() {
        return clientSessionMaxLifespan;
    }

    public void setClientSessionMaxLifespan(Integer clientSessionMaxLifespan) {
        this.clientSessionMaxLifespan = clientSessionMaxLifespan;
    }

    public RealmRepresentationDTO clientOfflineSessionIdleTimeout(Integer clientOfflineSessionIdleTimeout) {
        this.clientOfflineSessionIdleTimeout = clientOfflineSessionIdleTimeout;
        return this;
    }


    public Integer getClientOfflineSessionIdleTimeout() {
        return clientOfflineSessionIdleTimeout;
    }

    public void setClientOfflineSessionIdleTimeout(Integer clientOfflineSessionIdleTimeout) {
        this.clientOfflineSessionIdleTimeout = clientOfflineSessionIdleTimeout;
    }

    public RealmRepresentationDTO clientOfflineSessionMaxLifespan(Integer clientOfflineSessionMaxLifespan) {
        this.clientOfflineSessionMaxLifespan = clientOfflineSessionMaxLifespan;
        return this;
    }


    public Integer getClientOfflineSessionMaxLifespan() {
        return clientOfflineSessionMaxLifespan;
    }

    public void setClientOfflineSessionMaxLifespan(Integer clientOfflineSessionMaxLifespan) {
        this.clientOfflineSessionMaxLifespan = clientOfflineSessionMaxLifespan;
    }

    public RealmRepresentationDTO scopeMappings(List<ScopeMappingRepresentationDTO> scopeMappings) {
        this.scopeMappings = scopeMappings;
        return this;
    }

    public RealmRepresentationDTO addScopeMappingsItem(ScopeMappingRepresentationDTO scopeMappingsItem) {
        if (this.scopeMappings == null) {
            this.scopeMappings = new ArrayList<ScopeMappingRepresentationDTO>();
        }
        this.scopeMappings.add(scopeMappingsItem);
        return this;
    }


    public List<ScopeMappingRepresentationDTO> getScopeMappings() {
        return scopeMappings;
    }

    public void setScopeMappings(List<ScopeMappingRepresentationDTO> scopeMappings) {
        this.scopeMappings = scopeMappings;
    }

    public RealmRepresentationDTO requiredCredentials(List<String> requiredCredentials) {
        this.requiredCredentials = requiredCredentials;
        return this;
    }

    public RealmRepresentationDTO addRequiredCredentialsItem(String requiredCredentialsItem) {
        if (this.requiredCredentials == null) {
            this.requiredCredentials = new ArrayList<String>();
        }
        this.requiredCredentials.add(requiredCredentialsItem);
        return this;
    }


    public List<String> getRequiredCredentials() {
        return requiredCredentials;
    }

    public void setRequiredCredentials(List<String> requiredCredentials) {
        this.requiredCredentials = requiredCredentials;
    }

    public RealmRepresentationDTO passwordPolicy(String passwordPolicy) {
        this.passwordPolicy = passwordPolicy;
        return this;
    }


    public String getPasswordPolicy() {
        return passwordPolicy;
    }

    public void setPasswordPolicy(String passwordPolicy) {
        this.passwordPolicy = passwordPolicy;
    }

    public RealmRepresentationDTO accessCodeLifespan(Integer accessCodeLifespan) {
        this.accessCodeLifespan = accessCodeLifespan;
        return this;
    }


    public Integer getAccessCodeLifespan() {
        return accessCodeLifespan;
    }

    public void setAccessCodeLifespan(Integer accessCodeLifespan) {
        this.accessCodeLifespan = accessCodeLifespan;
    }

    public RealmRepresentationDTO accessCodeLifespanUserAction(Integer accessCodeLifespanUserAction) {
        this.accessCodeLifespanUserAction = accessCodeLifespanUserAction;
        return this;
    }


    public Integer getAccessCodeLifespanUserAction() {
        return accessCodeLifespanUserAction;
    }

    public void setAccessCodeLifespanUserAction(Integer accessCodeLifespanUserAction) {
        this.accessCodeLifespanUserAction = accessCodeLifespanUserAction;
    }

    public RealmRepresentationDTO accessCodeLifespanLogin(Integer accessCodeLifespanLogin) {
        this.accessCodeLifespanLogin = accessCodeLifespanLogin;
        return this;
    }


    public Integer getAccessCodeLifespanLogin() {
        return accessCodeLifespanLogin;
    }

    public void setAccessCodeLifespanLogin(Integer accessCodeLifespanLogin) {
        this.accessCodeLifespanLogin = accessCodeLifespanLogin;
    }

    public RealmRepresentationDTO actionTokenGeneratedByAdminLifespan(Integer actionTokenGeneratedByAdminLifespan) {
        this.actionTokenGeneratedByAdminLifespan = actionTokenGeneratedByAdminLifespan;
        return this;
    }


    public Integer getActionTokenGeneratedByAdminLifespan() {
        return actionTokenGeneratedByAdminLifespan;
    }

    public void setActionTokenGeneratedByAdminLifespan(Integer actionTokenGeneratedByAdminLifespan) {
        this.actionTokenGeneratedByAdminLifespan = actionTokenGeneratedByAdminLifespan;
    }

    public RealmRepresentationDTO oAuth2DeviceCodeLifespan(Integer oAuth2DeviceCodeLifespan) {
        this.oAuth2DeviceCodeLifespan = oAuth2DeviceCodeLifespan;
        return this;
    }


    public Integer getOAuth2DeviceCodeLifespan() {
        return oAuth2DeviceCodeLifespan;
    }

    public void setOAuth2DeviceCodeLifespan(Integer oAuth2DeviceCodeLifespan) {
        this.oAuth2DeviceCodeLifespan = oAuth2DeviceCodeLifespan;
    }

    public RealmRepresentationDTO oAuth2DevicePollingInterval(Integer oAuth2DevicePollingInterval) {
        this.oAuth2DevicePollingInterval = oAuth2DevicePollingInterval;
        return this;
    }


    public Integer getOAuth2DevicePollingInterval() {
        return oAuth2DevicePollingInterval;
    }

    public void setOAuth2DevicePollingInterval(Integer oAuth2DevicePollingInterval) {
        this.oAuth2DevicePollingInterval = oAuth2DevicePollingInterval;
    }

    public RealmRepresentationDTO actionTokenGeneratedByUserLifespan(Integer actionTokenGeneratedByUserLifespan) {
        this.actionTokenGeneratedByUserLifespan = actionTokenGeneratedByUserLifespan;
        return this;
    }


    public Integer getActionTokenGeneratedByUserLifespan() {
        return actionTokenGeneratedByUserLifespan;
    }

    public void setActionTokenGeneratedByUserLifespan(Integer actionTokenGeneratedByUserLifespan) {
        this.actionTokenGeneratedByUserLifespan = actionTokenGeneratedByUserLifespan;
    }

    public RealmRepresentationDTO defaultRoles(List<String> defaultRoles) {
        this.defaultRoles = defaultRoles;
        return this;
    }

    public RealmRepresentationDTO addDefaultRolesItem(String defaultRolesItem) {
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

    public RealmRepresentationDTO defaultRole(RoleRepresentationDTO defaultRole) {
        this.defaultRole = defaultRole;
        return this;
    }


    public RoleRepresentationDTO getDefaultRole() {
        return defaultRole;
    }

    public void setDefaultRole(RoleRepresentationDTO defaultRole) {
        this.defaultRole = defaultRole;
    }

    public RealmRepresentationDTO defaultGroups(List<String> defaultGroups) {
        this.defaultGroups = defaultGroups;
        return this;
    }

    public RealmRepresentationDTO addDefaultGroupsItem(String defaultGroupsItem) {
        if (this.defaultGroups == null) {
            this.defaultGroups = new ArrayList<String>();
        }
        this.defaultGroups.add(defaultGroupsItem);
        return this;
    }


    public List<String> getDefaultGroups() {
        return defaultGroups;
    }

    public void setDefaultGroups(List<String> defaultGroups) {
        this.defaultGroups = defaultGroups;
    }

    public RealmRepresentationDTO privateKey(String privateKey) {
        this.privateKey = privateKey;
        return this;
    }


    public String getPrivateKey() {
        return privateKey;
    }

    public void setPrivateKey(String privateKey) {
        this.privateKey = privateKey;
    }

    public RealmRepresentationDTO publicKey(String publicKey) {
        this.publicKey = publicKey;
        return this;
    }


    public String getPublicKey() {
        return publicKey;
    }

    public void setPublicKey(String publicKey) {
        this.publicKey = publicKey;
    }

    public RealmRepresentationDTO certificate(String certificate) {
        this.certificate = certificate;
        return this;
    }


    public String getCertificate() {
        return certificate;
    }

    public void setCertificate(String certificate) {
        this.certificate = certificate;
    }

    public RealmRepresentationDTO codeSecret(String codeSecret) {
        this.codeSecret = codeSecret;
        return this;
    }


    public String getCodeSecret() {
        return codeSecret;
    }

    public void setCodeSecret(String codeSecret) {
        this.codeSecret = codeSecret;
    }

    public RealmRepresentationDTO passwordCredentialGrantAllowed(Boolean passwordCredentialGrantAllowed) {
        this.passwordCredentialGrantAllowed = passwordCredentialGrantAllowed;
        return this;
    }


    public Boolean isPasswordCredentialGrantAllowed() {
        return passwordCredentialGrantAllowed;
    }

    public void setPasswordCredentialGrantAllowed(Boolean passwordCredentialGrantAllowed) {
        this.passwordCredentialGrantAllowed = passwordCredentialGrantAllowed;
    }

    public RealmRepresentationDTO registrationAllowed(Boolean registrationAllowed) {
        this.registrationAllowed = registrationAllowed;
        return this;
    }


    public Boolean isRegistrationAllowed() {
        return registrationAllowed;
    }

    public void setRegistrationAllowed(Boolean registrationAllowed) {
        this.registrationAllowed = registrationAllowed;
    }

    public RealmRepresentationDTO registrationEmailAsUsername(Boolean registrationEmailAsUsername) {
        this.registrationEmailAsUsername = registrationEmailAsUsername;
        return this;
    }


    public Boolean isRegistrationEmailAsUsername() {
        return registrationEmailAsUsername;
    }

    public void setRegistrationEmailAsUsername(Boolean registrationEmailAsUsername) {
        this.registrationEmailAsUsername = registrationEmailAsUsername;
    }

    public RealmRepresentationDTO rememberMe(Boolean rememberMe) {
        this.rememberMe = rememberMe;
        return this;
    }


    public Boolean isRememberMe() {
        return rememberMe;
    }

    public void setRememberMe(Boolean rememberMe) {
        this.rememberMe = rememberMe;
    }

    public RealmRepresentationDTO verifyEmail(Boolean verifyEmail) {
        this.verifyEmail = verifyEmail;
        return this;
    }


    public Boolean isVerifyEmail() {
        return verifyEmail;
    }

    public void setVerifyEmail(Boolean verifyEmail) {
        this.verifyEmail = verifyEmail;
    }

    public RealmRepresentationDTO loginWithEmailAllowed(Boolean loginWithEmailAllowed) {
        this.loginWithEmailAllowed = loginWithEmailAllowed;
        return this;
    }


    public Boolean isLoginWithEmailAllowed() {
        return loginWithEmailAllowed;
    }

    public void setLoginWithEmailAllowed(Boolean loginWithEmailAllowed) {
        this.loginWithEmailAllowed = loginWithEmailAllowed;
    }

    public RealmRepresentationDTO duplicateEmailsAllowed(Boolean duplicateEmailsAllowed) {
        this.duplicateEmailsAllowed = duplicateEmailsAllowed;
        return this;
    }


    public Boolean isDuplicateEmailsAllowed() {
        return duplicateEmailsAllowed;
    }

    public void setDuplicateEmailsAllowed(Boolean duplicateEmailsAllowed) {
        this.duplicateEmailsAllowed = duplicateEmailsAllowed;
    }

    public RealmRepresentationDTO resetPasswordAllowed(Boolean resetPasswordAllowed) {
        this.resetPasswordAllowed = resetPasswordAllowed;
        return this;
    }


    public Boolean isResetPasswordAllowed() {
        return resetPasswordAllowed;
    }

    public void setResetPasswordAllowed(Boolean resetPasswordAllowed) {
        this.resetPasswordAllowed = resetPasswordAllowed;
    }

    public RealmRepresentationDTO editUsernameAllowed(Boolean editUsernameAllowed) {
        this.editUsernameAllowed = editUsernameAllowed;
        return this;
    }


    public Boolean isEditUsernameAllowed() {
        return editUsernameAllowed;
    }

    public void setEditUsernameAllowed(Boolean editUsernameAllowed) {
        this.editUsernameAllowed = editUsernameAllowed;
    }

    public RealmRepresentationDTO social(Boolean social) {
        this.social = social;
        return this;
    }


    public Boolean isSocial() {
        return social;
    }

    public void setSocial(Boolean social) {
        this.social = social;
    }

    public RealmRepresentationDTO updateProfileOnInitialSocialLogin(Boolean updateProfileOnInitialSocialLogin) {
        this.updateProfileOnInitialSocialLogin = updateProfileOnInitialSocialLogin;
        return this;
    }


    public Boolean isUpdateProfileOnInitialSocialLogin() {
        return updateProfileOnInitialSocialLogin;
    }

    public void setUpdateProfileOnInitialSocialLogin(Boolean updateProfileOnInitialSocialLogin) {
        this.updateProfileOnInitialSocialLogin = updateProfileOnInitialSocialLogin;
    }

    public RealmRepresentationDTO browserSecurityHeaders(Map<String, String> browserSecurityHeaders) {
        this.browserSecurityHeaders = browserSecurityHeaders;
        return this;
    }

    public RealmRepresentationDTO putBrowserSecurityHeadersItem(String key, String browserSecurityHeadersItem) {
        if (this.browserSecurityHeaders == null) {
            this.browserSecurityHeaders = null;
        }
        this.browserSecurityHeaders.put(key, browserSecurityHeadersItem);
        return this;
    }


    public Map<String, String> getBrowserSecurityHeaders() {
        return browserSecurityHeaders;
    }

    public void setBrowserSecurityHeaders(Map<String, String> browserSecurityHeaders) {
        this.browserSecurityHeaders = browserSecurityHeaders;
    }

    public RealmRepresentationDTO socialProviders(Map<String, String> socialProviders) {
        this.socialProviders = socialProviders;
        return this;
    }

    public RealmRepresentationDTO putSocialProvidersItem(String key, String socialProvidersItem) {
        if (this.socialProviders == null) {
            this.socialProviders = null;
        }
        this.socialProviders.put(key, socialProvidersItem);
        return this;
    }


    public Map<String, String> getSocialProviders() {
        return socialProviders;
    }

    public void setSocialProviders(Map<String, String> socialProviders) {
        this.socialProviders = socialProviders;
    }

    public RealmRepresentationDTO smtpServer(Map<String, String> smtpServer) {
        this.smtpServer = smtpServer;
        return this;
    }

    public RealmRepresentationDTO putSmtpServerItem(String key, String smtpServerItem) {
        if (this.smtpServer == null) {
            this.smtpServer = null;
        }
        this.smtpServer.put(key, smtpServerItem);
        return this;
    }


    public Map<String, String> getSmtpServer() {
        return smtpServer;
    }

    public void setSmtpServer(Map<String, String> smtpServer) {
        this.smtpServer = smtpServer;
    }

    public RealmRepresentationDTO oauthClients(List<OAuthClientRepresentationDTO> oauthClients) {
        this.oauthClients = oauthClients;
        return this;
    }

    public RealmRepresentationDTO addOauthClientsItem(OAuthClientRepresentationDTO oauthClientsItem) {
        if (this.oauthClients == null) {
            this.oauthClients = new ArrayList<OAuthClientRepresentationDTO>();
        }
        this.oauthClients.add(oauthClientsItem);
        return this;
    }


    public List<OAuthClientRepresentationDTO> getOauthClients() {
        return oauthClients;
    }

    public void setOauthClients(List<OAuthClientRepresentationDTO> oauthClients) {
        this.oauthClients = oauthClients;
    }

    public RealmRepresentationDTO clientScopeMappings(Map<String, List<ScopeMappingRepresentationDTO>> clientScopeMappings) {
        this.clientScopeMappings = clientScopeMappings;
        return this;
    }

    public RealmRepresentationDTO putClientScopeMappingsItem(String key, List<ScopeMappingRepresentationDTO> clientScopeMappingsItem) {
        if (this.clientScopeMappings == null) {
            this.clientScopeMappings = null;
        }
        this.clientScopeMappings.put(key, clientScopeMappingsItem);
        return this;
    }


    public Map<String, List<ScopeMappingRepresentationDTO>> getClientScopeMappings() {
        return clientScopeMappings;
    }

    public void setClientScopeMappings(Map<String, List<ScopeMappingRepresentationDTO>> clientScopeMappings) {
        this.clientScopeMappings = clientScopeMappings;
    }

    public RealmRepresentationDTO applicationScopeMappings(Map<String, List<ScopeMappingRepresentationDTO>> applicationScopeMappings) {
        this.applicationScopeMappings = applicationScopeMappings;
        return this;
    }

    public RealmRepresentationDTO putApplicationScopeMappingsItem(String key, List<ScopeMappingRepresentationDTO> applicationScopeMappingsItem) {
        if (this.applicationScopeMappings == null) {
            this.applicationScopeMappings = null;
        }
        this.applicationScopeMappings.put(key, applicationScopeMappingsItem);
        return this;
    }


    public Map<String, List<ScopeMappingRepresentationDTO>> getApplicationScopeMappings() {
        return applicationScopeMappings;
    }

    public void setApplicationScopeMappings(Map<String, List<ScopeMappingRepresentationDTO>> applicationScopeMappings) {
        this.applicationScopeMappings = applicationScopeMappings;
    }

    public RealmRepresentationDTO roles(RolesRepresentationDTO roles) {
        this.roles = roles;
        return this;
    }


    public RolesRepresentationDTO getRoles() {
        return roles;
    }

    public void setRoles(RolesRepresentationDTO roles) {
        this.roles = roles;
    }

    public RealmRepresentationDTO loginTheme(String loginTheme) {
        this.loginTheme = loginTheme;
        return this;
    }


    public String getLoginTheme() {
        return loginTheme;
    }

    public void setLoginTheme(String loginTheme) {
        this.loginTheme = loginTheme;
    }

    public RealmRepresentationDTO accountTheme(String accountTheme) {
        this.accountTheme = accountTheme;
        return this;
    }


    public String getAccountTheme() {
        return accountTheme;
    }

    public void setAccountTheme(String accountTheme) {
        this.accountTheme = accountTheme;
    }

    public RealmRepresentationDTO adminTheme(String adminTheme) {
        this.adminTheme = adminTheme;
        return this;
    }


    public String getAdminTheme() {
        return adminTheme;
    }

    public void setAdminTheme(String adminTheme) {
        this.adminTheme = adminTheme;
    }

    public RealmRepresentationDTO emailTheme(String emailTheme) {
        this.emailTheme = emailTheme;
        return this;
    }


    public String getEmailTheme() {
        return emailTheme;
    }

    public void setEmailTheme(String emailTheme) {
        this.emailTheme = emailTheme;
    }

    public RealmRepresentationDTO notBefore(Integer notBefore) {
        this.notBefore = notBefore;
        return this;
    }


    public Integer getNotBefore() {
        return notBefore;
    }

    public void setNotBefore(Integer notBefore) {
        this.notBefore = notBefore;
    }

    public RealmRepresentationDTO bruteForceProtected(Boolean bruteForceProtected) {
        this.bruteForceProtected = bruteForceProtected;
        return this;
    }


    public Boolean isBruteForceProtected() {
        return bruteForceProtected;
    }

    public void setBruteForceProtected(Boolean bruteForceProtected) {
        this.bruteForceProtected = bruteForceProtected;
    }

    public RealmRepresentationDTO permanentLockout(Boolean permanentLockout) {
        this.permanentLockout = permanentLockout;
        return this;
    }


    public Boolean isPermanentLockout() {
        return permanentLockout;
    }

    public void setPermanentLockout(Boolean permanentLockout) {
        this.permanentLockout = permanentLockout;
    }

    public RealmRepresentationDTO maxFailureWaitSeconds(Integer maxFailureWaitSeconds) {
        this.maxFailureWaitSeconds = maxFailureWaitSeconds;
        return this;
    }


    public Integer getMaxFailureWaitSeconds() {
        return maxFailureWaitSeconds;
    }

    public void setMaxFailureWaitSeconds(Integer maxFailureWaitSeconds) {
        this.maxFailureWaitSeconds = maxFailureWaitSeconds;
    }

    public RealmRepresentationDTO minimumQuickLoginWaitSeconds(Integer minimumQuickLoginWaitSeconds) {
        this.minimumQuickLoginWaitSeconds = minimumQuickLoginWaitSeconds;
        return this;
    }


    public Integer getMinimumQuickLoginWaitSeconds() {
        return minimumQuickLoginWaitSeconds;
    }

    public void setMinimumQuickLoginWaitSeconds(Integer minimumQuickLoginWaitSeconds) {
        this.minimumQuickLoginWaitSeconds = minimumQuickLoginWaitSeconds;
    }

    public RealmRepresentationDTO waitIncrementSeconds(Integer waitIncrementSeconds) {
        this.waitIncrementSeconds = waitIncrementSeconds;
        return this;
    }


    public Integer getWaitIncrementSeconds() {
        return waitIncrementSeconds;
    }

    public void setWaitIncrementSeconds(Integer waitIncrementSeconds) {
        this.waitIncrementSeconds = waitIncrementSeconds;
    }

    public RealmRepresentationDTO quickLoginCheckMilliSeconds(Long quickLoginCheckMilliSeconds) {
        this.quickLoginCheckMilliSeconds = quickLoginCheckMilliSeconds;
        return this;
    }


    public Long getQuickLoginCheckMilliSeconds() {
        return quickLoginCheckMilliSeconds;
    }

    public void setQuickLoginCheckMilliSeconds(Long quickLoginCheckMilliSeconds) {
        this.quickLoginCheckMilliSeconds = quickLoginCheckMilliSeconds;
    }

    public RealmRepresentationDTO maxDeltaTimeSeconds(Integer maxDeltaTimeSeconds) {
        this.maxDeltaTimeSeconds = maxDeltaTimeSeconds;
        return this;
    }


    public Integer getMaxDeltaTimeSeconds() {
        return maxDeltaTimeSeconds;
    }

    public void setMaxDeltaTimeSeconds(Integer maxDeltaTimeSeconds) {
        this.maxDeltaTimeSeconds = maxDeltaTimeSeconds;
    }

    public RealmRepresentationDTO failureFactor(Integer failureFactor) {
        this.failureFactor = failureFactor;
        return this;
    }


    public Integer getFailureFactor() {
        return failureFactor;
    }

    public void setFailureFactor(Integer failureFactor) {
        this.failureFactor = failureFactor;
    }

    public RealmRepresentationDTO eventsEnabled(Boolean eventsEnabled) {
        this.eventsEnabled = eventsEnabled;
        return this;
    }


    public Boolean isEventsEnabled() {
        return eventsEnabled;
    }

    public void setEventsEnabled(Boolean eventsEnabled) {
        this.eventsEnabled = eventsEnabled;
    }

    public RealmRepresentationDTO eventsExpiration(Long eventsExpiration) {
        this.eventsExpiration = eventsExpiration;
        return this;
    }


    public Long getEventsExpiration() {
        return eventsExpiration;
    }

    public void setEventsExpiration(Long eventsExpiration) {
        this.eventsExpiration = eventsExpiration;
    }

    public RealmRepresentationDTO eventsListeners(List<String> eventsListeners) {
        this.eventsListeners = eventsListeners;
        return this;
    }

    public RealmRepresentationDTO addEventsListenersItem(String eventsListenersItem) {
        if (this.eventsListeners == null) {
            this.eventsListeners = new ArrayList<String>();
        }
        this.eventsListeners.add(eventsListenersItem);
        return this;
    }


    public List<String> getEventsListeners() {
        return eventsListeners;
    }

    public void setEventsListeners(List<String> eventsListeners) {
        this.eventsListeners = eventsListeners;
    }

    public RealmRepresentationDTO enabledEventTypes(List<String> enabledEventTypes) {
        this.enabledEventTypes = enabledEventTypes;
        return this;
    }

    public RealmRepresentationDTO addEnabledEventTypesItem(String enabledEventTypesItem) {
        if (this.enabledEventTypes == null) {
            this.enabledEventTypes = new ArrayList<String>();
        }
        this.enabledEventTypes.add(enabledEventTypesItem);
        return this;
    }


    public List<String> getEnabledEventTypes() {
        return enabledEventTypes;
    }

    public void setEnabledEventTypes(List<String> enabledEventTypes) {
        this.enabledEventTypes = enabledEventTypes;
    }

    public RealmRepresentationDTO adminEventsEnabled(Boolean adminEventsEnabled) {
        this.adminEventsEnabled = adminEventsEnabled;
        return this;
    }


    public Boolean isAdminEventsEnabled() {
        return adminEventsEnabled;
    }

    public void setAdminEventsEnabled(Boolean adminEventsEnabled) {
        this.adminEventsEnabled = adminEventsEnabled;
    }

    public RealmRepresentationDTO adminEventsDetailsEnabled(Boolean adminEventsDetailsEnabled) {
        this.adminEventsDetailsEnabled = adminEventsDetailsEnabled;
        return this;
    }


    public Boolean isAdminEventsDetailsEnabled() {
        return adminEventsDetailsEnabled;
    }

    public void setAdminEventsDetailsEnabled(Boolean adminEventsDetailsEnabled) {
        this.adminEventsDetailsEnabled = adminEventsDetailsEnabled;
    }

    public RealmRepresentationDTO userFederationProviders(List<UserFederationProviderRepresentationDTO> userFederationProviders) {
        this.userFederationProviders = userFederationProviders;
        return this;
    }

    public RealmRepresentationDTO addUserFederationProvidersItem(UserFederationProviderRepresentationDTO userFederationProvidersItem) {
        if (this.userFederationProviders == null) {
            this.userFederationProviders = new ArrayList<UserFederationProviderRepresentationDTO>();
        }
        this.userFederationProviders.add(userFederationProvidersItem);
        return this;
    }


    public List<UserFederationProviderRepresentationDTO> getUserFederationProviders() {
        return userFederationProviders;
    }

    public void setUserFederationProviders(List<UserFederationProviderRepresentationDTO> userFederationProviders) {
        this.userFederationProviders = userFederationProviders;
    }

    public RealmRepresentationDTO userFederationMappers(List<UserFederationMapperRepresentationDTO> userFederationMappers) {
        this.userFederationMappers = userFederationMappers;
        return this;
    }

    public RealmRepresentationDTO addUserFederationMappersItem(UserFederationMapperRepresentationDTO userFederationMappersItem) {
        if (this.userFederationMappers == null) {
            this.userFederationMappers = new ArrayList<UserFederationMapperRepresentationDTO>();
        }
        this.userFederationMappers.add(userFederationMappersItem);
        return this;
    }


    public List<UserFederationMapperRepresentationDTO> getUserFederationMappers() {
        return userFederationMappers;
    }

    public void setUserFederationMappers(List<UserFederationMapperRepresentationDTO> userFederationMappers) {
        this.userFederationMappers = userFederationMappers;
    }

    public RealmRepresentationDTO identityProviders(List<IdentityProviderRepresentationDTO> identityProviders) {
        this.identityProviders = identityProviders;
        return this;
    }

    public RealmRepresentationDTO addIdentityProvidersItem(IdentityProviderRepresentationDTO identityProvidersItem) {
        if (this.identityProviders == null) {
            this.identityProviders = new ArrayList<IdentityProviderRepresentationDTO>();
        }
        this.identityProviders.add(identityProvidersItem);
        return this;
    }


    public List<IdentityProviderRepresentationDTO> getIdentityProviders() {
        return identityProviders;
    }

    public void setIdentityProviders(List<IdentityProviderRepresentationDTO> identityProviders) {
        this.identityProviders = identityProviders;
    }

    public RealmRepresentationDTO protocolMappers(List<ProtocolMapperRepresentationDTO> protocolMappers) {
        this.protocolMappers = protocolMappers;
        return this;
    }

    public RealmRepresentationDTO addProtocolMappersItem(ProtocolMapperRepresentationDTO protocolMappersItem) {
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

    public RealmRepresentationDTO internationalizationEnabled(Boolean internationalizationEnabled) {
        this.internationalizationEnabled = internationalizationEnabled;
        return this;
    }


    public Boolean isInternationalizationEnabled() {
        return internationalizationEnabled;
    }

    public void setInternationalizationEnabled(Boolean internationalizationEnabled) {
        this.internationalizationEnabled = internationalizationEnabled;
    }

    public RealmRepresentationDTO supportedLocales(List<String> supportedLocales) {
        this.supportedLocales = supportedLocales;
        return this;
    }

    public RealmRepresentationDTO addSupportedLocalesItem(String supportedLocalesItem) {
        if (this.supportedLocales == null) {
            this.supportedLocales = new ArrayList<String>();
        }
        this.supportedLocales.add(supportedLocalesItem);
        return this;
    }


    public List<String> getSupportedLocales() {
        return supportedLocales;
    }

    public void setSupportedLocales(List<String> supportedLocales) {
        this.supportedLocales = supportedLocales;
    }

    public RealmRepresentationDTO defaultLocale(String defaultLocale) {
        this.defaultLocale = defaultLocale;
        return this;
    }


    public String getDefaultLocale() {
        return defaultLocale;
    }

    public void setDefaultLocale(String defaultLocale) {
        this.defaultLocale = defaultLocale;
    }

    public RealmRepresentationDTO identityProviderMappers(List<IdentityProviderMapperRepresentationDTO> identityProviderMappers) {
        this.identityProviderMappers = identityProviderMappers;
        return this;
    }

    public RealmRepresentationDTO addIdentityProviderMappersItem(IdentityProviderMapperRepresentationDTO identityProviderMappersItem) {
        if (this.identityProviderMappers == null) {
            this.identityProviderMappers = new ArrayList<IdentityProviderMapperRepresentationDTO>();
        }
        this.identityProviderMappers.add(identityProviderMappersItem);
        return this;
    }


    public List<IdentityProviderMapperRepresentationDTO> getIdentityProviderMappers() {
        return identityProviderMappers;
    }

    public void setIdentityProviderMappers(List<IdentityProviderMapperRepresentationDTO> identityProviderMappers) {
        this.identityProviderMappers = identityProviderMappers;
    }

    public RealmRepresentationDTO authenticationFlows(List<AuthenticationFlowRepresentationDTO> authenticationFlows) {
        this.authenticationFlows = authenticationFlows;
        return this;
    }

    public RealmRepresentationDTO addAuthenticationFlowsItem(AuthenticationFlowRepresentationDTO authenticationFlowsItem) {
        if (this.authenticationFlows == null) {
            this.authenticationFlows = new ArrayList<AuthenticationFlowRepresentationDTO>();
        }
        this.authenticationFlows.add(authenticationFlowsItem);
        return this;
    }


    public List<AuthenticationFlowRepresentationDTO> getAuthenticationFlows() {
        return authenticationFlows;
    }

    public void setAuthenticationFlows(List<AuthenticationFlowRepresentationDTO> authenticationFlows) {
        this.authenticationFlows = authenticationFlows;
    }

    public RealmRepresentationDTO authenticatorConfig(List<AuthenticatorConfigRepresentationDTO> authenticatorConfig) {
        this.authenticatorConfig = authenticatorConfig;
        return this;
    }

    public RealmRepresentationDTO addAuthenticatorConfigItem(AuthenticatorConfigRepresentationDTO authenticatorConfigItem) {
        if (this.authenticatorConfig == null) {
            this.authenticatorConfig = new ArrayList<AuthenticatorConfigRepresentationDTO>();
        }
        this.authenticatorConfig.add(authenticatorConfigItem);
        return this;
    }


    public List<AuthenticatorConfigRepresentationDTO> getAuthenticatorConfig() {
        return authenticatorConfig;
    }

    public void setAuthenticatorConfig(List<AuthenticatorConfigRepresentationDTO> authenticatorConfig) {
        this.authenticatorConfig = authenticatorConfig;
    }

    public RealmRepresentationDTO requiredActions(List<RequiredActionProviderRepresentationDTO> requiredActions) {
        this.requiredActions = requiredActions;
        return this;
    }

    public RealmRepresentationDTO addRequiredActionsItem(RequiredActionProviderRepresentationDTO requiredActionsItem) {
        if (this.requiredActions == null) {
            this.requiredActions = new ArrayList<RequiredActionProviderRepresentationDTO>();
        }
        this.requiredActions.add(requiredActionsItem);
        return this;
    }


    public List<RequiredActionProviderRepresentationDTO> getRequiredActions() {
        return requiredActions;
    }

    public void setRequiredActions(List<RequiredActionProviderRepresentationDTO> requiredActions) {
        this.requiredActions = requiredActions;
    }

    public RealmRepresentationDTO otpPolicyType(String otpPolicyType) {
        this.otpPolicyType = otpPolicyType;
        return this;
    }


    public String getOtpPolicyType() {
        return otpPolicyType;
    }

    public void setOtpPolicyType(String otpPolicyType) {
        this.otpPolicyType = otpPolicyType;
    }

    public RealmRepresentationDTO otpPolicyAlgorithm(String otpPolicyAlgorithm) {
        this.otpPolicyAlgorithm = otpPolicyAlgorithm;
        return this;
    }


    public String getOtpPolicyAlgorithm() {
        return otpPolicyAlgorithm;
    }

    public void setOtpPolicyAlgorithm(String otpPolicyAlgorithm) {
        this.otpPolicyAlgorithm = otpPolicyAlgorithm;
    }

    public RealmRepresentationDTO otpPolicyInitialCounter(Integer otpPolicyInitialCounter) {
        this.otpPolicyInitialCounter = otpPolicyInitialCounter;
        return this;
    }


    public Integer getOtpPolicyInitialCounter() {
        return otpPolicyInitialCounter;
    }

    public void setOtpPolicyInitialCounter(Integer otpPolicyInitialCounter) {
        this.otpPolicyInitialCounter = otpPolicyInitialCounter;
    }

    public RealmRepresentationDTO otpPolicyDigits(Integer otpPolicyDigits) {
        this.otpPolicyDigits = otpPolicyDigits;
        return this;
    }


    public Integer getOtpPolicyDigits() {
        return otpPolicyDigits;
    }

    public void setOtpPolicyDigits(Integer otpPolicyDigits) {
        this.otpPolicyDigits = otpPolicyDigits;
    }

    public RealmRepresentationDTO otpPolicyLookAheadWindow(Integer otpPolicyLookAheadWindow) {
        this.otpPolicyLookAheadWindow = otpPolicyLookAheadWindow;
        return this;
    }


    public Integer getOtpPolicyLookAheadWindow() {
        return otpPolicyLookAheadWindow;
    }

    public void setOtpPolicyLookAheadWindow(Integer otpPolicyLookAheadWindow) {
        this.otpPolicyLookAheadWindow = otpPolicyLookAheadWindow;
    }

    public RealmRepresentationDTO otpPolicyPeriod(Integer otpPolicyPeriod) {
        this.otpPolicyPeriod = otpPolicyPeriod;
        return this;
    }


    public Integer getOtpPolicyPeriod() {
        return otpPolicyPeriod;
    }

    public void setOtpPolicyPeriod(Integer otpPolicyPeriod) {
        this.otpPolicyPeriod = otpPolicyPeriod;
    }

    public RealmRepresentationDTO otpSupportedApplications(List<String> otpSupportedApplications) {
        this.otpSupportedApplications = otpSupportedApplications;
        return this;
    }

    public RealmRepresentationDTO addOtpSupportedApplicationsItem(String otpSupportedApplicationsItem) {
        if (this.otpSupportedApplications == null) {
            this.otpSupportedApplications = new ArrayList<String>();
        }
        this.otpSupportedApplications.add(otpSupportedApplicationsItem);
        return this;
    }


    public List<String> getOtpSupportedApplications() {
        return otpSupportedApplications;
    }

    public void setOtpSupportedApplications(List<String> otpSupportedApplications) {
        this.otpSupportedApplications = otpSupportedApplications;
    }

    public RealmRepresentationDTO webAuthnPolicyRpEntityName(String webAuthnPolicyRpEntityName) {
        this.webAuthnPolicyRpEntityName = webAuthnPolicyRpEntityName;
        return this;
    }


    public String getWebAuthnPolicyRpEntityName() {
        return webAuthnPolicyRpEntityName;
    }

    public void setWebAuthnPolicyRpEntityName(String webAuthnPolicyRpEntityName) {
        this.webAuthnPolicyRpEntityName = webAuthnPolicyRpEntityName;
    }

    public RealmRepresentationDTO webAuthnPolicySignatureAlgorithms(List<String> webAuthnPolicySignatureAlgorithms) {
        this.webAuthnPolicySignatureAlgorithms = webAuthnPolicySignatureAlgorithms;
        return this;
    }

    public RealmRepresentationDTO addWebAuthnPolicySignatureAlgorithmsItem(String webAuthnPolicySignatureAlgorithmsItem) {
        if (this.webAuthnPolicySignatureAlgorithms == null) {
            this.webAuthnPolicySignatureAlgorithms = new ArrayList<String>();
        }
        this.webAuthnPolicySignatureAlgorithms.add(webAuthnPolicySignatureAlgorithmsItem);
        return this;
    }


    public List<String> getWebAuthnPolicySignatureAlgorithms() {
        return webAuthnPolicySignatureAlgorithms;
    }

    public void setWebAuthnPolicySignatureAlgorithms(List<String> webAuthnPolicySignatureAlgorithms) {
        this.webAuthnPolicySignatureAlgorithms = webAuthnPolicySignatureAlgorithms;
    }

    public RealmRepresentationDTO webAuthnPolicyRpId(String webAuthnPolicyRpId) {
        this.webAuthnPolicyRpId = webAuthnPolicyRpId;
        return this;
    }


    public String getWebAuthnPolicyRpId() {
        return webAuthnPolicyRpId;
    }

    public void setWebAuthnPolicyRpId(String webAuthnPolicyRpId) {
        this.webAuthnPolicyRpId = webAuthnPolicyRpId;
    }

    public RealmRepresentationDTO webAuthnPolicyAttestationConveyancePreference(String webAuthnPolicyAttestationConveyancePreference) {
        this.webAuthnPolicyAttestationConveyancePreference = webAuthnPolicyAttestationConveyancePreference;
        return this;
    }


    public String getWebAuthnPolicyAttestationConveyancePreference() {
        return webAuthnPolicyAttestationConveyancePreference;
    }

    public void setWebAuthnPolicyAttestationConveyancePreference(String webAuthnPolicyAttestationConveyancePreference) {
        this.webAuthnPolicyAttestationConveyancePreference = webAuthnPolicyAttestationConveyancePreference;
    }

    public RealmRepresentationDTO webAuthnPolicyAuthenticatorAttachment(String webAuthnPolicyAuthenticatorAttachment) {
        this.webAuthnPolicyAuthenticatorAttachment = webAuthnPolicyAuthenticatorAttachment;
        return this;
    }


    public String getWebAuthnPolicyAuthenticatorAttachment() {
        return webAuthnPolicyAuthenticatorAttachment;
    }

    public void setWebAuthnPolicyAuthenticatorAttachment(String webAuthnPolicyAuthenticatorAttachment) {
        this.webAuthnPolicyAuthenticatorAttachment = webAuthnPolicyAuthenticatorAttachment;
    }

    public RealmRepresentationDTO webAuthnPolicyRequireResidentKey(String webAuthnPolicyRequireResidentKey) {
        this.webAuthnPolicyRequireResidentKey = webAuthnPolicyRequireResidentKey;
        return this;
    }


    public String getWebAuthnPolicyRequireResidentKey() {
        return webAuthnPolicyRequireResidentKey;
    }

    public void setWebAuthnPolicyRequireResidentKey(String webAuthnPolicyRequireResidentKey) {
        this.webAuthnPolicyRequireResidentKey = webAuthnPolicyRequireResidentKey;
    }

    public RealmRepresentationDTO webAuthnPolicyUserVerificationRequirement(String webAuthnPolicyUserVerificationRequirement) {
        this.webAuthnPolicyUserVerificationRequirement = webAuthnPolicyUserVerificationRequirement;
        return this;
    }


    public String getWebAuthnPolicyUserVerificationRequirement() {
        return webAuthnPolicyUserVerificationRequirement;
    }

    public void setWebAuthnPolicyUserVerificationRequirement(String webAuthnPolicyUserVerificationRequirement) {
        this.webAuthnPolicyUserVerificationRequirement = webAuthnPolicyUserVerificationRequirement;
    }

    public RealmRepresentationDTO webAuthnPolicyCreateTimeout(Integer webAuthnPolicyCreateTimeout) {
        this.webAuthnPolicyCreateTimeout = webAuthnPolicyCreateTimeout;
        return this;
    }


    public Integer getWebAuthnPolicyCreateTimeout() {
        return webAuthnPolicyCreateTimeout;
    }

    public void setWebAuthnPolicyCreateTimeout(Integer webAuthnPolicyCreateTimeout) {
        this.webAuthnPolicyCreateTimeout = webAuthnPolicyCreateTimeout;
    }

    public RealmRepresentationDTO webAuthnPolicyAvoidSameAuthenticatorRegister(Boolean webAuthnPolicyAvoidSameAuthenticatorRegister) {
        this.webAuthnPolicyAvoidSameAuthenticatorRegister = webAuthnPolicyAvoidSameAuthenticatorRegister;
        return this;
    }


    public Boolean isWebAuthnPolicyAvoidSameAuthenticatorRegister() {
        return webAuthnPolicyAvoidSameAuthenticatorRegister;
    }

    public void setWebAuthnPolicyAvoidSameAuthenticatorRegister(Boolean webAuthnPolicyAvoidSameAuthenticatorRegister) {
        this.webAuthnPolicyAvoidSameAuthenticatorRegister = webAuthnPolicyAvoidSameAuthenticatorRegister;
    }

    public RealmRepresentationDTO webAuthnPolicyAcceptableAaguids(List<String> webAuthnPolicyAcceptableAaguids) {
        this.webAuthnPolicyAcceptableAaguids = webAuthnPolicyAcceptableAaguids;
        return this;
    }

    public RealmRepresentationDTO addWebAuthnPolicyAcceptableAaguidsItem(String webAuthnPolicyAcceptableAaguidsItem) {
        if (this.webAuthnPolicyAcceptableAaguids == null) {
            this.webAuthnPolicyAcceptableAaguids = new ArrayList<String>();
        }
        this.webAuthnPolicyAcceptableAaguids.add(webAuthnPolicyAcceptableAaguidsItem);
        return this;
    }


    public List<String> getWebAuthnPolicyAcceptableAaguids() {
        return webAuthnPolicyAcceptableAaguids;
    }

    public void setWebAuthnPolicyAcceptableAaguids(List<String> webAuthnPolicyAcceptableAaguids) {
        this.webAuthnPolicyAcceptableAaguids = webAuthnPolicyAcceptableAaguids;
    }

    public RealmRepresentationDTO webAuthnPolicyPasswordlessRpEntityName(String webAuthnPolicyPasswordlessRpEntityName) {
        this.webAuthnPolicyPasswordlessRpEntityName = webAuthnPolicyPasswordlessRpEntityName;
        return this;
    }


    public String getWebAuthnPolicyPasswordlessRpEntityName() {
        return webAuthnPolicyPasswordlessRpEntityName;
    }

    public void setWebAuthnPolicyPasswordlessRpEntityName(String webAuthnPolicyPasswordlessRpEntityName) {
        this.webAuthnPolicyPasswordlessRpEntityName = webAuthnPolicyPasswordlessRpEntityName;
    }

    public RealmRepresentationDTO webAuthnPolicyPasswordlessSignatureAlgorithms(List<String> webAuthnPolicyPasswordlessSignatureAlgorithms) {
        this.webAuthnPolicyPasswordlessSignatureAlgorithms = webAuthnPolicyPasswordlessSignatureAlgorithms;
        return this;
    }

    public RealmRepresentationDTO addWebAuthnPolicyPasswordlessSignatureAlgorithmsItem(String webAuthnPolicyPasswordlessSignatureAlgorithmsItem) {
        if (this.webAuthnPolicyPasswordlessSignatureAlgorithms == null) {
            this.webAuthnPolicyPasswordlessSignatureAlgorithms = new ArrayList<String>();
        }
        this.webAuthnPolicyPasswordlessSignatureAlgorithms.add(webAuthnPolicyPasswordlessSignatureAlgorithmsItem);
        return this;
    }


    public List<String> getWebAuthnPolicyPasswordlessSignatureAlgorithms() {
        return webAuthnPolicyPasswordlessSignatureAlgorithms;
    }

    public void setWebAuthnPolicyPasswordlessSignatureAlgorithms(List<String> webAuthnPolicyPasswordlessSignatureAlgorithms) {
        this.webAuthnPolicyPasswordlessSignatureAlgorithms = webAuthnPolicyPasswordlessSignatureAlgorithms;
    }

    public RealmRepresentationDTO webAuthnPolicyPasswordlessRpId(String webAuthnPolicyPasswordlessRpId) {
        this.webAuthnPolicyPasswordlessRpId = webAuthnPolicyPasswordlessRpId;
        return this;
    }


    public String getWebAuthnPolicyPasswordlessRpId() {
        return webAuthnPolicyPasswordlessRpId;
    }

    public void setWebAuthnPolicyPasswordlessRpId(String webAuthnPolicyPasswordlessRpId) {
        this.webAuthnPolicyPasswordlessRpId = webAuthnPolicyPasswordlessRpId;
    }

    public RealmRepresentationDTO webAuthnPolicyPasswordlessAttestationConveyancePreference(String webAuthnPolicyPasswordlessAttestationConveyancePreference) {
        this.webAuthnPolicyPasswordlessAttestationConveyancePreference = webAuthnPolicyPasswordlessAttestationConveyancePreference;
        return this;
    }


    public String getWebAuthnPolicyPasswordlessAttestationConveyancePreference() {
        return webAuthnPolicyPasswordlessAttestationConveyancePreference;
    }

    public void setWebAuthnPolicyPasswordlessAttestationConveyancePreference(String webAuthnPolicyPasswordlessAttestationConveyancePreference) {
        this.webAuthnPolicyPasswordlessAttestationConveyancePreference = webAuthnPolicyPasswordlessAttestationConveyancePreference;
    }

    public RealmRepresentationDTO webAuthnPolicyPasswordlessAuthenticatorAttachment(String webAuthnPolicyPasswordlessAuthenticatorAttachment) {
        this.webAuthnPolicyPasswordlessAuthenticatorAttachment = webAuthnPolicyPasswordlessAuthenticatorAttachment;
        return this;
    }


    public String getWebAuthnPolicyPasswordlessAuthenticatorAttachment() {
        return webAuthnPolicyPasswordlessAuthenticatorAttachment;
    }

    public void setWebAuthnPolicyPasswordlessAuthenticatorAttachment(String webAuthnPolicyPasswordlessAuthenticatorAttachment) {
        this.webAuthnPolicyPasswordlessAuthenticatorAttachment = webAuthnPolicyPasswordlessAuthenticatorAttachment;
    }

    public RealmRepresentationDTO webAuthnPolicyPasswordlessRequireResidentKey(String webAuthnPolicyPasswordlessRequireResidentKey) {
        this.webAuthnPolicyPasswordlessRequireResidentKey = webAuthnPolicyPasswordlessRequireResidentKey;
        return this;
    }


    public String getWebAuthnPolicyPasswordlessRequireResidentKey() {
        return webAuthnPolicyPasswordlessRequireResidentKey;
    }

    public void setWebAuthnPolicyPasswordlessRequireResidentKey(String webAuthnPolicyPasswordlessRequireResidentKey) {
        this.webAuthnPolicyPasswordlessRequireResidentKey = webAuthnPolicyPasswordlessRequireResidentKey;
    }

    public RealmRepresentationDTO webAuthnPolicyPasswordlessUserVerificationRequirement(String webAuthnPolicyPasswordlessUserVerificationRequirement) {
        this.webAuthnPolicyPasswordlessUserVerificationRequirement = webAuthnPolicyPasswordlessUserVerificationRequirement;
        return this;
    }


    public String getWebAuthnPolicyPasswordlessUserVerificationRequirement() {
        return webAuthnPolicyPasswordlessUserVerificationRequirement;
    }

    public void setWebAuthnPolicyPasswordlessUserVerificationRequirement(String webAuthnPolicyPasswordlessUserVerificationRequirement) {
        this.webAuthnPolicyPasswordlessUserVerificationRequirement = webAuthnPolicyPasswordlessUserVerificationRequirement;
    }

    public RealmRepresentationDTO webAuthnPolicyPasswordlessCreateTimeout(Integer webAuthnPolicyPasswordlessCreateTimeout) {
        this.webAuthnPolicyPasswordlessCreateTimeout = webAuthnPolicyPasswordlessCreateTimeout;
        return this;
    }


    public Integer getWebAuthnPolicyPasswordlessCreateTimeout() {
        return webAuthnPolicyPasswordlessCreateTimeout;
    }

    public void setWebAuthnPolicyPasswordlessCreateTimeout(Integer webAuthnPolicyPasswordlessCreateTimeout) {
        this.webAuthnPolicyPasswordlessCreateTimeout = webAuthnPolicyPasswordlessCreateTimeout;
    }

    public RealmRepresentationDTO webAuthnPolicyPasswordlessAvoidSameAuthenticatorRegister(Boolean webAuthnPolicyPasswordlessAvoidSameAuthenticatorRegister) {
        this.webAuthnPolicyPasswordlessAvoidSameAuthenticatorRegister = webAuthnPolicyPasswordlessAvoidSameAuthenticatorRegister;
        return this;
    }


    public Boolean isWebAuthnPolicyPasswordlessAvoidSameAuthenticatorRegister() {
        return webAuthnPolicyPasswordlessAvoidSameAuthenticatorRegister;
    }

    public void setWebAuthnPolicyPasswordlessAvoidSameAuthenticatorRegister(Boolean webAuthnPolicyPasswordlessAvoidSameAuthenticatorRegister) {
        this.webAuthnPolicyPasswordlessAvoidSameAuthenticatorRegister = webAuthnPolicyPasswordlessAvoidSameAuthenticatorRegister;
    }

    public RealmRepresentationDTO webAuthnPolicyPasswordlessAcceptableAaguids(List<String> webAuthnPolicyPasswordlessAcceptableAaguids) {
        this.webAuthnPolicyPasswordlessAcceptableAaguids = webAuthnPolicyPasswordlessAcceptableAaguids;
        return this;
    }

    public RealmRepresentationDTO addWebAuthnPolicyPasswordlessAcceptableAaguidsItem(String webAuthnPolicyPasswordlessAcceptableAaguidsItem) {
        if (this.webAuthnPolicyPasswordlessAcceptableAaguids == null) {
            this.webAuthnPolicyPasswordlessAcceptableAaguids = new ArrayList<String>();
        }
        this.webAuthnPolicyPasswordlessAcceptableAaguids.add(webAuthnPolicyPasswordlessAcceptableAaguidsItem);
        return this;
    }


    public List<String> getWebAuthnPolicyPasswordlessAcceptableAaguids() {
        return webAuthnPolicyPasswordlessAcceptableAaguids;
    }

    public void setWebAuthnPolicyPasswordlessAcceptableAaguids(List<String> webAuthnPolicyPasswordlessAcceptableAaguids) {
        this.webAuthnPolicyPasswordlessAcceptableAaguids = webAuthnPolicyPasswordlessAcceptableAaguids;
    }

    public RealmRepresentationDTO browserFlow(String browserFlow) {
        this.browserFlow = browserFlow;
        return this;
    }


    public String getBrowserFlow() {
        return browserFlow;
    }

    public void setBrowserFlow(String browserFlow) {
        this.browserFlow = browserFlow;
    }

    public RealmRepresentationDTO registrationFlow(String registrationFlow) {
        this.registrationFlow = registrationFlow;
        return this;
    }


    public String getRegistrationFlow() {
        return registrationFlow;
    }

    public void setRegistrationFlow(String registrationFlow) {
        this.registrationFlow = registrationFlow;
    }

    public RealmRepresentationDTO directGrantFlow(String directGrantFlow) {
        this.directGrantFlow = directGrantFlow;
        return this;
    }


    public String getDirectGrantFlow() {
        return directGrantFlow;
    }

    public void setDirectGrantFlow(String directGrantFlow) {
        this.directGrantFlow = directGrantFlow;
    }

    public RealmRepresentationDTO resetCredentialsFlow(String resetCredentialsFlow) {
        this.resetCredentialsFlow = resetCredentialsFlow;
        return this;
    }


    public String getResetCredentialsFlow() {
        return resetCredentialsFlow;
    }

    public void setResetCredentialsFlow(String resetCredentialsFlow) {
        this.resetCredentialsFlow = resetCredentialsFlow;
    }

    public RealmRepresentationDTO clientAuthenticationFlow(String clientAuthenticationFlow) {
        this.clientAuthenticationFlow = clientAuthenticationFlow;
        return this;
    }


    public String getClientAuthenticationFlow() {
        return clientAuthenticationFlow;
    }

    public void setClientAuthenticationFlow(String clientAuthenticationFlow) {
        this.clientAuthenticationFlow = clientAuthenticationFlow;
    }

    public RealmRepresentationDTO dockerAuthenticationFlow(String dockerAuthenticationFlow) {
        this.dockerAuthenticationFlow = dockerAuthenticationFlow;
        return this;
    }


    public String getDockerAuthenticationFlow() {
        return dockerAuthenticationFlow;
    }

    public void setDockerAuthenticationFlow(String dockerAuthenticationFlow) {
        this.dockerAuthenticationFlow = dockerAuthenticationFlow;
    }

    public RealmRepresentationDTO keycloakVersion(String keycloakVersion) {
        this.keycloakVersion = keycloakVersion;
        return this;
    }


    public String getKeycloakVersion() {
        return keycloakVersion;
    }

    public void setKeycloakVersion(String keycloakVersion) {
        this.keycloakVersion = keycloakVersion;
    }

    public RealmRepresentationDTO groups(List<GroupRepresentationDTO> groups) {
        this.groups = groups;
        return this;
    }

    public RealmRepresentationDTO addGroupsItem(GroupRepresentationDTO groupsItem) {
        if (this.groups == null) {
            this.groups = new ArrayList<GroupRepresentationDTO>();
        }
        this.groups.add(groupsItem);
        return this;
    }


    public List<GroupRepresentationDTO> getGroups() {
        return groups;
    }

    public void setGroups(List<GroupRepresentationDTO> groups) {
        this.groups = groups;
    }

    public RealmRepresentationDTO clientTemplates(List<ClientTemplateRepresentationDTO> clientTemplates) {
        this.clientTemplates = clientTemplates;
        return this;
    }

    public RealmRepresentationDTO addClientTemplatesItem(ClientTemplateRepresentationDTO clientTemplatesItem) {
        if (this.clientTemplates == null) {
            this.clientTemplates = new ArrayList<ClientTemplateRepresentationDTO>();
        }
        this.clientTemplates.add(clientTemplatesItem);
        return this;
    }


    public List<ClientTemplateRepresentationDTO> getClientTemplates() {
        return clientTemplates;
    }

    public void setClientTemplates(List<ClientTemplateRepresentationDTO> clientTemplates) {
        this.clientTemplates = clientTemplates;
    }

    public RealmRepresentationDTO clientScopes(List<ClientScopeRepresentationDTO> clientScopes) {
        this.clientScopes = clientScopes;
        return this;
    }

    public RealmRepresentationDTO addClientScopesItem(ClientScopeRepresentationDTO clientScopesItem) {
        if (this.clientScopes == null) {
            this.clientScopes = new ArrayList<ClientScopeRepresentationDTO>();
        }
        this.clientScopes.add(clientScopesItem);
        return this;
    }


    public List<ClientScopeRepresentationDTO> getClientScopes() {
        return clientScopes;
    }

    public void setClientScopes(List<ClientScopeRepresentationDTO> clientScopes) {
        this.clientScopes = clientScopes;
    }

    public RealmRepresentationDTO defaultDefaultClientScopes(List<String> defaultDefaultClientScopes) {
        this.defaultDefaultClientScopes = defaultDefaultClientScopes;
        return this;
    }

    public RealmRepresentationDTO addDefaultDefaultClientScopesItem(String defaultDefaultClientScopesItem) {
        if (this.defaultDefaultClientScopes == null) {
            this.defaultDefaultClientScopes = new ArrayList<String>();
        }
        this.defaultDefaultClientScopes.add(defaultDefaultClientScopesItem);
        return this;
    }


    public List<String> getDefaultDefaultClientScopes() {
        return defaultDefaultClientScopes;
    }

    public void setDefaultDefaultClientScopes(List<String> defaultDefaultClientScopes) {
        this.defaultDefaultClientScopes = defaultDefaultClientScopes;
    }

    public RealmRepresentationDTO defaultOptionalClientScopes(List<String> defaultOptionalClientScopes) {
        this.defaultOptionalClientScopes = defaultOptionalClientScopes;
        return this;
    }

    public RealmRepresentationDTO addDefaultOptionalClientScopesItem(String defaultOptionalClientScopesItem) {
        if (this.defaultOptionalClientScopes == null) {
            this.defaultOptionalClientScopes = new ArrayList<String>();
        }
        this.defaultOptionalClientScopes.add(defaultOptionalClientScopesItem);
        return this;
    }


    public List<String> getDefaultOptionalClientScopes() {
        return defaultOptionalClientScopes;
    }

    public void setDefaultOptionalClientScopes(List<String> defaultOptionalClientScopes) {
        this.defaultOptionalClientScopes = defaultOptionalClientScopes;
    }

    public RealmRepresentationDTO components(Map<String, ComponentExportRepresentationDTO> components) {
        this.components = components;
        return this;
    }

    public RealmRepresentationDTO putComponentsItem(String key, ComponentExportRepresentationDTO componentsItem) {
        if (this.components == null) {
            this.components = null;
        }
        this.components.put(key, componentsItem);
        return this;
    }


    public Map<String, ComponentExportRepresentationDTO> getComponents() {
        return components;
    }

    public void setComponents(Map<String, ComponentExportRepresentationDTO> components) {
        this.components = components;
    }

    public RealmRepresentationDTO attributes(Map<String, String> attributes) {
        this.attributes = attributes;
        return this;
    }

    public RealmRepresentationDTO putAttributesItem(String key, String attributesItem) {
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

    public RealmRepresentationDTO federatedUsers(List<UserRepresentationDTO> federatedUsers) {
        this.federatedUsers = federatedUsers;
        return this;
    }

    public RealmRepresentationDTO addFederatedUsersItem(UserRepresentationDTO federatedUsersItem) {
        if (this.federatedUsers == null) {
            this.federatedUsers = new ArrayList<UserRepresentationDTO>();
        }
        this.federatedUsers.add(federatedUsersItem);
        return this;
    }


    public List<UserRepresentationDTO> getFederatedUsers() {
        return federatedUsers;
    }

    public void setFederatedUsers(List<UserRepresentationDTO> federatedUsers) {
        this.federatedUsers = federatedUsers;
    }

    public RealmRepresentationDTO userManagedAccessAllowed(Boolean userManagedAccessAllowed) {
        this.userManagedAccessAllowed = userManagedAccessAllowed;
        return this;
    }


    public Boolean isUserManagedAccessAllowed() {
        return userManagedAccessAllowed;
    }

    public void setUserManagedAccessAllowed(Boolean userManagedAccessAllowed) {
        this.userManagedAccessAllowed = userManagedAccessAllowed;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        RealmRepresentationDTO realmRepresentation = (RealmRepresentationDTO) o;
        return Objects.equals(this.id, realmRepresentation.id) &&
                Objects.equals(this.realm, realmRepresentation.realm) &&
                Objects.equals(this.displayName, realmRepresentation.displayName) &&
                Objects.equals(this.displayNameHtml, realmRepresentation.displayNameHtml) &&
                Objects.equals(this.users, realmRepresentation.users) &&
                Objects.equals(this.applications, realmRepresentation.applications) &&
                Objects.equals(this.clients, realmRepresentation.clients) &&
                Objects.equals(this.enabled, realmRepresentation.enabled) &&
                Objects.equals(this.sslRequired, realmRepresentation.sslRequired) &&
                Objects.equals(this.defaultSignatureAlgorithm, realmRepresentation.defaultSignatureAlgorithm) &&
                Objects.equals(this.revokeRefreshToken, realmRepresentation.revokeRefreshToken) &&
                Objects.equals(this.refreshTokenMaxReuse, realmRepresentation.refreshTokenMaxReuse) &&
                Objects.equals(this.accessTokenLifespan, realmRepresentation.accessTokenLifespan) &&
                Objects.equals(this.accessTokenLifespanForImplicitFlow, realmRepresentation.accessTokenLifespanForImplicitFlow) &&
                Objects.equals(this.ssoSessionIdleTimeout, realmRepresentation.ssoSessionIdleTimeout) &&
                Objects.equals(this.ssoSessionMaxLifespan, realmRepresentation.ssoSessionMaxLifespan) &&
                Objects.equals(this.ssoSessionMaxLifespanRememberMe, realmRepresentation.ssoSessionMaxLifespanRememberMe) &&
                Objects.equals(this.ssoSessionIdleTimeoutRememberMe, realmRepresentation.ssoSessionIdleTimeoutRememberMe) &&
                Objects.equals(this.offlineSessionIdleTimeout, realmRepresentation.offlineSessionIdleTimeout) &&
                Objects.equals(this.offlineSessionMaxLifespanEnabled, realmRepresentation.offlineSessionMaxLifespanEnabled) &&
                Objects.equals(this.offlineSessionMaxLifespan, realmRepresentation.offlineSessionMaxLifespan) &&
                Objects.equals(this.clientSessionIdleTimeout, realmRepresentation.clientSessionIdleTimeout) &&
                Objects.equals(this.clientSessionMaxLifespan, realmRepresentation.clientSessionMaxLifespan) &&
                Objects.equals(this.clientOfflineSessionIdleTimeout, realmRepresentation.clientOfflineSessionIdleTimeout) &&
                Objects.equals(this.clientOfflineSessionMaxLifespan, realmRepresentation.clientOfflineSessionMaxLifespan) &&
                Objects.equals(this.scopeMappings, realmRepresentation.scopeMappings) &&
                Objects.equals(this.requiredCredentials, realmRepresentation.requiredCredentials) &&
                Objects.equals(this.passwordPolicy, realmRepresentation.passwordPolicy) &&
                Objects.equals(this.accessCodeLifespan, realmRepresentation.accessCodeLifespan) &&
                Objects.equals(this.accessCodeLifespanUserAction, realmRepresentation.accessCodeLifespanUserAction) &&
                Objects.equals(this.accessCodeLifespanLogin, realmRepresentation.accessCodeLifespanLogin) &&
                Objects.equals(this.actionTokenGeneratedByAdminLifespan, realmRepresentation.actionTokenGeneratedByAdminLifespan) &&
                Objects.equals(this.oAuth2DeviceCodeLifespan, realmRepresentation.oAuth2DeviceCodeLifespan) &&
                Objects.equals(this.oAuth2DevicePollingInterval, realmRepresentation.oAuth2DevicePollingInterval) &&
                Objects.equals(this.actionTokenGeneratedByUserLifespan, realmRepresentation.actionTokenGeneratedByUserLifespan) &&
                Objects.equals(this.defaultRoles, realmRepresentation.defaultRoles) &&
                Objects.equals(this.defaultRole, realmRepresentation.defaultRole) &&
                Objects.equals(this.defaultGroups, realmRepresentation.defaultGroups) &&
                Objects.equals(this.privateKey, realmRepresentation.privateKey) &&
                Objects.equals(this.publicKey, realmRepresentation.publicKey) &&
                Objects.equals(this.certificate, realmRepresentation.certificate) &&
                Objects.equals(this.codeSecret, realmRepresentation.codeSecret) &&
                Objects.equals(this.passwordCredentialGrantAllowed, realmRepresentation.passwordCredentialGrantAllowed) &&
                Objects.equals(this.registrationAllowed, realmRepresentation.registrationAllowed) &&
                Objects.equals(this.registrationEmailAsUsername, realmRepresentation.registrationEmailAsUsername) &&
                Objects.equals(this.rememberMe, realmRepresentation.rememberMe) &&
                Objects.equals(this.verifyEmail, realmRepresentation.verifyEmail) &&
                Objects.equals(this.loginWithEmailAllowed, realmRepresentation.loginWithEmailAllowed) &&
                Objects.equals(this.duplicateEmailsAllowed, realmRepresentation.duplicateEmailsAllowed) &&
                Objects.equals(this.resetPasswordAllowed, realmRepresentation.resetPasswordAllowed) &&
                Objects.equals(this.editUsernameAllowed, realmRepresentation.editUsernameAllowed) &&
                Objects.equals(this.social, realmRepresentation.social) &&
                Objects.equals(this.updateProfileOnInitialSocialLogin, realmRepresentation.updateProfileOnInitialSocialLogin) &&
                Objects.equals(this.browserSecurityHeaders, realmRepresentation.browserSecurityHeaders) &&
                Objects.equals(this.socialProviders, realmRepresentation.socialProviders) &&
                Objects.equals(this.smtpServer, realmRepresentation.smtpServer) &&
                Objects.equals(this.oauthClients, realmRepresentation.oauthClients) &&
                Objects.equals(this.clientScopeMappings, realmRepresentation.clientScopeMappings) &&
                Objects.equals(this.applicationScopeMappings, realmRepresentation.applicationScopeMappings) &&
                Objects.equals(this.roles, realmRepresentation.roles) &&
                Objects.equals(this.loginTheme, realmRepresentation.loginTheme) &&
                Objects.equals(this.accountTheme, realmRepresentation.accountTheme) &&
                Objects.equals(this.adminTheme, realmRepresentation.adminTheme) &&
                Objects.equals(this.emailTheme, realmRepresentation.emailTheme) &&
                Objects.equals(this.notBefore, realmRepresentation.notBefore) &&
                Objects.equals(this.bruteForceProtected, realmRepresentation.bruteForceProtected) &&
                Objects.equals(this.permanentLockout, realmRepresentation.permanentLockout) &&
                Objects.equals(this.maxFailureWaitSeconds, realmRepresentation.maxFailureWaitSeconds) &&
                Objects.equals(this.minimumQuickLoginWaitSeconds, realmRepresentation.minimumQuickLoginWaitSeconds) &&
                Objects.equals(this.waitIncrementSeconds, realmRepresentation.waitIncrementSeconds) &&
                Objects.equals(this.quickLoginCheckMilliSeconds, realmRepresentation.quickLoginCheckMilliSeconds) &&
                Objects.equals(this.maxDeltaTimeSeconds, realmRepresentation.maxDeltaTimeSeconds) &&
                Objects.equals(this.failureFactor, realmRepresentation.failureFactor) &&
                Objects.equals(this.eventsEnabled, realmRepresentation.eventsEnabled) &&
                Objects.equals(this.eventsExpiration, realmRepresentation.eventsExpiration) &&
                Objects.equals(this.eventsListeners, realmRepresentation.eventsListeners) &&
                Objects.equals(this.enabledEventTypes, realmRepresentation.enabledEventTypes) &&
                Objects.equals(this.adminEventsEnabled, realmRepresentation.adminEventsEnabled) &&
                Objects.equals(this.adminEventsDetailsEnabled, realmRepresentation.adminEventsDetailsEnabled) &&
                Objects.equals(this.userFederationProviders, realmRepresentation.userFederationProviders) &&
                Objects.equals(this.userFederationMappers, realmRepresentation.userFederationMappers) &&
                Objects.equals(this.identityProviders, realmRepresentation.identityProviders) &&
                Objects.equals(this.protocolMappers, realmRepresentation.protocolMappers) &&
                Objects.equals(this.internationalizationEnabled, realmRepresentation.internationalizationEnabled) &&
                Objects.equals(this.supportedLocales, realmRepresentation.supportedLocales) &&
                Objects.equals(this.defaultLocale, realmRepresentation.defaultLocale) &&
                Objects.equals(this.identityProviderMappers, realmRepresentation.identityProviderMappers) &&
                Objects.equals(this.authenticationFlows, realmRepresentation.authenticationFlows) &&
                Objects.equals(this.authenticatorConfig, realmRepresentation.authenticatorConfig) &&
                Objects.equals(this.requiredActions, realmRepresentation.requiredActions) &&
                Objects.equals(this.otpPolicyType, realmRepresentation.otpPolicyType) &&
                Objects.equals(this.otpPolicyAlgorithm, realmRepresentation.otpPolicyAlgorithm) &&
                Objects.equals(this.otpPolicyInitialCounter, realmRepresentation.otpPolicyInitialCounter) &&
                Objects.equals(this.otpPolicyDigits, realmRepresentation.otpPolicyDigits) &&
                Objects.equals(this.otpPolicyLookAheadWindow, realmRepresentation.otpPolicyLookAheadWindow) &&
                Objects.equals(this.otpPolicyPeriod, realmRepresentation.otpPolicyPeriod) &&
                Objects.equals(this.otpSupportedApplications, realmRepresentation.otpSupportedApplications) &&
                Objects.equals(this.webAuthnPolicyRpEntityName, realmRepresentation.webAuthnPolicyRpEntityName) &&
                Objects.equals(this.webAuthnPolicySignatureAlgorithms, realmRepresentation.webAuthnPolicySignatureAlgorithms) &&
                Objects.equals(this.webAuthnPolicyRpId, realmRepresentation.webAuthnPolicyRpId) &&
                Objects.equals(this.webAuthnPolicyAttestationConveyancePreference, realmRepresentation.webAuthnPolicyAttestationConveyancePreference) &&
                Objects.equals(this.webAuthnPolicyAuthenticatorAttachment, realmRepresentation.webAuthnPolicyAuthenticatorAttachment) &&
                Objects.equals(this.webAuthnPolicyRequireResidentKey, realmRepresentation.webAuthnPolicyRequireResidentKey) &&
                Objects.equals(this.webAuthnPolicyUserVerificationRequirement, realmRepresentation.webAuthnPolicyUserVerificationRequirement) &&
                Objects.equals(this.webAuthnPolicyCreateTimeout, realmRepresentation.webAuthnPolicyCreateTimeout) &&
                Objects.equals(this.webAuthnPolicyAvoidSameAuthenticatorRegister, realmRepresentation.webAuthnPolicyAvoidSameAuthenticatorRegister) &&
                Objects.equals(this.webAuthnPolicyAcceptableAaguids, realmRepresentation.webAuthnPolicyAcceptableAaguids) &&
                Objects.equals(this.webAuthnPolicyPasswordlessRpEntityName, realmRepresentation.webAuthnPolicyPasswordlessRpEntityName) &&
                Objects.equals(this.webAuthnPolicyPasswordlessSignatureAlgorithms, realmRepresentation.webAuthnPolicyPasswordlessSignatureAlgorithms) &&
                Objects.equals(this.webAuthnPolicyPasswordlessRpId, realmRepresentation.webAuthnPolicyPasswordlessRpId) &&
                Objects.equals(this.webAuthnPolicyPasswordlessAttestationConveyancePreference, realmRepresentation.webAuthnPolicyPasswordlessAttestationConveyancePreference) &&
                Objects.equals(this.webAuthnPolicyPasswordlessAuthenticatorAttachment, realmRepresentation.webAuthnPolicyPasswordlessAuthenticatorAttachment) &&
                Objects.equals(this.webAuthnPolicyPasswordlessRequireResidentKey, realmRepresentation.webAuthnPolicyPasswordlessRequireResidentKey) &&
                Objects.equals(this.webAuthnPolicyPasswordlessUserVerificationRequirement, realmRepresentation.webAuthnPolicyPasswordlessUserVerificationRequirement) &&
                Objects.equals(this.webAuthnPolicyPasswordlessCreateTimeout, realmRepresentation.webAuthnPolicyPasswordlessCreateTimeout) &&
                Objects.equals(this.webAuthnPolicyPasswordlessAvoidSameAuthenticatorRegister, realmRepresentation.webAuthnPolicyPasswordlessAvoidSameAuthenticatorRegister) &&
                Objects.equals(this.webAuthnPolicyPasswordlessAcceptableAaguids, realmRepresentation.webAuthnPolicyPasswordlessAcceptableAaguids) &&
                Objects.equals(this.browserFlow, realmRepresentation.browserFlow) &&
                Objects.equals(this.registrationFlow, realmRepresentation.registrationFlow) &&
                Objects.equals(this.directGrantFlow, realmRepresentation.directGrantFlow) &&
                Objects.equals(this.resetCredentialsFlow, realmRepresentation.resetCredentialsFlow) &&
                Objects.equals(this.clientAuthenticationFlow, realmRepresentation.clientAuthenticationFlow) &&
                Objects.equals(this.dockerAuthenticationFlow, realmRepresentation.dockerAuthenticationFlow) &&
                Objects.equals(this.keycloakVersion, realmRepresentation.keycloakVersion) &&
                Objects.equals(this.groups, realmRepresentation.groups) &&
                Objects.equals(this.clientTemplates, realmRepresentation.clientTemplates) &&
                Objects.equals(this.clientScopes, realmRepresentation.clientScopes) &&
                Objects.equals(this.defaultDefaultClientScopes, realmRepresentation.defaultDefaultClientScopes) &&
                Objects.equals(this.defaultOptionalClientScopes, realmRepresentation.defaultOptionalClientScopes) &&
                Objects.equals(this.components, realmRepresentation.components) &&
                Objects.equals(this.attributes, realmRepresentation.attributes) &&
                Objects.equals(this.federatedUsers, realmRepresentation.federatedUsers) &&
                Objects.equals(this.userManagedAccessAllowed, realmRepresentation.userManagedAccessAllowed);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, realm, displayName, displayNameHtml, users, applications, clients, enabled, sslRequired, defaultSignatureAlgorithm, revokeRefreshToken, refreshTokenMaxReuse, accessTokenLifespan, accessTokenLifespanForImplicitFlow, ssoSessionIdleTimeout, ssoSessionMaxLifespan, ssoSessionMaxLifespanRememberMe, ssoSessionIdleTimeoutRememberMe, offlineSessionIdleTimeout, offlineSessionMaxLifespanEnabled, offlineSessionMaxLifespan, clientSessionIdleTimeout, clientSessionMaxLifespan, clientOfflineSessionIdleTimeout, clientOfflineSessionMaxLifespan, scopeMappings, requiredCredentials, passwordPolicy, accessCodeLifespan, accessCodeLifespanUserAction, accessCodeLifespanLogin, actionTokenGeneratedByAdminLifespan, oAuth2DeviceCodeLifespan, oAuth2DevicePollingInterval, actionTokenGeneratedByUserLifespan, defaultRoles, defaultRole, defaultGroups, privateKey, publicKey, certificate, codeSecret, passwordCredentialGrantAllowed, registrationAllowed, registrationEmailAsUsername, rememberMe, verifyEmail, loginWithEmailAllowed, duplicateEmailsAllowed, resetPasswordAllowed, editUsernameAllowed, social, updateProfileOnInitialSocialLogin, browserSecurityHeaders, socialProviders, smtpServer, oauthClients, clientScopeMappings, applicationScopeMappings, roles, loginTheme, accountTheme, adminTheme, emailTheme, notBefore, bruteForceProtected, permanentLockout, maxFailureWaitSeconds, minimumQuickLoginWaitSeconds, waitIncrementSeconds, quickLoginCheckMilliSeconds, maxDeltaTimeSeconds, failureFactor, eventsEnabled, eventsExpiration, eventsListeners, enabledEventTypes, adminEventsEnabled, adminEventsDetailsEnabled, userFederationProviders, userFederationMappers, identityProviders, protocolMappers, internationalizationEnabled, supportedLocales, defaultLocale, identityProviderMappers, authenticationFlows, authenticatorConfig, requiredActions, otpPolicyType, otpPolicyAlgorithm, otpPolicyInitialCounter, otpPolicyDigits, otpPolicyLookAheadWindow, otpPolicyPeriod, otpSupportedApplications, webAuthnPolicyRpEntityName, webAuthnPolicySignatureAlgorithms, webAuthnPolicyRpId, webAuthnPolicyAttestationConveyancePreference, webAuthnPolicyAuthenticatorAttachment, webAuthnPolicyRequireResidentKey, webAuthnPolicyUserVerificationRequirement, webAuthnPolicyCreateTimeout, webAuthnPolicyAvoidSameAuthenticatorRegister, webAuthnPolicyAcceptableAaguids, webAuthnPolicyPasswordlessRpEntityName, webAuthnPolicyPasswordlessSignatureAlgorithms, webAuthnPolicyPasswordlessRpId, webAuthnPolicyPasswordlessAttestationConveyancePreference, webAuthnPolicyPasswordlessAuthenticatorAttachment, webAuthnPolicyPasswordlessRequireResidentKey, webAuthnPolicyPasswordlessUserVerificationRequirement, webAuthnPolicyPasswordlessCreateTimeout, webAuthnPolicyPasswordlessAvoidSameAuthenticatorRegister, webAuthnPolicyPasswordlessAcceptableAaguids, browserFlow, registrationFlow, directGrantFlow, resetCredentialsFlow, clientAuthenticationFlow, dockerAuthenticationFlow, keycloakVersion, groups, clientTemplates, clientScopes, defaultDefaultClientScopes, defaultOptionalClientScopes, components, attributes, federatedUsers, userManagedAccessAllowed);
    }

    @Override
    public String toString() {

        String sb = "class RealmRepresentationDTO {\n" +
                "    id: " + toIndentedString(id) + "\n" +
                "    realm: " + toIndentedString(realm) + "\n" +
                "    displayName: " + toIndentedString(displayName) + "\n" +
                "    displayNameHtml: " + toIndentedString(displayNameHtml) + "\n" +
                "    users: " + toIndentedString(users) + "\n" +
                "    applications: " + toIndentedString(applications) + "\n" +
                "    clients: " + toIndentedString(clients) + "\n" +
                "    enabled: " + toIndentedString(enabled) + "\n" +
                "    sslRequired: " + toIndentedString(sslRequired) + "\n" +
                "    defaultSignatureAlgorithm: " + toIndentedString(defaultSignatureAlgorithm) + "\n" +
                "    revokeRefreshToken: " + toIndentedString(revokeRefreshToken) + "\n" +
                "    refreshTokenMaxReuse: " + toIndentedString(refreshTokenMaxReuse) + "\n" +
                "    accessTokenLifespan: " + toIndentedString(accessTokenLifespan) + "\n" +
                "    accessTokenLifespanForImplicitFlow: " + toIndentedString(accessTokenLifespanForImplicitFlow) + "\n" +
                "    ssoSessionIdleTimeout: " + toIndentedString(ssoSessionIdleTimeout) + "\n" +
                "    ssoSessionMaxLifespan: " + toIndentedString(ssoSessionMaxLifespan) + "\n" +
                "    ssoSessionMaxLifespanRememberMe: " + toIndentedString(ssoSessionMaxLifespanRememberMe) + "\n" +
                "    ssoSessionIdleTimeoutRememberMe: " + toIndentedString(ssoSessionIdleTimeoutRememberMe) + "\n" +
                "    offlineSessionIdleTimeout: " + toIndentedString(offlineSessionIdleTimeout) + "\n" +
                "    offlineSessionMaxLifespanEnabled: " + toIndentedString(offlineSessionMaxLifespanEnabled) + "\n" +
                "    offlineSessionMaxLifespan: " + toIndentedString(offlineSessionMaxLifespan) + "\n" +
                "    clientSessionIdleTimeout: " + toIndentedString(clientSessionIdleTimeout) + "\n" +
                "    clientSessionMaxLifespan: " + toIndentedString(clientSessionMaxLifespan) + "\n" +
                "    clientOfflineSessionIdleTimeout: " + toIndentedString(clientOfflineSessionIdleTimeout) + "\n" +
                "    clientOfflineSessionMaxLifespan: " + toIndentedString(clientOfflineSessionMaxLifespan) + "\n" +
                "    scopeMappings: " + toIndentedString(scopeMappings) + "\n" +
                "    requiredCredentials: " + toIndentedString(requiredCredentials) + "\n" +
                "    passwordPolicy: " + toIndentedString(passwordPolicy) + "\n" +
                "    accessCodeLifespan: " + toIndentedString(accessCodeLifespan) + "\n" +
                "    accessCodeLifespanUserAction: " + toIndentedString(accessCodeLifespanUserAction) + "\n" +
                "    accessCodeLifespanLogin: " + toIndentedString(accessCodeLifespanLogin) + "\n" +
                "    actionTokenGeneratedByAdminLifespan: " + toIndentedString(actionTokenGeneratedByAdminLifespan) + "\n" +
                "    oAuth2DeviceCodeLifespan: " + toIndentedString(oAuth2DeviceCodeLifespan) + "\n" +
                "    oAuth2DevicePollingInterval: " + toIndentedString(oAuth2DevicePollingInterval) + "\n" +
                "    actionTokenGeneratedByUserLifespan: " + toIndentedString(actionTokenGeneratedByUserLifespan) + "\n" +
                "    defaultRoles: " + toIndentedString(defaultRoles) + "\n" +
                "    defaultRole: " + toIndentedString(defaultRole) + "\n" +
                "    defaultGroups: " + toIndentedString(defaultGroups) + "\n" +
                "    privateKey: " + toIndentedString(privateKey) + "\n" +
                "    publicKey: " + toIndentedString(publicKey) + "\n" +
                "    certificate: " + toIndentedString(certificate) + "\n" +
                "    codeSecret: " + toIndentedString(codeSecret) + "\n" +
                "    passwordCredentialGrantAllowed: " + toIndentedString(passwordCredentialGrantAllowed) + "\n" +
                "    registrationAllowed: " + toIndentedString(registrationAllowed) + "\n" +
                "    registrationEmailAsUsername: " + toIndentedString(registrationEmailAsUsername) + "\n" +
                "    rememberMe: " + toIndentedString(rememberMe) + "\n" +
                "    verifyEmail: " + toIndentedString(verifyEmail) + "\n" +
                "    loginWithEmailAllowed: " + toIndentedString(loginWithEmailAllowed) + "\n" +
                "    duplicateEmailsAllowed: " + toIndentedString(duplicateEmailsAllowed) + "\n" +
                "    resetPasswordAllowed: " + toIndentedString(resetPasswordAllowed) + "\n" +
                "    editUsernameAllowed: " + toIndentedString(editUsernameAllowed) + "\n" +
                "    social: " + toIndentedString(social) + "\n" +
                "    updateProfileOnInitialSocialLogin: " + toIndentedString(updateProfileOnInitialSocialLogin) + "\n" +
                "    browserSecurityHeaders: " + toIndentedString(browserSecurityHeaders) + "\n" +
                "    socialProviders: " + toIndentedString(socialProviders) + "\n" +
                "    smtpServer: " + toIndentedString(smtpServer) + "\n" +
                "    oauthClients: " + toIndentedString(oauthClients) + "\n" +
                "    clientScopeMappings: " + toIndentedString(clientScopeMappings) + "\n" +
                "    applicationScopeMappings: " + toIndentedString(applicationScopeMappings) + "\n" +
                "    roles: " + toIndentedString(roles) + "\n" +
                "    loginTheme: " + toIndentedString(loginTheme) + "\n" +
                "    accountTheme: " + toIndentedString(accountTheme) + "\n" +
                "    adminTheme: " + toIndentedString(adminTheme) + "\n" +
                "    emailTheme: " + toIndentedString(emailTheme) + "\n" +
                "    notBefore: " + toIndentedString(notBefore) + "\n" +
                "    bruteForceProtected: " + toIndentedString(bruteForceProtected) + "\n" +
                "    permanentLockout: " + toIndentedString(permanentLockout) + "\n" +
                "    maxFailureWaitSeconds: " + toIndentedString(maxFailureWaitSeconds) + "\n" +
                "    minimumQuickLoginWaitSeconds: " + toIndentedString(minimumQuickLoginWaitSeconds) + "\n" +
                "    waitIncrementSeconds: " + toIndentedString(waitIncrementSeconds) + "\n" +
                "    quickLoginCheckMilliSeconds: " + toIndentedString(quickLoginCheckMilliSeconds) + "\n" +
                "    maxDeltaTimeSeconds: " + toIndentedString(maxDeltaTimeSeconds) + "\n" +
                "    failureFactor: " + toIndentedString(failureFactor) + "\n" +
                "    eventsEnabled: " + toIndentedString(eventsEnabled) + "\n" +
                "    eventsExpiration: " + toIndentedString(eventsExpiration) + "\n" +
                "    eventsListeners: " + toIndentedString(eventsListeners) + "\n" +
                "    enabledEventTypes: " + toIndentedString(enabledEventTypes) + "\n" +
                "    adminEventsEnabled: " + toIndentedString(adminEventsEnabled) + "\n" +
                "    adminEventsDetailsEnabled: " + toIndentedString(adminEventsDetailsEnabled) + "\n" +
                "    userFederationProviders: " + toIndentedString(userFederationProviders) + "\n" +
                "    userFederationMappers: " + toIndentedString(userFederationMappers) + "\n" +
                "    identityProviders: " + toIndentedString(identityProviders) + "\n" +
                "    protocolMappers: " + toIndentedString(protocolMappers) + "\n" +
                "    internationalizationEnabled: " + toIndentedString(internationalizationEnabled) + "\n" +
                "    supportedLocales: " + toIndentedString(supportedLocales) + "\n" +
                "    defaultLocale: " + toIndentedString(defaultLocale) + "\n" +
                "    identityProviderMappers: " + toIndentedString(identityProviderMappers) + "\n" +
                "    authenticationFlows: " + toIndentedString(authenticationFlows) + "\n" +
                "    authenticatorConfig: " + toIndentedString(authenticatorConfig) + "\n" +
                "    requiredActions: " + toIndentedString(requiredActions) + "\n" +
                "    otpPolicyType: " + toIndentedString(otpPolicyType) + "\n" +
                "    otpPolicyAlgorithm: " + toIndentedString(otpPolicyAlgorithm) + "\n" +
                "    otpPolicyInitialCounter: " + toIndentedString(otpPolicyInitialCounter) + "\n" +
                "    otpPolicyDigits: " + toIndentedString(otpPolicyDigits) + "\n" +
                "    otpPolicyLookAheadWindow: " + toIndentedString(otpPolicyLookAheadWindow) + "\n" +
                "    otpPolicyPeriod: " + toIndentedString(otpPolicyPeriod) + "\n" +
                "    otpSupportedApplications: " + toIndentedString(otpSupportedApplications) + "\n" +
                "    webAuthnPolicyRpEntityName: " + toIndentedString(webAuthnPolicyRpEntityName) + "\n" +
                "    webAuthnPolicySignatureAlgorithms: " + toIndentedString(webAuthnPolicySignatureAlgorithms) + "\n" +
                "    webAuthnPolicyRpId: " + toIndentedString(webAuthnPolicyRpId) + "\n" +
                "    webAuthnPolicyAttestationConveyancePreference: " + toIndentedString(webAuthnPolicyAttestationConveyancePreference) + "\n" +
                "    webAuthnPolicyAuthenticatorAttachment: " + toIndentedString(webAuthnPolicyAuthenticatorAttachment) + "\n" +
                "    webAuthnPolicyRequireResidentKey: " + toIndentedString(webAuthnPolicyRequireResidentKey) + "\n" +
                "    webAuthnPolicyUserVerificationRequirement: " + toIndentedString(webAuthnPolicyUserVerificationRequirement) + "\n" +
                "    webAuthnPolicyCreateTimeout: " + toIndentedString(webAuthnPolicyCreateTimeout) + "\n" +
                "    webAuthnPolicyAvoidSameAuthenticatorRegister: " + toIndentedString(webAuthnPolicyAvoidSameAuthenticatorRegister) + "\n" +
                "    webAuthnPolicyAcceptableAaguids: " + toIndentedString(webAuthnPolicyAcceptableAaguids) + "\n" +
                "    webAuthnPolicyPasswordlessRpEntityName: " + toIndentedString(webAuthnPolicyPasswordlessRpEntityName) + "\n" +
                "    webAuthnPolicyPasswordlessSignatureAlgorithms: " + toIndentedString(webAuthnPolicyPasswordlessSignatureAlgorithms) + "\n" +
                "    webAuthnPolicyPasswordlessRpId: " + toIndentedString(webAuthnPolicyPasswordlessRpId) + "\n" +
                "    webAuthnPolicyPasswordlessAttestationConveyancePreference: " + toIndentedString(webAuthnPolicyPasswordlessAttestationConveyancePreference) + "\n" +
                "    webAuthnPolicyPasswordlessAuthenticatorAttachment: " + toIndentedString(webAuthnPolicyPasswordlessAuthenticatorAttachment) + "\n" +
                "    webAuthnPolicyPasswordlessRequireResidentKey: " + toIndentedString(webAuthnPolicyPasswordlessRequireResidentKey) + "\n" +
                "    webAuthnPolicyPasswordlessUserVerificationRequirement: " + toIndentedString(webAuthnPolicyPasswordlessUserVerificationRequirement) + "\n" +
                "    webAuthnPolicyPasswordlessCreateTimeout: " + toIndentedString(webAuthnPolicyPasswordlessCreateTimeout) + "\n" +
                "    webAuthnPolicyPasswordlessAvoidSameAuthenticatorRegister: " + toIndentedString(webAuthnPolicyPasswordlessAvoidSameAuthenticatorRegister) + "\n" +
                "    webAuthnPolicyPasswordlessAcceptableAaguids: " + toIndentedString(webAuthnPolicyPasswordlessAcceptableAaguids) + "\n" +
                "    browserFlow: " + toIndentedString(browserFlow) + "\n" +
                "    registrationFlow: " + toIndentedString(registrationFlow) + "\n" +
                "    directGrantFlow: " + toIndentedString(directGrantFlow) + "\n" +
                "    resetCredentialsFlow: " + toIndentedString(resetCredentialsFlow) + "\n" +
                "    clientAuthenticationFlow: " + toIndentedString(clientAuthenticationFlow) + "\n" +
                "    dockerAuthenticationFlow: " + toIndentedString(dockerAuthenticationFlow) + "\n" +
                "    keycloakVersion: " + toIndentedString(keycloakVersion) + "\n" +
                "    groups: " + toIndentedString(groups) + "\n" +
                "    clientTemplates: " + toIndentedString(clientTemplates) + "\n" +
                "    clientScopes: " + toIndentedString(clientScopes) + "\n" +
                "    defaultDefaultClientScopes: " + toIndentedString(defaultDefaultClientScopes) + "\n" +
                "    defaultOptionalClientScopes: " + toIndentedString(defaultOptionalClientScopes) + "\n" +
                "    components: " + toIndentedString(components) + "\n" +
                "    attributes: " + toIndentedString(attributes) + "\n" +
                "    federatedUsers: " + toIndentedString(federatedUsers) + "\n" +
                "    userManagedAccessAllowed: " + toIndentedString(userManagedAccessAllowed) + "\n" +
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

