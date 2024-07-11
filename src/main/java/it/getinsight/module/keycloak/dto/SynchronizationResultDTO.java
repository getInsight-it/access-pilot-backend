package it.getinsight.module.keycloak.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Objects;


public class SynchronizationResultDTO {
    @JsonProperty("ignored")
    private Boolean ignored = null;

    @JsonProperty("added")
    private Integer added = null;

    @JsonProperty("updated")
    private Integer updated = null;

    @JsonProperty("removed")
    private Integer removed = null;

    @JsonProperty("failed")
    private Integer failed = null;

    @JsonProperty("status")
    private String status = null;

    public SynchronizationResultDTO ignored(Boolean ignored) {
        this.ignored = ignored;
        return this;
    }


    public Boolean isIgnored() {
        return ignored;
    }

    public void setIgnored(Boolean ignored) {
        this.ignored = ignored;
    }

    public SynchronizationResultDTO added(Integer added) {
        this.added = added;
        return this;
    }


    public Integer getAdded() {
        return added;
    }

    public void setAdded(Integer added) {
        this.added = added;
    }

    public SynchronizationResultDTO updated(Integer updated) {
        this.updated = updated;
        return this;
    }


    public Integer getUpdated() {
        return updated;
    }

    public void setUpdated(Integer updated) {
        this.updated = updated;
    }

    public SynchronizationResultDTO removed(Integer removed) {
        this.removed = removed;
        return this;
    }


    public Integer getRemoved() {
        return removed;
    }

    public void setRemoved(Integer removed) {
        this.removed = removed;
    }

    public SynchronizationResultDTO failed(Integer failed) {
        this.failed = failed;
        return this;
    }


    public Integer getFailed() {
        return failed;
    }

    public void setFailed(Integer failed) {
        this.failed = failed;
    }

    public SynchronizationResultDTO status(String status) {
        this.status = status;
        return this;
    }


    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        SynchronizationResultDTO synchronizationResult = (SynchronizationResultDTO) o;
        return Objects.equals(this.ignored, synchronizationResult.ignored) &&
                Objects.equals(this.added, synchronizationResult.added) &&
                Objects.equals(this.updated, synchronizationResult.updated) &&
                Objects.equals(this.removed, synchronizationResult.removed) &&
                Objects.equals(this.failed, synchronizationResult.failed) &&
                Objects.equals(this.status, synchronizationResult.status);
    }

    @Override
    public int hashCode() {
        return Objects.hash(ignored, added, updated, removed, failed, status);
    }

    @Override
    public String toString() {

        String sb = "class SynchronizationResultDTO {\n" +
                "    ignored: " + toIndentedString(ignored) + "\n" +
                "    added: " + toIndentedString(added) + "\n" +
                "    updated: " + toIndentedString(updated) + "\n" +
                "    removed: " + toIndentedString(removed) + "\n" +
                "    failed: " + toIndentedString(failed) + "\n" +
                "    status: " + toIndentedString(status) + "\n" +
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

