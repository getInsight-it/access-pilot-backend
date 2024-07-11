package it.getinsight.module.keycloak.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Objects;


public class RoleDefinitionDTO {
    @JsonProperty("id")
    private String id = null;

    @JsonProperty("required")
    private Boolean required = null;

    public RoleDefinitionDTO id(String id) {
        this.id = id;
        return this;
    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public RoleDefinitionDTO required(Boolean required) {
        this.required = required;
        return this;
    }


    public Boolean isRequired() {
        return required;
    }

    public void setRequired(Boolean required) {
        this.required = required;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        RoleDefinitionDTO roleDefinition = (RoleDefinitionDTO) o;
        return Objects.equals(this.id, roleDefinition.id) &&
                Objects.equals(this.required, roleDefinition.required);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, required);
    }

    @Override
    public String toString() {

        String sb = "class RoleDefinitionDTO {\n" +
                "    id: " + toIndentedString(id) + "\n" +
                "    required: " + toIndentedString(required) + "\n" +
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

