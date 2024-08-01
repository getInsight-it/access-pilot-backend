package it.getinsight.module.keycloak.dto;

import java.util.Map;

public record ManagementPermissionReferenceDTO(Boolean enabled, String resource, Map<String, String> scopePermissions) {
}
