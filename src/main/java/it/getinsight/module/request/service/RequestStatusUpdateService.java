package it.getinsight.module.request.service;

import it.getinsight.module.request.dto.RequestUpdateDTO;
import it.getinsight.module.request.entity.RequestEntity;
import it.getinsight.module.request.enuns.RequestStatus;
import it.getinsight.module.request.repository.RequestRepository;
import it.getinsight.module.role.service.RoleService;
import it.getinsight.module.user.repository.UserRepository;
import it.getinsight.module.user.service.AuthenticationContextService;
import it.getinsight.module.user.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

import static it.getinsight.message.MessageProperty.REQUEST_INVALID_STATUS_TRANSITION;
import static it.getinsight.message.MessageProperty.USER_NOT_FOUND_ERROR;


@Service
@RequiredArgsConstructor
@Slf4j
public class RequestStatusUpdateService {

    private final AuthenticationContextService authenticationContextService;
    private final UserService userService;
    private final RoleService roleService;
    private final UserAccessValidationService userAccessValidationService;
    private final UserRepository userRepository;
    private final RequestRepository requestRepository;


    public boolean isLoggedUserAssignedAsApprover(RequestEntity requestEntity) {
        if (userAccessValidationService.hasNoValidScopeWithHierarchyForRequest(requestEntity)) {
            return false;
        }

        var currentUserId = authenticationContextService.getCurrentUserId();
        var approvingUserDTO = userService.findOrImportByExternalId(currentUserId);
        var approvingUsersDTO = roleService.getOrImportApprovesByRequest(requestEntity);

        return approvingUsersDTO.stream().anyMatch(obj -> obj.id().equals(approvingUserDTO.id()));
    }


    public void updateRequestBasicFields(RequestEntity requestEntity, RequestUpdateDTO requestUpdateDTO) {
        var currentUserId = authenticationContextService.getCurrentUserId();
        var approvingUserDTO = userService.findOrImportByExternalId(currentUserId);
        var approvingUserEntity = userRepository.findById(approvingUserDTO.id())
            .orElseThrow(USER_NOT_FOUND_ERROR::businessException);

        var newStatus = RequestStatus.valueOf(requestUpdateDTO.status());
        var currentStatus = requestEntity.getStatus();

        if (newStatus == RequestStatus.REVOKED && currentStatus != RequestStatus.APPROVED) {
                throw REQUEST_INVALID_STATUS_TRANSITION
                    .bind(currentStatus.name(), newStatus.name())
                    .businessException();
        }

        requestEntity.setStatus(newStatus);

        switch (newStatus) {
            case REVOKED -> {
                requestEntity.setRevokingUser(approvingUserEntity);
                requestEntity.setRevocationReason(requestUpdateDTO.revocationReason());
            }
            case APPROVED, REJECTED -> {
                requestEntity.setApprovingUser(approvingUserEntity);
                requestEntity.setFinalReason(requestUpdateDTO.finalReason());
            }
            default -> log.warn("Invalid status transition from {} to {}", currentStatus, newStatus);
        }
    }


    public void persistRejectedOrCanceledRequest(RequestEntity requestEntity, Long requestId, String status) {
        var currentUserId = authenticationContextService.getCurrentUserId();
        var approvingUserDTO = userService.findOrImportByExternalId(currentUserId);

        log.info("User {} is updating request {} to status {}", approvingUserDTO.email(), requestId, status);
        requestRepository.save(requestEntity);
    }


    public boolean shouldPersistRequest(RequestStatus status) {
        return List.of(RequestStatus.REJECTED, RequestStatus.CANCELED, RequestStatus.REVOKED).contains(status);
    }
}
