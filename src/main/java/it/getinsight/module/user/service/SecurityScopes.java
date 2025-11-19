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
                    log.debug("JWT found, extracting scopes...");
                    List<ScopeRef> extracted = extractor.extract(jwt);
                    log.debug("Extracted scopes: {}", extracted);
                    return extracted;
                }
                log.debug("JWT is null");
            } else {
                log.debug("User not authenticated or auth context is null");
            }
        } catch (Exception e) {
            log.error("Error processing scopes", e);
        }
        return List.of();
    }
}


