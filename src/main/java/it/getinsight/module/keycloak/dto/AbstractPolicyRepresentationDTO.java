package it.getinsight.module.keycloak.dto;


import java.util.List;

public record AbstractPolicyRepresentationDTO(String id, String type, String decisionStrategy, String logic,
                                              String name, String description, List<PolicyRepresentationDTO> policies,
                                              List<ResourceRepresentationDTO> resources,
                                              List<ScopeRepresentationDTO> scopes, String owner,
                                              List<ResourceRepresentationDTO> resourcesData,
                                              List<ScopeRepresentationDTO> scopesData) {
}
