package it.getinsight.module.request.service;

import it.getinsight.core.queue.QueueMessageDTO;
import it.getinsight.core.queue.RoutingKeys;
import it.getinsight.module.notification.queue.NotificationPublisher;
import it.getinsight.module.notification.queue.dto.NotificationPayloadDTO;
import it.getinsight.module.request.entity.RequestEntity;
import it.getinsight.module.request.queue.RequestEventPublisher;
import it.getinsight.module.request.queue.dto.RequestEventPayloadDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.UUID;


@Service
@RequiredArgsConstructor
@Slf4j
public class RequestNotificationService {

    private final RequestEventPublisher requestEventPublisher;
    private final NotificationPublisher notificationPublisher;
    private final ApproverEmailBuilderService approverEmailBuilderService;

    
    public void publishRequestCreated(RequestEntity request, String actorUserId) {
        var message = buildRequestEventMessage(request, RoutingKeys.REQUEST_CREATED, actorUserId);
        requestEventPublisher.publish(message);
    }

    
    public void publishRequestStatusChanged(RequestEntity request, String actorUserId) {
        var message = buildRequestEventMessage(request, RoutingKeys.REQUEST_STATUS_CHANGED, actorUserId);
        requestEventPublisher.publish(message);
    }

    
    public void notifyRequestCreated(RequestEntity request, String actorUserId) {
        var approvers = approverEmailBuilderService.findApproversForRole(request);
        log.info("Queueing notifications for {} approvers on request {}", approvers.size(), request.getId());

        approvers.forEach(approver -> {
            var message = buildNotificationMessage(request.getId(), approver.id(), RoutingKeys.NOTIFICATION_REQUEST_CREATED_APPROVER, actorUserId);
            notificationPublisher.publish(message);
        });

        var requesterMessage = buildNotificationMessage(
            request.getId(),
            request.getRequestingUser().getId(),
            RoutingKeys.NOTIFICATION_REQUEST_CREATED_CONFIRMATION,
            actorUserId
        );
        notificationPublisher.publish(requesterMessage);

        log.info("Notification events published for request {} creation", request.getId());
    }

    
    public void notifyStatusChanged(RequestEntity request, String actorUserId) {
        var message = buildNotificationMessage(
            request.getId(),
            request.getRequestingUser().getId(),
            RoutingKeys.NOTIFICATION_REQUEST_STATUS_CHANGED,
            actorUserId
        );
        notificationPublisher.publish(message);

        log.info("Notification events published for request {} status change to {}",
            request.getId(), request.getStatus());
    }

    
    private QueueMessageDTO<RequestEventPayloadDTO> buildRequestEventMessage(
            RequestEntity request, String eventType, String userId) {
        var payload = RequestEventPayloadDTO.builder()
            .eventType(eventType)
            .requestId(request.getId())
            .requesterId(request.getRequestingUser().getId())
            .roleId(request.getRole() != null ? request.getRole().getId() : null)
            .status(request.getStatus().name())
            .protocolCode(request.getProtocolCode())
            .build();

        return QueueMessageDTO.<RequestEventPayloadDTO>builder()
            .messageId(UUID.randomUUID().toString())
            .userId(userId)
            .payload(payload)
            .build();
    }

    
    private QueueMessageDTO<NotificationPayloadDTO> buildNotificationMessage(
            Long requestId, Long recipientId, String type, String userId) {
        var payload = NotificationPayloadDTO.builder()
            .version("v1")
            .requestId(requestId)
            .recipientId(recipientId)
            .notificationType(type)
            .build();

        return QueueMessageDTO.<NotificationPayloadDTO>builder()
            .messageId(UUID.randomUUID().toString())
            .userId(userId)
            .payload(payload)
            .build();
    }
}
