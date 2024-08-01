package it.getinsight.module.keycloak.dto;

import java.util.List;
import java.util.Map;

public record ClientScopeRepresentationDTO(String id, String name, String description,
                                           List<ProtocolMapperRepresentationDTO> protocolMappers, String protocol,
                                           Map<String, String> attributes) {
}
