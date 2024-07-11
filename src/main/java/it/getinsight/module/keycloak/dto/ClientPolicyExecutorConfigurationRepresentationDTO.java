package it.getinsight.module.keycloak.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Map;
import java.util.Objects;


public class ClientPolicyExecutorConfigurationRepresentationDTO {
    @JsonProperty("configAsMap")
    private Map<String, Object> configAsMap = null;

    public ClientPolicyExecutorConfigurationRepresentationDTO configAsMap(Map<String, Object> configAsMap) {
        this.configAsMap = configAsMap;
        return this;
    }

    public ClientPolicyExecutorConfigurationRepresentationDTO putConfigAsMapItem(String key, Object configAsMapItem) {
        if (this.configAsMap == null) {
            this.configAsMap = null;
        }
        this.configAsMap.put(key, configAsMapItem);
        return this;
    }


    public Map<String, Object> getConfigAsMap() {
        return configAsMap;
    }

    public void setConfigAsMap(Map<String, Object> configAsMap) {
        this.configAsMap = configAsMap;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ClientPolicyExecutorConfigurationRepresentationDTO clientPolicyExecutorConfigurationRepresentation = (ClientPolicyExecutorConfigurationRepresentationDTO) o;
        return Objects.equals(this.configAsMap, clientPolicyExecutorConfigurationRepresentation.configAsMap);
    }

    @Override
    public int hashCode() {
        return Objects.hash(configAsMap);
    }

    @Override
    public String toString() {

        String sb = "class ClientPolicyExecutorConfigurationRepresentationDTO {\n" +
                "    configAsMap: " + toIndentedString(configAsMap) + "\n" +
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

