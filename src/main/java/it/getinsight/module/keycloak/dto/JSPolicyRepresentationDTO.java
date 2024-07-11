package it.getinsight.module.keycloak.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Objects;


public class JSPolicyRepresentationDTO {
    @JsonProperty("type")
    private String type = null;

    @JsonProperty("code")
    private String code = null;

    public JSPolicyRepresentationDTO type(String type) {
        this.type = type;
        return this;
    }


    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public JSPolicyRepresentationDTO code(String code) {
        this.code = code;
        return this;
    }


    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        JSPolicyRepresentationDTO jsPolicyRepresentation = (JSPolicyRepresentationDTO) o;
        return Objects.equals(this.type, jsPolicyRepresentation.type) &&
                Objects.equals(this.code, jsPolicyRepresentation.code);
    }

    @Override
    public int hashCode() {
        return Objects.hash(type, code);
    }

    @Override
    public String toString() {

        String sb = "class JSPolicyRepresentationDTO {\n" +
                "    type: " + toIndentedString(type) + "\n" +
                "    code: " + toIndentedString(code) + "\n" +
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

