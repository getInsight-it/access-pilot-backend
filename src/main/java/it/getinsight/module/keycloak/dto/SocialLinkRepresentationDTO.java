package it.getinsight.module.keycloak.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Objects;


public class SocialLinkRepresentationDTO {
    @JsonProperty("socialProvider")
    private String socialProvider = null;

    @JsonProperty("socialUserId")
    private String socialUserId = null;

    @JsonProperty("socialUsername")
    private String socialUsername = null;

    public SocialLinkRepresentationDTO socialProvider(String socialProvider) {
        this.socialProvider = socialProvider;
        return this;
    }


    public String getSocialProvider() {
        return socialProvider;
    }

    public void setSocialProvider(String socialProvider) {
        this.socialProvider = socialProvider;
    }

    public SocialLinkRepresentationDTO socialUserId(String socialUserId) {
        this.socialUserId = socialUserId;
        return this;
    }


    public String getSocialUserId() {
        return socialUserId;
    }

    public void setSocialUserId(String socialUserId) {
        this.socialUserId = socialUserId;
    }

    public SocialLinkRepresentationDTO socialUsername(String socialUsername) {
        this.socialUsername = socialUsername;
        return this;
    }


    public String getSocialUsername() {
        return socialUsername;
    }

    public void setSocialUsername(String socialUsername) {
        this.socialUsername = socialUsername;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        SocialLinkRepresentationDTO socialLinkRepresentation = (SocialLinkRepresentationDTO) o;
        return Objects.equals(this.socialProvider, socialLinkRepresentation.socialProvider) &&
                Objects.equals(this.socialUserId, socialLinkRepresentation.socialUserId) &&
                Objects.equals(this.socialUsername, socialLinkRepresentation.socialUsername);
    }

    @Override
    public int hashCode() {
        return Objects.hash(socialProvider, socialUserId, socialUsername);
    }

    @Override
    public String toString() {

        String sb = "class SocialLinkRepresentationDTO {\n" +
                "    socialProvider: " + toIndentedString(socialProvider) + "\n" +
                "    socialUserId: " + toIndentedString(socialUserId) + "\n" +
                "    socialUsername: " + toIndentedString(socialUsername) + "\n" +
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

