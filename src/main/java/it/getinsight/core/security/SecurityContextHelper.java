package it.getinsight.core.security;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.security.Principal;
import java.util.List;

@Slf4j
public final class SecurityContextHelper {

    private SecurityContextHelper() {
    }

    public static void setSecurityContext(String userId) {
        if (userId == null || userId.isBlank()) {
            throw new IllegalArgumentException("userId is required for SecurityContext configuration");
        }

        Principal principal = () -> userId;
        Authentication auth = new UsernamePasswordAuthenticationToken(principal, null, List.of());

        SecurityContext context = SecurityContextHolder.createEmptyContext();
        context.setAuthentication(auth);
        SecurityContextHolder.setContext(context);

        log.debug("SecurityContext set with userId: {}", userId);
    }

    public static void clearSecurityContext() {
        SecurityContextHolder.clearContext();
        log.debug("SecurityContext cleared");
    }

}
