package it.getinsight.module.user.service;

import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Component
public class LevelAttributesExtractor {
    private static final String CLAIM = "levelAttributes";

    public List<ScopeRef> extract(Jwt jwt) {
        Object claim = jwt.getClaims().get(CLAIM);
        if (claim instanceof Collection<?> col) {
            return col.stream()
                .map(Objects::toString)
                .map(ScopeRef::parse)
                .flatMap(Optional::stream)
                .toList();
        }
        return List.of();
    }
}


