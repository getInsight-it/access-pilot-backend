package it.getinsight.module.keycloak.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Objects;


public class LDAPCapabilityRepresentationDTO {
    @JsonProperty("oid")
    private String oid = null;

    @JsonProperty("type")
    private Object type = null;

    public LDAPCapabilityRepresentationDTO oid(String oid) {
        this.oid = oid;
        return this;
    }


    public String getOid() {
        return oid;
    }

    public void setOid(String oid) {
        this.oid = oid;
    }

    public LDAPCapabilityRepresentationDTO type(Object type) {
        this.type = type;
        return this;
    }


    public Object getType() {
        return type;
    }

    public void setType(Object type) {
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
        LDAPCapabilityRepresentationDTO ldAPCapabilityRepresentation = (LDAPCapabilityRepresentationDTO) o;
        return Objects.equals(this.oid, ldAPCapabilityRepresentation.oid) &&
                Objects.equals(this.type, ldAPCapabilityRepresentation.type);
    }

    @Override
    public int hashCode() {
        return Objects.hash(oid, type);
    }

    @Override
    public String toString() {

        String sb = "class LDAPCapabilityRepresentationDTO {\n" +
                "    oid: " + toIndentedString(oid) + "\n" +
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

