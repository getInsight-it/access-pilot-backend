package it.getinsight.module.keycloak.dto;

import java.util.Map;

public record JsonWebTokenDTO(String id, Long exp, Long nbf, Long iat, String issuer, String suer, String subject,
                              String type, String issuedFor, String suedFor, Map<String, Object> otherClaims,
                              String category) {
}
