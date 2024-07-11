package it.getinsight.module.keycloak.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Objects;


public class PublishedRealmRepresentationDTO {
    @JsonProperty("publicKeyPem")
    private String publicKeyPem = null;

    @JsonProperty("tokenServiceUrl")
    private String tokenServiceUrl = null;

    @JsonProperty("accountServiceUrl")
    private String accountServiceUrl = null;

    @JsonProperty("notBefore")
    private Integer notBefore = null;

    public PublishedRealmRepresentationDTO publicKeyPem(String publicKeyPem) {
        this.publicKeyPem = publicKeyPem;
        return this;
    }


    public String getPublicKeyPem() {
        return publicKeyPem;
    }

    public void setPublicKeyPem(String publicKeyPem) {
        this.publicKeyPem = publicKeyPem;
    }

    public PublishedRealmRepresentationDTO tokenServiceUrl(String tokenServiceUrl) {
        this.tokenServiceUrl = tokenServiceUrl;
        return this;
    }


    public String getTokenServiceUrl() {
        return tokenServiceUrl;
    }

    public void setTokenServiceUrl(String tokenServiceUrl) {
        this.tokenServiceUrl = tokenServiceUrl;
    }

    public PublishedRealmRepresentationDTO accountServiceUrl(String accountServiceUrl) {
        this.accountServiceUrl = accountServiceUrl;
        return this;
    }


    public String getAccountServiceUrl() {
        return accountServiceUrl;
    }

    public void setAccountServiceUrl(String accountServiceUrl) {
        this.accountServiceUrl = accountServiceUrl;
    }

    public PublishedRealmRepresentationDTO notBefore(Integer notBefore) {
        this.notBefore = notBefore;
        return this;
    }


    public Integer getNotBefore() {
        return notBefore;
    }

    public void setNotBefore(Integer notBefore) {
        this.notBefore = notBefore;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        PublishedRealmRepresentationDTO publishedRealmRepresentation = (PublishedRealmRepresentationDTO) o;
        return Objects.equals(this.publicKeyPem, publishedRealmRepresentation.publicKeyPem) &&
                Objects.equals(this.tokenServiceUrl, publishedRealmRepresentation.tokenServiceUrl) &&
                Objects.equals(this.accountServiceUrl, publishedRealmRepresentation.accountServiceUrl) &&
                Objects.equals(this.notBefore, publishedRealmRepresentation.notBefore);
    }

    @Override
    public int hashCode() {
        return Objects.hash(publicKeyPem, tokenServiceUrl, accountServiceUrl, notBefore);
    }

    @Override
    public String toString() {

        String sb = "class PublishedRealmRepresentationDTO {\n" +
                "    publicKeyPem: " + toIndentedString(publicKeyPem) + "\n" +
                "    tokenServiceUrl: " + toIndentedString(tokenServiceUrl) + "\n" +
                "    accountServiceUrl: " + toIndentedString(accountServiceUrl) + "\n" +
                "    notBefore: " + toIndentedString(notBefore) + "\n" +
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

