package it.getinsight.module.keycloak.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Objects;


public class AuthenticationExecutionExportRepresentationDTO {
    @JsonProperty("userSetupAllowed")
    private Boolean userSetupAllowed = null;

    @JsonProperty("flowAlias")
    private String flowAlias = null;

    public AuthenticationExecutionExportRepresentationDTO userSetupAllowed(Boolean userSetupAllowed) {
        this.userSetupAllowed = userSetupAllowed;
        return this;
    }


    public Boolean isUserSetupAllowed() {
        return userSetupAllowed;
    }

    public void setUserSetupAllowed(Boolean userSetupAllowed) {
        this.userSetupAllowed = userSetupAllowed;
    }

    public AuthenticationExecutionExportRepresentationDTO flowAlias(String flowAlias) {
        this.flowAlias = flowAlias;
        return this;
    }


    public String getFlowAlias() {
        return flowAlias;
    }

    public void setFlowAlias(String flowAlias) {
        this.flowAlias = flowAlias;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        AuthenticationExecutionExportRepresentationDTO authenticationExecutionExportRepresentation = (AuthenticationExecutionExportRepresentationDTO) o;
        return Objects.equals(this.userSetupAllowed, authenticationExecutionExportRepresentation.userSetupAllowed) &&
                Objects.equals(this.flowAlias, authenticationExecutionExportRepresentation.flowAlias);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userSetupAllowed, flowAlias);
    }

    @Override
    public String toString() {

        String sb = "class AuthenticationExecutionExportRepresentationDTO {\n" +
                "    userSetupAllowed: " + toIndentedString(userSetupAllowed) + "\n" +
                "    flowAlias: " + toIndentedString(flowAlias) + "\n" +
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

