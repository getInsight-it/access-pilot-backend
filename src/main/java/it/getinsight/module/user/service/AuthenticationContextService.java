package it.getinsight.module.user.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
@Slf4j
public class AuthenticationContextService {


    public Jwt getCurrentJwt() {
        return (Jwt) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }


    public String getCurrentUserId() {
        return getCurrentJwt().getSubject();
    }


    public String getCurrentUserEmail() {
        return getCurrentJwt().getClaimAsString("email");
    }


    public String getCurrentUserName() {
        return getCurrentJwt().getClaimAsString("key");
    }


    public String getCurrentUserLastName() {
        return getCurrentJwt().getClaimAsString("family_name");
    }


    public java.util.Map<String, java.util.List<String>> getCurrentUserResourceAccess() {
        return getCurrentJwt().getClaim("resource_access");
    }


    public boolean isAuthenticated() {
        return SecurityContextHolder.getContext().getAuthentication() != null
            && SecurityContextHolder.getContext().getAuthentication().getPrincipal() instanceof Jwt;
    }
}
