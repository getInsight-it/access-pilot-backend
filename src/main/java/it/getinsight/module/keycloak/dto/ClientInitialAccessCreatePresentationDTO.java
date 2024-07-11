package it.getinsight.module.keycloak.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Objects;


public class ClientInitialAccessCreatePresentationDTO {
    @JsonProperty("expiration")
    private Integer expiration = null;

    @JsonProperty("count")
    private Integer count = null;

    public ClientInitialAccessCreatePresentationDTO expiration(Integer expiration) {
        this.expiration = expiration;
        return this;
    }


    public Integer getExpiration() {
        return expiration;
    }

    public void setExpiration(Integer expiration) {
        this.expiration = expiration;
    }

    public ClientInitialAccessCreatePresentationDTO count(Integer count) {
        this.count = count;
        return this;
    }


    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ClientInitialAccessCreatePresentationDTO clientInitialAccessCreatePresentation = (ClientInitialAccessCreatePresentationDTO) o;
        return Objects.equals(this.expiration, clientInitialAccessCreatePresentation.expiration) &&
                Objects.equals(this.count, clientInitialAccessCreatePresentation.count);
    }

    @Override
    public int hashCode() {
        return Objects.hash(expiration, count);
    }

    @Override
    public String toString() {

        String sb = "class ClientInitialAccessCreatePresentationDTO {\n" +
                "    expiration: " + toIndentedString(expiration) + "\n" +
                "    count: " + toIndentedString(count) + "\n" +
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

