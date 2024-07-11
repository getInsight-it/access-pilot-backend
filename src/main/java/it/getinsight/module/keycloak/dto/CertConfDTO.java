package it.getinsight.module.keycloak.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Objects;


public class CertConfDTO {
    @JsonProperty("certThumbprint")
    private String certThumbprint = null;

    public CertConfDTO certThumbprint(String certThumbprint) {
        this.certThumbprint = certThumbprint;
        return this;
    }


    public String getCertThumbprint() {
        return certThumbprint;
    }

    public void setCertThumbprint(String certThumbprint) {
        this.certThumbprint = certThumbprint;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        CertConfDTO certConf = (CertConfDTO) o;
        return Objects.equals(this.certThumbprint, certConf.certThumbprint);
    }

    @Override
    public int hashCode() {
        return Objects.hash(certThumbprint);
    }

    @Override
    public String toString() {

        String sb = "class CertConfDTO {\n" +
                "    certThumbprint: " + toIndentedString(certThumbprint) + "\n" +
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

