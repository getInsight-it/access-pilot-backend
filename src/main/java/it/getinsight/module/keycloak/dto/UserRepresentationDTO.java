package it.getinsight.module.keycloak.dto;

import lombok.Builder;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

@Builder
public record UserRepresentationDTO(String self, String id, Long createdTimestamp, String firstName, String lastName,
                                    String email, String username, Boolean enabled, Boolean totp, Boolean emailVerified,
                                    Map<String, List<String>> attributes, List<CredentialRepresentationDTO> credentials,
                                    List<String> requiredActions,
                                    List<FederatedIdentityRepresentationDTO> federatedIdentities,
                                    List<SocialLinkRepresentationDTO> socialLinks, List<String> realmRoles,
                                    Map<String, List<String>> clientRoles,
                                    List<UserConsentRepresentationDTO> clientConsents, Integer notBefore,
                                    Map<String, List<String>> applicationRoles, String federationLink,
                                    String serviceAccountClientId, List<String> groups, String origin,
                                    List<String> disableableCredentialTypes, Map<String, Boolean> access) {

    public UserRepresentationDTO withLevelAttributes(List<String> newLevelAttributes) {
        Map<String, List<String>> safeAttributes =
            this.attributes == null ? new HashMap<>() : new HashMap<>(this.attributes);

        safeAttributes.put("levelAttributes",
            newLevelAttributes == null ? List.of() : newLevelAttributes.stream().distinct().toList()
        );
        return new UserRepresentationDTO(
            self, id, createdTimestamp, firstName, lastName, email, username, enabled, totp, emailVerified, safeAttributes, credentials, requiredActions, federatedIdentities, socialLinks, realmRoles,
            clientRoles, clientConsents, notBefore, applicationRoles, federationLink, serviceAccountClientId,
            groups, origin, disableableCredentialTypes, access
        );
    }
}
