package it.getinsight.module.keycloak.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


public class ConfigPropertyRepresentationDTO {
    @JsonProperty("name")
    private String name = null;

    @JsonProperty("label")
    private String label = null;

    @JsonProperty("type")
    private String type = null;

    @JsonProperty("defaultValue")
    private Object defaultValue = null;

    @JsonProperty("helpText")
    private String helpText = null;

    @JsonProperty("options")
    private List<String> options = null;

    @JsonProperty("secret")
    private Boolean secret = null;

    @JsonProperty("readOnly")
    private Boolean readOnly = null;

    public ConfigPropertyRepresentationDTO name(String name) {
        this.name = name;
        return this;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ConfigPropertyRepresentationDTO label(String label) {
        this.label = label;
        return this;
    }


    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public ConfigPropertyRepresentationDTO type(String type) {
        this.type = type;
        return this;
    }


    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public ConfigPropertyRepresentationDTO defaultValue(Object defaultValue) {
        this.defaultValue = defaultValue;
        return this;
    }


    public Object getDefaultValue() {
        return defaultValue;
    }

    public void setDefaultValue(Object defaultValue) {
        this.defaultValue = defaultValue;
    }

    public ConfigPropertyRepresentationDTO helpText(String helpText) {
        this.helpText = helpText;
        return this;
    }


    public String getHelpText() {
        return helpText;
    }

    public void setHelpText(String helpText) {
        this.helpText = helpText;
    }

    public ConfigPropertyRepresentationDTO options(List<String> options) {
        this.options = options;
        return this;
    }

    public ConfigPropertyRepresentationDTO addOptionsItem(String optionsItem) {
        if (this.options == null) {
            this.options = new ArrayList<String>();
        }
        this.options.add(optionsItem);
        return this;
    }


    public List<String> getOptions() {
        return options;
    }

    public void setOptions(List<String> options) {
        this.options = options;
    }

    public ConfigPropertyRepresentationDTO secret(Boolean secret) {
        this.secret = secret;
        return this;
    }


    public Boolean isSecret() {
        return secret;
    }

    public void setSecret(Boolean secret) {
        this.secret = secret;
    }

    public ConfigPropertyRepresentationDTO readOnly(Boolean readOnly) {
        this.readOnly = readOnly;
        return this;
    }


    public Boolean isReadOnly() {
        return readOnly;
    }

    public void setReadOnly(Boolean readOnly) {
        this.readOnly = readOnly;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ConfigPropertyRepresentationDTO configPropertyRepresentation = (ConfigPropertyRepresentationDTO) o;
        return Objects.equals(this.name, configPropertyRepresentation.name) &&
                Objects.equals(this.label, configPropertyRepresentation.label) &&
                Objects.equals(this.type, configPropertyRepresentation.type) &&
                Objects.equals(this.defaultValue, configPropertyRepresentation.defaultValue) &&
                Objects.equals(this.helpText, configPropertyRepresentation.helpText) &&
                Objects.equals(this.options, configPropertyRepresentation.options) &&
                Objects.equals(this.secret, configPropertyRepresentation.secret) &&
                Objects.equals(this.readOnly, configPropertyRepresentation.readOnly);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, label, type, defaultValue, helpText, options, secret, readOnly);
    }

    @Override
    public String toString() {

        String sb = "class ConfigPropertyRepresentationDTO {\n" +
                "    name: " + toIndentedString(name) + "\n" +
                "    label: " + toIndentedString(label) + "\n" +
                "    type: " + toIndentedString(type) + "\n" +
                "    defaultValue: " + toIndentedString(defaultValue) + "\n" +
                "    helpText: " + toIndentedString(helpText) + "\n" +
                "    options: " + toIndentedString(options) + "\n" +
                "    secret: " + toIndentedString(secret) + "\n" +
                "    readOnly: " + toIndentedString(readOnly) + "\n" +
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

