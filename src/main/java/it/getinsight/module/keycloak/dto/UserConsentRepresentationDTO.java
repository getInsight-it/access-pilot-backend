package it.getinsight.module.keycloak.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


public class UserConsentRepresentationDTO {
    @JsonProperty("clientId")
    private String clientId = null;

    @JsonProperty("grantedClientScopes")
    private List<String> grantedClientScopes = null;

    @JsonProperty("createdDate")
    private Long createdDate = null;

    @JsonProperty("lastUpdatedDate")
    private Long lastUpdatedDate = null;

    @JsonProperty("grantedRealmRoles")
    private List<String> grantedRealmRoles = null;

    public UserConsentRepresentationDTO clientId(String clientId) {
        this.clientId = clientId;
        return this;
    }


    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public UserConsentRepresentationDTO grantedClientScopes(List<String> grantedClientScopes) {
        this.grantedClientScopes = grantedClientScopes;
        return this;
    }

    public UserConsentRepresentationDTO addGrantedClientScopesItem(String grantedClientScopesItem) {
        if (this.grantedClientScopes == null) {
            this.grantedClientScopes = new ArrayList<String>();
        }
        this.grantedClientScopes.add(grantedClientScopesItem);
        return this;
    }


    public List<String> getGrantedClientScopes() {
        return grantedClientScopes;
    }

    public void setGrantedClientScopes(List<String> grantedClientScopes) {
        this.grantedClientScopes = grantedClientScopes;
    }

    public UserConsentRepresentationDTO createdDate(Long createdDate) {
        this.createdDate = createdDate;
        return this;
    }


    public Long getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Long createdDate) {
        this.createdDate = createdDate;
    }

    public UserConsentRepresentationDTO lastUpdatedDate(Long lastUpdatedDate) {
        this.lastUpdatedDate = lastUpdatedDate;
        return this;
    }


    public Long getLastUpdatedDate() {
        return lastUpdatedDate;
    }

    public void setLastUpdatedDate(Long lastUpdatedDate) {
        this.lastUpdatedDate = lastUpdatedDate;
    }

    public UserConsentRepresentationDTO grantedRealmRoles(List<String> grantedRealmRoles) {
        this.grantedRealmRoles = grantedRealmRoles;
        return this;
    }

    public UserConsentRepresentationDTO addGrantedRealmRolesItem(String grantedRealmRolesItem) {
        if (this.grantedRealmRoles == null) {
            this.grantedRealmRoles = new ArrayList<String>();
        }
        this.grantedRealmRoles.add(grantedRealmRolesItem);
        return this;
    }


    public List<String> getGrantedRealmRoles() {
        return grantedRealmRoles;
    }

    public void setGrantedRealmRoles(List<String> grantedRealmRoles) {
        this.grantedRealmRoles = grantedRealmRoles;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        UserConsentRepresentationDTO userConsentRepresentation = (UserConsentRepresentationDTO) o;
        return Objects.equals(this.clientId, userConsentRepresentation.clientId) &&
                Objects.equals(this.grantedClientScopes, userConsentRepresentation.grantedClientScopes) &&
                Objects.equals(this.createdDate, userConsentRepresentation.createdDate) &&
                Objects.equals(this.lastUpdatedDate, userConsentRepresentation.lastUpdatedDate) &&
                Objects.equals(this.grantedRealmRoles, userConsentRepresentation.grantedRealmRoles);
    }

    @Override
    public int hashCode() {
        return Objects.hash(clientId, grantedClientScopes, createdDate, lastUpdatedDate, grantedRealmRoles);
    }

    @Override
    public String toString() {

        String sb = "class UserConsentRepresentationDTO {\n" +
                "    clientId: " + toIndentedString(clientId) + "\n" +
                "    grantedClientScopes: " + toIndentedString(grantedClientScopes) + "\n" +
                "    createdDate: " + toIndentedString(createdDate) + "\n" +
                "    lastUpdatedDate: " + toIndentedString(lastUpdatedDate) + "\n" +
                "    grantedRealmRoles: " + toIndentedString(grantedRealmRoles) + "\n" +
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

