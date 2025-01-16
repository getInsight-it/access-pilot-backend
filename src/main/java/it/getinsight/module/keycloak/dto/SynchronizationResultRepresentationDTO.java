package it.getinsight.module.keycloak.dto;

public record SynchronizationResultRepresentationDTO(Integer ignored, Integer added, Integer updated, Integer removed,
                                                     Integer failed, String status) {
}
