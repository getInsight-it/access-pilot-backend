package it.getinsight.module.keycloak.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Objects;


public class PasswordPolicyTypeRepresentationDTO {
    @JsonProperty("id")
    private String id = null;

    @JsonProperty("displayName")
    private String displayName = null;

    @JsonProperty("configType")
    private String configType = null;

    @JsonProperty("defaultValue")
    private String defaultValue = null;

    @JsonProperty("multipleSupported")
    private Boolean multipleSupported = null;

    public PasswordPolicyTypeRepresentationDTO id(String id) {
        this.id = id;
        return this;
    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public PasswordPolicyTypeRepresentationDTO displayName(String displayName) {
        this.displayName = displayName;
        return this;
    }


    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public PasswordPolicyTypeRepresentationDTO configType(String configType) {
        this.configType = configType;
        return this;
    }


    public String getConfigType() {
        return configType;
    }

    public void setConfigType(String configType) {
        this.configType = configType;
    }

    public PasswordPolicyTypeRepresentationDTO defaultValue(String defaultValue) {
        this.defaultValue = defaultValue;
        return this;
    }


    public String getDefaultValue() {
        return defaultValue;
    }

    public void setDefaultValue(String defaultValue) {
        this.defaultValue = defaultValue;
    }

    public PasswordPolicyTypeRepresentationDTO multipleSupported(Boolean multipleSupported) {
        this.multipleSupported = multipleSupported;
        return this;
    }


    public Boolean isMultipleSupported() {
        return multipleSupported;
    }

    public void setMultipleSupported(Boolean multipleSupported) {
        this.multipleSupported = multipleSupported;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        PasswordPolicyTypeRepresentationDTO passwordPolicyTypeRepresentation = (PasswordPolicyTypeRepresentationDTO) o;
        return Objects.equals(this.id, passwordPolicyTypeRepresentation.id) &&
                Objects.equals(this.displayName, passwordPolicyTypeRepresentation.displayName) &&
                Objects.equals(this.configType, passwordPolicyTypeRepresentation.configType) &&
                Objects.equals(this.defaultValue, passwordPolicyTypeRepresentation.defaultValue) &&
                Objects.equals(this.multipleSupported, passwordPolicyTypeRepresentation.multipleSupported);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, displayName, configType, defaultValue, multipleSupported);
    }

    @Override
    public String toString() {

        String sb = "class PasswordPolicyTypeRepresentationDTO {\n" +
                "    id: " + toIndentedString(id) + "\n" +
                "    displayName: " + toIndentedString(displayName) + "\n" +
                "    configType: " + toIndentedString(configType) + "\n" +
                "    defaultValue: " + toIndentedString(defaultValue) + "\n" +
                "    multipleSupported: " + toIndentedString(multipleSupported) + "\n" +
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

