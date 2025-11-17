package it.getinsight.module.request.service;

import it.getinsight.module.keycloak.dto.RoleRepresentationDTO;
import it.getinsight.module.keycloak.service.IdentityProviderService;
import it.getinsight.module.request.entity.RequestEntity;
import it.getinsight.module.role.entity.RoleEntity;
import it.getinsight.module.role.repository.RoleRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static it.getinsight.message.MessageProperty.REQUEST_ERROR_WHEN_TRYING_TO_ASSIGN_ROLE;
import static it.getinsight.message.MessageProperty.ROLE_NOT_FOUND_ERROR;


@Service
@RequiredArgsConstructor
@Slf4j
public class RoleAssignmentService {

    private final IdentityProviderService identityProviderService;
    private final RoleRepository roleRepository;


    @Transactional
    public void confirmRoles(RequestEntity entity) {
        try {
            var roleEntity = roleRepository.findById(entity.getRole().getId())
                .orElseThrow(ROLE_NOT_FOUND_ERROR::resourceNotFoundException);
            var role = identityProviderService.getRoleByNameAndClientUUID(roleEntity.getName(), roleEntity.getClient().getClientUUID());

            log.info("Assigning role {} to user {}", role.name(), entity.getRequestingUser().getExternalId());
            assignRoleToUser(entity, roleEntity, role);
        } catch (Exception e) {
            log.error("Error assigning role to user {}", entity.getRequestingUser().getExternalId(), e);
            throw REQUEST_ERROR_WHEN_TRYING_TO_ASSIGN_ROLE.businessException();
        }
    }


    private void assignRoleToUser(RequestEntity entity, RoleEntity roleEntity, RoleRepresentationDTO role) {
        identityProviderService.assignRoles(
            entity.getRequestingUser().getExternalId(),
            roleEntity.getClient().getClientUUID(),
            List.of(role)
        );
    }

    @Transactional
    public void revokeRoles(RequestEntity entity) {
        try {
            var roleEntity = roleRepository.findById(entity.getRole().getId())
                .orElseThrow(ROLE_NOT_FOUND_ERROR::resourceNotFoundException);
            var role = identityProviderService.getRoleByNameAndClientUUID(roleEntity.getName(), roleEntity.getClient().getClientUUID());

            if (role == null) {
                log.warn("Role {} not found in IDP for user {}, nothing to revoke", roleEntity.getName(), entity.getRequestingUser().getExternalId());
                return;
            }

            log.info("Revoking role {} from user {}", role.name(), entity.getRequestingUser().getExternalId());
            removeRoleFromUser(entity, roleEntity, role);
        } catch (Exception e) {
            log.error("Error revoking role from user {}", entity.getRequestingUser().getExternalId(), e);
            throw REQUEST_ERROR_WHEN_TRYING_TO_ASSIGN_ROLE.businessException();
        }
    }

    private void removeRoleFromUser(RequestEntity entity, RoleEntity roleEntity, RoleRepresentationDTO role) {
        identityProviderService.removeRoles(
            entity.getRequestingUser().getExternalId(),
            roleEntity.getClient().getClientUUID(),
            List.of(role)
        );
    }
}
