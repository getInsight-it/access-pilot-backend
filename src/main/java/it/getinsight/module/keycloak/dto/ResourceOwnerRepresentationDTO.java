package it.getinsight.module.keycloak.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Objects;


public class ResourceOwnerRepresentationDTO {
    @JsonProperty("id")
    private String id = null;

    @JsonProperty("name")
    private String name = null;

    public ResourceOwnerRepresentationDTO id(String id) {
        this.id = id;
        return this;
    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public ResourceOwnerRepresentationDTO name(String name) {
        this.name = name;
        return this;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ResourceOwnerRepresentationDTO resourceOwnerRepresentation = (ResourceOwnerRepresentationDTO) o;
        return Objects.equals(this.id, resourceOwnerRepresentation.id) &&
                Objects.equals(this.name, resourceOwnerRepresentation.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name);
    }

    @Override
    public String toString() {

        String sb = "class ResourceOwnerRepresentationDTO {\n" +
                "    id: " + toIndentedString(id) + "\n" +
                "    name: " + toIndentedString(name) + "\n" +
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

