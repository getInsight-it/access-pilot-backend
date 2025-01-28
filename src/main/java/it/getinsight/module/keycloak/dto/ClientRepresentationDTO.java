package it.getinsight.module.keycloak.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.Map;

@Builder
@Getter
@Setter
public class ClientRepresentationDTO{
    @JsonProperty("id") String id;
    @JsonProperty("name") String name;
    @JsonProperty("description") String description;
    @JsonProperty("clientId") String clientId;
    @JsonProperty("enabled") Boolean enabled;
    @JsonProperty("alwaysDisplayInConsole") Boolean alwaysDisplayInConsole;
    @JsonProperty("surrogateAuthRequired") Boolean surrogateAuthRequired;
    @JsonProperty("rootUrl") String rootUrl;
    @JsonProperty("adminUrl") String adminUrl;
    @JsonProperty("baseUrl") String baseUrl;
    @JsonProperty("clientAuthenticatorType") String clientAuthenticatorType;
    @JsonProperty("secret") String secret;
    @JsonProperty("registrationAccessToken") String registrationAccessToken;
    @JsonProperty("redirectUris") List<String> redirectUris;
    @JsonProperty("webOrigins") List<String> webOrigins;
    @JsonProperty("defaultRoles") List<String> defaultRoles;
    @JsonProperty("notBefore") Integer notBefore;
    @JsonProperty("bearerOnly") Boolean bearerOnly;
    @JsonProperty("consentRequired") Boolean consentRequired;
    @JsonProperty("standardFlowEnabled") Boolean standardFlowEnabled;
    @JsonProperty("implicitFlowEnabled") Boolean implicitFlowEnabled;
    @JsonProperty("directAccessGrantsEnabled") Boolean directAccessGrantsEnabled;
    @JsonProperty("serviceAccountsEnabled") Boolean serviceAccountsEnabled;
    @JsonProperty("authorizationServicesEnabled") Boolean authorizationServicesEnabled;
    @JsonProperty("directGrantsOnly") Boolean directGrantsOnly;
    @JsonProperty("publicClient") Boolean publicClient;
    @JsonProperty("fullScopeAllowed") Boolean fullScopeAllowed;
    @JsonProperty("protocol") String protocol;
    @JsonProperty("attributes") Map<String, String> attributes;
    @JsonProperty("authenticationFlowBindingOverrides") Map<String, String> authenticationFlowBindingOverrides;
    @JsonProperty("nodeReRegistrationTimeout") Integer nodeReRegistrationTimeout;
    @JsonProperty("registeredNodes") Map<String, Integer> registeredNodes;
    @JsonProperty("frontchannelLogout") Boolean frontchannelLogout;
    @JsonProperty("protocolMappers") List<ProtocolMapperRepresentationDTO> protocolMappers;
    @JsonProperty("clientTemplate") String clientTemplate;
    @JsonProperty("useTemplateConfig") Boolean useTemplateConfig;
    @JsonProperty("useTemplateScope") Boolean useTemplateScope;
    @JsonProperty("useTemplateMappers") Boolean useTemplateMappers;
    @JsonProperty("defaultClientScopes") List<String> defaultClientScopes;
    @JsonProperty("optionalClientScopes") List<String> optionalClientScopes;
    @JsonProperty("authorizationSettings") ResourceServerRepresentationDTO authorizationSettings;
    @JsonProperty("access") Map<String, Boolean> access;
    @JsonProperty("origin") String origi;

    public static ClientRepresentationDTO createDefault(String clientId, String description, String baseUrl) {
        return ClientRepresentationDTO.builder()
            .alwaysDisplayInConsole(false)
            .attributes(Map.of(
                "saml_idp_initiated_sso_url_name", "",
                "oauth2.device.authorization.grant.enabled", "false",
                "oidc.ciba.grant.enabled", "false",
                "acl.client.managed", "true"
            ))
            .authorizationServicesEnabled(false)
            .baseUrl(baseUrl)
            .clientId(clientId)
            .description(description)
            .directAccessGrantsEnabled(true)
            .frontchannelLogout(true)
            .implicitFlowEnabled(false)
            .name("")
            .protocol("openid-connect")
            .publicClient(true)
            .rootUrl("")
            .serviceAccountsEnabled(false)
            .standardFlowEnabled(true)
            .build();
    }

}
