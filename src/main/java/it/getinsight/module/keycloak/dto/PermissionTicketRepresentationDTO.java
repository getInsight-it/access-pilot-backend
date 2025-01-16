package it.getinsight.module.keycloak.dto;

public record PermissionTicketRepresentationDTO(String id, String owner, String resource, String scope, Boolean granted,
                                                String scopeName, String resourceName, String requesterName,
                                                String requester, String ownerName) {
}
