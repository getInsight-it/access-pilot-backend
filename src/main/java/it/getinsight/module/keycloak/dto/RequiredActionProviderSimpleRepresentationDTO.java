package it.getinsight.module.keycloak.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Objects;


public class RequiredActionProviderSimpleRepresentationDTO {
    @JsonProperty("id")
    private String id = null;

    @JsonProperty("name")
    private String name = null;

    @JsonProperty("providerId")
    private String providerId = null;

    public RequiredActionProviderSimpleRepresentationDTO id(String id) {
        this.id = id;
        return this;
    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public RequiredActionProviderSimpleRepresentationDTO name(String name) {
        this.name = name;
        return this;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public RequiredActionProviderSimpleRepresentationDTO providerId(String providerId) {
        this.providerId = providerId;
        return this;
    }


    public String getProviderId() {
        return providerId;
    }

    public void setProviderId(String providerId) {
        this.providerId = providerId;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        RequiredActionProviderSimpleRepresentationDTO requiredActionProviderSimpleRepresentation = (RequiredActionProviderSimpleRepresentationDTO) o;
        return Objects.equals(this.id, requiredActionProviderSimpleRepresentation.id) &&
                Objects.equals(this.name, requiredActionProviderSimpleRepresentation.name) &&
                Objects.equals(this.providerId, requiredActionProviderSimpleRepresentation.providerId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, providerId);
    }

    @Override
    public String toString() {

        String sb = "class RequiredActionProviderSimpleRepresentationDTO {\n" +
                "    id: " + toIndentedString(id) + "\n" +
                "    name: " + toIndentedString(name) + "\n" +
                "    providerId: " + toIndentedString(providerId) + "\n" +
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

