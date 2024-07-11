package it.getinsight.module.keycloak.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Objects;


public class TestLdapConnectionRepresentationDTO {
    @JsonProperty("action")
    private String action = null;

    @JsonProperty("connectionUrl")
    private String connectionUrl = null;

    @JsonProperty("authType")
    private String authType = null;

    @JsonProperty("bindDn")
    private String bindDn = null;

    @JsonProperty("bindCredential")
    private String bindCredential = null;

    @JsonProperty("useTruststoreSpi")
    private String useTruststoreSpi = null;

    @JsonProperty("connectionTimeout")
    private String connectionTimeout = null;

    @JsonProperty("componentId")
    private String componentId = null;

    @JsonProperty("startTls")
    private String startTls = null;

    public TestLdapConnectionRepresentationDTO action(String action) {
        this.action = action;
        return this;
    }


    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public TestLdapConnectionRepresentationDTO connectionUrl(String connectionUrl) {
        this.connectionUrl = connectionUrl;
        return this;
    }


    public String getConnectionUrl() {
        return connectionUrl;
    }

    public void setConnectionUrl(String connectionUrl) {
        this.connectionUrl = connectionUrl;
    }

    public TestLdapConnectionRepresentationDTO authType(String authType) {
        this.authType = authType;
        return this;
    }


    public String getAuthType() {
        return authType;
    }

    public void setAuthType(String authType) {
        this.authType = authType;
    }

    public TestLdapConnectionRepresentationDTO bindDn(String bindDn) {
        this.bindDn = bindDn;
        return this;
    }


    public String getBindDn() {
        return bindDn;
    }

    public void setBindDn(String bindDn) {
        this.bindDn = bindDn;
    }

    public TestLdapConnectionRepresentationDTO bindCredential(String bindCredential) {
        this.bindCredential = bindCredential;
        return this;
    }


    public String getBindCredential() {
        return bindCredential;
    }

    public void setBindCredential(String bindCredential) {
        this.bindCredential = bindCredential;
    }

    public TestLdapConnectionRepresentationDTO useTruststoreSpi(String useTruststoreSpi) {
        this.useTruststoreSpi = useTruststoreSpi;
        return this;
    }


    public String getUseTruststoreSpi() {
        return useTruststoreSpi;
    }

    public void setUseTruststoreSpi(String useTruststoreSpi) {
        this.useTruststoreSpi = useTruststoreSpi;
    }

    public TestLdapConnectionRepresentationDTO connectionTimeout(String connectionTimeout) {
        this.connectionTimeout = connectionTimeout;
        return this;
    }


    public String getConnectionTimeout() {
        return connectionTimeout;
    }

    public void setConnectionTimeout(String connectionTimeout) {
        this.connectionTimeout = connectionTimeout;
    }

    public TestLdapConnectionRepresentationDTO componentId(String componentId) {
        this.componentId = componentId;
        return this;
    }


    public String getComponentId() {
        return componentId;
    }

    public void setComponentId(String componentId) {
        this.componentId = componentId;
    }

    public TestLdapConnectionRepresentationDTO startTls(String startTls) {
        this.startTls = startTls;
        return this;
    }


    public String getStartTls() {
        return startTls;
    }

    public void setStartTls(String startTls) {
        this.startTls = startTls;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        TestLdapConnectionRepresentationDTO testLdapConnectionRepresentation = (TestLdapConnectionRepresentationDTO) o;
        return Objects.equals(this.action, testLdapConnectionRepresentation.action) &&
                Objects.equals(this.connectionUrl, testLdapConnectionRepresentation.connectionUrl) &&
                Objects.equals(this.authType, testLdapConnectionRepresentation.authType) &&
                Objects.equals(this.bindDn, testLdapConnectionRepresentation.bindDn) &&
                Objects.equals(this.bindCredential, testLdapConnectionRepresentation.bindCredential) &&
                Objects.equals(this.useTruststoreSpi, testLdapConnectionRepresentation.useTruststoreSpi) &&
                Objects.equals(this.connectionTimeout, testLdapConnectionRepresentation.connectionTimeout) &&
                Objects.equals(this.componentId, testLdapConnectionRepresentation.componentId) &&
                Objects.equals(this.startTls, testLdapConnectionRepresentation.startTls);
    }

    @Override
    public int hashCode() {
        return Objects.hash(action, connectionUrl, authType, bindDn, bindCredential, useTruststoreSpi, connectionTimeout, componentId, startTls);
    }

    @Override
    public String toString() {

        String sb = "class TestLdapConnectionRepresentationDTO {\n" +
                "    action: " + toIndentedString(action) + "\n" +
                "    connectionUrl: " + toIndentedString(connectionUrl) + "\n" +
                "    authType: " + toIndentedString(authType) + "\n" +
                "    bindDn: " + toIndentedString(bindDn) + "\n" +
                "    bindCredential: " + toIndentedString(bindCredential) + "\n" +
                "    useTruststoreSpi: " + toIndentedString(useTruststoreSpi) + "\n" +
                "    connectionTimeout: " + toIndentedString(connectionTimeout) + "\n" +
                "    componentId: " + toIndentedString(componentId) + "\n" +
                "    startTls: " + toIndentedString(startTls) + "\n" +
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

