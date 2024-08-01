package it.getinsight.module.keycloak.dto;

import java.util.List;

public record AuthenticatorConfigInfoRepresentationDTO(String name, String helpText, String providerId,
                                                       List<ConfigPropertyRepresentationDTO> properties) {
}
