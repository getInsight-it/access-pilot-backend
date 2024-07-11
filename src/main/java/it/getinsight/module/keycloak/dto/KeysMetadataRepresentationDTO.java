package it.getinsight.module.keycloak.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;


public class KeysMetadataRepresentationDTO {
    @JsonProperty("active")
    private Map<String, String> active = null;

    @JsonProperty("keys")
    private List<KeyMetadataRepresentationDTO> keys = null;

    public KeysMetadataRepresentationDTO active(Map<String, String> active) {
        this.active = active;
        return this;
    }

    public KeysMetadataRepresentationDTO putActiveItem(String key, String activeItem) {
        if (this.active == null) {
            this.active = null;
        }
        this.active.put(key, activeItem);
        return this;
    }


    public Map<String, String> getActive() {
        return active;
    }

    public void setActive(Map<String, String> active) {
        this.active = active;
    }

    public KeysMetadataRepresentationDTO keys(List<KeyMetadataRepresentationDTO> keys) {
        this.keys = keys;
        return this;
    }

    public KeysMetadataRepresentationDTO addKeysItem(KeyMetadataRepresentationDTO keysItem) {
        if (this.keys == null) {
            this.keys = new ArrayList<KeyMetadataRepresentationDTO>();
        }
        this.keys.add(keysItem);
        return this;
    }


    public List<KeyMetadataRepresentationDTO> getKeys() {
        return keys;
    }

    public void setKeys(List<KeyMetadataRepresentationDTO> keys) {
        this.keys = keys;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        KeysMetadataRepresentationDTO keysMetadataRepresentation = (KeysMetadataRepresentationDTO) o;
        return Objects.equals(this.active, keysMetadataRepresentation.active) &&
                Objects.equals(this.keys, keysMetadataRepresentation.keys);
    }

    @Override
    public int hashCode() {
        return Objects.hash(active, keys);
    }

    @Override
    public String toString() {

        String sb = "class KeysMetadataRepresentationDTO {\n" +
                "    active: " + toIndentedString(active) + "\n" +
                "    keys: " + toIndentedString(keys) + "\n" +
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

