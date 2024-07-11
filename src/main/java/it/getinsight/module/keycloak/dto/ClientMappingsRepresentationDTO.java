package it.getinsight.module.keycloak.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


public class ClientMappingsRepresentationDTO {
    @JsonProperty("id")
    private String id = null;

    @JsonProperty("client")
    private String client = null;

    @JsonProperty("mappings")
    private List<RoleRepresentationDTO> mappings = null;

    public ClientMappingsRepresentationDTO id(String id) {
        this.id = id;
        return this;
    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public ClientMappingsRepresentationDTO client(String client) {
        this.client = client;
        return this;
    }


    public String getClient() {
        return client;
    }

    public void setClient(String client) {
        this.client = client;
    }

    public ClientMappingsRepresentationDTO mappings(List<RoleRepresentationDTO> mappings) {
        this.mappings = mappings;
        return this;
    }

    public ClientMappingsRepresentationDTO addMappingsItem(RoleRepresentationDTO mappingsItem) {
        if (this.mappings == null) {
            this.mappings = new ArrayList<RoleRepresentationDTO>();
        }
        this.mappings.add(mappingsItem);
        return this;
    }


    public List<RoleRepresentationDTO> getMappings() {
        return mappings;
    }

    public void setMappings(List<RoleRepresentationDTO> mappings) {
        this.mappings = mappings;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ClientMappingsRepresentationDTO clientMappingsRepresentation = (ClientMappingsRepresentationDTO) o;
        return Objects.equals(this.id, clientMappingsRepresentation.id) &&
                Objects.equals(this.client, clientMappingsRepresentation.client) &&
                Objects.equals(this.mappings, clientMappingsRepresentation.mappings);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, client, mappings);
    }

    @Override
    public String toString() {

        String sb = "class ClientMappingsRepresentationDTO {\n" +
                "    id: " + toIndentedString(id) + "\n" +
                "    client: " + toIndentedString(client) + "\n" +
                "    mappings: " + toIndentedString(mappings) + "\n" +
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

