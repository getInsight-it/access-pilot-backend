package it.getinsight.module.role.service;

import it.getinsight.core.exception.InfraException;
import it.getinsight.core.exception.ResourceNotFoundException;
import it.getinsight.module.keycloak.config.KeycloakProperties;
import it.getinsight.module.keycloak.service.IdentityProviderService;
import it.getinsight.module.role.dto.RoleDTO;
import it.getinsight.utilitario.StringValidationUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.BooleanUtils;
import org.springframework.stereotype.Service;

import static it.getinsight.message.MessageProperty.*;


@Service
@RequiredArgsConstructor
@Slf4j
public class RoleValidationService {

    private final IdentityProviderService identityProviderService;
    private final KeycloakProperties keycloakProperties;


    public void validateRoleInput(RoleDTO roleDTO) {
        log.debug("Validating role input for role: {}", roleDTO != null ? roleDTO.name() : "null");

        if (roleDTO == null) {
            log.error("RoleDTO is null");
            throw ROLE_NOT_FOUND_ERROR.resourceNotFoundException();
        }

        if (roleDTO.client() == null) {
            log.error("Client is null for role: {}", roleDTO.name());
            throw CLIENT_NOT_FOUND_ERROR.resourceNotFoundException();
        }

        if (BooleanUtils.isFalse(roleDTO.client().managed())) {
            log.error("Client is not managed for role: {}", roleDTO.name());
            throw CLIENT_ROLE_MANAGEMENT_NOT_ENABLED_ERROR.businessException();
        }

        if (!StringValidationUtils.isUpperSnakeCase(roleDTO.name())) {
            log.error("Invalid role name pattern: {}", roleDTO.name());
            throw ERROR_VALIDATION_PATTERN_ROLE_NAME.businessException();
        }
    }


    public void ensureRoleDoesNotExistInIDP(RoleDTO roleDTO) {
        log.debug("Checking if role {} exists in IDP", roleDTO.name());

        try {
            final var role = identityProviderService.getRole(roleDTO.client().clientUUID(), roleDTO.name());
            if (role != null) {
                log.error("Role {} already exists in IDP", roleDTO.name());
                throw ROLE_ALREADY_EXISTS_IDP_ERROR.businessException();
            }
        } catch (InfraException | ResourceNotFoundException e) {
            log.debug("Role {} not found in IDP (expected for new roles)", roleDTO.name());
        }
    }


    public boolean isIgnoredRoleName(String roleName) {
        if (roleName == null) {
            return false;
        }
        var ignoreRoles = keycloakProperties.getIgnoreRoles();
        if (CollectionUtils.isEmpty(ignoreRoles)) {
            return false;
        }
        String normalized = roleName.trim().toLowerCase();
        return ignoreRoles.stream().anyMatch(r -> r != null && normalized.equals(r.trim().toLowerCase()));
    }
}

