package it.getinsight.module.keycloak.dto;

import java.util.List;
import java.util.Map;

public record ResourceRepresentationDTO(String id, String name, String displayName,
                                        List<String> uris, String type, List<String> scopes, String iconUri,
                                        String owner, Boolean ownerManagedAccess,
                                        Map<String, List<String>> attributes) {
}
