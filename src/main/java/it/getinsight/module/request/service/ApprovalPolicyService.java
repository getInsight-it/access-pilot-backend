package it.getinsight.module.request.service;

import it.getinsight.module.keycloak.service.IdentityProviderService;
import it.getinsight.module.request.dto.RequestUpdateDTO;
import it.getinsight.module.request.entity.RequestEntity;
import it.getinsight.module.request.enuns.RequestStatus;
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
public class ApprovalPolicyService {

    private final IdentityProviderService identityProviderService;
    private final RoleRepository roleRepository;

    private final RoleAssignmentService roleAssignmentService;
    private final UserAttributeService userAttributeService;
    private final RequestStatusUpdateService requestStatusUpdateService;

    @Transactional(propagation = Propagation.REQUIRED)
    public void processRequestStatusUpdate(Long id, RequestUpdateDTO requestUpdateDTO, RequestEntity requestEntity) {
        requestStatusUpdateService.updateRequestBasicFields(requestEntity, requestUpdateDTO);

        if (RequestStatus.APPROVED.equals(requestEntity.getStatus()) && requestStatusUpdateService.canUserApproveRequest(requestEntity)) {
            confirmRoles(requestEntity);
        } else if (requestStatusUpdateService.shouldPersistRequest(requestEntity.getStatus())) {
            requestStatusUpdateService.persistRejectedOrCanceledRequest(requestEntity, id, requestUpdateDTO.status());
        }
    }


    private void confirmRoles(RequestEntity entity) {
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
}
