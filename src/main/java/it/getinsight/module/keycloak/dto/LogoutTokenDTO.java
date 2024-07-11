package it.getinsight.module.keycloak.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonValue;

import java.util.Map;
import java.util.Objects;


public class LogoutTokenDTO {
    @JsonProperty("events")
    private Map<String, Object> events = null;

    @JsonProperty("sid")
    private String sid = null;
    @JsonProperty("category")
    private CategoryEnum category = null;

    public LogoutTokenDTO events(Map<String, Object> events) {
        this.events = events;
        return this;
    }

    public LogoutTokenDTO putEventsItem(String key, Object eventsItem) {
        if (this.events == null) {
            this.events = null;
        }
        this.events.put(key, eventsItem);
        return this;
    }


    public Map<String, Object> getEvents() {
        return events;
    }

    public void setEvents(Map<String, Object> events) {
        this.events = events;
    }

    public LogoutTokenDTO sid(String sid) {
        this.sid = sid;
        return this;
    }


    public String getSid() {
        return sid;
    }

    public void setSid(String sid) {
        this.sid = sid;
    }

    public LogoutTokenDTO category(CategoryEnum category) {
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
        LogoutTokenDTO logoutToken = (LogoutTokenDTO) o;
        return Objects.equals(this.events, logoutToken.events) &&
                Objects.equals(this.sid, logoutToken.sid) &&
                Objects.equals(this.category, logoutToken.category);
    }

    @Override
    public int hashCode() {
        return Objects.hash(events, sid, category);
    }

    @Override
    public String toString() {

        String sb = "class LogoutTokenDTO {\n" +
                "    events: " + toIndentedString(events) + "\n" +
                "    sid: " + toIndentedString(sid) + "\n" +
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

