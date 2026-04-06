package it.getinsight.core.test.helper;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@Slf4j
@Component
@RequiredArgsConstructor
public class TokenHelper {

    private final TestRestTemplate testRestTemplate;

    @Value("${test.oauth2.token-uri}")
    private String tokenUri;

    @Value("${test.oauth2.client-id}")
    private String clientId;

    @Value("${test.oauth2.client-secret}")
    private String clientSecret;

    private final Map<String, String> tokenCache = new HashMap<>();

    public String getToken(String username, String password) {
        String cacheKey = username + ":" + password;

        if (tokenCache.containsKey(cacheKey)) {
            log.debug("Using cached token for user: {}", username);
            return tokenCache.get(cacheKey);
        }

        log.debug("Requesting new token for user: {}", username);
        String token = requestToken(username, password);

        tokenCache.put(cacheKey, token);

        return token;
    }

    public String getToken() {
        return getToken("teste", "123456");
    }

    public void clearCache() {
        log.debug("Clearing token cache");
        tokenCache.clear();
    }

    public void invalidateToken(String username, String password) {
        String cacheKey = username + ":" + password;
        tokenCache.remove(cacheKey);
        log.debug("Invalidated token for user: {}", username);
    }

    public Map<String, Object> introspectToken(String token) {
        log.warn("Token introspection not implemented yet");
        return Map.of();
    }

    public HttpHeaders createAuthHeaders(String username, String password) {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(getToken(username, password));
        return headers;
    }

    public HttpHeaders createJsonAuthHeaders(String username, String password) {
        HttpHeaders headers = createAuthHeaders(username, password);
        headers.setContentType(MediaType.APPLICATION_JSON);
        return headers;
    }

    private String requestToken(String username, String password) {
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
            log.info("Token obtained successfully for user: {}", username);

            return accessToken;

        } catch (Exception e) {
            log.error("Failed to obtain token for user: {}", username, e);
            throw new RuntimeException("Falha ao obter token: " + e.getMessage(), e);
        }
    }
}
