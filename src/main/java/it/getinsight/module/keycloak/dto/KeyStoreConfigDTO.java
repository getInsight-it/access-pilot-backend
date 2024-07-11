package it.getinsight.module.keycloak.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Objects;


public class KeyStoreConfigDTO {
    @JsonProperty("realmCertificate")
    private Boolean realmCertificate = null;

    @JsonProperty("storePassword")
    private String storePassword = null;

    @JsonProperty("keyPassword")
    private String keyPassword = null;

    @JsonProperty("keyAlias")
    private String keyAlias = null;

    @JsonProperty("realmAlias")
    private String realmAlias = null;

    @JsonProperty("format")
    private String format = null;

    public KeyStoreConfigDTO realmCertificate(Boolean realmCertificate) {
        this.realmCertificate = realmCertificate;
        return this;
    }


    public Boolean isRealmCertificate() {
        return realmCertificate;
    }

    public void setRealmCertificate(Boolean realmCertificate) {
        this.realmCertificate = realmCertificate;
    }

    public KeyStoreConfigDTO storePassword(String storePassword) {
        this.storePassword = storePassword;
        return this;
    }


    public String getStorePassword() {
        return storePassword;
    }

    public void setStorePassword(String storePassword) {
        this.storePassword = storePassword;
    }

    public KeyStoreConfigDTO keyPassword(String keyPassword) {
        this.keyPassword = keyPassword;
        return this;
    }


    public String getKeyPassword() {
        return keyPassword;
    }

    public void setKeyPassword(String keyPassword) {
        this.keyPassword = keyPassword;
    }

    public KeyStoreConfigDTO keyAlias(String keyAlias) {
        this.keyAlias = keyAlias;
        return this;
    }


    public String getKeyAlias() {
        return keyAlias;
    }

    public void setKeyAlias(String keyAlias) {
        this.keyAlias = keyAlias;
    }

    public KeyStoreConfigDTO realmAlias(String realmAlias) {
        this.realmAlias = realmAlias;
        return this;
    }


    public String getRealmAlias() {
        return realmAlias;
    }

    public void setRealmAlias(String realmAlias) {
        this.realmAlias = realmAlias;
    }

    public KeyStoreConfigDTO format(String format) {
        this.format = format;
        return this;
    }


    public String getFormat() {
        return format;
    }

    public void setFormat(String format) {
        this.format = format;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        KeyStoreConfigDTO keyStoreConfig = (KeyStoreConfigDTO) o;
        return Objects.equals(this.realmCertificate, keyStoreConfig.realmCertificate) &&
                Objects.equals(this.storePassword, keyStoreConfig.storePassword) &&
                Objects.equals(this.keyPassword, keyStoreConfig.keyPassword) &&
                Objects.equals(this.keyAlias, keyStoreConfig.keyAlias) &&
                Objects.equals(this.realmAlias, keyStoreConfig.realmAlias) &&
                Objects.equals(this.format, keyStoreConfig.format);
    }

    @Override
    public int hashCode() {
        return Objects.hash(realmCertificate, storePassword, keyPassword, keyAlias, realmAlias, format);
    }

    @Override
    public String toString() {

        String sb = "class KeyStoreConfigDTO {\n" +
                "    realmCertificate: " + toIndentedString(realmCertificate) + "\n" +
                "    storePassword: " + toIndentedString(storePassword) + "\n" +
                "    keyPassword: " + toIndentedString(keyPassword) + "\n" +
                "    keyAlias: " + toIndentedString(keyAlias) + "\n" +
                "    realmAlias: " + toIndentedString(realmAlias) + "\n" +
                "    format: " + toIndentedString(format) + "\n" +
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

