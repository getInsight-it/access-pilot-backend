package it.getinsight.module.keycloak.dto;

import java.util.List;

public record ConfigPropertyRepresentationDTO(String name, String label, String type, String defaultValue,
                                              String helpText, List<String> options, Boolean secret, Boolean readOnly) {
}
