package it.getinsight.integration.module.request;

import it.getinsight.core.test.BaseIntegrationTest;
import it.getinsight.core.test.helper.KeycloakTestHelper;
import it.getinsight.core.test.helper.TokenHelper;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.*;

@Slf4j
class InfrastructureSmokeTest extends BaseIntegrationTest {

    @Autowired
    private KeycloakTestHelper keycloakHelper;

    @Autowired
    private TokenHelper tokenHelper;

    private static final String TEST_USERNAME = "teste";
    private static final String TEST_PASSWORD = "123456";
    private static final String TEST_EMAIL = "teste@accesspilot.test";

    @Test
    @DisplayName("Should start Spring Boot application context")
    void shouldStartApplicationContext() {
        log.info("Testing application context...");
        assertNotNull(applicationContext, "Application context should be loaded");
        assertNotNull(dataSource, "DataSource should be configured");
        assertNotNull(testRestTemplate, "TestRestTemplate should be available");
        log.info("✓ Application context is running");
    }

    @Test
    @DisplayName("Should create test user in Keycloak")
    void shouldCreateTestUserInKeycloak() {
        log.info("Testing Keycloak user creation...");

        String testUserId = ensureTestUser();

        assertNotNull(testUserId, "User ID should be returned");
        assertFalse(testUserId.isEmpty(), "User ID should not be empty");

        log.info("✓ Test user created with ID: {}", testUserId);
    }

    @Test
    @DisplayName("Should obtain OAuth2 token for test user")
    void shouldObtainOAuth2Token() {
        log.info("Testing OAuth2 token acquisition...");

        ensureTestUser();
        String token = tokenHelper.getToken(TEST_USERNAME, TEST_PASSWORD);

        assertNotNull(token, "Token should be returned");
        assertFalse(token.isEmpty(), "Token should not be empty");
        assertTrue(token.length() > 100, "Token should be a JWT (length > 100)");

        log.info("✓ OAuth2 token obtained successfully");
    }

    @Test
    @DisplayName("Should make authenticated request to health endpoint")
    void shouldMakeAuthenticatedRequest() {
        log.info("Testing authenticated HTTP request...");
        ensureTestUser();

        ResponseEntity<String> response = testRestTemplate.exchange(
            "/actuator/health",
            org.springframework.http.HttpMethod.GET,
            new org.springframework.http.HttpEntity<>(authenticatedUser(TEST_USERNAME, TEST_PASSWORD)),
            String.class
        );

        assertEquals(HttpStatus.OK, response.getStatusCode(), "Health endpoint should return 200 OK");
        assertNotNull(response.getBody(), "Response body should not be empty");

        log.info("✓ Authenticated request successful: {}", response.getStatusCode());
    }

    @Test
    @DisplayName("Should verify Testcontainers are running")
    void shouldVerifyContainersRunning() {
        log.info("Testing Testcontainers status...");

        var holder = it.getinsight.core.test.TestcontainersHolder.getInstance();

        assertTrue(holder.getPostgres().isRunning(), "PostgreSQL container should be running");
        assertTrue(holder.getRedis().isRunning(), "Redis container should be running");
        assertTrue(holder.getKeycloak().isRunning(), "Keycloak container should be running");
        assertTrue(holder.getRabbitmq().isRunning(), "RabbitMQ container should be running");
        assertTrue(holder.getMailhog().isRunning(), "Mailhog container should be running");

        log.info("✓ All containers are running:");
        log.info("  - PostgreSQL: {}", holder.getPostgres().getJdbcUrl());
        log.info("  - Redis: {}:{}", holder.getRedis().getHost(), holder.getRedis().getMappedPort(6379));
        log.info("  - Keycloak: {}", holder.getKeycloak().getAuthServerUrl());
        log.info("  - RabbitMQ: {}", holder.getRabbitmq().getAmqpUrl());
        log.info("  - Mailhog: {}:{}", holder.getMailhog().getHost(), holder.getMailhog().getMappedPort(8025));
    }

    @AfterEach
    void cleanup() {
        log.info("Cleaning up test data...");

        keycloakHelper.deleteUserByUsername(TEST_USERNAME);
        tokenHelper.clearCache();
        log.info("✓ Test user deleted and token cache cleared");
    }

    private String ensureTestUser() {
        return keycloakHelper.createUser(TEST_USERNAME, TEST_PASSWORD, TEST_EMAIL);
    }

    @AfterAll
    static void afterAll() {
        log.info("==================================================");
        log.info("Infrastructure smoke test completed successfully!");
        log.info("All components are operational:");
        log.info("  ✓ Spring Boot application");
        log.info("  ✓ Testcontainers (6 containers)");
        log.info("  ✓ Keycloak OAuth2 authentication");
        log.info("  ✓ HTTP endpoints");
        log.info("==================================================");
    }
}
