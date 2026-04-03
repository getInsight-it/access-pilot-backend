package it.getinsight.module.request.service;

import it.getinsight.module.request.dto.RequestUpdateDTO;
import it.getinsight.module.request.entity.RequestEntity;
import it.getinsight.module.request.enuns.RequestAction;
import it.getinsight.module.request.enuns.RequestStatus;
import it.getinsight.module.request.repository.RequestRepository;
import it.getinsight.module.user.repository.UserRepository;
import it.getinsight.module.user.service.AuthenticationContextService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static it.getinsight.message.MessageProperty.REQUEST_NOT_FOUND_ERROR;
import static it.getinsight.message.MessageProperty.USER_NOT_AUTHORIZED;
import static it.getinsight.message.MessageProperty.USER_NOT_FOUND_ERROR;

@Service
@RequiredArgsConstructor
@Slf4j
public class RequestManagementService {

    private final RequestRepository requestRepository;
    private final RequestValidationService requestValidationService;
    private final RequestStatusManagementService requestStatusManagementService;
    private final RequestNotificationService requestNotificationService;
    private final AuthenticationContextService authenticationContextService;
    private final UserAccessValidationService userAccessValidationService;
    private final UserRepository userRepository;

    @Transactional(propagation = Propagation.REQUIRED)
    public void processRequestUpdate(Long id, RequestUpdateDTO requestUpdateDTO) {
        log.debug("Processing request update for ID: {} with status: {}", id, requestUpdateDTO.status());

        var requestEntity = findAndValidateRequest(id);

        validateUpdatePermissions(requestEntity, RequestStatus.valueOf(requestUpdateDTO.status()));

        requestStatusManagementService.processRequestStatusUpdate(id, requestUpdateDTO, requestEntity);

        if (shouldSendStatusNotification(requestEntity.getStatus())) {
            publishStatusNotificationEvent(requestEntity);
        }

        log.info("Request {} updated to status: {}", id, requestEntity.getStatus());
    }

    private RequestEntity findAndValidateRequest(Long id) {
        return requestRepository.findById(id)
            .orElseThrow(REQUEST_NOT_FOUND_ERROR::resourceNotFoundException);
    }

    private void validateUpdatePermissions(RequestEntity requestEntity, RequestStatus finalStatus) {
        var allowedTargetStatuses = getAllowedActionsForRequest(requestEntity).stream()
            .map(RequestAction::getTargetStatus)
            .collect(Collectors.toSet());

        if (!allowedTargetStatuses.contains(finalStatus)) {
            throw USER_NOT_AUTHORIZED.accessForbiddenException();
        }

        if (!userAccessValidationService.canTransitionToStatus(requestEntity, finalStatus)
            && Set.of(RequestStatus.APPROVED, RequestStatus.REJECTED, RequestStatus.REVOKED).contains(finalStatus)) {
            throw USER_NOT_AUTHORIZED.accessForbiddenException();
        }
        requestValidationService.validateClientStatus(requestEntity);
    }

    private boolean shouldSendStatusNotification(RequestStatus status) {
        return List.of(RequestStatus.APPROVED, RequestStatus.REJECTED, RequestStatus.CANCELED, RequestStatus.REVOKED)
            .contains(status);
    }

    private void publishStatusNotificationEvent(RequestEntity requestEntity) {
        String externalId = authenticationContextService.getCurrentUserId();
        String userId = userRepository.findByExternalId(externalId)
            .map(user -> user.getId().toString())
            .orElseThrow(USER_NOT_FOUND_ERROR::resourceNotFoundException);

        requestNotificationService.publishRequestStatusChanged(requestEntity, userId);
    }


    public List<RequestAction> getAllowedActionsForRequest(Long requestId) {
        var requestEntity = requestRepository.findById(requestId).orElseThrow(REQUEST_NOT_FOUND_ERROR::resourceNotFoundException);
        return getAllowedActionsForRequest(requestEntity);
    }



    public List<RequestAction> getAllowedActionsForRequest(RequestEntity requestEntity) {
        var isOwnerRequester = authenticationContextService.getCurrentUserId().equals(requestEntity.getRequestingUser().getExternalId());
        var canApprove = userAccessValidationService.canApproveRequest(requestEntity);
        var canReject = userAccessValidationService.canRejectRequest(requestEntity);
        var canRevoke = userAccessValidationService.canRevokeRequest(requestEntity);
        List<RequestAction> actions = new ArrayList<>();

        if (RequestStatus.PENDING.equals(requestEntity.getStatus())) {
            if (isOwnerRequester) {
                actions.add(RequestAction.CANCEL);
            }
            if (canApprove) {
                actions.add(RequestAction.APPROVE);
            }
            if (canReject) {
                actions.add(RequestAction.REJECT);
            }
        }

        if (RequestStatus.APPROVED.equals(requestEntity.getStatus()) && canRevoke) {
            actions.add(RequestAction.REVOKE);
        }

        return actions;
    }
}
