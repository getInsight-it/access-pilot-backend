package it.getinsight.core.test.helper;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import it.getinsight.core.test.TestcontainersHolder;
import lombok.extern.slf4j.Slf4j;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.KeycloakBuilder;
import org.keycloak.admin.client.resource.RealmResource;
import org.keycloak.admin.client.resource.UserResource;
import org.keycloak.admin.client.resource.UsersResource;
import org.keycloak.representations.idm.ClientRepresentation;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.RoleRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.stereotype.Component;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import jakarta.ws.rs.ClientErrorException;
import jakarta.ws.rs.NotFoundException;
import jakarta.ws.rs.core.Response;
import java.io.IOException;
import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Component
public class KeycloakTestHelper {

    private static final String REALM_NAME = "access-pilot";
    private static final String CLIENT_ID = "accesspilot-backend";
    private static final String CLIENT_SECRET = "test-secret";
    private static final HttpClient HTTP_CLIENT = HttpClient.newHttpClient();
    private static final ObjectMapper JSON_MAPPER = new ObjectMapper();

    private Keycloak keycloakAdmin;
    private RealmResource realmResource;
    private String keycloakUrl;

    @PostConstruct
    public void init() {
        TestcontainersHolder holder = TestcontainersHolder.getInstance();
        keycloakUrl = holder.getKeycloak().getAuthServerUrl();

        log.info("Initializing KeycloakTestHelper with URL: {}", keycloakUrl);

        keycloakAdmin = KeycloakBuilder.builder()
            .serverUrl(keycloakUrl)
            .realm("master")
            .username("admin")
            .password("admin")
            .clientId("admin-cli")
            .build();

        realmResource = keycloakAdmin.realm(REALM_NAME);

        validateImportedRealmAndClient();

        log.info("KeycloakTestHelper initialized successfully");
    }

    @PreDestroy
    public void cleanup() {
        if (keycloakAdmin != null) {
            keycloakAdmin.close();
        }
    }

    private void validateImportedRealmAndClient() {
        List<ClientRepresentation> clients = findClientsById(CLIENT_ID);
        if (clients.isEmpty()) {
            throw new IllegalStateException(
                "Client '" + CLIENT_ID + "' não encontrado no realm '" + REALM_NAME + "'. "
                    + "Verifique o import do arquivo access-pilot-realm.json."
            );
        }
    }

    private List<ClientRepresentation> findClientsById(String clientId) {
        try {
            return realmResource.clients().findByClientId(clientId);
        } catch (ClientErrorException e) {
            if (e.getResponse() != null && e.getResponse().getStatus() == 404) {
                throw new IllegalStateException(
                    "Realm '" + REALM_NAME + "' not found. It must be created by TestcontainersHolder before KeycloakTestHelper initialization.",
                    e
                );
            }
            throw e;
        }
    }

    public String ensureClientExists(String clientId) {
        return ensureClientExists(clientId, CLIENT_SECRET, true);
    }

    private String ensureClientExists(String clientId, String clientSecret, boolean directAccessGrantsEnabled) {
        List<ClientRepresentation> clients = findClientsById(clientId);

        if (clients.isEmpty()) {
            log.info("Creating client: {}", clientId);
            ClientRepresentation client = new ClientRepresentation();
            client.setClientId(clientId);
            client.setEnabled(true);
            client.setPublicClient(false);
            client.setDirectAccessGrantsEnabled(directAccessGrantsEnabled);
            client.setServiceAccountsEnabled(true);
            client.setSecret(clientSecret);
            try {
                realmResource.clients().create(client);
            } catch (ClientErrorException e) {
                if (e.getResponse() == null || e.getResponse().getStatus() != 409) {
                    throw e;
                }
                log.info("Client {} already exists (HTTP 409), reusing", clientId);
            }
            clients = findClientsById(clientId);
            if (clients.isEmpty()) {
                throw new RuntimeException("Failed to create client: " + clientId);
            }
            log.info("Client {} is available", clientId);
        } else {
            log.info("Client {} already exists", clientId);
        }

        return clients.getFirst().getId();
    }

    public String getClientUuid(String clientId) {
        return realmResource.clients().findByClientId(clientId).stream()
            .findFirst()
            .map(ClientRepresentation::getId)
            .orElseThrow(() -> new RuntimeException("Client not found: " + clientId));
    }

    public String createUser(String username, String password, String email) {
        UsersResource usersResource = realmResource.users();

        Optional<String> existingUserId = findUserIdByUsername(username);
        if (existingUserId.isPresent()) {
            log.info("User {} already exists with ID: {}", username, existingUserId.get());
            updateUserPassword(existingUserId.get(), password);
            return existingUserId.get();
        }

        UserRepresentation user = new UserRepresentation();
        user.setUsername(username);
        user.setEmail(email);
        user.setFirstName(username);
        user.setLastName("Test");
        user.setEnabled(true);
        user.setEmailVerified(true);
        user.setRequiredActions(Collections.emptyList());

        log.info("Creating user: {}", username);
        Response response = usersResource.create(user);

        if (response.getStatus() != 201) {
            throw new RuntimeException("Failed to create user: " + response.getStatusInfo());
        }

        String userId = extractIdFromLocation(response.getLocation().toString());
        log.info("User {} created with ID: {}", username, userId);

        updateUserPassword(userId, password);

        return userId;
    }

    public void updateUserPassword(String userId, String password) {
        CredentialRepresentation credential = new CredentialRepresentation();
        credential.setType(CredentialRepresentation.PASSWORD);
        credential.setValue(password);
        credential.setTemporary(false);

        realmResource.users().get(userId).resetPassword(credential);
        log.debug("Password updated for user ID: {}", userId);
    }

    public void assignClientRoles(String userId, String clientId, List<String> roleNames) {
        UserResource userResource = realmResource.users().get(userId);

        String clientUuid = getClientUuid(clientId);

        Set<String> currentRoleNames = userResource.roles().clientLevel(clientUuid)
            .listAll()
            .stream()
            .map(RoleRepresentation::getName)
            .collect(Collectors.toSet());

        List<RoleRepresentation> rolesToAssign = roleNames.stream()
            .filter(roleName -> !currentRoleNames.contains(roleName))
            .map(roleName -> realmResource.clients().get(clientUuid).roles().get(roleName).toRepresentation())
            .toList();

        if (rolesToAssign.isEmpty()) {
            log.debug("Roles {} already assigned to user {} in client {}", roleNames, userId, clientId);
            return;
        }

        userResource.roles().clientLevel(clientUuid).add(rolesToAssign);
        log.info("Assigned roles {} to user {} in client {}", roleNames, userId, clientId);
    }

    public void setUserAttribute(String userId, String attributeName, List<String> values) {
        UserResource userResource = realmResource.users().get(userId);
        UserRepresentation user = userResource.toRepresentation();

        if (user.getAttributes() == null) {
            user.setAttributes(new HashMap<>());
        }
        user.getAttributes().put(attributeName, values);

        userResource.update(user);
        log.info("Set attribute {} for user {}: {}", attributeName, userId, values);
    }

    public void setLevelAttribute(String userId, String levelAttributeValue) {
        setUserAttribute(userId, "levelAttributes", Collections.singletonList(levelAttributeValue));
    }

    public Optional<String> findUserIdByUsername(String username) {
        String encodedUsername = URLEncoder.encode(username, StandardCharsets.UTF_8);
        String usersUrl = String.format(
            "%s/admin/realms/%s/users?username=%s&exact=true&briefRepresentation=true",
            keycloakUrl,
            REALM_NAME,
            encodedUsername
        );

        HttpRequest request = HttpRequest.newBuilder(URI.create(usersUrl))
            .header("Authorization", "Bearer " + keycloakAdmin.tokenManager().getAccessTokenString())
            .header("Accept", "application/json")
            .GET()
            .build();

        try {
            HttpResponse<String> response = HTTP_CLIENT.send(request, HttpResponse.BodyHandlers.ofString());
            if (response.statusCode() >= 400) {
                throw new RuntimeException(
                    "Failed to search user '" + username + "' in Keycloak. HTTP status: " + response.statusCode()
                );
            }

            JsonNode users = JSON_MAPPER.readTree(response.body());
            if (!users.isArray() || users.isEmpty()) {
                return Optional.empty();
            }

            JsonNode id = users.get(0).get("id");
            return id == null || id.isNull() || id.asText().isBlank() ? Optional.empty() : Optional.of(id.asText());
        } catch (IOException e) {
            throw new RuntimeException("Failed to parse Keycloak user search response", e);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new RuntimeException("Interrupted while searching user in Keycloak", e);
        }
    }

    public void deleteUser(String userId) {
        realmResource.users().get(userId).remove();
        log.info("User deleted: {}", userId);
    }

    public void deleteUserByUsername(String username) {
        findUserIdByUsername(username).ifPresent(this::deleteUser);
    }

    public void createClientRole(String clientId, String roleName) {
        ensureClientRoleExists(clientId, roleName);
    }

    public void ensureClientRoleExists(String clientId, String roleName) {
        String clientUuid = getClientUuid(clientId);
        var rolesResource = realmResource.clients().get(clientUuid).roles();

        try {
            rolesResource.get(roleName).toRepresentation();
            log.debug("Role {} already exists in client {}", roleName, clientId);
            return;
        } catch (NotFoundException ignored) {
        }

        RoleRepresentation role = new RoleRepresentation();
        role.setName(roleName);
        role.setClientRole(true);

        rolesResource.create(role);
        log.info("Created role {} in client {}", roleName, clientId);
    }

    public RealmResource getRealmResource() {
        return realmResource;
    }

    public Keycloak getKeycloakAdmin() {
        return keycloakAdmin;
    }

    private String extractIdFromLocation(String location) {
        String[] parts = location.split("/");
        return parts[parts.length - 1];
    }
}
