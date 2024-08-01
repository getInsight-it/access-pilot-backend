package it.getinsight.module.keycloak.dto;

public record PublishedRealmRepresentationDTO(String publicKeyPem, String tokenServiceUrl, String accountServiceUrl,
                                              Integer notBefore) {
}
