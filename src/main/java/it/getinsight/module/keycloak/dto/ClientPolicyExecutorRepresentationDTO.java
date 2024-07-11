package it.getinsight.module.keycloak.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Objects;


public class ClientPolicyExecutorRepresentationDTO {
    @JsonProperty("executorProviderId")
    private String executorProviderId = null;

    @JsonProperty("configuracao")
    private Object _configuration = null;

    public ClientPolicyExecutorRepresentationDTO executorProviderId(String executorProviderId) {
        this.executorProviderId = executorProviderId;
        return this;
    }


    public String getExecutorProviderId() {
        return executorProviderId;
    }

    public void setExecutorProviderId(String executorProviderId) {
        this.executorProviderId = executorProviderId;
    }

    public ClientPolicyExecutorRepresentationDTO _configuration(Object _configuration) {
        this._configuration = _configuration;
        return this;
    }


    public Object getConfiguration() {
        return _configuration;
    }

    public void setConfiguration(Object _configuration) {
        this._configuration = _configuration;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ClientPolicyExecutorRepresentationDTO clientPolicyExecutorRepresentation = (ClientPolicyExecutorRepresentationDTO) o;
        return Objects.equals(this.executorProviderId, clientPolicyExecutorRepresentation.executorProviderId) &&
                Objects.equals(this._configuration, clientPolicyExecutorRepresentation._configuration);
    }

    @Override
    public int hashCode() {
        return Objects.hash(executorProviderId, _configuration);
    }

    @Override
    public String toString() {

        String sb = "class ClientPolicyExecutorRepresentationDTO {\n" +
                "    executorProviderId: " + toIndentedString(executorProviderId) + "\n" +
                "    _configuration: " + toIndentedString(_configuration) + "\n" +
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

