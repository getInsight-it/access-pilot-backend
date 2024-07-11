package it.getinsight.module.keycloak.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Map;
import java.util.Objects;


public class EventRepresentationDTO {
    @JsonProperty("time")
    private Long time = null;

    @JsonProperty("type")
    private String type = null;

    @JsonProperty("realmId")
    private String realmId = null;

    @JsonProperty("clientId")
    private String clientId = null;

    @JsonProperty("userId")
    private String userId = null;

    @JsonProperty("sessionId")
    private String sessionId = null;

    @JsonProperty("ipAddress")
    private String ipAddress = null;

    @JsonProperty("error")
    private String error = null;

    @JsonProperty("details")
    private Map<String, String> details = null;

    public EventRepresentationDTO time(Long time) {
        this.time = time;
        return this;
    }


    public Long getTime() {
        return time;
    }

    public void setTime(Long time) {
        this.time = time;
    }

    public EventRepresentationDTO type(String type) {
        this.type = type;
        return this;
    }


    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public EventRepresentationDTO realmId(String realmId) {
        this.realmId = realmId;
        return this;
    }


    public String getRealmId() {
        return realmId;
    }

    public void setRealmId(String realmId) {
        this.realmId = realmId;
    }

    public EventRepresentationDTO clientId(String clientId) {
        this.clientId = clientId;
        return this;
    }


    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public EventRepresentationDTO userId(String userId) {
        this.userId = userId;
        return this;
    }


    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public EventRepresentationDTO sessionId(String sessionId) {
        this.sessionId = sessionId;
        return this;
    }


    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    public EventRepresentationDTO ipAddress(String ipAddress) {
        this.ipAddress = ipAddress;
        return this;
    }


    public String getIpAddress() {
        return ipAddress;
    }

    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }

    public EventRepresentationDTO error(String error) {
        this.error = error;
        return this;
    }


    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public EventRepresentationDTO details(Map<String, String> details) {
        this.details = details;
        return this;
    }

    public EventRepresentationDTO putDetailsItem(String key, String detailsItem) {
        if (this.details == null) {
            this.details = null;
        }
        this.details.put(key, detailsItem);
        return this;
    }


    public Map<String, String> getDetails() {
        return details;
    }

    public void setDetails(Map<String, String> details) {
        this.details = details;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        EventRepresentationDTO eventRepresentation = (EventRepresentationDTO) o;
        return Objects.equals(this.time, eventRepresentation.time) &&
                Objects.equals(this.type, eventRepresentation.type) &&
                Objects.equals(this.realmId, eventRepresentation.realmId) &&
                Objects.equals(this.clientId, eventRepresentation.clientId) &&
                Objects.equals(this.userId, eventRepresentation.userId) &&
                Objects.equals(this.sessionId, eventRepresentation.sessionId) &&
                Objects.equals(this.ipAddress, eventRepresentation.ipAddress) &&
                Objects.equals(this.error, eventRepresentation.error) &&
                Objects.equals(this.details, eventRepresentation.details);
    }

    @Override
    public int hashCode() {
        return Objects.hash(time, type, realmId, clientId, userId, sessionId, ipAddress, error, details);
    }

    @Override
    public String toString() {

        String sb = "class EventRepresentationDTO {\n" +
                "    time: " + toIndentedString(time) + "\n" +
                "    type: " + toIndentedString(type) + "\n" +
                "    realmId: " + toIndentedString(realmId) + "\n" +
                "    clientId: " + toIndentedString(clientId) + "\n" +
                "    userId: " + toIndentedString(userId) + "\n" +
                "    sessionId: " + toIndentedString(sessionId) + "\n" +
                "    ipAddress: " + toIndentedString(ipAddress) + "\n" +
                "    error: " + toIndentedString(error) + "\n" +
                "    details: " + toIndentedString(details) + "\n" +
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

