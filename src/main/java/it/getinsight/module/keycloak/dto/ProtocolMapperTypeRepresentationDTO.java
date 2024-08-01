package it.getinsight.module.keycloak.dto;

import java.util.List;

public record ProtocolMapperTypeRepresentationDTO(String id, String name, String category, String helpText,
                                                  Integer priority, List<ConfigPropertyRepresentationDTO> properties) {
}
