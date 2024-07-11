package it.getinsight.module.keycloak.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Objects;


public class ManagementPermissionRepresentationDTO {
    @JsonProperty("enabled")
    private Boolean enabled = null;

    public ManagementPermissionRepresentationDTO enabled(Boolean enabled) {
        this.enabled = enabled;
        return this;
    }


    public Boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(Boolean enabled) {
        this.enabled = enabled;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ManagementPermissionRepresentationDTO managementPermissionRepresentation = (ManagementPermissionRepresentationDTO) o;
        return Objects.equals(this.enabled, managementPermissionRepresentation.enabled);
    }

    @Override
    public int hashCode() {
        return Objects.hash(enabled);
    }

    @Override
    public String toString() {

        String sb = "class ManagementPermissionRepresentationDTO {\n" +
                "    enabled: " + toIndentedString(enabled) + "\n" +
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

