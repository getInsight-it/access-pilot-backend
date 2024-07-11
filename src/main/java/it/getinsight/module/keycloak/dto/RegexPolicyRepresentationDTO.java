package it.getinsight.module.keycloak.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Objects;


public class RegexPolicyRepresentationDTO {
    @JsonProperty("type")
    private String type = null;

    @JsonProperty("targetClaim")
    private String targetClaim = null;

    @JsonProperty("pattern")
    private String pattern = null;

    public RegexPolicyRepresentationDTO type(String type) {
        this.type = type;
        return this;
    }


    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public RegexPolicyRepresentationDTO targetClaim(String targetClaim) {
        this.targetClaim = targetClaim;
        return this;
    }


    public String getTargetClaim() {
        return targetClaim;
    }

    public void setTargetClaim(String targetClaim) {
        this.targetClaim = targetClaim;
    }

    public RegexPolicyRepresentationDTO pattern(String pattern) {
        this.pattern = pattern;
        return this;
    }


    public String getPattern() {
        return pattern;
    }

    public void setPattern(String pattern) {
        this.pattern = pattern;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        RegexPolicyRepresentationDTO regexPolicyRepresentation = (RegexPolicyRepresentationDTO) o;
        return Objects.equals(this.type, regexPolicyRepresentation.type) &&
                Objects.equals(this.targetClaim, regexPolicyRepresentation.targetClaim) &&
                Objects.equals(this.pattern, regexPolicyRepresentation.pattern);
    }

    @Override
    public int hashCode() {
        return Objects.hash(type, targetClaim, pattern);
    }

    @Override
    public String toString() {

        String sb = "class RegexPolicyRepresentationDTO {\n" +
                "    type: " + toIndentedString(type) + "\n" +
                "    targetClaim: " + toIndentedString(targetClaim) + "\n" +
                "    pattern: " + toIndentedString(pattern) + "\n" +
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

