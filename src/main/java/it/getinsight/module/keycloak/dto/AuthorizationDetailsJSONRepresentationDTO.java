package it.getinsight.module.keycloak.dto;

import java.util.List;
import java.util.Map;

public record AuthorizationDetailsJSONRepresentationDTO(String type, List<String> locations, List<String> actions,
                                                        List<String> datatypes, String identifier,
                                                        List<String> privileges, Map<String, Object> customData,
                                                        String scopeNameFromCustomData,
                                                        String dynamicScopeParamFromCustomData) {
}
