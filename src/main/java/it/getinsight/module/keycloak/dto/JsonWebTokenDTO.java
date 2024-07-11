package it.getinsight.module.keycloak.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonValue;

import java.util.Map;
import java.util.Objects;


public class JsonWebTokenDTO {
    @JsonProperty("id")
    private String id = null;

    @JsonProperty("exp")
    private Long exp = null;

    @JsonProperty("nbf")
    private Long nbf = null;

    @JsonProperty("iat")
    private Long iat = null;

    @JsonProperty("issuer")
    private String issuer = null;

    @JsonProperty("suer")
    private JsonWebTokenDTO suer = null;

    @JsonProperty("subject")
    private String subject = null;

    @JsonProperty("type")
    private String type = null;

    @JsonProperty("issuedFor")
    private String issuedFor = null;

    @JsonProperty("suedFor")
    private JsonWebTokenDTO suedFor = null;

    @JsonProperty("otherClaims")
    private Map<String, Object> otherClaims = null;
    @JsonProperty("category")
    private CategoryEnum category = null;

    public JsonWebTokenDTO id(String id) {
        this.id = id;
        return this;
    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public JsonWebTokenDTO exp(Long exp) {
        this.exp = exp;
        return this;
    }


    public Long getExp() {
        return exp;
    }

    public void setExp(Long exp) {
        this.exp = exp;
    }

    public JsonWebTokenDTO nbf(Long nbf) {
        this.nbf = nbf;
        return this;
    }


    public Long getNbf() {
        return nbf;
    }

    public void setNbf(Long nbf) {
        this.nbf = nbf;
    }

    public JsonWebTokenDTO iat(Long iat) {
        this.iat = iat;
        return this;
    }


    public Long getIat() {
        return iat;
    }

    public void setIat(Long iat) {
        this.iat = iat;
    }

    public JsonWebTokenDTO issuer(String issuer) {
        this.issuer = issuer;
        return this;
    }


    public String getIssuer() {
        return issuer;
    }

    public void setIssuer(String issuer) {
        this.issuer = issuer;
    }

    public JsonWebTokenDTO suer(JsonWebTokenDTO suer) {
        this.suer = suer;
        return this;
    }


    public JsonWebTokenDTO getSuer() {
        return suer;
    }

    public void setSuer(JsonWebTokenDTO suer) {
        this.suer = suer;
    }

    public JsonWebTokenDTO subject(String subject) {
        this.subject = subject;
        return this;
    }


    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public JsonWebTokenDTO type(String type) {
        this.type = type;
        return this;
    }


    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public JsonWebTokenDTO issuedFor(String issuedFor) {
        this.issuedFor = issuedFor;
        return this;
    }


    public String getIssuedFor() {
        return issuedFor;
    }

    public void setIssuedFor(String issuedFor) {
        this.issuedFor = issuedFor;
    }

    public JsonWebTokenDTO suedFor(JsonWebTokenDTO suedFor) {
        this.suedFor = suedFor;
        return this;
    }


    public JsonWebTokenDTO getSuedFor() {
        return suedFor;
    }

    public void setSuedFor(JsonWebTokenDTO suedFor) {
        this.suedFor = suedFor;
    }

    public JsonWebTokenDTO otherClaims(Map<String, Object> otherClaims) {
        this.otherClaims = otherClaims;
        return this;
    }

    public JsonWebTokenDTO putOtherClaimsItem(String key, Object otherClaimsItem) {
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

    public JsonWebTokenDTO category(CategoryEnum category) {
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
        JsonWebTokenDTO jsonWebToken = (JsonWebTokenDTO) o;
        return Objects.equals(this.id, jsonWebToken.id) &&
                Objects.equals(this.exp, jsonWebToken.exp) &&
                Objects.equals(this.nbf, jsonWebToken.nbf) &&
                Objects.equals(this.iat, jsonWebToken.iat) &&
                Objects.equals(this.issuer, jsonWebToken.issuer) &&
                Objects.equals(this.suer, jsonWebToken.suer) &&
                Objects.equals(this.subject, jsonWebToken.subject) &&
                Objects.equals(this.type, jsonWebToken.type) &&
                Objects.equals(this.issuedFor, jsonWebToken.issuedFor) &&
                Objects.equals(this.suedFor, jsonWebToken.suedFor) &&
                Objects.equals(this.otherClaims, jsonWebToken.otherClaims) &&
                Objects.equals(this.category, jsonWebToken.category);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, exp, nbf, iat, issuer, suer, subject, type, issuedFor, suedFor, otherClaims, category);
    }

    @Override
    public String toString() {

        String sb = "class JsonWebTokenDTO {\n" +
                "    id: " + toIndentedString(id) + "\n" +
                "    exp: " + toIndentedString(exp) + "\n" +
                "    nbf: " + toIndentedString(nbf) + "\n" +
                "    iat: " + toIndentedString(iat) + "\n" +
                "    issuer: " + toIndentedString(issuer) + "\n" +
                "    suer: " + toIndentedString(suer) + "\n" +
                "    subject: " + toIndentedString(subject) + "\n" +
                "    type: " + toIndentedString(type) + "\n" +
                "    issuedFor: " + toIndentedString(issuedFor) + "\n" +
                "    suedFor: " + toIndentedString(suedFor) + "\n" +
                "    otherClaims: " + toIndentedString(otherClaims) + "\n" +
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

