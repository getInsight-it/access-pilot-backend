package it.getinsight.module.user.service;


import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Slf4j
public class SecurityScopes {
    private final AuthenticationContextService authCtx;
    private final LevelAttributesExtractor extractor;

    public SecurityScopes(AuthenticationContextService authCtx, LevelAttributesExtractor extractor) {
        this.authCtx = authCtx;
        this.extractor = extractor;
    }

    public boolean isEmpty() { return all().isEmpty(); }

    public List<ScopeRef> all() {
        try {
            if (authCtx != null && authCtx.isAuthenticated()) {
                var jwt = authCtx.getCurrentJwt();
                if (jwt != null) {
                    return extractor.extract(jwt);
                }
            }
        } catch (Exception e) {
            log.error("Failed to extract scopes from JWT", e);
        }
        return List.of();
    }
}


