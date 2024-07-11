package it.getinsight.module.keycloak.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;


public class RolesRepresentationDTO {
    @JsonProperty("realm")
    private List<RoleRepresentationDTO> realm = null;

    @JsonProperty("client")
    private Map<String, List<RoleRepresentationDTO>> client = null;

    @JsonProperty("application")
    private Map<String, List<RoleRepresentationDTO>> application = null;

    public RolesRepresentationDTO realm(List<RoleRepresentationDTO> realm) {
        this.realm = realm;
        return this;
    }

    public RolesRepresentationDTO addRealmItem(RoleRepresentationDTO realmItem) {
        if (this.realm == null) {
            this.realm = new ArrayList<RoleRepresentationDTO>();
        }
        this.realm.add(realmItem);
        return this;
    }


    public List<RoleRepresentationDTO> getRealm() {
        return realm;
    }

    public void setRealm(List<RoleRepresentationDTO> realm) {
        this.realm = realm;
    }

    public RolesRepresentationDTO client(Map<String, List<RoleRepresentationDTO>> client) {
        this.client = client;
        return this;
    }

    public RolesRepresentationDTO putClientItem(String key, List<RoleRepresentationDTO> clientItem) {
        if (this.client == null) {
            this.client = null;
        }
        this.client.put(key, clientItem);
        return this;
    }


    public Map<String, List<RoleRepresentationDTO>> getClient() {
        return client;
    }

    public void setClient(Map<String, List<RoleRepresentationDTO>> client) {
        this.client = client;
    }

    public RolesRepresentationDTO application(Map<String, List<RoleRepresentationDTO>> application) {
        this.application = application;
        return this;
    }

    public RolesRepresentationDTO putApplicationItem(String key, List<RoleRepresentationDTO> applicationItem) {
        if (this.application == null) {
            this.application = null;
        }
        this.application.put(key, applicationItem);
        return this;
    }


    public Map<String, List<RoleRepresentationDTO>> getApplication() {
        return application;
    }

    public void setApplication(Map<String, List<RoleRepresentationDTO>> application) {
        this.application = application;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        RolesRepresentationDTO rolesRepresentation = (RolesRepresentationDTO) o;
        return Objects.equals(this.realm, rolesRepresentation.realm) &&
                Objects.equals(this.client, rolesRepresentation.client) &&
                Objects.equals(this.application, rolesRepresentation.application);
    }

    @Override
    public int hashCode() {
        return Objects.hash(realm, client, application);
    }

    @Override
    public String toString() {

        String sb = "class RolesRepresentationDTO {\n" +
                "    realm: " + toIndentedString(realm) + "\n" +
                "    client: " + toIndentedString(client) + "\n" +
                "    application: " + toIndentedString(application) + "\n" +
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

