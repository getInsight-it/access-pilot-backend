package it.getinsight.module.keycloak.dto;

import java.util.List;
import java.util.Map;

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
}
