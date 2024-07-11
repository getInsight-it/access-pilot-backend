package it.getinsight.module.keycloak.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonValue;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


public class AccessTokenDTO {
    @JsonProperty("suedAt")
    private AccessTokenDTO suedAt = null;

    @JsonProperty("suer")
    private AccessTokenDTO suer = null;

    @JsonProperty("allowedOrigins")
    private List<String> allowedOrigins = null;

    @JsonProperty("realmAccess")
    private AccessDTO realmAccess = null;

    @JsonProperty("trustedCertificates")
    private List<String> trustedCertificates = null;

    @JsonProperty("suedFor")
    private AccessTokenDTO suedFor = null;

    @JsonProperty("authorization")
    private AuthorizationDTO authorization = null;

    @JsonProperty("certConf")
    private CertConfDTO certConf = null;

    @JsonProperty("scope")
    private String scope = null;
    @JsonProperty("category")
    private CategoryEnum category = null;

    public AccessTokenDTO suedAt(AccessTokenDTO suedAt) {
        this.suedAt = suedAt;
        return this;
    }


    public AccessTokenDTO getSuedAt() {
        return suedAt;
    }

    public void setSuedAt(AccessTokenDTO suedAt) {
        this.suedAt = suedAt;
    }

    public AccessTokenDTO suer(AccessTokenDTO suer) {
        this.suer = suer;
        return this;
    }


    public AccessTokenDTO getSuer() {
        return suer;
    }

    public void setSuer(AccessTokenDTO suer) {
        this.suer = suer;
    }

    public AccessTokenDTO allowedOrigins(List<String> allowedOrigins) {
        this.allowedOrigins = allowedOrigins;
        return this;
    }

    public AccessTokenDTO addAllowedOriginsItem(String allowedOriginsItem) {
        if (this.allowedOrigins == null) {
            this.allowedOrigins = new ArrayList<String>();
        }
        this.allowedOrigins.add(allowedOriginsItem);
        return this;
    }


    public List<String> getAllowedOrigins() {
        return allowedOrigins;
    }

    public void setAllowedOrigins(List<String> allowedOrigins) {
        this.allowedOrigins = allowedOrigins;
    }

    public AccessTokenDTO realmAccess(AccessDTO realmAccess) {
        this.realmAccess = realmAccess;
        return this;
    }


    public AccessDTO getRealmAccess() {
        return realmAccess;
    }

    public void setRealmAccess(AccessDTO realmAccess) {
        this.realmAccess = realmAccess;
    }

    public AccessTokenDTO trustedCertificates(List<String> trustedCertificates) {
        this.trustedCertificates = trustedCertificates;
        return this;
    }

    public AccessTokenDTO addTrustedCertificatesItem(String trustedCertificatesItem) {
        if (this.trustedCertificates == null) {
            this.trustedCertificates = new ArrayList<String>();
        }
        this.trustedCertificates.add(trustedCertificatesItem);
        return this;
    }


    public List<String> getTrustedCertificates() {
        return trustedCertificates;
    }

    public void setTrustedCertificates(List<String> trustedCertificates) {
        this.trustedCertificates = trustedCertificates;
    }

    public AccessTokenDTO suedFor(AccessTokenDTO suedFor) {
        this.suedFor = suedFor;
        return this;
    }


    public AccessTokenDTO getSuedFor() {
        return suedFor;
    }

    public void setSuedFor(AccessTokenDTO suedFor) {
        this.suedFor = suedFor;
    }

    public AccessTokenDTO authorization(AuthorizationDTO authorization) {
        this.authorization = authorization;
        return this;
    }


    public AuthorizationDTO getAuthorization() {
        return authorization;
    }

    public void setAuthorization(AuthorizationDTO authorization) {
        this.authorization = authorization;
    }

    public AccessTokenDTO certConf(CertConfDTO certConf) {
        this.certConf = certConf;
        return this;
    }


    public CertConfDTO getCertConf() {
        return certConf;
    }

    public void setCertConf(CertConfDTO certConf) {
        this.certConf = certConf;
    }

    public AccessTokenDTO scope(String scope) {
        this.scope = scope;
        return this;
    }


    public String getScope() {
        return scope;
    }

    public void setScope(String scope) {
        this.scope = scope;
    }

    public AccessTokenDTO category(CategoryEnum category) {
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
        AccessTokenDTO accessToken = (AccessTokenDTO) o;
        return Objects.equals(this.suedAt, accessToken.suedAt) &&
                Objects.equals(this.suer, accessToken.suer) &&
                Objects.equals(this.allowedOrigins, accessToken.allowedOrigins) &&
                Objects.equals(this.realmAccess, accessToken.realmAccess) &&
                Objects.equals(this.trustedCertificates, accessToken.trustedCertificates) &&
                Objects.equals(this.suedFor, accessToken.suedFor) &&
                Objects.equals(this.authorization, accessToken.authorization) &&
                Objects.equals(this.certConf, accessToken.certConf) &&
                Objects.equals(this.scope, accessToken.scope) &&
                Objects.equals(this.category, accessToken.category);
    }

    @Override
    public int hashCode() {
        return Objects.hash(suedAt, suer, allowedOrigins, realmAccess, trustedCertificates, suedFor, authorization, certConf, scope, category);
    }

    @Override
    public String toString() {

        String sb = "class AccessTokenDTO {\n" +
                "    suedAt: " + toIndentedString(suedAt) + "\n" +
                "    suer: " + toIndentedString(suer) + "\n" +
                "    allowedOrigins: " + toIndentedString(allowedOrigins) + "\n" +
                "    realmAccess: " + toIndentedString(realmAccess) + "\n" +
                "    trustedCertificates: " + toIndentedString(trustedCertificates) + "\n" +
                "    suedFor: " + toIndentedString(suedFor) + "\n" +
                "    authorization: " + toIndentedString(authorization) + "\n" +
                "    certConf: " + toIndentedString(certConf) + "\n" +
                "    scope: " + toIndentedString(scope) + "\n" +
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

