package it.getinsight.module.keycloak.dto;

import java.util.Map;

public record ProtocolMapperRepresentationDTO(String id, String name, String protocol, String protocolMapper,
                                              Map<String, String> config, Boolean consentRequired, String consentText) {
}
