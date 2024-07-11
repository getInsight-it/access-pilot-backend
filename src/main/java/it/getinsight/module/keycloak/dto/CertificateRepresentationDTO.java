package it.getinsight.module.keycloak.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Objects;


public class CertificateRepresentationDTO {
    @JsonProperty("privateKey")
    private String privateKey = null;

    @JsonProperty("publicKey")
    private String publicKey = null;

    @JsonProperty("certificate")
    private String certificate = null;

    @JsonProperty("kid")
    private String kid = null;

    public CertificateRepresentationDTO privateKey(String privateKey) {
        this.privateKey = privateKey;
        return this;
    }


    public String getPrivateKey() {
        return privateKey;
    }

    public void setPrivateKey(String privateKey) {
        this.privateKey = privateKey;
    }

    public CertificateRepresentationDTO publicKey(String publicKey) {
        this.publicKey = publicKey;
        return this;
    }


    public String getPublicKey() {
        return publicKey;
    }

    public void setPublicKey(String publicKey) {
        this.publicKey = publicKey;
    }

    public CertificateRepresentationDTO certificate(String certificate) {
        this.certificate = certificate;
        return this;
    }


    public String getCertificate() {
        return certificate;
    }

    public void setCertificate(String certificate) {
        this.certificate = certificate;
    }

    public CertificateRepresentationDTO kid(String kid) {
        this.kid = kid;
        return this;
    }


    public String getKid() {
        return kid;
    }

    public void setKid(String kid) {
        this.kid = kid;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        CertificateRepresentationDTO certificateRepresentation = (CertificateRepresentationDTO) o;
        return Objects.equals(this.privateKey, certificateRepresentation.privateKey) &&
                Objects.equals(this.publicKey, certificateRepresentation.publicKey) &&
                Objects.equals(this.certificate, certificateRepresentation.certificate) &&
                Objects.equals(this.kid, certificateRepresentation.kid);
    }

    @Override
    public int hashCode() {
        return Objects.hash(privateKey, publicKey, certificate, kid);
    }

    @Override
    public String toString() {

        String sb = "class CertificateRepresentationDTO {\n" +
                "    privateKey: " + toIndentedString(privateKey) + "\n" +
                "    publicKey: " + toIndentedString(publicKey) + "\n" +
                "    certificate: " + toIndentedString(certificate) + "\n" +
                "    kid: " + toIndentedString(kid) + "\n" +
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

