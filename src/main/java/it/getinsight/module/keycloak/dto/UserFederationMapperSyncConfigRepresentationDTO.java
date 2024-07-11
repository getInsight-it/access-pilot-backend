package it.getinsight.module.keycloak.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Objects;


public class UserFederationMapperSyncConfigRepresentationDTO {
    @JsonProperty("fedToKeycloakSyncSupported")
    private Boolean fedToKeycloakSyncSupported = null;

    @JsonProperty("fedToKeycloakSyncMessage")
    private String fedToKeycloakSyncMessage = null;

    @JsonProperty("keycloakToFedSyncSupported")
    private Boolean keycloakToFedSyncSupported = null;

    @JsonProperty("keycloakToFedSyncMessage")
    private String keycloakToFedSyncMessage = null;

    public UserFederationMapperSyncConfigRepresentationDTO fedToKeycloakSyncSupported(Boolean fedToKeycloakSyncSupported) {
        this.fedToKeycloakSyncSupported = fedToKeycloakSyncSupported;
        return this;
    }


    public Boolean isFedToKeycloakSyncSupported() {
        return fedToKeycloakSyncSupported;
    }

    public void setFedToKeycloakSyncSupported(Boolean fedToKeycloakSyncSupported) {
        this.fedToKeycloakSyncSupported = fedToKeycloakSyncSupported;
    }

    public UserFederationMapperSyncConfigRepresentationDTO fedToKeycloakSyncMessage(String fedToKeycloakSyncMessage) {
        this.fedToKeycloakSyncMessage = fedToKeycloakSyncMessage;
        return this;
    }


    public String getFedToKeycloakSyncMessage() {
        return fedToKeycloakSyncMessage;
    }

    public void setFedToKeycloakSyncMessage(String fedToKeycloakSyncMessage) {
        this.fedToKeycloakSyncMessage = fedToKeycloakSyncMessage;
    }

    public UserFederationMapperSyncConfigRepresentationDTO keycloakToFedSyncSupported(Boolean keycloakToFedSyncSupported) {
        this.keycloakToFedSyncSupported = keycloakToFedSyncSupported;
        return this;
    }


    public Boolean isKeycloakToFedSyncSupported() {
        return keycloakToFedSyncSupported;
    }

    public void setKeycloakToFedSyncSupported(Boolean keycloakToFedSyncSupported) {
        this.keycloakToFedSyncSupported = keycloakToFedSyncSupported;
    }

    public UserFederationMapperSyncConfigRepresentationDTO keycloakToFedSyncMessage(String keycloakToFedSyncMessage) {
        this.keycloakToFedSyncMessage = keycloakToFedSyncMessage;
        return this;
    }


    public String getKeycloakToFedSyncMessage() {
        return keycloakToFedSyncMessage;
    }

    public void setKeycloakToFedSyncMessage(String keycloakToFedSyncMessage) {
        this.keycloakToFedSyncMessage = keycloakToFedSyncMessage;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        UserFederationMapperSyncConfigRepresentationDTO userFederationMapperSyncConfigRepresentation = (UserFederationMapperSyncConfigRepresentationDTO) o;
        return Objects.equals(this.fedToKeycloakSyncSupported, userFederationMapperSyncConfigRepresentation.fedToKeycloakSyncSupported) &&
                Objects.equals(this.fedToKeycloakSyncMessage, userFederationMapperSyncConfigRepresentation.fedToKeycloakSyncMessage) &&
                Objects.equals(this.keycloakToFedSyncSupported, userFederationMapperSyncConfigRepresentation.keycloakToFedSyncSupported) &&
                Objects.equals(this.keycloakToFedSyncMessage, userFederationMapperSyncConfigRepresentation.keycloakToFedSyncMessage);
    }

    @Override
    public int hashCode() {
        return Objects.hash(fedToKeycloakSyncSupported, fedToKeycloakSyncMessage, keycloakToFedSyncSupported, keycloakToFedSyncMessage);
    }

    @Override
    public String toString() {

        String sb = "class UserFederationMapperSyncConfigRepresentationDTO {\n" +
                "    fedToKeycloakSyncSupported: " + toIndentedString(fedToKeycloakSyncSupported) + "\n" +
                "    fedToKeycloakSyncMessage: " + toIndentedString(fedToKeycloakSyncMessage) + "\n" +
                "    keycloakToFedSyncSupported: " + toIndentedString(keycloakToFedSyncSupported) + "\n" +
                "    keycloakToFedSyncMessage: " + toIndentedString(keycloakToFedSyncMessage) + "\n" +
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

