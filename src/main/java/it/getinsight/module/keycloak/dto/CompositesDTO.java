package it.getinsight.module.keycloak.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;


public class CompositesDTO {
    @JsonProperty("realm")
    private List<String> realm = null;

    @JsonProperty("client")
    private Map<String, List<String>> client = null;

    @JsonProperty("application")
    private Map<String, List<String>> application = null;

    public CompositesDTO realm(List<String> realm) {
        this.realm = realm;
        return this;
    }

    public CompositesDTO addRealmItem(String realmItem) {
        if (this.realm == null) {
            this.realm = new ArrayList<String>();
        }
        this.realm.add(realmItem);
        return this;
    }


    public List<String> getRealm() {
        return realm;
    }

    public void setRealm(List<String> realm) {
        this.realm = realm;
    }

    public CompositesDTO client(Map<String, List<String>> client) {
        this.client = client;
        return this;
    }

    public CompositesDTO putClientItem(String key, List<String> clientItem) {
        if (this.client == null) {
            this.client = null;
        }
        this.client.put(key, clientItem);
        return this;
    }


    public Map<String, List<String>> getClient() {
        return client;
    }

    public void setClient(Map<String, List<String>> client) {
        this.client = client;
    }

    public CompositesDTO application(Map<String, List<String>> application) {
        this.application = application;
        return this;
    }

    public CompositesDTO putApplicationItem(String key, List<String> applicationItem) {
        if (this.application == null) {
            this.application = null;
        }
        this.application.put(key, applicationItem);
        return this;
    }


    public Map<String, List<String>> getApplication() {
        return application;
    }

    public void setApplication(Map<String, List<String>> application) {
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
        CompositesDTO composites = (CompositesDTO) o;
        return Objects.equals(this.realm, composites.realm) &&
                Objects.equals(this.client, composites.client) &&
                Objects.equals(this.application, composites.application);
    }

    @Override
    public int hashCode() {
        return Objects.hash(realm, client, application);
    }

    @Override
    public String toString() {

        String sb = "class CompositesDTO {\n" +
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

