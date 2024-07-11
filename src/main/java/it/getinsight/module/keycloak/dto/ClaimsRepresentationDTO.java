package it.getinsight.module.keycloak.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Map;
import java.util.Objects;


public class ClaimsRepresentationDTO {
    @JsonProperty("idTokenClaims")
    private Map<String, Object> idTokenClaims = null;

    @JsonProperty("userinfoClaims")
    private Map<String, Object> userinfoClaims = null;

    @JsonProperty("present")
    private Boolean present = null;

    @JsonProperty("presentAsNullClaim")
    private Boolean presentAsNullClaim = null;

    @JsonProperty("claimValue")
    private Object claimValue = null;

    public ClaimsRepresentationDTO idTokenClaims(Map<String, Object> idTokenClaims) {
        this.idTokenClaims = idTokenClaims;
        return this;
    }

    public ClaimsRepresentationDTO putIdTokenClaimsItem(String key, Object idTokenClaimsItem) {
        if (this.idTokenClaims == null) {
            this.idTokenClaims = null;
        }
        this.idTokenClaims.put(key, idTokenClaimsItem);
        return this;
    }


    public Map<String, Object> getIdTokenClaims() {
        return idTokenClaims;
    }

    public void setIdTokenClaims(Map<String, Object> idTokenClaims) {
        this.idTokenClaims = idTokenClaims;
    }

    public ClaimsRepresentationDTO userinfoClaims(Map<String, Object> userinfoClaims) {
        this.userinfoClaims = userinfoClaims;
        return this;
    }

    public ClaimsRepresentationDTO putUserinfoClaimsItem(String key, Object userinfoClaimsItem) {
        if (this.userinfoClaims == null) {
            this.userinfoClaims = null;
        }
        this.userinfoClaims.put(key, userinfoClaimsItem);
        return this;
    }


    public Map<String, Object> getUserinfoClaims() {
        return userinfoClaims;
    }

    public void setUserinfoClaims(Map<String, Object> userinfoClaims) {
        this.userinfoClaims = userinfoClaims;
    }

    public ClaimsRepresentationDTO present(Boolean present) {
        this.present = present;
        return this;
    }


    public Boolean isPresent() {
        return present;
    }

    public void setPresent(Boolean present) {
        this.present = present;
    }

    public ClaimsRepresentationDTO presentAsNullClaim(Boolean presentAsNullClaim) {
        this.presentAsNullClaim = presentAsNullClaim;
        return this;
    }


    public Boolean isPresentAsNullClaim() {
        return presentAsNullClaim;
    }

    public void setPresentAsNullClaim(Boolean presentAsNullClaim) {
        this.presentAsNullClaim = presentAsNullClaim;
    }

    public ClaimsRepresentationDTO claimValue(Object claimValue) {
        this.claimValue = claimValue;
        return this;
    }


    public Object getClaimValue() {
        return claimValue;
    }

    public void setClaimValue(Object claimValue) {
        this.claimValue = claimValue;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ClaimsRepresentationDTO claimsRepresentation = (ClaimsRepresentationDTO) o;
        return Objects.equals(this.idTokenClaims, claimsRepresentation.idTokenClaims) &&
                Objects.equals(this.userinfoClaims, claimsRepresentation.userinfoClaims) &&
                Objects.equals(this.present, claimsRepresentation.present) &&
                Objects.equals(this.presentAsNullClaim, claimsRepresentation.presentAsNullClaim) &&
                Objects.equals(this.claimValue, claimsRepresentation.claimValue);
    }

    @Override
    public int hashCode() {
        return Objects.hash(idTokenClaims, userinfoClaims, present, presentAsNullClaim, claimValue);
    }

    @Override
    public String toString() {

        String sb = "class ClaimsRepresentationDTO {\n" +
                "    idTokenClaims: " + toIndentedString(idTokenClaims) + "\n" +
                "    userinfoClaims: " + toIndentedString(userinfoClaims) + "\n" +
                "    present: " + toIndentedString(present) + "\n" +
                "    presentAsNullClaim: " + toIndentedString(presentAsNullClaim) + "\n" +
                "    claimValue: " + toIndentedString(claimValue) + "\n" +
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

