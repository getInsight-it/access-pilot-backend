package it.getinsight.module.notification.service;

import it.getinsight.core.queue.RoutingKeys;
import it.getinsight.module.notification.enums.NotificationType;
import it.getinsight.module.notification.queue.dto.NotificationPayloadDTO;
import it.getinsight.module.request.repository.RequestRepository;
import it.getinsight.module.web_notification.dto.WebNotificationDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.UUID;


@Service
@RequiredArgsConstructor
@Slf4j
public class NotificationWebService {

    private final NotificationService notificationService;
    private final RequestRepository requestRepository;

    
    public void createWebNotification(NotificationPayloadDTO payload) {
        var request = requestRepository.findByIdWithRelationships(payload.getRequestId())
            .orElseThrow(() -> new IllegalStateException("Request not found: " + payload.getRequestId()));

        String description = resolveDescription(payload.getNotificationType(), request);

        var webDTO = WebNotificationDTO.builder()
            .userId(payload.getRecipientId())
            .title("protocolo: " + request.getProtocolCode())
            .uuid(UUID.randomUUID().toString())
            .requestId(request.getId())
            .isOpened(false)
            .type(NotificationType.WEB)
            .priority(1L)
            .description(description)
            .build();

        notificationService.send(webDTO);

        log.info("Web notification created: type={}, recipientId={}, requestId={}",
            payload.getNotificationType(), payload.getRecipientId(), payload.getRequestId());
    }

    private String resolveDescription(String notificationType, it.getinsight.module.request.entity.RequestEntity request) {
        return switch (notificationType) {
            case RoutingKeys.NOTIFICATION_REQUEST_CREATED_APPROVER ->
                "Nova solicitacao de acesso aguardando aprovacao";
            case RoutingKeys.NOTIFICATION_REQUEST_CREATED_CONFIRMATION ->
                request.getDescription() != null
                    ? request.getDescription()
                    : "Solicitacao de acesso criada com sucesso";
            case RoutingKeys.NOTIFICATION_REQUEST_STATUS_CHANGED ->
                "Status atualizado: " + request.getStatus().getDescription();
            default -> request.getDescription();
        };
    }
}
