package it.getinsight.module.user.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Slf4j
@RequiredArgsConstructor
public class SecurityScopes {

    private final AuthenticationContextService authCtx;
    private final SecurityScopesCache cache;

    private static final CachedScopes EMPTY = new CachedScopes(
        List.of(),
        List.of(),
        List.of(),
        List.of()
    );

    public boolean isEmpty() {
        return all().isEmpty();
    }

    public List<ScopeRef> all() {
        return resolveCached().scopes();
    }

    public List<String> findDirectRoleAccessScopes() {
        return resolveCached().directScopes();
    }

    public List<String> findHierarchicalRoleAccessScopes() {
        return resolveCached().hierarchicalScopes();
    }

    public List<String> findRolesWithoutDirectAccess() {
        return resolveCached().rolesWithoutDirectAccess();
    }

    private CachedScopes resolveCached() {
        try {
            if (authCtx == null || !authCtx.isAuthenticated()) {
                log.debug("User not authenticated or auth context is null");
                return EMPTY;
            }

            Jwt jwt = authCtx.getCurrentJwt();
            if (jwt == null) {
                log.debug("JWT is null");
                return EMPTY;
            }

            String tokenKey = buildTokenKey(jwt);
            if (tokenKey == null) {
                log.debug("Could not build tokenKey, skipping cache");
                return EMPTY;
            }

            log.debug("Resolving cached scopes for tokenKey={}", tokenKey);
            return cache.getOrBuildCachedScopes(tokenKey, jwt);

        } catch (Exception e) {
            log.error("Error resolving cached scopes", e);
            return EMPTY;
        }
    }

    private String buildTokenKey(Jwt jwt) {
        if (jwt.getId() != null) {
            log.debug("Building tokenKey from jti: {}", jwt.getId());
            return jwt.getId();
        }

        if (jwt.getSubject() == null || jwt.getIssuedAt() == null || jwt.getExpiresAt() == null) {
            return null;
        }

        String tokenKey = jwt.getSubject() + "|" + jwt.getIssuedAt() + "|" + jwt.getExpiresAt();
        log.debug("Building tokenKey from subject|iat|exp: {}", tokenKey);
        return tokenKey;
    }

    public record CachedScopes(
        List<ScopeRef> scopes,
        List<String> directScopes,
        List<String> hierarchicalScopes,
        List<String> rolesWithoutDirectAccess
    ) {}
}
