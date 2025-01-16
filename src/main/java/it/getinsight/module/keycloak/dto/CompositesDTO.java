package it.getinsight.module.keycloak.dto;

import java.util.List;
import java.util.Map;

public record CompositesDTO(List<String> realm, Map<String, List<String>> client,
                            Map<String, List<String>> application) {
}
