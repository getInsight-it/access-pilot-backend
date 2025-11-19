package it.getinsight.module.user.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Slf4j
@Component
public class LevelAttributesExtractor {
    private static final String CLAIM = "levelAttributes";

    public List<ScopeRef> extract(Jwt jwt) {
        if (!jwt.getClaims().containsKey(CLAIM)) {
            log.debug("Claim '{}' not found in JWT", CLAIM);
            return List.of();
        }

        Object claim = jwt.getClaims().get(CLAIM);
        log.debug("Claim '{}' found: {}", CLAIM, claim);

        if (claim instanceof Collection<?> col) {
            return col.stream()
                .map(Objects::toString)
                .map(ScopeRef::parse)
                .flatMap(Optional::stream)
                .toList();
        }

        log.debug("Claim '{}' is not a valid collection", CLAIM);
        return List.of();
    }
}


