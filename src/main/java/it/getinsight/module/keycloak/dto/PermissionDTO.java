package it.getinsight.module.keycloak.dto;

import java.util.List;
import java.util.Map;

public record PermissionDTO(String resourceId, String resourceName, List<String> scopes, Map<String, Object> claims) {
}

