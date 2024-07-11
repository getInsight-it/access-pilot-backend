package it.getinsight.module.keycloak.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Objects;


public class ClientInitialAccessPresentationDTO {
    @JsonProperty("id")
    private String id = null;

    @JsonProperty("token")
    private String token = null;

    @JsonProperty("timestamp")
    private Integer timestamp = null;

    @JsonProperty("expiration")
    private Integer expiration = null;

    @JsonProperty("count")
    private Integer count = null;

    @JsonProperty("remainingCount")
    private Integer remainingCount = null;

    public ClientInitialAccessPresentationDTO id(String id) {
        this.id = id;
        return this;
    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public ClientInitialAccessPresentationDTO token(String token) {
        this.token = token;
        return this;
    }


    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public ClientInitialAccessPresentationDTO timestamp(Integer timestamp) {
        this.timestamp = timestamp;
        return this;
    }


    public Integer getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Integer timestamp) {
        this.timestamp = timestamp;
    }

    public ClientInitialAccessPresentationDTO expiration(Integer expiration) {
        this.expiration = expiration;
        return this;
    }


    public Integer getExpiration() {
        return expiration;
    }

    public void setExpiration(Integer expiration) {
        this.expiration = expiration;
    }

    public ClientInitialAccessPresentationDTO count(Integer count) {
        this.count = count;
        return this;
    }


    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    public ClientInitialAccessPresentationDTO remainingCount(Integer remainingCount) {
        this.remainingCount = remainingCount;
        return this;
    }


    public Integer getRemainingCount() {
        return remainingCount;
    }

    public void setRemainingCount(Integer remainingCount) {
        this.remainingCount = remainingCount;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ClientInitialAccessPresentationDTO clientInitialAccessPresentation = (ClientInitialAccessPresentationDTO) o;
        return Objects.equals(this.id, clientInitialAccessPresentation.id) &&
                Objects.equals(this.token, clientInitialAccessPresentation.token) &&
                Objects.equals(this.timestamp, clientInitialAccessPresentation.timestamp) &&
                Objects.equals(this.expiration, clientInitialAccessPresentation.expiration) &&
                Objects.equals(this.count, clientInitialAccessPresentation.count) &&
                Objects.equals(this.remainingCount, clientInitialAccessPresentation.remainingCount);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, token, timestamp, expiration, count, remainingCount);
    }

    @Override
    public String toString() {

        String sb = "class ClientInitialAccessPresentationDTO {\n" +
                "    id: " + toIndentedString(id) + "\n" +
                "    token: " + toIndentedString(token) + "\n" +
                "    timestamp: " + toIndentedString(timestamp) + "\n" +
                "    expiration: " + toIndentedString(expiration) + "\n" +
                "    count: " + toIndentedString(count) + "\n" +
                "    remainingCount: " + toIndentedString(remainingCount) + "\n" +
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

