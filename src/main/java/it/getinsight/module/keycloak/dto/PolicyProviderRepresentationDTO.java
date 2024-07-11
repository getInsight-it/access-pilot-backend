package it.getinsight.module.keycloak.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Objects;


public class PolicyProviderRepresentationDTO {
    @JsonProperty("type")
    private String type = null;

    @JsonProperty("name")
    private String name = null;

    @JsonProperty("group")
    private String group = null;

    public PolicyProviderRepresentationDTO type(String type) {
        this.type = type;
        return this;
    }


    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public PolicyProviderRepresentationDTO name(String name) {
        this.name = name;
        return this;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public PolicyProviderRepresentationDTO group(String group) {
        this.group = group;
        return this;
    }


    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        PolicyProviderRepresentationDTO policyProviderRepresentation = (PolicyProviderRepresentationDTO) o;
        return Objects.equals(this.type, policyProviderRepresentation.type) &&
                Objects.equals(this.name, policyProviderRepresentation.name) &&
                Objects.equals(this.group, policyProviderRepresentation.group);
    }

    @Override
    public int hashCode() {
        return Objects.hash(type, name, group);
    }

    @Override
    public String toString() {

        String sb = "class PolicyProviderRepresentationDTO {\n" +
                "    type: " + toIndentedString(type) + "\n" +
                "    name: " + toIndentedString(name) + "\n" +
                "    group: " + toIndentedString(group) + "\n" +
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

