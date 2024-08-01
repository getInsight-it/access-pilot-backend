package it.getinsight.module.keycloak.dto;

import java.util.List;

public record UserConsentRepresentationDTO(String clientId, List<String> grantedClientScopes, Long createdDate,
                                           Long lastUpdatedDate, List<String> grantedRealmRoles) {
}
