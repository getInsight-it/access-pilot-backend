package it.getinsight.module.keycloak.dto;

import java.util.List;

public record ClientScopePolicyRepresentationDTO(String type, List<ClientScopeRepresentationDTO> clientScopes) {
}
