package it.getinsight.module.keycloak.dto;

public record AdminEventRepresentationDTO(Long time, String realmId, AuthDetailsRepresentationDTO authDetails,
                                          String operationType, String resourceType, String resourcePath,
                                          String representation, String error) {
}
