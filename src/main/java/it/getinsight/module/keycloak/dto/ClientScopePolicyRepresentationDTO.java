package it.getinsight.module.keycloak.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


public class ClientScopePolicyRepresentationDTO {
    @JsonProperty("type")
    private String type = null;

    @JsonProperty("clientScopes")
    private List<ClientScopeDefinitionDTO> clientScopes = null;

    public ClientScopePolicyRepresentationDTO type(String type) {
        this.type = type;
        return this;
    }


    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public ClientScopePolicyRepresentationDTO clientScopes(List<ClientScopeDefinitionDTO> clientScopes) {
        this.clientScopes = clientScopes;
        return this;
    }

    public ClientScopePolicyRepresentationDTO addClientScopesItem(ClientScopeDefinitionDTO clientScopesItem) {
        if (this.clientScopes == null) {
            this.clientScopes = new ArrayList<ClientScopeDefinitionDTO>();
        }
        this.clientScopes.add(clientScopesItem);
        return this;
    }


    public List<ClientScopeDefinitionDTO> getClientScopes() {
        return clientScopes;
    }

    public void setClientScopes(List<ClientScopeDefinitionDTO> clientScopes) {
        this.clientScopes = clientScopes;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ClientScopePolicyRepresentationDTO clientScopePolicyRepresentation = (ClientScopePolicyRepresentationDTO) o;
        return Objects.equals(this.type, clientScopePolicyRepresentation.type) &&
                Objects.equals(this.clientScopes, clientScopePolicyRepresentation.clientScopes);
    }

    @Override
    public int hashCode() {
        return Objects.hash(type, clientScopes);
    }

    @Override
    public String toString() {

        String sb = "class ClientScopePolicyRepresentationDTO {\n" +
                "    type: " + toIndentedString(type) + "\n" +
                "    clientScopes: " + toIndentedString(clientScopes) + "\n" +
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

