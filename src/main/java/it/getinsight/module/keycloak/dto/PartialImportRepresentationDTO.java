package it.getinsight.module.keycloak.dto;


import java.util.List;

public record PartialImportRepresentationDTO(String ifResourceExists, String policy, List<UserRepresentationDTO> users,
                                             List<ClientRepresentationDTO> clients, List<GroupRepresentationDTO> groups,
                                             List<IdentityProviderRepresentationDTO> identityProviders,
                                             List<IdentityProviderMapperRepresentationDTO> identityProviderMappers,
                                             List<RoleRepresentationDTO> roles) {
}
