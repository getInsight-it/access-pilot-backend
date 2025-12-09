package it.getinsight.module.request.service;

import it.getinsight.module.request.dto.RequestUpdateDTO;
import it.getinsight.module.request.entity.RequestEntity;
import it.getinsight.module.request.enuns.RequestAction;
import it.getinsight.module.request.enuns.RequestStatus;
import it.getinsight.module.request.event.RequestStatusToUserEvent;
import it.getinsight.module.request.repository.RequestRepository;
import it.getinsight.module.user.service.AuthenticationContextService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

import static it.getinsight.message.MessageProperty.REQUEST_NOT_FOUND_ERROR;
import static it.getinsight.message.MessageProperty.USER_NOT_AUTHORIZED;

@Service
@RequiredArgsConstructor
@Slf4j
public class RequestManagementService {

    private final RequestRepository requestRepository;
    private final RequestValidationService requestValidationService;
    private final RequestStatusManagementService requestStatusManagementService;
    private final ApplicationEventPublisher applicationEventPublisher;
    private final AuthenticationContextService authenticationContextService;
    private final UserAccessValidationService userAccessValidationService;

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
        if (getAllowedActionsForRequest(requestEntity).stream().anyMatch(action -> action.getTargetStatus().isHierarchicalApprovalStatus(finalStatus))) {
            requestValidationService.validateUserPermissionToUpdateToAllowOrDenyRequest(requestEntity);
        }else if (getAllowedActionsForRequest(requestEntity).stream().noneMatch(action -> action.getTargetStatus().isNotHierarchicalApprovalStatus(finalStatus))) {
            throw USER_NOT_AUTHORIZED.accessForbiddenException();
        }
        requestValidationService.validateClientStatus(requestEntity);
    }

    private boolean shouldSendStatusNotification(RequestStatus status) {
        return List.of(RequestStatus.APPROVED, RequestStatus.REJECTED, RequestStatus.CANCELED, RequestStatus.REVOKED)
            .contains(status);
    }

    private void publishStatusNotificationEvent(RequestEntity requestEntity) {
        applicationEventPublisher.publishEvent(
            new RequestStatusToUserEvent(requestEntity.getId())
        );
    }


    public List<RequestAction> getAllowedActionsForRequest(Long requestId) {
        var requestEntity = requestRepository.findById(requestId).orElseThrow(REQUEST_NOT_FOUND_ERROR::resourceNotFoundException);
        return getAllowedActionsForRequest(requestEntity);
    }



    public List<RequestAction> getAllowedActionsForRequest(RequestEntity requestEntity) {
        var isOwnerRequester = authenticationContextService.getCurrentUserId().equals(requestEntity.getRequestingUser().getExternalId());
        var canApproveOrReject = !userAccessValidationService.hasNoValidScopeWithHierarchyForRequest(requestEntity);
        List<RequestAction> actions = new ArrayList<>();
        if (isOwnerRequester){
            actions.add(RequestAction.CANCEL);
        }
        if (canApproveOrReject) {
            actions.add(RequestAction.APPROVE);
            actions.add(RequestAction.REJECT);
        }
        if (RequestStatus.APPROVED.equals(requestEntity.getStatus())) {
            actions.add(RequestAction.REVOKE);
        }
        return actions;
    }
}

