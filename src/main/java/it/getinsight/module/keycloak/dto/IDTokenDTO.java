package it.getinsight.module.keycloak.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonValue;

import java.util.Objects;


public class IDTokenDTO {
    @JsonProperty("nonce")
    private String nonce = null;

    @JsonProperty("auth_time")
    private Long authTime = null;

    @JsonProperty("sessionId")
    private String sessionId = null;

    @JsonProperty("sessionState")
    private String sessionState = null;

    @JsonProperty("accessTokenHash")
    private String accessTokenHash = null;

    @JsonProperty("codeHash")
    private String codeHash = null;

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

    @JsonProperty("claimsLocales")
    private String claimsLocales = null;

    @JsonProperty("acr")
    private String acr = null;

    @JsonProperty("stateHash")
    private String stateHash = null;
    @JsonProperty("category")
    private CategoryEnum category = null;

    public IDTokenDTO nonce(String nonce) {
        this.nonce = nonce;
        return this;
    }


    public String getNonce() {
        return nonce;
    }

    public void setNonce(String nonce) {
        this.nonce = nonce;
    }

    public IDTokenDTO authTime(Long authTime) {
        this.authTime = authTime;
        return this;
    }


    public Long getAuthTime() {
        return authTime;
    }

    public void setAuthTime(Long authTime) {
        this.authTime = authTime;
    }

    public IDTokenDTO sessionId(String sessionId) {
        this.sessionId = sessionId;
        return this;
    }


    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    public IDTokenDTO sessionState(String sessionState) {
        this.sessionState = sessionState;
        return this;
    }


    public String getSessionState() {
        return sessionState;
    }

    public void setSessionState(String sessionState) {
        this.sessionState = sessionState;
    }

    public IDTokenDTO accessTokenHash(String accessTokenHash) {
        this.accessTokenHash = accessTokenHash;
        return this;
    }


    public String getAccessTokenHash() {
        return accessTokenHash;
    }

    public void setAccessTokenHash(String accessTokenHash) {
        this.accessTokenHash = accessTokenHash;
    }

    public IDTokenDTO codeHash(String codeHash) {
        this.codeHash = codeHash;
        return this;
    }


    public String getCodeHash() {
        return codeHash;
    }

    public void setCodeHash(String codeHash) {
        this.codeHash = codeHash;
    }

    public IDTokenDTO name(String name) {
        this.name = name;
        return this;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public IDTokenDTO givenName(String givenName) {
        this.givenName = givenName;
        return this;
    }


    public String getGivenName() {
        return givenName;
    }

    public void setGivenName(String givenName) {
        this.givenName = givenName;
    }

    public IDTokenDTO familyName(String familyName) {
        this.familyName = familyName;
        return this;
    }


    public String getFamilyName() {
        return familyName;
    }

    public void setFamilyName(String familyName) {
        this.familyName = familyName;
    }

    public IDTokenDTO middleName(String middleName) {
        this.middleName = middleName;
        return this;
    }


    public String getMiddleName() {
        return middleName;
    }

    public void setMiddleName(String middleName) {
        this.middleName = middleName;
    }

    public IDTokenDTO nickName(String nickName) {
        this.nickName = nickName;
        return this;
    }


    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public IDTokenDTO preferredUsername(String preferredUsername) {
        this.preferredUsername = preferredUsername;
        return this;
    }


    public String getPreferredUsername() {
        return preferredUsername;
    }

    public void setPreferredUsername(String preferredUsername) {
        this.preferredUsername = preferredUsername;
    }

    public IDTokenDTO profile(String profile) {
        this.profile = profile;
        return this;
    }


    public String getProfile() {
        return profile;
    }

    public void setProfile(String profile) {
        this.profile = profile;
    }

    public IDTokenDTO picture(String picture) {
        this.picture = picture;
        return this;
    }


    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public IDTokenDTO website(String website) {
        this.website = website;
        return this;
    }


    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    public IDTokenDTO email(String email) {
        this.email = email;
        return this;
    }


    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public IDTokenDTO emailVerified(Boolean emailVerified) {
        this.emailVerified = emailVerified;
        return this;
    }


    public Boolean isEmailVerified() {
        return emailVerified;
    }

    public void setEmailVerified(Boolean emailVerified) {
        this.emailVerified = emailVerified;
    }

    public IDTokenDTO gender(String gender) {
        this.gender = gender;
        return this;
    }


    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public IDTokenDTO birthdate(String birthdate) {
        this.birthdate = birthdate;
        return this;
    }


    public String getBirthdate() {
        return birthdate;
    }

    public void setBirthdate(String birthdate) {
        this.birthdate = birthdate;
    }

    public IDTokenDTO zoneinfo(String zoneinfo) {
        this.zoneinfo = zoneinfo;
        return this;
    }


    public String getZoneinfo() {
        return zoneinfo;
    }

    public void setZoneinfo(String zoneinfo) {
        this.zoneinfo = zoneinfo;
    }

    public IDTokenDTO locale(String locale) {
        this.locale = locale;
        return this;
    }


    public String getLocale() {
        return locale;
    }

    public void setLocale(String locale) {
        this.locale = locale;
    }

    public IDTokenDTO phoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
        return this;
    }


    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public IDTokenDTO phoneNumberVerified(Boolean phoneNumberVerified) {
        this.phoneNumberVerified = phoneNumberVerified;
        return this;
    }


    public Boolean isPhoneNumberVerified() {
        return phoneNumberVerified;
    }

    public void setPhoneNumberVerified(Boolean phoneNumberVerified) {
        this.phoneNumberVerified = phoneNumberVerified;
    }

    public IDTokenDTO address(AddressClaimSetDTO address) {
        this.address = address;
        return this;
    }


    public AddressClaimSetDTO getAddress() {
        return address;
    }

    public void setAddress(AddressClaimSetDTO address) {
        this.address = address;
    }

    public IDTokenDTO updatedAt(Long updatedAt) {
        this.updatedAt = updatedAt;
        return this;
    }


    public Long getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Long updatedAt) {
        this.updatedAt = updatedAt;
    }

    public IDTokenDTO claimsLocales(String claimsLocales) {
        this.claimsLocales = claimsLocales;
        return this;
    }


    public String getClaimsLocales() {
        return claimsLocales;
    }

    public void setClaimsLocales(String claimsLocales) {
        this.claimsLocales = claimsLocales;
    }

    public IDTokenDTO acr(String acr) {
        this.acr = acr;
        return this;
    }


    public String getAcr() {
        return acr;
    }

    public void setAcr(String acr) {
        this.acr = acr;
    }

    public IDTokenDTO stateHash(String stateHash) {
        this.stateHash = stateHash;
        return this;
    }


    public String getStateHash() {
        return stateHash;
    }

    public void setStateHash(String stateHash) {
        this.stateHash = stateHash;
    }

    public IDTokenDTO category(CategoryEnum category) {
        this.category = category;
        return this;
    }


    public CategoryEnum getCategory() {
        return category;
    }

    public void setCategory(CategoryEnum category) {
        this.category = category;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        IDTokenDTO idToken = (IDTokenDTO) o;
        return Objects.equals(this.nonce, idToken.nonce) &&
                Objects.equals(this.authTime, idToken.authTime) &&
                Objects.equals(this.sessionId, idToken.sessionId) &&
                Objects.equals(this.sessionState, idToken.sessionState) &&
                Objects.equals(this.accessTokenHash, idToken.accessTokenHash) &&
                Objects.equals(this.codeHash, idToken.codeHash) &&
                Objects.equals(this.name, idToken.name) &&
                Objects.equals(this.givenName, idToken.givenName) &&
                Objects.equals(this.familyName, idToken.familyName) &&
                Objects.equals(this.middleName, idToken.middleName) &&
                Objects.equals(this.nickName, idToken.nickName) &&
                Objects.equals(this.preferredUsername, idToken.preferredUsername) &&
                Objects.equals(this.profile, idToken.profile) &&
                Objects.equals(this.picture, idToken.picture) &&
                Objects.equals(this.website, idToken.website) &&
                Objects.equals(this.email, idToken.email) &&
                Objects.equals(this.emailVerified, idToken.emailVerified) &&
                Objects.equals(this.gender, idToken.gender) &&
                Objects.equals(this.birthdate, idToken.birthdate) &&
                Objects.equals(this.zoneinfo, idToken.zoneinfo) &&
                Objects.equals(this.locale, idToken.locale) &&
                Objects.equals(this.phoneNumber, idToken.phoneNumber) &&
                Objects.equals(this.phoneNumberVerified, idToken.phoneNumberVerified) &&
                Objects.equals(this.address, idToken.address) &&
                Objects.equals(this.updatedAt, idToken.updatedAt) &&
                Objects.equals(this.claimsLocales, idToken.claimsLocales) &&
                Objects.equals(this.acr, idToken.acr) &&
                Objects.equals(this.stateHash, idToken.stateHash) &&
                Objects.equals(this.category, idToken.category);
    }

    @Override
    public int hashCode() {
        return Objects.hash(nonce, authTime, sessionId, sessionState, accessTokenHash, codeHash, name, givenName, familyName, middleName, nickName, preferredUsername, profile, picture, website, email, emailVerified, gender, birthdate, zoneinfo, locale, phoneNumber, phoneNumberVerified, address, updatedAt, claimsLocales, acr, stateHash, category);
    }

    @Override
    public String toString() {

        String sb = "class IDTokenDTO {\n" +
                "    nonce: " + toIndentedString(nonce) + "\n" +
                "    authTime: " + toIndentedString(authTime) + "\n" +
                "    sessionId: " + toIndentedString(sessionId) + "\n" +
                "    sessionState: " + toIndentedString(sessionState) + "\n" +
                "    accessTokenHash: " + toIndentedString(accessTokenHash) + "\n" +
                "    codeHash: " + toIndentedString(codeHash) + "\n" +
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
                "    claimsLocales: " + toIndentedString(claimsLocales) + "\n" +
                "    acr: " + toIndentedString(acr) + "\n" +
                "    stateHash: " + toIndentedString(stateHash) + "\n" +
                "    category: " + toIndentedString(category) + "\n" +
                "}";
        return sb;
    }

    private String toIndentedString(Object o) {
        if (o == null) {
            return "null";
        }
        return o.toString().replace("\n", "\n    ");
    }


    public enum CategoryEnum {
        INTERNAL("INTERNAL"),

        ACCESS("ACCESS"),

        ID("ID"),

        ADMIN("ADMIN"),

        USERINFO("USERINFO"),

        LOGOUT("LOGOUT"),

        AUTHORIZATION_RESPONSE("AUTHORIZATION_RESPONSE");

        private final String value;

        CategoryEnum(String value) {
            this.value = value;
        }

        @JsonCreator
        public static CategoryEnum fromValue(String text) {
            for (CategoryEnum b : CategoryEnum.values()) {
                if (String.valueOf(b.value).equals(text)) {
                    return b;
                }
            }
            return null;
        }

        @Override
        @JsonValue
        public String toString() {
            return String.valueOf(value);
        }
    }
}

