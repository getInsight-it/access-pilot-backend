package it.getinsight.module.keycloak.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


public class RealmEventsConfigRepresentationDTO {
    @JsonProperty("eventsEnabled")
    private Boolean eventsEnabled = null;

    @JsonProperty("eventsExpiration")
    private Long eventsExpiration = null;

    @JsonProperty("eventsListeners")
    private List<String> eventsListeners = null;

    @JsonProperty("enabledEventTypes")
    private List<String> enabledEventTypes = null;

    @JsonProperty("adminEventsEnabled")
    private Boolean adminEventsEnabled = null;

    @JsonProperty("adminEventsDetailsEnabled")
    private Boolean adminEventsDetailsEnabled = null;

    public RealmEventsConfigRepresentationDTO eventsEnabled(Boolean eventsEnabled) {
        this.eventsEnabled = eventsEnabled;
        return this;
    }


    public Boolean isEventsEnabled() {
        return eventsEnabled;
    }

    public void setEventsEnabled(Boolean eventsEnabled) {
        this.eventsEnabled = eventsEnabled;
    }

    public RealmEventsConfigRepresentationDTO eventsExpiration(Long eventsExpiration) {
        this.eventsExpiration = eventsExpiration;
        return this;
    }


    public Long getEventsExpiration() {
        return eventsExpiration;
    }

    public void setEventsExpiration(Long eventsExpiration) {
        this.eventsExpiration = eventsExpiration;
    }

    public RealmEventsConfigRepresentationDTO eventsListeners(List<String> eventsListeners) {
        this.eventsListeners = eventsListeners;
        return this;
    }

    public RealmEventsConfigRepresentationDTO addEventsListenersItem(String eventsListenersItem) {
        if (this.eventsListeners == null) {
            this.eventsListeners = new ArrayList<String>();
        }
        this.eventsListeners.add(eventsListenersItem);
        return this;
    }


    public List<String> getEventsListeners() {
        return eventsListeners;
    }

    public void setEventsListeners(List<String> eventsListeners) {
        this.eventsListeners = eventsListeners;
    }

    public RealmEventsConfigRepresentationDTO enabledEventTypes(List<String> enabledEventTypes) {
        this.enabledEventTypes = enabledEventTypes;
        return this;
    }

    public RealmEventsConfigRepresentationDTO addEnabledEventTypesItem(String enabledEventTypesItem) {
        if (this.enabledEventTypes == null) {
            this.enabledEventTypes = new ArrayList<String>();
        }
        this.enabledEventTypes.add(enabledEventTypesItem);
        return this;
    }


    public List<String> getEnabledEventTypes() {
        return enabledEventTypes;
    }

    public void setEnabledEventTypes(List<String> enabledEventTypes) {
        this.enabledEventTypes = enabledEventTypes;
    }

    public RealmEventsConfigRepresentationDTO adminEventsEnabled(Boolean adminEventsEnabled) {
        this.adminEventsEnabled = adminEventsEnabled;
        return this;
    }


    public Boolean isAdminEventsEnabled() {
        return adminEventsEnabled;
    }

    public void setAdminEventsEnabled(Boolean adminEventsEnabled) {
        this.adminEventsEnabled = adminEventsEnabled;
    }

    public RealmEventsConfigRepresentationDTO adminEventsDetailsEnabled(Boolean adminEventsDetailsEnabled) {
        this.adminEventsDetailsEnabled = adminEventsDetailsEnabled;
        return this;
    }


    public Boolean isAdminEventsDetailsEnabled() {
        return adminEventsDetailsEnabled;
    }

    public void setAdminEventsDetailsEnabled(Boolean adminEventsDetailsEnabled) {
        this.adminEventsDetailsEnabled = adminEventsDetailsEnabled;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        RealmEventsConfigRepresentationDTO realmEventsConfigRepresentation = (RealmEventsConfigRepresentationDTO) o;
        return Objects.equals(this.eventsEnabled, realmEventsConfigRepresentation.eventsEnabled) &&
                Objects.equals(this.eventsExpiration, realmEventsConfigRepresentation.eventsExpiration) &&
                Objects.equals(this.eventsListeners, realmEventsConfigRepresentation.eventsListeners) &&
                Objects.equals(this.enabledEventTypes, realmEventsConfigRepresentation.enabledEventTypes) &&
                Objects.equals(this.adminEventsEnabled, realmEventsConfigRepresentation.adminEventsEnabled) &&
                Objects.equals(this.adminEventsDetailsEnabled, realmEventsConfigRepresentation.adminEventsDetailsEnabled);
    }

    @Override
    public int hashCode() {
        return Objects.hash(eventsEnabled, eventsExpiration, eventsListeners, enabledEventTypes, adminEventsEnabled, adminEventsDetailsEnabled);
    }

    @Override
    public String toString() {

        String sb = "class RealmEventsConfigRepresentationDTO {\n" +
                "    eventsEnabled: " + toIndentedString(eventsEnabled) + "\n" +
                "    eventsExpiration: " + toIndentedString(eventsExpiration) + "\n" +
                "    eventsListeners: " + toIndentedString(eventsListeners) + "\n" +
                "    enabledEventTypes: " + toIndentedString(enabledEventTypes) + "\n" +
                "    adminEventsEnabled: " + toIndentedString(adminEventsEnabled) + "\n" +
                "    adminEventsDetailsEnabled: " + toIndentedString(adminEventsDetailsEnabled) + "\n" +
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

