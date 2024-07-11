package it.getinsight.module.keycloak.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;


public class UserRepresentationDTO {
    @JsonProperty("self")
    private String self = null;

    @JsonProperty("id")
    private String id = null;

    @JsonProperty("createdTimestamp")
    private Long createdTimestamp = null;

    @JsonProperty("firstName")
    private String firstName = null;

    @JsonProperty("lastName")
    private String lastName = null;

    @JsonProperty("email")
    private String email = null;

    @JsonProperty("username")
    private String username = null;

    @JsonProperty("enabled")
    private Boolean enabled = null;

    @JsonProperty("totp")
    private Boolean totp = null;

    @JsonProperty("emailVerified")
    private Boolean emailVerified = null;

    @JsonProperty("attributes")
    private Map<String, List<String>> attributes = null;

    @JsonProperty("credentials")
    private List<CredentialRepresentationDTO> credentials = null;

    @JsonProperty("requiredActions")
    private List<String> requiredActions = null;

    @JsonProperty("federatedIdentities")
    private List<it.getinsight.module.keycloak.dto.FederatedIdentityRepresentationDTO> federatedIdentities = null;

    @JsonProperty("socialLinks")
    private List<SocialLinkRepresentationDTO> socialLinks = null;

    @JsonProperty("realmRoles")
    private List<String> realmRoles = null;

    @JsonProperty("clientRoles")
    private Map<String, List<String>> clientRoles = null;

    @JsonProperty("clientConsents")
    private List<UserConsentRepresentationDTO> clientConsents = null;

    @JsonProperty("notBefore")
    private Integer notBefore = null;

    @JsonProperty("applicationRoles")
    private Map<String, List<String>> applicationRoles = null;

    @JsonProperty("federationLink")
    private String federationLink = null;

    @JsonProperty("serviceAccountClientId")
    private String serviceAccountClientId = null;

    @JsonProperty("groups")
    private List<String> groups = null;

    @JsonProperty("origin")
    private String origin = null;

    @JsonProperty("disableableCredentialTypes")
    private List<String> disableableCredentialTypes = null;

    @JsonProperty("access")
    private Map<String, Boolean> access = null;

    public UserRepresentationDTO self(String self) {
        this.self = self;
        return this;
    }


    public String getSelf() {
        return self;
    }

    public void setSelf(String self) {
        this.self = self;
    }

    public UserRepresentationDTO id(String id) {
        this.id = id;
        return this;
    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public UserRepresentationDTO createdTimestamp(Long createdTimestamp) {
        this.createdTimestamp = createdTimestamp;
        return this;
    }


    public Long getCreatedTimestamp() {
        return createdTimestamp;
    }

    public void setCreatedTimestamp(Long createdTimestamp) {
        this.createdTimestamp = createdTimestamp;
    }

    public UserRepresentationDTO firstName(String firstName) {
        this.firstName = firstName;
        return this;
    }


    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public UserRepresentationDTO lastName(String lastName) {
        this.lastName = lastName;
        return this;
    }


    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public UserRepresentationDTO email(String email) {
        this.email = email;
        return this;
    }


    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public UserRepresentationDTO username(String username) {
        this.username = username;
        return this;
    }


    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public UserRepresentationDTO enabled(Boolean enabled) {
        this.enabled = enabled;
        return this;
    }


    public Boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(Boolean enabled) {
        this.enabled = enabled;
    }

    public UserRepresentationDTO totp(Boolean totp) {
        this.totp = totp;
        return this;
    }


    public Boolean isTotp() {
        return totp;
    }

    public void setTotp(Boolean totp) {
        this.totp = totp;
    }

    public UserRepresentationDTO emailVerified(Boolean emailVerified) {
        this.emailVerified = emailVerified;
        return this;
    }


    public Boolean isEmailVerified() {
        return emailVerified;
    }

    public void setEmailVerified(Boolean emailVerified) {
        this.emailVerified = emailVerified;
    }

    public UserRepresentationDTO attributes(Map<String, List<String>> attributes) {
        this.attributes = attributes;
        return this;
    }

    public UserRepresentationDTO putAttributesItem(String key, List<String> attributesItem) {
        if (this.attributes == null) {
            this.attributes = null;
        }
        this.attributes.put(key, attributesItem);
        return this;
    }


    public Map<String, List<String>> getAttributes() {
        return attributes;
    }

    public void setAttributes(Map<String, List<String>> attributes) {
        this.attributes = attributes;
    }

    public UserRepresentationDTO credentials(List<CredentialRepresentationDTO> credentials) {
        this.credentials = credentials;
        return this;
    }

    public UserRepresentationDTO addCredentialsItem(CredentialRepresentationDTO credentialsItem) {
        if (this.credentials == null) {
            this.credentials = new ArrayList<CredentialRepresentationDTO>();
        }
        this.credentials.add(credentialsItem);
        return this;
    }


    public List<CredentialRepresentationDTO> getCredentials() {
        return credentials;
    }

    public void setCredentials(List<CredentialRepresentationDTO> credentials) {
        this.credentials = credentials;
    }

    public UserRepresentationDTO requiredActions(List<String> requiredActions) {
        this.requiredActions = requiredActions;
        return this;
    }

    public UserRepresentationDTO addRequiredActionsItem(String requiredActionsItem) {
        if (this.requiredActions == null) {
            this.requiredActions = new ArrayList<String>();
        }
        this.requiredActions.add(requiredActionsItem);
        return this;
    }


    public List<String> getRequiredActions() {
        return requiredActions;
    }

    public void setRequiredActions(List<String> requiredActions) {
        this.requiredActions = requiredActions;
    }

    public UserRepresentationDTO federatedIdentities(List<FederatedIdentityRepresentationDTO> federatedIdentities) {
        this.federatedIdentities = federatedIdentities;
        return this;
    }

    public UserRepresentationDTO addFederatedIdentitiesItem(FederatedIdentityRepresentationDTO federatedIdentitiesItem) {
        if (this.federatedIdentities == null) {
            this.federatedIdentities = new ArrayList<FederatedIdentityRepresentationDTO>();
        }
        this.federatedIdentities.add(federatedIdentitiesItem);
        return this;
    }


    public List<FederatedIdentityRepresentationDTO> getFederatedIdentities() {
        return federatedIdentities;
    }

    public void setFederatedIdentities(List<FederatedIdentityRepresentationDTO> federatedIdentities) {
        this.federatedIdentities = federatedIdentities;
    }

    public UserRepresentationDTO socialLinks(List<SocialLinkRepresentationDTO> socialLinks) {
        this.socialLinks = socialLinks;
        return this;
    }

    public UserRepresentationDTO addSocialLinksItem(SocialLinkRepresentationDTO socialLinksItem) {
        if (this.socialLinks == null) {
            this.socialLinks = new ArrayList<SocialLinkRepresentationDTO>();
        }
        this.socialLinks.add(socialLinksItem);
        return this;
    }


    public List<SocialLinkRepresentationDTO> getSocialLinks() {
        return socialLinks;
    }

    public void setSocialLinks(List<SocialLinkRepresentationDTO> socialLinks) {
        this.socialLinks = socialLinks;
    }

    public UserRepresentationDTO realmRoles(List<String> realmRoles) {
        this.realmRoles = realmRoles;
        return this;
    }

    public UserRepresentationDTO addRealmRolesItem(String realmRolesItem) {
        if (this.realmRoles == null) {
            this.realmRoles = new ArrayList<String>();
        }
        this.realmRoles.add(realmRolesItem);
        return this;
    }


    public List<String> getRealmRoles() {
        return realmRoles;
    }

    public void setRealmRoles(List<String> realmRoles) {
        this.realmRoles = realmRoles;
    }

    public UserRepresentationDTO clientRoles(Map<String, List<String>> clientRoles) {
        this.clientRoles = clientRoles;
        return this;
    }

    public UserRepresentationDTO putClientRolesItem(String key, List<String> clientRolesItem) {
        if (this.clientRoles == null) {
            this.clientRoles = null;
        }
        this.clientRoles.put(key, clientRolesItem);
        return this;
    }


    public Map<String, List<String>> getClientRoles() {
        return clientRoles;
    }

    public void setClientRoles(Map<String, List<String>> clientRoles) {
        this.clientRoles = clientRoles;
    }

    public UserRepresentationDTO clientConsents(List<UserConsentRepresentationDTO> clientConsents) {
        this.clientConsents = clientConsents;
        return this;
    }

    public UserRepresentationDTO addClientConsentsItem(UserConsentRepresentationDTO clientConsentsItem) {
        if (this.clientConsents == null) {
            this.clientConsents = new ArrayList<UserConsentRepresentationDTO>();
        }
        this.clientConsents.add(clientConsentsItem);
        return this;
    }


    public List<UserConsentRepresentationDTO> getClientConsents() {
        return clientConsents;
    }

    public void setClientConsents(List<UserConsentRepresentationDTO> clientConsents) {
        this.clientConsents = clientConsents;
    }

    public UserRepresentationDTO notBefore(Integer notBefore) {
        this.notBefore = notBefore;
        return this;
    }


    public Integer getNotBefore() {
        return notBefore;
    }

    public void setNotBefore(Integer notBefore) {
        this.notBefore = notBefore;
    }

    public UserRepresentationDTO applicationRoles(Map<String, List<String>> applicationRoles) {
        this.applicationRoles = applicationRoles;
        return this;
    }

    public UserRepresentationDTO putApplicationRolesItem(String key, List<String> applicationRolesItem) {
        if (this.applicationRoles == null) {
            this.applicationRoles = null;
        }
        this.applicationRoles.put(key, applicationRolesItem);
        return this;
    }


    public Map<String, List<String>> getApplicationRoles() {
        return applicationRoles;
    }

    public void setApplicationRoles(Map<String, List<String>> applicationRoles) {
        this.applicationRoles = applicationRoles;
    }

    public UserRepresentationDTO federationLink(String federationLink) {
        this.federationLink = federationLink;
        return this;
    }


    public String getFederationLink() {
        return federationLink;
    }

    public void setFederationLink(String federationLink) {
        this.federationLink = federationLink;
    }

    public UserRepresentationDTO serviceAccountClientId(String serviceAccountClientId) {
        this.serviceAccountClientId = serviceAccountClientId;
        return this;
    }


    public String getServiceAccountClientId() {
        return serviceAccountClientId;
    }

    public void setServiceAccountClientId(String serviceAccountClientId) {
        this.serviceAccountClientId = serviceAccountClientId;
    }

    public UserRepresentationDTO groups(List<String> groups) {
        this.groups = groups;
        return this;
    }

    public UserRepresentationDTO addGroupsItem(String groupsItem) {
        if (this.groups == null) {
            this.groups = new ArrayList<String>();
        }
        this.groups.add(groupsItem);
        return this;
    }


    public List<String> getGroups() {
        return groups;
    }

    public void setGroups(List<String> groups) {
        this.groups = groups;
    }

    public UserRepresentationDTO origin(String origin) {
        this.origin = origin;
        return this;
    }


    public String getOrigin() {
        return origin;
    }

    public void setOrigin(String origin) {
        this.origin = origin;
    }

    public UserRepresentationDTO disableableCredentialTypes(List<String> disableableCredentialTypes) {
        this.disableableCredentialTypes = disableableCredentialTypes;
        return this;
    }

    public UserRepresentationDTO addDisableableCredentialTypesItem(String disableableCredentialTypesItem) {
        if (this.disableableCredentialTypes == null) {
            this.disableableCredentialTypes = new ArrayList<String>();
        }
        this.disableableCredentialTypes.add(disableableCredentialTypesItem);
        return this;
    }


    public List<String> getDisableableCredentialTypes() {
        return disableableCredentialTypes;
    }

    public void setDisableableCredentialTypes(List<String> disableableCredentialTypes) {
        this.disableableCredentialTypes = disableableCredentialTypes;
    }

    public UserRepresentationDTO access(Map<String, Boolean> access) {
        this.access = access;
        return this;
    }

    public UserRepresentationDTO putAccessItem(String key, Boolean accessItem) {
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


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        UserRepresentationDTO userRepresentation = (UserRepresentationDTO) o;
        return Objects.equals(this.self, userRepresentation.self) &&
            Objects.equals(this.id, userRepresentation.id) &&
            Objects.equals(this.createdTimestamp, userRepresentation.createdTimestamp) &&
            Objects.equals(this.firstName, userRepresentation.firstName) &&
            Objects.equals(this.lastName, userRepresentation.lastName) &&
            Objects.equals(this.email, userRepresentation.email) &&
            Objects.equals(this.username, userRepresentation.username) &&
            Objects.equals(this.enabled, userRepresentation.enabled) &&
            Objects.equals(this.totp, userRepresentation.totp) &&
            Objects.equals(this.emailVerified, userRepresentation.emailVerified) &&
            Objects.equals(this.attributes, userRepresentation.attributes) &&
            Objects.equals(this.credentials, userRepresentation.credentials) &&
            Objects.equals(this.requiredActions, userRepresentation.requiredActions) &&
            Objects.equals(this.federatedIdentities, userRepresentation.federatedIdentities) &&
            Objects.equals(this.socialLinks, userRepresentation.socialLinks) &&
            Objects.equals(this.realmRoles, userRepresentation.realmRoles) &&
            Objects.equals(this.clientRoles, userRepresentation.clientRoles) &&
            Objects.equals(this.clientConsents, userRepresentation.clientConsents) &&
            Objects.equals(this.notBefore, userRepresentation.notBefore) &&
            Objects.equals(this.applicationRoles, userRepresentation.applicationRoles) &&
            Objects.equals(this.federationLink, userRepresentation.federationLink) &&
            Objects.equals(this.serviceAccountClientId, userRepresentation.serviceAccountClientId) &&
            Objects.equals(this.groups, userRepresentation.groups) &&
            Objects.equals(this.origin, userRepresentation.origin) &&
            Objects.equals(this.disableableCredentialTypes, userRepresentation.disableableCredentialTypes) &&
            Objects.equals(this.access, userRepresentation.access);
    }

    @Override
    public int hashCode() {
        return Objects.hash(self, id, createdTimestamp, firstName, lastName, email, username, enabled, totp, emailVerified, attributes, credentials, requiredActions, federatedIdentities, socialLinks, realmRoles, clientRoles, clientConsents, notBefore, applicationRoles, federationLink, serviceAccountClientId, groups, origin, disableableCredentialTypes, access);
    }


    private String toIndentedString(Object o) {
        if (o == null) {
            return "null";
        }
        return o.toString().replace("\n", "\n    ");
    }
}

