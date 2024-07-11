package it.getinsight.module.keycloak.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Objects;


public class ClaimRepresentationDTO {
    @JsonProperty("name")
    private Boolean name = null;

    @JsonProperty("username")
    private Boolean username = null;

    @JsonProperty("profile")
    private Boolean profile = null;

    @JsonProperty("picture")
    private Boolean picture = null;

    @JsonProperty("website")
    private Boolean website = null;

    @JsonProperty("email")
    private Boolean email = null;

    @JsonProperty("gender")
    private Boolean gender = null;

    @JsonProperty("locale")
    private Boolean locale = null;

    @JsonProperty("address")
    private Boolean address = null;

    @JsonProperty("phone")
    private Boolean phone = null;

    public ClaimRepresentationDTO name(Boolean name) {
        this.name = name;
        return this;
    }


    public Boolean isName() {
        return name;
    }

    public void setName(Boolean name) {
        this.name = name;
    }

    public ClaimRepresentationDTO username(Boolean username) {
        this.username = username;
        return this;
    }


    public Boolean isUsername() {
        return username;
    }

    public void setUsername(Boolean username) {
        this.username = username;
    }

    public ClaimRepresentationDTO profile(Boolean profile) {
        this.profile = profile;
        return this;
    }


    public Boolean isProfile() {
        return profile;
    }

    public void setProfile(Boolean profile) {
        this.profile = profile;
    }

    public ClaimRepresentationDTO picture(Boolean picture) {
        this.picture = picture;
        return this;
    }


    public Boolean isPicture() {
        return picture;
    }

    public void setPicture(Boolean picture) {
        this.picture = picture;
    }

    public ClaimRepresentationDTO website(Boolean website) {
        this.website = website;
        return this;
    }


    public Boolean isWebsite() {
        return website;
    }

    public void setWebsite(Boolean website) {
        this.website = website;
    }

    public ClaimRepresentationDTO email(Boolean email) {
        this.email = email;
        return this;
    }


    public Boolean isEmail() {
        return email;
    }

    public void setEmail(Boolean email) {
        this.email = email;
    }

    public ClaimRepresentationDTO gender(Boolean gender) {
        this.gender = gender;
        return this;
    }


    public Boolean isGender() {
        return gender;
    }

    public void setGender(Boolean gender) {
        this.gender = gender;
    }

    public ClaimRepresentationDTO locale(Boolean locale) {
        this.locale = locale;
        return this;
    }


    public Boolean isLocale() {
        return locale;
    }

    public void setLocale(Boolean locale) {
        this.locale = locale;
    }

    public ClaimRepresentationDTO address(Boolean address) {
        this.address = address;
        return this;
    }


    public Boolean isAddress() {
        return address;
    }

    public void setAddress(Boolean address) {
        this.address = address;
    }

    public ClaimRepresentationDTO phone(Boolean phone) {
        this.phone = phone;
        return this;
    }


    public Boolean isPhone() {
        return phone;
    }

    public void setPhone(Boolean phone) {
        this.phone = phone;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ClaimRepresentationDTO claimRepresentation = (ClaimRepresentationDTO) o;
        return Objects.equals(this.name, claimRepresentation.name) &&
                Objects.equals(this.username, claimRepresentation.username) &&
                Objects.equals(this.profile, claimRepresentation.profile) &&
                Objects.equals(this.picture, claimRepresentation.picture) &&
                Objects.equals(this.website, claimRepresentation.website) &&
                Objects.equals(this.email, claimRepresentation.email) &&
                Objects.equals(this.gender, claimRepresentation.gender) &&
                Objects.equals(this.locale, claimRepresentation.locale) &&
                Objects.equals(this.address, claimRepresentation.address) &&
                Objects.equals(this.phone, claimRepresentation.phone);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, username, profile, picture, website, email, gender, locale, address, phone);
    }

    @Override
    public String toString() {

        String sb = "class ClaimRepresentationDTO {\n" +
                "    name: " + toIndentedString(name) + "\n" +
                "    username: " + toIndentedString(username) + "\n" +
                "    profile: " + toIndentedString(profile) + "\n" +
                "    picture: " + toIndentedString(picture) + "\n" +
                "    website: " + toIndentedString(website) + "\n" +
                "    email: " + toIndentedString(email) + "\n" +
                "    gender: " + toIndentedString(gender) + "\n" +
                "    locale: " + toIndentedString(locale) + "\n" +
                "    address: " + toIndentedString(address) + "\n" +
                "    phone: " + toIndentedString(phone) + "\n" +
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

