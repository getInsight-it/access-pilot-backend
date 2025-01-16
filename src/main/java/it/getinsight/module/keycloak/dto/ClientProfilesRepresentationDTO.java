package it.getinsight.module.keycloak.dto;

import java.util.List;

public record ClientProfilesRepresentationDTO(List<ClientProfileRepresentationDTO> profiles,
                                              List<ClientProfileRepresentationDTO> globalProfiles) {
}
