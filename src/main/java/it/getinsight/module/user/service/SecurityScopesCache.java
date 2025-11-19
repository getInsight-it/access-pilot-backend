package it.getinsight.module.user.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
@Slf4j
public class SecurityScopesCache {

    private final SecurityScopesBuilderService builder;

    @Cacheable(cacheNames = "securityScopes", key = "#tokenKey")
    @Transactional(readOnly = true)
    public SecurityScopes.CachedScopes getOrBuildCachedScopes(String tokenKey, Jwt jwt) {
        log.debug("Cache MISS for tokenKey={}, building scopes...", tokenKey);
        return builder.buildScopes(jwt);
    }
}

