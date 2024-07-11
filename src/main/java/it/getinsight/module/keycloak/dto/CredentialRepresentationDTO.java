package it.getinsight.module.keycloak.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Map;
import java.util.Objects;


public class CredentialRepresentationDTO {
    @JsonProperty("id")
    private String id = null;

    @JsonProperty("type")
    private String type = null;

    @JsonProperty("userLabel")
    private String userLabel = null;

    @JsonProperty("secretData")
    private String secretData = null;

    @JsonProperty("credentialData")
    private String credentialData = null;

    @JsonProperty("priority")
    private Integer priority = null;

    @JsonProperty("createdDate")
    private Long createdDate = null;

    @JsonProperty("value")
    private String value = null;

    @JsonProperty("temporary")
    private Boolean temporary = null;

    @JsonProperty("device")
    private String device = null;

    @JsonProperty("hashedSaltedValue")
    private String hashedSaltedValue = null;

    @JsonProperty("salt")
    private String salt = null;

    @JsonProperty("hashIterations")
    private Integer hashIterations = null;

    @JsonProperty("counter")
    private Integer counter = null;

    @JsonProperty("algorithm")
    private String algorithm = null;

    @JsonProperty("digits")
    private Integer digits = null;

    @JsonProperty("period")
    private Integer period = null;

    @JsonProperty("config")
    private Map<String, String> config = null;

    public CredentialRepresentationDTO id(String id) {
        this.id = id;
        return this;
    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public CredentialRepresentationDTO type(String type) {
        this.type = type;
        return this;
    }


    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public CredentialRepresentationDTO userLabel(String userLabel) {
        this.userLabel = userLabel;
        return this;
    }


    public String getUserLabel() {
        return userLabel;
    }

    public void setUserLabel(String userLabel) {
        this.userLabel = userLabel;
    }

    public CredentialRepresentationDTO secretData(String secretData) {
        this.secretData = secretData;
        return this;
    }


    public String getSecretData() {
        return secretData;
    }

    public void setSecretData(String secretData) {
        this.secretData = secretData;
    }

    public CredentialRepresentationDTO credentialData(String credentialData) {
        this.credentialData = credentialData;
        return this;
    }


    public String getCredentialData() {
        return credentialData;
    }

    public void setCredentialData(String credentialData) {
        this.credentialData = credentialData;
    }

    public CredentialRepresentationDTO priority(Integer priority) {
        this.priority = priority;
        return this;
    }


    public Integer getPriority() {
        return priority;
    }

    public void setPriority(Integer priority) {
        this.priority = priority;
    }

    public CredentialRepresentationDTO createdDate(Long createdDate) {
        this.createdDate = createdDate;
        return this;
    }


    public Long getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Long createdDate) {
        this.createdDate = createdDate;
    }

    public CredentialRepresentationDTO value(String value) {
        this.value = value;
        return this;
    }


    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public CredentialRepresentationDTO temporary(Boolean temporary) {
        this.temporary = temporary;
        return this;
    }


    public Boolean isTemporary() {
        return temporary;
    }

    public void setTemporary(Boolean temporary) {
        this.temporary = temporary;
    }

    public CredentialRepresentationDTO device(String device) {
        this.device = device;
        return this;
    }


    public String getDevice() {
        return device;
    }

    public void setDevice(String device) {
        this.device = device;
    }

    public CredentialRepresentationDTO hashedSaltedValue(String hashedSaltedValue) {
        this.hashedSaltedValue = hashedSaltedValue;
        return this;
    }


    public String getHashedSaltedValue() {
        return hashedSaltedValue;
    }

    public void setHashedSaltedValue(String hashedSaltedValue) {
        this.hashedSaltedValue = hashedSaltedValue;
    }

    public CredentialRepresentationDTO salt(String salt) {
        this.salt = salt;
        return this;
    }


    public String getSalt() {
        return salt;
    }

    public void setSalt(String salt) {
        this.salt = salt;
    }

    public CredentialRepresentationDTO hashIterations(Integer hashIterations) {
        this.hashIterations = hashIterations;
        return this;
    }


    public Integer getHashIterations() {
        return hashIterations;
    }

    public void setHashIterations(Integer hashIterations) {
        this.hashIterations = hashIterations;
    }

    public CredentialRepresentationDTO counter(Integer counter) {
        this.counter = counter;
        return this;
    }


    public Integer getCounter() {
        return counter;
    }

    public void setCounter(Integer counter) {
        this.counter = counter;
    }

    public CredentialRepresentationDTO algorithm(String algorithm) {
        this.algorithm = algorithm;
        return this;
    }


    public String getAlgorithm() {
        return algorithm;
    }

    public void setAlgorithm(String algorithm) {
        this.algorithm = algorithm;
    }

    public CredentialRepresentationDTO digits(Integer digits) {
        this.digits = digits;
        return this;
    }


    public Integer getDigits() {
        return digits;
    }

    public void setDigits(Integer digits) {
        this.digits = digits;
    }

    public CredentialRepresentationDTO period(Integer period) {
        this.period = period;
        return this;
    }


    public Integer getPeriod() {
        return period;
    }

    public void setPeriod(Integer period) {
        this.period = period;
    }

    public CredentialRepresentationDTO config(Map<String, String> config) {
        this.config = config;
        return this;
    }

    public CredentialRepresentationDTO putConfigItem(String key, String configItem) {
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
        CredentialRepresentationDTO credentialRepresentation = (CredentialRepresentationDTO) o;
        return Objects.equals(this.id, credentialRepresentation.id) &&
                Objects.equals(this.type, credentialRepresentation.type) &&
                Objects.equals(this.userLabel, credentialRepresentation.userLabel) &&
                Objects.equals(this.secretData, credentialRepresentation.secretData) &&
                Objects.equals(this.credentialData, credentialRepresentation.credentialData) &&
                Objects.equals(this.priority, credentialRepresentation.priority) &&
                Objects.equals(this.createdDate, credentialRepresentation.createdDate) &&
                Objects.equals(this.value, credentialRepresentation.value) &&
                Objects.equals(this.temporary, credentialRepresentation.temporary) &&
                Objects.equals(this.device, credentialRepresentation.device) &&
                Objects.equals(this.hashedSaltedValue, credentialRepresentation.hashedSaltedValue) &&
                Objects.equals(this.salt, credentialRepresentation.salt) &&
                Objects.equals(this.hashIterations, credentialRepresentation.hashIterations) &&
                Objects.equals(this.counter, credentialRepresentation.counter) &&
                Objects.equals(this.algorithm, credentialRepresentation.algorithm) &&
                Objects.equals(this.digits, credentialRepresentation.digits) &&
                Objects.equals(this.period, credentialRepresentation.period) &&
                Objects.equals(this.config, credentialRepresentation.config);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, type, userLabel, secretData, credentialData, priority, createdDate, value, temporary, device, hashedSaltedValue, salt, hashIterations, counter, algorithm, digits, period, config);
    }

    @Override
    public String toString() {

        String sb = "class CredentialRepresentationDTO {\n" +
                "    id: " + toIndentedString(id) + "\n" +
                "    type: " + toIndentedString(type) + "\n" +
                "    userLabel: " + toIndentedString(userLabel) + "\n" +
                "    secretData: " + toIndentedString(secretData) + "\n" +
                "    credentialData: " + toIndentedString(credentialData) + "\n" +
                "    priority: " + toIndentedString(priority) + "\n" +
                "    createdDate: " + toIndentedString(createdDate) + "\n" +
                "    value: " + toIndentedString(value) + "\n" +
                "    temporary: " + toIndentedString(temporary) + "\n" +
                "    device: " + toIndentedString(device) + "\n" +
                "    hashedSaltedValue: " + toIndentedString(hashedSaltedValue) + "\n" +
                "    salt: " + toIndentedString(salt) + "\n" +
                "    hashIterations: " + toIndentedString(hashIterations) + "\n" +
                "    counter: " + toIndentedString(counter) + "\n" +
                "    algorithm: " + toIndentedString(algorithm) + "\n" +
                "    digits: " + toIndentedString(digits) + "\n" +
                "    period: " + toIndentedString(period) + "\n" +
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

