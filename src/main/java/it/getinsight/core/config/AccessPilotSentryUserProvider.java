package it.getinsight.core.config;

import io.sentry.protocol.User;
import io.sentry.spring.jakarta.SentryUserProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.Nullable;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBooleanProperty;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Slf4j
@Component
@ConditionalOnBooleanProperty("sentry.enabled")
@RequiredArgsConstructor
class AccessPilotSentryUserProvider implements SentryUserProvider {

    @Override
    public @Nullable User provideUser() {
        try {
            final var securityContext = SecurityContextHolder.getContext();
            final var user = new User();
            Optional.ofNullable(securityContext.getAuthentication()).ifPresent(authentication -> {
                final var principal = authentication.getPrincipal();

                if (principal instanceof JwtAuthenticationToken jwt) {
                    user.setId(jwt.getToken().getId());
                    user.setName(jwt.getToken().getClaimAsString("name"));
                    user.setEmail(jwt.getToken().getClaimAsString("email"));
                    user.setUsername(jwt.getToken().getClaimAsString("preferred_username"));
                    user.setUnknown(jwt.getTokenAttributes());
                }else if (principal instanceof String str) {
                    user.setName(str);
                }

            });
            return user;
        }catch (Exception e) {
            log.warn("Não foi possível identificar usuário para o Sentry.");
            return null;
        }
    }
}
