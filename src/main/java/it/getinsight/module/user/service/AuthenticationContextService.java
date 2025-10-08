package it.getinsight.module.user.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;

/**
 * Serviço responsável exclusivamente por gerenciar o contexto de autenticação.
 * Aplica SRP de forma agressiva - apenas operações relacionadas ao contexto de autenticação.
 * 
 * Centraliza o acesso ao SecurityContextHolder evitando duplicação de código
 * e violação do princípio DRY (Don't Repeat Yourself).
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class AuthenticationContextService {

    /**
     * Obtém o JWT do contexto de autenticação atual.
     */
    public Jwt getCurrentJwt() {
        return (Jwt) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }

    /**
     * Obtém o subject (ID do usuário) do JWT atual.
     */
    public String getCurrentUserId() {
        return getCurrentJwt().getSubject();
    }

    /**
     * Obtém o email do usuário atual do JWT.
     */
    public String getCurrentUserEmail() {
        return getCurrentJwt().getClaimAsString("email");
    }

    /**
     * Obtém o nome do usuário atual do JWT.
     */
    public String getCurrentUserName() {
        return getCurrentJwt().getClaimAsString("key");
    }

    /**
     * Obtém o sobrenome do usuário atual do JWT.
     */
    public String getCurrentUserLastName() {
        return getCurrentJwt().getClaimAsString("family_name");
    }

    /**
     * Obtém o resource_access do JWT atual.
     */
    public java.util.Map<String, java.util.List<String>> getCurrentUserResourceAccess() {
        return getCurrentJwt().getClaim("resource_access");
    }

    /**
     * Verifica se existe um usuário autenticado no contexto atual.
     */
    public boolean isAuthenticated() {
        return SecurityContextHolder.getContext().getAuthentication() != null 
            && SecurityContextHolder.getContext().getAuthentication().getPrincipal() instanceof Jwt;
    }
}
