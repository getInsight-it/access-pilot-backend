package it.getinsight.module.keycloak.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Objects;


public class ClientPolicyConditionRepresentationDTO {
    @JsonProperty("conditionProviderId")
    private String conditionProviderId = null;

    @JsonProperty("configuracao")
    private Object _configuration = null;

    public ClientPolicyConditionRepresentationDTO conditionProviderId(String conditionProviderId) {
        this.conditionProviderId = conditionProviderId;
        return this;
    }


    public String getConditionProviderId() {
        return conditionProviderId;
    }

    public void setConditionProviderId(String conditionProviderId) {
        this.conditionProviderId = conditionProviderId;
    }

    public ClientPolicyConditionRepresentationDTO _configuration(Object _configuration) {
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
        ClientPolicyConditionRepresentationDTO clientPolicyConditionRepresentation = (ClientPolicyConditionRepresentationDTO) o;
        return Objects.equals(this.conditionProviderId, clientPolicyConditionRepresentation.conditionProviderId) &&
                Objects.equals(this._configuration, clientPolicyConditionRepresentation._configuration);
    }

    @Override
    public int hashCode() {
        return Objects.hash(conditionProviderId, _configuration);
    }

    @Override
    public String toString() {

        String sb = "class ClientPolicyConditionRepresentationDTO {\n" +
                "    conditionProviderId: " + toIndentedString(conditionProviderId) + "\n" +
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

