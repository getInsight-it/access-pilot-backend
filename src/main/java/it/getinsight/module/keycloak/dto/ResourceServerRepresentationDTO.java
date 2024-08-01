package it.getinsight.module.keycloak.dto;

import java.util.List;

public record ResourceServerRepresentationDTO(String id, String clientId, String name,
                                              Boolean allowRemoteResourceManagement, String policyEnforcementMode,
                                              List<ResourceRepresentationDTO> resources,
                                              List<PolicyRepresentationDTO> policies,
                                              List<ScopeRepresentationDTO> scopes, String decisionStrategy) {
}
