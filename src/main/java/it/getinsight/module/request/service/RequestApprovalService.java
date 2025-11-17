package it.getinsight.module.request.service;

import it.getinsight.module.request.dto.RequestUpdateDTO;
import it.getinsight.module.request.entity.RequestEntity;
import it.getinsight.module.request.enuns.RequestStatus;
import it.getinsight.module.request.event.RequestStatusToUserEvent;
import it.getinsight.module.request.repository.RequestRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static it.getinsight.message.MessageProperty.REQUEST_NOT_FOUND_ERROR;

@Service
@RequiredArgsConstructor
@Slf4j
public class RequestApprovalService {

    private final RequestRepository requestRepository;
    private final RequestValidationService requestValidationService;
    private final RequestStatusManagementService requestStatusManagementService;
    private final ApplicationEventPublisher applicationEventPublisher;

    @Transactional(propagation = Propagation.REQUIRED)
    public void processRequestUpdate(Long id, RequestUpdateDTO requestUpdateDTO) {
        log.debug("Processing request update for ID: {} with status: {}", id, requestUpdateDTO.status());

        var requestEntity = findAndValidateRequest(id);

        validateUpdatePermissions(requestEntity);

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

    private void validateUpdatePermissions(RequestEntity requestEntity) {
        requestValidationService.validateUserPermissionToUpdateRequest(requestEntity);
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
}

