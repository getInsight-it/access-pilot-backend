package it.getinsight.module.keycloak.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Map;
import java.util.Objects;


public class UserSessionRepresentationDTO {
    @JsonProperty("id")
    private String id = null;

    @JsonProperty("username")
    private String username = null;

    @JsonProperty("userId")
    private String userId = null;

    @JsonProperty("ipAddress")
    private String ipAddress = null;

    @JsonProperty("start")
    private Long start = null;

    @JsonProperty("lastAccess")
    private Long lastAccess = null;

    @JsonProperty("clients")
    private Map<String, String> clients = null;

    public UserSessionRepresentationDTO id(String id) {
        this.id = id;
        return this;
    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public UserSessionRepresentationDTO username(String username) {
        this.username = username;
        return this;
    }


    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public UserSessionRepresentationDTO userId(String userId) {
        this.userId = userId;
        return this;
    }


    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public UserSessionRepresentationDTO ipAddress(String ipAddress) {
        this.ipAddress = ipAddress;
        return this;
    }


    public String getIpAddress() {
        return ipAddress;
    }

    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }

    public UserSessionRepresentationDTO start(Long start) {
        this.start = start;
        return this;
    }


    public Long getStart() {
        return start;
    }

    public void setStart(Long start) {
        this.start = start;
    }

    public UserSessionRepresentationDTO lastAccess(Long lastAccess) {
        this.lastAccess = lastAccess;
        return this;
    }


    public Long getLastAccess() {
        return lastAccess;
    }

    public void setLastAccess(Long lastAccess) {
        this.lastAccess = lastAccess;
    }

    public UserSessionRepresentationDTO clients(Map<String, String> clients) {
        this.clients = clients;
        return this;
    }

    public UserSessionRepresentationDTO putClientsItem(String key, String clientsItem) {
        if (this.clients == null) {
            this.clients = null;
        }
        this.clients.put(key, clientsItem);
        return this;
    }


    public Map<String, String> getClients() {
        return clients;
    }

    public void setClients(Map<String, String> clients) {
        this.clients = clients;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        UserSessionRepresentationDTO userSessionRepresentation = (UserSessionRepresentationDTO) o;
        return Objects.equals(this.id, userSessionRepresentation.id) &&
                Objects.equals(this.username, userSessionRepresentation.username) &&
                Objects.equals(this.userId, userSessionRepresentation.userId) &&
                Objects.equals(this.ipAddress, userSessionRepresentation.ipAddress) &&
                Objects.equals(this.start, userSessionRepresentation.start) &&
                Objects.equals(this.lastAccess, userSessionRepresentation.lastAccess) &&
                Objects.equals(this.clients, userSessionRepresentation.clients);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, username, userId, ipAddress, start, lastAccess, clients);
    }

    @Override
    public String toString() {

        String sb = "class UserSessionRepresentationDTO {\n" +
                "    id: " + toIndentedString(id) + "\n" +
                "    username: " + toIndentedString(username) + "\n" +
                "    userId: " + toIndentedString(userId) + "\n" +
                "    ipAddress: " + toIndentedString(ipAddress) + "\n" +
                "    start: " + toIndentedString(start) + "\n" +
                "    lastAccess: " + toIndentedString(lastAccess) + "\n" +
                "    clients: " + toIndentedString(clients) + "\n" +
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

