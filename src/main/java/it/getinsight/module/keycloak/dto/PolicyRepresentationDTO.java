package it.getinsight.module.keycloak.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Map;
import java.util.Objects;


public class PolicyRepresentationDTO {
    @JsonProperty("config")
    private Map<String, String> config = null;

    public PolicyRepresentationDTO config(Map<String, String> config) {
        this.config = config;
        return this;
    }

    public PolicyRepresentationDTO putConfigItem(String key, String configItem) {
        if (this.config == null) {
            this.config = null;
        }
        this.config.put(key, configItem);
        return this;
    }


    public Map<String, String> getConfig() {
        return config;
    }

    public void setConfig(Map<String, String> config) {
        this.config = config;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        PolicyRepresentationDTO policyRepresentation = (PolicyRepresentationDTO) o;
        return Objects.equals(this.config, policyRepresentation.config);
    }

    @Override
    public int hashCode() {
        return Objects.hash(config);
    }

    @Override
    public String toString() {

        String sb = "class PolicyRepresentationDTO {\n" +
                "    config: " + toIndentedString(config) + "\n" +
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

