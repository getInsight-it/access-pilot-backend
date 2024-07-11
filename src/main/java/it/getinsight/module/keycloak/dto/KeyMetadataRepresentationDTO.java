package it.getinsight.module.keycloak.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Objects;


public class KeyMetadataRepresentationDTO {
    @JsonProperty("providerId")
    private String providerId = null;

    @JsonProperty("providerPriority")
    private Long providerPriority = null;

    @JsonProperty("kid")
    private String kid = null;

    @JsonProperty("status")
    private String status = null;

    @JsonProperty("type")
    private String type = null;

    @JsonProperty("algorithm")
    private String algorithm = null;

    @JsonProperty("publicKey")
    private String publicKey = null;

    @JsonProperty("certificate")
    private String certificate = null;

    @JsonProperty("use")
    private Object use = null;

    public KeyMetadataRepresentationDTO providerId(String providerId) {
        this.providerId = providerId;
        return this;
    }


    public String getProviderId() {
        return providerId;
    }

    public void setProviderId(String providerId) {
        this.providerId = providerId;
    }

    public KeyMetadataRepresentationDTO providerPriority(Long providerPriority) {
        this.providerPriority = providerPriority;
        return this;
    }


    public Long getProviderPriority() {
        return providerPriority;
    }

    public void setProviderPriority(Long providerPriority) {
        this.providerPriority = providerPriority;
    }

    public KeyMetadataRepresentationDTO kid(String kid) {
        this.kid = kid;
        return this;
    }


    public String getKid() {
        return kid;
    }

    public void setKid(String kid) {
        this.kid = kid;
    }

    public KeyMetadataRepresentationDTO status(String status) {
        this.status = status;
        return this;
    }


    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public KeyMetadataRepresentationDTO type(String type) {
        this.type = type;
        return this;
    }


    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public KeyMetadataRepresentationDTO algorithm(String algorithm) {
        this.algorithm = algorithm;
        return this;
    }


    public String getAlgorithm() {
        return algorithm;
    }

    public void setAlgorithm(String algorithm) {
        this.algorithm = algorithm;
    }

    public KeyMetadataRepresentationDTO publicKey(String publicKey) {
        this.publicKey = publicKey;
        return this;
    }


    public String getPublicKey() {
        return publicKey;
    }

    public void setPublicKey(String publicKey) {
        this.publicKey = publicKey;
    }

    public KeyMetadataRepresentationDTO certificate(String certificate) {
        this.certificate = certificate;
        return this;
    }


    public String getCertificate() {
        return certificate;
    }

    public void setCertificate(String certificate) {
        this.certificate = certificate;
    }

    public KeyMetadataRepresentationDTO use(Object use) {
        this.use = use;
        return this;
    }


    public Object getUse() {
        return use;
    }

    public void setUse(Object use) {
        this.use = use;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        KeyMetadataRepresentationDTO keyMetadataRepresentation = (KeyMetadataRepresentationDTO) o;
        return Objects.equals(this.providerId, keyMetadataRepresentation.providerId) &&
                Objects.equals(this.providerPriority, keyMetadataRepresentation.providerPriority) &&
                Objects.equals(this.kid, keyMetadataRepresentation.kid) &&
                Objects.equals(this.status, keyMetadataRepresentation.status) &&
                Objects.equals(this.type, keyMetadataRepresentation.type) &&
                Objects.equals(this.algorithm, keyMetadataRepresentation.algorithm) &&
                Objects.equals(this.publicKey, keyMetadataRepresentation.publicKey) &&
                Objects.equals(this.certificate, keyMetadataRepresentation.certificate) &&
                Objects.equals(this.use, keyMetadataRepresentation.use);
    }

    @Override
    public int hashCode() {
        return Objects.hash(providerId, providerPriority, kid, status, type, algorithm, publicKey, certificate, use);
    }

    @Override
    public String toString() {

        String sb = "class KeyMetadataRepresentationDTO {\n" +
                "    providerId: " + toIndentedString(providerId) + "\n" +
                "    providerPriority: " + toIndentedString(providerPriority) + "\n" +
                "    kid: " + toIndentedString(kid) + "\n" +
                "    status: " + toIndentedString(status) + "\n" +
                "    type: " + toIndentedString(type) + "\n" +
                "    algorithm: " + toIndentedString(algorithm) + "\n" +
                "    publicKey: " + toIndentedString(publicKey) + "\n" +
                "    certificate: " + toIndentedString(certificate) + "\n" +
                "    use: " + toIndentedString(use) + "\n" +
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

