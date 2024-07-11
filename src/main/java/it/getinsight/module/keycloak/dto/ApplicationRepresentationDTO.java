package it.getinsight.module.keycloak.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Objects;


public class ApplicationRepresentationDTO {
    @JsonProperty("name")
    private String name = null;

    @JsonProperty("claims")
    private ClaimRepresentationDTO claims = null;

    public ApplicationRepresentationDTO name(String name) {
        this.name = name;
        return this;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ApplicationRepresentationDTO claims(ClaimRepresentationDTO claims) {
        this.claims = claims;
        return this;
    }


    public ClaimRepresentationDTO getClaims() {
        return claims;
    }

    public void setClaims(ClaimRepresentationDTO claims) {
        this.claims = claims;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ApplicationRepresentationDTO applicationRepresentation = (ApplicationRepresentationDTO) o;
        return Objects.equals(this.name, applicationRepresentation.name) &&
                Objects.equals(this.claims, applicationRepresentation.claims);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, claims);
    }

    @Override
    public String toString() {

        String sb = "class ApplicationRepresentationDTO {\n" +
                "    name: " + toIndentedString(name) + "\n" +
                "    claims: " + toIndentedString(claims) + "\n" +
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

