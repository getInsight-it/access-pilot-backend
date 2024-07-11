package it.getinsight.module.keycloak.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Map;
import java.util.Objects;


public class ClientPolicyConditionConfigurationRepresentationDTO {
    @JsonProperty("negativeLogic")
    private Boolean negativeLogic = null;

    @JsonProperty("configAsMap")
    private Map<String, Object> configAsMap = null;

    public ClientPolicyConditionConfigurationRepresentationDTO negativeLogic(Boolean negativeLogic) {
        this.negativeLogic = negativeLogic;
        return this;
    }


    public Boolean isNegativeLogic() {
        return negativeLogic;
    }

    public void setNegativeLogic(Boolean negativeLogic) {
        this.negativeLogic = negativeLogic;
    }

    public ClientPolicyConditionConfigurationRepresentationDTO configAsMap(Map<String, Object> configAsMap) {
        this.configAsMap = configAsMap;
        return this;
    }

    public ClientPolicyConditionConfigurationRepresentationDTO putConfigAsMapItem(String key, Object configAsMapItem) {
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
        ClientPolicyConditionConfigurationRepresentationDTO clientPolicyConditionConfigurationRepresentation = (ClientPolicyConditionConfigurationRepresentationDTO) o;
        return Objects.equals(this.negativeLogic, clientPolicyConditionConfigurationRepresentation.negativeLogic) &&
                Objects.equals(this.configAsMap, clientPolicyConditionConfigurationRepresentation.configAsMap);
    }

    @Override
    public int hashCode() {
        return Objects.hash(negativeLogic, configAsMap);
    }

    @Override
    public String toString() {

        String sb = "class ClientPolicyConditionConfigurationRepresentationDTO {\n" +
                "    negativeLogic: " + toIndentedString(negativeLogic) + "\n" +
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

