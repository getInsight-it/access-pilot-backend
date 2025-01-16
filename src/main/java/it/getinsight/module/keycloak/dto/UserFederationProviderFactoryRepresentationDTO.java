package it.getinsight.module.keycloak.dto;

import java.util.List;
import java.util.Map;

public record UserFederationProviderFactoryRepresentationDTO(String id, Map<String, String> options, String helpText,
                                                             List<ConfigPropertyRepresentationDTO> properties) {
}
