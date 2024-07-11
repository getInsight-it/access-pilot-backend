package it.getinsight.module.keycloak.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Map;
import java.util.Objects;


public class UserInfoDTO {
    @JsonProperty("issuer")
    private String issuer = null;

    @JsonProperty("subject")
    private String subject = null;

    @JsonProperty("name")
    private String name = null;

    @JsonProperty("givenName")
    private String givenName = null;

    @JsonProperty("familyName")
    private String familyName = null;

    @JsonProperty("middleName")
    private String middleName = null;

    @JsonProperty("nickName")
    private String nickName = null;

    @JsonProperty("preferredUsername")
    private String preferredUsername = null;

    @JsonProperty("profile")
    private String profile = null;

    @JsonProperty("picture")
    private String picture = null;

    @JsonProperty("website")
    private String website = null;

    @JsonProperty("email")
    private String email = null;

    @JsonProperty("emailVerified")
    private Boolean emailVerified = null;

    @JsonProperty("gender")
    private String gender = null;

    @JsonProperty("birthdate")
    private String birthdate = null;

    @JsonProperty("zoneinfo")
    private String zoneinfo = null;

    @JsonProperty("locale")
    private String locale = null;

    @JsonProperty("phoneNumber")
    private String phoneNumber = null;

    @JsonProperty("phoneNumberVerified")
    private Boolean phoneNumberVerified = null;

    @JsonProperty("address")
    private AddressClaimSetDTO address = null;

    @JsonProperty("updatedAt")
    private Long updatedAt = null;

    @JsonProperty("sub")
    private String sub = null;

    @JsonProperty("claimsLocales")
    private String claimsLocales = null;

    @JsonProperty("otherClaims")
    private Map<String, Object> otherClaims = null;

    public UserInfoDTO issuer(String issuer) {
        this.issuer = issuer;
        return this;
    }


    public String getIssuer() {
        return issuer;
    }

    public void setIssuer(String issuer) {
        this.issuer = issuer;
    }

    public UserInfoDTO subject(String subject) {
        this.subject = subject;
        return this;
    }


    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public UserInfoDTO name(String name) {
        this.name = name;
        return this;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public UserInfoDTO givenName(String givenName) {
        this.givenName = givenName;
        return this;
    }


    public String getGivenName() {
        return givenName;
    }

    public void setGivenName(String givenName) {
        this.givenName = givenName;
    }

    public UserInfoDTO familyName(String familyName) {
        this.familyName = familyName;
        return this;
    }


    public String getFamilyName() {
        return familyName;
    }

    public void setFamilyName(String familyName) {
        this.familyName = familyName;
    }

    public UserInfoDTO middleName(String middleName) {
        this.middleName = middleName;
        return this;
    }


    public String getMiddleName() {
        return middleName;
    }

    public void setMiddleName(String middleName) {
        this.middleName = middleName;
    }

    public UserInfoDTO nickName(String nickName) {
        this.nickName = nickName;
        return this;
    }


    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public UserInfoDTO preferredUsername(String preferredUsername) {
        this.preferredUsername = preferredUsername;
        return this;
    }


    public String getPreferredUsername() {
        return preferredUsername;
    }

    public void setPreferredUsername(String preferredUsername) {
        this.preferredUsername = preferredUsername;
    }

    public UserInfoDTO profile(String profile) {
        this.profile = profile;
        return this;
    }


    public String getProfile() {
        return profile;
    }

    public void setProfile(String profile) {
        this.profile = profile;
    }

    public UserInfoDTO picture(String picture) {
        this.picture = picture;
        return this;
    }


    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public UserInfoDTO website(String website) {
        this.website = website;
        return this;
    }


    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    public UserInfoDTO email(String email) {
        this.email = email;
        return this;
    }


    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public UserInfoDTO emailVerified(Boolean emailVerified) {
        this.emailVerified = emailVerified;
        return this;
    }


    public Boolean isEmailVerified() {
        return emailVerified;
    }

    public void setEmailVerified(Boolean emailVerified) {
        this.emailVerified = emailVerified;
    }

    public UserInfoDTO gender(String gender) {
        this.gender = gender;
        return this;
    }


    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public UserInfoDTO birthdate(String birthdate) {
        this.birthdate = birthdate;
        return this;
    }


    public String getBirthdate() {
        return birthdate;
    }

    public void setBirthdate(String birthdate) {
        this.birthdate = birthdate;
    }

    public UserInfoDTO zoneinfo(String zoneinfo) {
        this.zoneinfo = zoneinfo;
        return this;
    }


    public String getZoneinfo() {
        return zoneinfo;
    }

    public void setZoneinfo(String zoneinfo) {
        this.zoneinfo = zoneinfo;
    }

    public UserInfoDTO locale(String locale) {
        this.locale = locale;
        return this;
    }


    public String getLocale() {
        return locale;
    }

    public void setLocale(String locale) {
        this.locale = locale;
    }

    public UserInfoDTO phoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
        return this;
    }


    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public UserInfoDTO phoneNumberVerified(Boolean phoneNumberVerified) {
        this.phoneNumberVerified = phoneNumberVerified;
        return this;
    }


    public Boolean isPhoneNumberVerified() {
        return phoneNumberVerified;
    }

    public void setPhoneNumberVerified(Boolean phoneNumberVerified) {
        this.phoneNumberVerified = phoneNumberVerified;
    }

    public UserInfoDTO address(AddressClaimSetDTO address) {
        this.address = address;
        return this;
    }


    public AddressClaimSetDTO getAddress() {
        return address;
    }

    public void setAddress(AddressClaimSetDTO address) {
        this.address = address;
    }

    public UserInfoDTO updatedAt(Long updatedAt) {
        this.updatedAt = updatedAt;
        return this;
    }


    public Long getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Long updatedAt) {
        this.updatedAt = updatedAt;
    }

    public UserInfoDTO sub(String sub) {
        this.sub = sub;
        return this;
    }


    public String getSub() {
        return sub;
    }

    public void setSub(String sub) {
        this.sub = sub;
    }

    public UserInfoDTO claimsLocales(String claimsLocales) {
        this.claimsLocales = claimsLocales;
        return this;
    }


    public String getClaimsLocales() {
        return claimsLocales;
    }

    public void setClaimsLocales(String claimsLocales) {
        this.claimsLocales = claimsLocales;
    }

    public UserInfoDTO otherClaims(Map<String, Object> otherClaims) {
        this.otherClaims = otherClaims;
        return this;
    }

    public UserInfoDTO putOtherClaimsItem(String key, Object otherClaimsItem) {
        if (this.otherClaims == null) {
            this.otherClaims = null;
        }
        this.otherClaims.put(key, otherClaimsItem);
        return this;
    }


    public Map<String, Object> getOtherClaims() {
        return otherClaims;
    }

    public void setOtherClaims(Map<String, Object> otherClaims) {
        this.otherClaims = otherClaims;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        UserInfoDTO userInfo = (UserInfoDTO) o;
        return Objects.equals(this.issuer, userInfo.issuer) &&
                Objects.equals(this.subject, userInfo.subject) &&
                Objects.equals(this.name, userInfo.name) &&
                Objects.equals(this.givenName, userInfo.givenName) &&
                Objects.equals(this.familyName, userInfo.familyName) &&
                Objects.equals(this.middleName, userInfo.middleName) &&
                Objects.equals(this.nickName, userInfo.nickName) &&
                Objects.equals(this.preferredUsername, userInfo.preferredUsername) &&
                Objects.equals(this.profile, userInfo.profile) &&
                Objects.equals(this.picture, userInfo.picture) &&
                Objects.equals(this.website, userInfo.website) &&
                Objects.equals(this.email, userInfo.email) &&
                Objects.equals(this.emailVerified, userInfo.emailVerified) &&
                Objects.equals(this.gender, userInfo.gender) &&
                Objects.equals(this.birthdate, userInfo.birthdate) &&
                Objects.equals(this.zoneinfo, userInfo.zoneinfo) &&
                Objects.equals(this.locale, userInfo.locale) &&
                Objects.equals(this.phoneNumber, userInfo.phoneNumber) &&
                Objects.equals(this.phoneNumberVerified, userInfo.phoneNumberVerified) &&
                Objects.equals(this.address, userInfo.address) &&
                Objects.equals(this.updatedAt, userInfo.updatedAt) &&
                Objects.equals(this.sub, userInfo.sub) &&
                Objects.equals(this.claimsLocales, userInfo.claimsLocales) &&
                Objects.equals(this.otherClaims, userInfo.otherClaims);
    }

    @Override
    public int hashCode() {
        return Objects.hash(issuer, subject, name, givenName, familyName, middleName, nickName, preferredUsername, profile, picture, website, email, emailVerified, gender, birthdate, zoneinfo, locale, phoneNumber, phoneNumberVerified, address, updatedAt, sub, claimsLocales, otherClaims);
    }

    @Override
    public String toString() {

        String sb = "class UserInfoDTO {\n" +
                "    issuer: " + toIndentedString(issuer) + "\n" +
                "    subject: " + toIndentedString(subject) + "\n" +
                "    name: " + toIndentedString(name) + "\n" +
                "    givenName: " + toIndentedString(givenName) + "\n" +
                "    familyName: " + toIndentedString(familyName) + "\n" +
                "    middleName: " + toIndentedString(middleName) + "\n" +
                "    nickName: " + toIndentedString(nickName) + "\n" +
                "    preferredUsername: " + toIndentedString(preferredUsername) + "\n" +
                "    profile: " + toIndentedString(profile) + "\n" +
                "    picture: " + toIndentedString(picture) + "\n" +
                "    website: " + toIndentedString(website) + "\n" +
                "    email: " + toIndentedString(email) + "\n" +
                "    emailVerified: " + toIndentedString(emailVerified) + "\n" +
                "    gender: " + toIndentedString(gender) + "\n" +
                "    birthdate: " + toIndentedString(birthdate) + "\n" +
                "    zoneinfo: " + toIndentedString(zoneinfo) + "\n" +
                "    locale: " + toIndentedString(locale) + "\n" +
                "    phoneNumber: " + toIndentedString(phoneNumber) + "\n" +
                "    phoneNumberVerified: " + toIndentedString(phoneNumberVerified) + "\n" +
                "    address: " + toIndentedString(address) + "\n" +
                "    updatedAt: " + toIndentedString(updatedAt) + "\n" +
                "    sub: " + toIndentedString(sub) + "\n" +
                "    claimsLocales: " + toIndentedString(claimsLocales) + "\n" +
                "    otherClaims: " + toIndentedString(otherClaims) + "\n" +
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

