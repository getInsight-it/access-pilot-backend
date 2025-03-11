package it.getinsight.module.keycloak.dto;

import lombok.Builder;

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
        attributes.putIfAbsent("levelAttributes", new HashSet<>(newLevelAttributes).stream().toList());
        return new UserRepresentationDTO(
            self, id, createdTimestamp, firstName, lastName, email, username, enabled, totp, emailVerified, attributes, credentials, requiredActions, federatedIdentities, socialLinks, realmRoles,
            clientRoles, clientConsents, notBefore, applicationRoles, federationLink, serviceAccountClientId,
            groups, origin, disableableCredentialTypes, access
        );
    }
}
