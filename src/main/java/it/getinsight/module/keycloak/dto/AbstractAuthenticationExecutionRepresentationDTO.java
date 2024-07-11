package it.getinsight.module.keycloak.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Objects;


public class AbstractAuthenticationExecutionRepresentationDTO {
    @JsonProperty("authenticatorConfig")
    private String authenticatorConfig = null;

    @JsonProperty("authenticator")
    private String authenticator = null;

    @JsonProperty("requirement")
    private String requirement = null;

    @JsonProperty("priority")
    private Integer priority = null;

    @JsonProperty("autheticatorFlow")
    private Boolean autheticatorFlow = null;

    @JsonProperty("authenticatorFlow")
    private Boolean authenticatorFlow = null;

    public AbstractAuthenticationExecutionRepresentationDTO authenticatorConfig(String authenticatorConfig) {
        this.authenticatorConfig = authenticatorConfig;
        return this;
    }


    public String getAuthenticatorConfig() {
        return authenticatorConfig;
    }

    public void setAuthenticatorConfig(String authenticatorConfig) {
        this.authenticatorConfig = authenticatorConfig;
    }

    public AbstractAuthenticationExecutionRepresentationDTO authenticator(String authenticator) {
        this.authenticator = authenticator;
        return this;
    }


    public String getAuthenticator() {
        return authenticator;
    }

    public void setAuthenticator(String authenticator) {
        this.authenticator = authenticator;
    }

    public AbstractAuthenticationExecutionRepresentationDTO requirement(String requirement) {
        this.requirement = requirement;
        return this;
    }


    public String getRequirement() {
        return requirement;
    }

    public void setRequirement(String requirement) {
        this.requirement = requirement;
    }

    public AbstractAuthenticationExecutionRepresentationDTO priority(Integer priority) {
        this.priority = priority;
        return this;
    }


    public Integer getPriority() {
        return priority;
    }

    public void setPriority(Integer priority) {
        this.priority = priority;
    }

    public AbstractAuthenticationExecutionRepresentationDTO autheticatorFlow(Boolean autheticatorFlow) {
        this.autheticatorFlow = autheticatorFlow;
        return this;
    }


    public Boolean isAutheticatorFlow() {
        return autheticatorFlow;
    }

    public void setAutheticatorFlow(Boolean autheticatorFlow) {
        this.autheticatorFlow = autheticatorFlow;
    }

    public AbstractAuthenticationExecutionRepresentationDTO authenticatorFlow(Boolean authenticatorFlow) {
        this.authenticatorFlow = authenticatorFlow;
        return this;
    }


    public Boolean isAuthenticatorFlow() {
        return authenticatorFlow;
    }

    public void setAuthenticatorFlow(Boolean authenticatorFlow) {
        this.authenticatorFlow = authenticatorFlow;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        AbstractAuthenticationExecutionRepresentationDTO abstractAuthenticationExecutionRepresentation = (AbstractAuthenticationExecutionRepresentationDTO) o;
        return Objects.equals(this.authenticatorConfig, abstractAuthenticationExecutionRepresentation.authenticatorConfig) &&
                Objects.equals(this.authenticator, abstractAuthenticationExecutionRepresentation.authenticator) &&
                Objects.equals(this.requirement, abstractAuthenticationExecutionRepresentation.requirement) &&
                Objects.equals(this.priority, abstractAuthenticationExecutionRepresentation.priority) &&
                Objects.equals(this.autheticatorFlow, abstractAuthenticationExecutionRepresentation.autheticatorFlow) &&
                Objects.equals(this.authenticatorFlow, abstractAuthenticationExecutionRepresentation.authenticatorFlow);
    }

    @Override
    public int hashCode() {
        return Objects.hash(authenticatorConfig, authenticator, requirement, priority, autheticatorFlow, authenticatorFlow);
    }

    @Override
    public String toString() {

        String sb = "class AbstractAuthenticationExecutionRepresentationDTO {\n" +
                "    authenticatorConfig: " + toIndentedString(authenticatorConfig) + "\n" +
                "    authenticator: " + toIndentedString(authenticator) + "\n" +
                "    requirement: " + toIndentedString(requirement) + "\n" +
                "    priority: " + toIndentedString(priority) + "\n" +
                "    autheticatorFlow: " + toIndentedString(autheticatorFlow) + "\n" +
                "    authenticatorFlow: " + toIndentedString(authenticatorFlow) + "\n" +
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

