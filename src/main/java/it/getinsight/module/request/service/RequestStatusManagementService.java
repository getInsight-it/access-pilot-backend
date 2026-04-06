package it.getinsight.module.request.service;

import it.getinsight.module.keycloak.service.IdentityProviderService;
import it.getinsight.module.request.dto.RequestUpdateDTO;
import it.getinsight.module.request.entity.RequestEntity;
import it.getinsight.module.role.repository.RoleRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static it.getinsight.message.MessageProperty.REQUEST_ERROR_WHEN_TRYING_TO_ASSIGN_ROLE;
import static it.getinsight.message.MessageProperty.ROLE_NOT_FOUND_ERROR;

@Service
@RequiredArgsConstructor
@Slf4j
public class RequestStatusManagementService {

    private final IdentityProviderService identityProviderService;
    private final RoleRepository roleRepository;

    private final RoleAssignmentService roleAssignmentService;
    private final UserAttributeService userAttributeService;
    private final RequestStatusUpdateService requestStatusUpdateService;

    @Transactional(propagation = Propagation.REQUIRED)
    public void processRequestStatusUpdate(Long id, RequestUpdateDTO requestUpdateDTO, RequestEntity requestEntity) {
        requestStatusUpdateService.updateRequestBasicFields(requestEntity, requestUpdateDTO);

        if (requestStatusUpdateService.isLoggedUserAssignedAsApprover(requestEntity)) {
            switch (requestEntity.getStatus()) {
                case APPROVED -> confirmRoles(requestEntity);
                case REVOKED -> revokeRoles(requestEntity);
                default -> log.info("no action for status {}", requestEntity.getStatus());
            }
        }

        if (requestStatusUpdateService.shouldPersistRequest(requestEntity.getStatus())) {
            requestStatusUpdateService.persistRejectedOrCanceledRequest(requestEntity, id, requestUpdateDTO.status());
        }
    }

    public void confirmRoles(RequestEntity entity) {
        try {
            var roleEntity = roleRepository.findById(entity.getRole().getId())
                .orElseThrow(ROLE_NOT_FOUND_ERROR::resourceNotFoundException);
            Optional.ofNullable(identityProviderService.getRoleByNameAndClientUUID(roleEntity.getName(), roleEntity.getClient().getClientUUID())).orElseThrow(ROLE_NOT_FOUND_ERROR::resourceNotFoundException);

            if (entity.getCodeItem() != null) {
                userAttributeService.updateUserAttributes(entity, roleEntity);
            }

            roleAssignmentService.confirmRoles(entity);
        } catch (Exception e) {
            log.error("Error assigning role to user {}", entity.getRequestingUser().getExternalId(), e);
            throw REQUEST_ERROR_WHEN_TRYING_TO_ASSIGN_ROLE.businessException();
        }
    }

    private void revokeRoles(RequestEntity entity) {
        try {
            var roleEntity = roleRepository.findById(entity.getRole().getId())
                .orElseThrow(ROLE_NOT_FOUND_ERROR::resourceNotFoundException);
            Optional.ofNullable(identityProviderService.getRoleByNameAndClientUUID(roleEntity.getName(), roleEntity.getClient().getClientUUID())).orElseThrow(ROLE_NOT_FOUND_ERROR::resourceNotFoundException);

            if (entity.getCodeItem() != null) {
                userAttributeService.removeUserAttributes(entity, roleEntity);
            }

            roleAssignmentService.revokeRoles(entity);
            log.info("Forcing logout for user {}", entity.getRequestingUser().getExternalId());
            identityProviderService.logoutUser(entity.getRequestingUser().getExternalId());
        } catch (Exception e) {
            log.error("Error revoking role from user {}", entity.getRequestingUser().getExternalId(), e);
            throw REQUEST_ERROR_WHEN_TRYING_TO_ASSIGN_ROLE.businessException();
        }
    }
}
