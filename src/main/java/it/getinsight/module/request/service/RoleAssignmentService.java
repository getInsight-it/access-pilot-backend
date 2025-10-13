package it.getinsight.module.request.service;

import it.getinsight.module.keycloak.service.IdentityProviderService;
import it.getinsight.module.keycloak.dto.RoleRepresentationDTO;
import it.getinsight.module.request.entity.RequestEntity;
import it.getinsight.module.role.entity.RoleEntity;
import it.getinsight.module.role.repository.RoleRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static it.getinsight.message.MessageProperty.*;


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
                .orElseThrow(ROLE_NOT_FOUND_ERROR::businessException);
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
            List.of(role.name())
        );
    }
}
