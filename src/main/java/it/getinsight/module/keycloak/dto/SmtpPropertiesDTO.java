package it.getinsight.module.keycloak.dto;

public record SmtpPropertiesDTO(
        String host,

        int port,

        String username,

        String password,

        String from,

        boolean tls

) {
}
