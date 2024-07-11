package it.getinsight.module.keycloak.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Objects;


public class OAuth2ErrorRepresentationDTO {
    @JsonProperty("error")
    private String error = null;

    @JsonProperty("errorDescription")
    private String errorDescription = null;

    public OAuth2ErrorRepresentationDTO error(String error) {
        this.error = error;
        return this;
    }


    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public OAuth2ErrorRepresentationDTO errorDescription(String errorDescription) {
        this.errorDescription = errorDescription;
        return this;
    }


    public String getErrorDescription() {
        return errorDescription;
    }

    public void setErrorDescription(String errorDescription) {
        this.errorDescription = errorDescription;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        OAuth2ErrorRepresentationDTO oauth2ErrorRepresentation = (OAuth2ErrorRepresentationDTO) o;
        return Objects.equals(this.error, oauth2ErrorRepresentation.error) &&
                Objects.equals(this.errorDescription, oauth2ErrorRepresentation.errorDescription);
    }

    @Override
    public int hashCode() {
        return Objects.hash(error, errorDescription);
    }

    @Override
    public String toString() {

        String sb = "class OAuth2ErrorRepresentationDTO {\n" +
                "    error: " + toIndentedString(error) + "\n" +
                "    errorDescription: " + toIndentedString(errorDescription) + "\n" +
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

