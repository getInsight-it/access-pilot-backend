package it.getinsight.module.keycloak.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Objects;


public class VersionRepresentationDTO {
    @JsonProperty("version")
    private String version = null;

    @JsonProperty("buildTime")
    private String buildTime = null;

    public VersionRepresentationDTO version(String version) {
        this.version = version;
        return this;
    }


    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public VersionRepresentationDTO buildTime(String buildTime) {
        this.buildTime = buildTime;
        return this;
    }


    public String getBuildTime() {
        return buildTime;
    }

    public void setBuildTime(String buildTime) {
        this.buildTime = buildTime;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        VersionRepresentationDTO versionRepresentation = (VersionRepresentationDTO) o;
        return Objects.equals(this.version, versionRepresentation.version) &&
                Objects.equals(this.buildTime, versionRepresentation.buildTime);
    }

    @Override
    public int hashCode() {
        return Objects.hash(version, buildTime);
    }

    @Override
    public String toString() {

        String sb = "class VersionRepresentationDTO {\n" +
                "    version: " + toIndentedString(version) + "\n" +
                "    buildTime: " + toIndentedString(buildTime) + "\n" +
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

