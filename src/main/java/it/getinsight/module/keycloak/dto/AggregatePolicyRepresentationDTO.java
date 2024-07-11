package it.getinsight.module.keycloak.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Objects;


public class AggregatePolicyRepresentationDTO {
    @JsonProperty("type")
    private String type = null;

    public AggregatePolicyRepresentationDTO type(String type) {
        this.type = type;
        return this;
    }


    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        AggregatePolicyRepresentationDTO aggregatePolicyRepresentation = (AggregatePolicyRepresentationDTO) o;
        return Objects.equals(this.type, aggregatePolicyRepresentation.type);
    }

    @Override
    public int hashCode() {
        return Objects.hash(type);
    }

    @Override
    public String toString() {

        String sb = "class AggregatePolicyRepresentationDTO {\n" +
                "    type: " + toIndentedString(type) + "\n" +
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

