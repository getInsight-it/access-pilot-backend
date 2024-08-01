package it.getinsight.module.keycloak.dto;

import java.util.List;
import java.util.Map;

public record GroupRepresentationDTO(String id, String name, String path, List<String> realmRoles,
                                     Map<String, List<String>> clientRoles, Map<String, List<String>> attributes,
                                     List<GroupRepresentationDTO> subGroups, Map<String, Boolean> access) {
}
