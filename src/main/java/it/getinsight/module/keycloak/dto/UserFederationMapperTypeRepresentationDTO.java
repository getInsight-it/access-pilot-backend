package it.getinsight.module.keycloak.dto;

import java.util.List;
import java.util.Map;

public record UserFederationMapperTypeRepresentationDTO(String id, String name, String category, String helpText,
                                                        UserFederationMapperSyncConfigRepresentationDTO syncConfig,
                                                        List<ConfigPropertyRepresentationDTO> properties,
                                                        Map<String, String> defaultConfig) {
}
