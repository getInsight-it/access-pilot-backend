package it.getinsight.core.test;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.context.ApplicationContext;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.testcontainers.junit.jupiter.Testcontainers;

import javax.sql.DataSource;
import java.security.Principal;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@Slf4j
@Testcontainers
@ActiveProfiles("test")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public abstract class BaseIntegrationTest {

    @Autowired
    protected DataSource dataSource;

    @Autowired
    protected TestRestTemplate testRestTemplate;

    @Autowired
    protected ApplicationContext applicationContext;

    @Autowired
    protected ObjectMapper objectMapper;

    @Value("${test.oauth2.token-uri}")
    private String tokenUri;

    @Value("${test.oauth2.client-id}")
    private String clientId;

    @Value("${test.oauth2.client-secret}")
    private String clientSecret;

    @DynamicPropertySource
    static void registerProperties(DynamicPropertyRegistry registry) {
        TestcontainersHolder.getInstance().setProperties(registry);
    }

    @BeforeEach
    void bindRequestContext() {
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setMethod("GET");
        request.setRequestURI("/test-context");
        RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(request));

        Principal principal = () -> "integration-test-auditor";
        var authentication = new UsernamePasswordAuthenticationToken(principal, "N/A", List.of());
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }

    @AfterEach
    void clearRequestContext() {
        RequestContextHolder.resetRequestAttributes();
        SecurityContextHolder.clearContext();
    }

    protected HttpHeaders authenticatedUser() {
        return authenticatedUser("teste", "123456");
    }

    protected HttpHeaders authenticatedUser(String username, String password) {
        log.debug("Authenticating user: {}", username);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("grant_type", "password");
        body.add("client_id", clientId);
        body.add("client_secret", clientSecret);
        body.add("username", username);
        body.add("password", password);

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(body, headers);

        try {
            ResponseEntity<Map<String, Object>> response = testRestTemplate.exchange(
                tokenUri,
                HttpMethod.POST,
                request,
                new ParameterizedTypeReference<>() {
                }
            );

            assertEquals(HttpStatus.OK, response.getStatusCode(),
                String.format("Falha ao obter token OAuth2 para usuário: %s. Status: %s",
                    username, response.getStatusCode()));
            assertNotNull(response.getBody(), "Resposta de token vazia");
            assertTrue(response.getBody().containsKey("access_token"),
                "Token não encontrado na resposta OAuth2");

            String accessToken = (String) response.getBody().get("access_token");
            log.debug("Token obtained successfully for user: {}", username);

            HttpHeaders authHeaders = new HttpHeaders();
            authHeaders.setBearerAuth(accessToken);
            return authHeaders;

        } catch (Exception e) {
            log.error("Failed to authenticate user: {}", username, e);
            throw new RuntimeException("Falha na autenticação: " + e.getMessage(), e);
        }
    }

    protected HttpHeaders jsonAuth() {
        HttpHeaders headers = authenticatedUser();
        headers.setContentType(MediaType.APPLICATION_JSON);
        return headers;
    }

    protected HttpHeaders jsonAuth(String username, String password) {
        HttpHeaders headers = authenticatedUser(username, password);
        headers.setContentType(MediaType.APPLICATION_JSON);
        return headers;
    }

    protected String json(Object obj) throws Exception {
        return objectMapper.writeValueAsString(obj);
    }

    protected Long extractIdFromLocation(String locationUri) {
        if (locationUri == null || locationUri.isEmpty()) {
            throw new IllegalArgumentException("Location URI is null or empty");
        }
        String[] parts = locationUri.split("/");
        return Long.parseLong(parts[parts.length - 1]);
    }

    protected void warmUpUser(String username, String password) {
        try {
            log.debug("Warming up user: {}", username);
            HttpEntity<Void> entity = new HttpEntity<>(authenticatedUser(username, password));
            testRestTemplate.exchange(
                "/actuator/health",  // Usando health como fallback
                HttpMethod.GET,
                entity,
                String.class
            );
            log.debug("User warmed up: {}", username);
        } catch (Exception e) {
            log.warn("Failed to warm up user {}: {}", username, e.getMessage());
        }
    }

    protected void warmUpUser() {
        warmUpUser("teste", "123456");
    }
}
