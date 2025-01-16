package it.getinsight.module.keycloak.dto;

import java.util.List;

public record RealmEventsConfigRepresentationDTO(Boolean eventsEnabled, Long eventsExpiration,
                                                 List<String> eventsListeners, List<String> enabledEventTypes,
                                                 Boolean adminEventsEnabled, Boolean adminEventsDetailsEnabled) {
}
