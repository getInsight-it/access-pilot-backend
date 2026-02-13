package it.getinsight.module.keycloak.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@ConfigurationProperties(prefix = "provider.keycloak")
@Data
public class KeycloakProperties {

    private String url;
    private String realm;
    private String clientId;
    private String clientSecret;
    private List<String> ignoreClients;
    private List<String> ignoreRoles;
}
