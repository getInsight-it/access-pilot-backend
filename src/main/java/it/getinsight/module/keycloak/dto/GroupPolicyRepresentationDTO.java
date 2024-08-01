package it.getinsight.module.keycloak.dto;

import java.util.List;

public record GroupPolicyRepresentationDTO(String type, String groupsClaim, List<GroupRepresentationDTO> groups) {
}
