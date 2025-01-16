package it.getinsight.module.keycloak.dto;

public record UserFederationMapperSyncConfigRepresentationDTO(Boolean fedToKeycloakSyncSupported,
                                                              String fedToKeycloakSyncMessage,
                                                              Boolean keycloakToFedSyncSupported,
                                                              String keycloakToFedSyncMessage) {
}
