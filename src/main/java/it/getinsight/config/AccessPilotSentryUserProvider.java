package it.getinsight.config;

import io.sentry.protocol.User;
import io.sentry.spring.jakarta.SentryUserProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.Nullable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Slf4j
@Component
@RequiredArgsConstructor
class AccessPilotSentryUserProvider implements SentryUserProvider {

    @Override
    public @Nullable User provideUser() {
        try {
            final var securityContext = SecurityContextHolder.getContext();
            final var user = new User();
            Optional.ofNullable(securityContext.getAuthentication()).ifPresent(authentication -> {
                final var principal = (JwtAuthenticationToken) authentication.getPrincipal();

                user.setId(principal.getToken().getId());
                user.setName(principal.getToken().getClaimAsString("name"));
                user.setEmail(principal.getToken().getClaimAsString("email"));
                user.setUsername(principal.getToken().getClaimAsString("preferred_username"));
                user.setUnknown(principal.getTokenAttributes());
            });
            return user;
        }catch (Exception e) {
            log.warn("Não foi possível identificar usuário para o Sentry.", e);
            return null;
        }
    }
}
