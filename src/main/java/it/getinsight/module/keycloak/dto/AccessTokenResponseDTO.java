package it.getinsight.module.keycloak.dto;

import java.util.Map;

public record AccessTokenResponseDTO(String scope, String token, Long expiresIn, Long refreshExpiresIn,
                                     String refreshToken, String tokenType, String idToken, Integer notBeforePolicy,
                                     String sessionState, Map<String, Object> otherClaims, String error,
                                     String errorDescription, String errorUri) {
}
