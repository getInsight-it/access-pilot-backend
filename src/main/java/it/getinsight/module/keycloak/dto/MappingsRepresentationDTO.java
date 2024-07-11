package it.getinsight.module.keycloak.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;


public class MappingsRepresentationDTO {
    @JsonProperty("realmMappings")
    private List<RoleRepresentationDTO> realmMappings = null;

    @JsonProperty("clientMappings")
    private Map<String, ClientMappingsRepresentationDTO> clientMappings = null;

    public MappingsRepresentationDTO realmMappings(List<RoleRepresentationDTO> realmMappings) {
        this.realmMappings = realmMappings;
        return this;
    }

    public MappingsRepresentationDTO addRealmMappingsItem(RoleRepresentationDTO realmMappingsItem) {
        if (this.realmMappings == null) {
            this.realmMappings = new ArrayList<RoleRepresentationDTO>();
        }
        this.realmMappings.add(realmMappingsItem);
        return this;
    }


    public List<RoleRepresentationDTO> getRealmMappings() {
        return realmMappings;
    }

    public void setRealmMappings(List<RoleRepresentationDTO> realmMappings) {
        this.realmMappings = realmMappings;
    }

    public MappingsRepresentationDTO clientMappings(Map<String, ClientMappingsRepresentationDTO> clientMappings) {
        this.clientMappings = clientMappings;
        return this;
    }

    public MappingsRepresentationDTO putClientMappingsItem(String key, ClientMappingsRepresentationDTO clientMappingsItem) {
        if (this.clientMappings == null) {
            this.clientMappings = null;
        }
        this.clientMappings.put(key, clientMappingsItem);
        return this;
    }


    public Map<String, ClientMappingsRepresentationDTO> getClientMappings() {
        return clientMappings;
    }

    public void setClientMappings(Map<String, ClientMappingsRepresentationDTO> clientMappings) {
        this.clientMappings = clientMappings;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        MappingsRepresentationDTO mappingsRepresentation = (MappingsRepresentationDTO) o;
        return Objects.equals(this.realmMappings, mappingsRepresentation.realmMappings) &&
                Objects.equals(this.clientMappings, mappingsRepresentation.clientMappings);
    }

    @Override
    public int hashCode() {
        return Objects.hash(realmMappings, clientMappings);
    }

    @Override
    public String toString() {

        String sb = "class MappingsRepresentationDTO {\n" +
                "    realmMappings: " + toIndentedString(realmMappings) + "\n" +
                "    clientMappings: " + toIndentedString(clientMappings) + "\n" +
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

