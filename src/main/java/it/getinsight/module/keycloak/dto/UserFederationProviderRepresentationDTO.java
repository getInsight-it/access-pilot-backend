package it.getinsight.module.keycloak.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Map;
import java.util.Objects;


public class UserFederationProviderRepresentationDTO {
    @JsonProperty("id")
    private String id = null;

    @JsonProperty("displayName")
    private String displayName = null;

    @JsonProperty("providerName")
    private String providerName = null;

    @JsonProperty("config")
    private Map<String, String> config = null;

    @JsonProperty("priority")
    private Integer priority = null;

    @JsonProperty("fullSyncPeriod")
    private Integer fullSyncPeriod = null;

    @JsonProperty("changedSyncPeriod")
    private Integer changedSyncPeriod = null;

    @JsonProperty("lastSync")
    private Integer lastSync = null;

    public UserFederationProviderRepresentationDTO id(String id) {
        this.id = id;
        return this;
    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public UserFederationProviderRepresentationDTO displayName(String displayName) {
        this.displayName = displayName;
        return this;
    }


    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public UserFederationProviderRepresentationDTO providerName(String providerName) {
        this.providerName = providerName;
        return this;
    }


    public String getProviderName() {
        return providerName;
    }

    public void setProviderName(String providerName) {
        this.providerName = providerName;
    }

    public UserFederationProviderRepresentationDTO config(Map<String, String> config) {
        this.config = config;
        return this;
    }

    public UserFederationProviderRepresentationDTO putConfigItem(String key, String configItem) {
        if (this.config == null) {
            this.config = null;
        }
        this.config.put(key, configItem);
        return this;
    }


    public Map<String, String> getConfig() {
        return config;
    }

    public void setConfig(Map<String, String> config) {
        this.config = config;
    }

    public UserFederationProviderRepresentationDTO priority(Integer priority) {
        this.priority = priority;
        return this;
    }


    public Integer getPriority() {
        return priority;
    }

    public void setPriority(Integer priority) {
        this.priority = priority;
    }

    public UserFederationProviderRepresentationDTO fullSyncPeriod(Integer fullSyncPeriod) {
        this.fullSyncPeriod = fullSyncPeriod;
        return this;
    }


    public Integer getFullSyncPeriod() {
        return fullSyncPeriod;
    }

    public void setFullSyncPeriod(Integer fullSyncPeriod) {
        this.fullSyncPeriod = fullSyncPeriod;
    }

    public UserFederationProviderRepresentationDTO changedSyncPeriod(Integer changedSyncPeriod) {
        this.changedSyncPeriod = changedSyncPeriod;
        return this;
    }


    public Integer getChangedSyncPeriod() {
        return changedSyncPeriod;
    }

    public void setChangedSyncPeriod(Integer changedSyncPeriod) {
        this.changedSyncPeriod = changedSyncPeriod;
    }

    public UserFederationProviderRepresentationDTO lastSync(Integer lastSync) {
        this.lastSync = lastSync;
        return this;
    }


    public Integer getLastSync() {
        return lastSync;
    }

    public void setLastSync(Integer lastSync) {
        this.lastSync = lastSync;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        UserFederationProviderRepresentationDTO userFederationProviderRepresentation = (UserFederationProviderRepresentationDTO) o;
        return Objects.equals(this.id, userFederationProviderRepresentation.id) &&
                Objects.equals(this.displayName, userFederationProviderRepresentation.displayName) &&
                Objects.equals(this.providerName, userFederationProviderRepresentation.providerName) &&
                Objects.equals(this.config, userFederationProviderRepresentation.config) &&
                Objects.equals(this.priority, userFederationProviderRepresentation.priority) &&
                Objects.equals(this.fullSyncPeriod, userFederationProviderRepresentation.fullSyncPeriod) &&
                Objects.equals(this.changedSyncPeriod, userFederationProviderRepresentation.changedSyncPeriod) &&
                Objects.equals(this.lastSync, userFederationProviderRepresentation.lastSync);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, displayName, providerName, config, priority, fullSyncPeriod, changedSyncPeriod, lastSync);
    }

    @Override
    public String toString() {

        String sb = "class UserFederationProviderRepresentationDTO {\n" +
                "    id: " + toIndentedString(id) + "\n" +
                "    displayName: " + toIndentedString(displayName) + "\n" +
                "    providerName: " + toIndentedString(providerName) + "\n" +
                "    config: " + toIndentedString(config) + "\n" +
                "    priority: " + toIndentedString(priority) + "\n" +
                "    fullSyncPeriod: " + toIndentedString(fullSyncPeriod) + "\n" +
                "    changedSyncPeriod: " + toIndentedString(changedSyncPeriod) + "\n" +
                "    lastSync: " + toIndentedString(lastSync) + "\n" +
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

