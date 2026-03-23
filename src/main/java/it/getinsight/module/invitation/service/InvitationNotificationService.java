package it.getinsight.module.invitation.service;

import it.getinsight.core.queue.QueueMessageDTO;
import it.getinsight.core.queue.RoutingKeys;
import it.getinsight.module.invitation.entity.InvitationEntity;
import it.getinsight.module.notification.queue.NotificationPublisher;
import it.getinsight.module.notification.queue.dto.NotificationPayloadDTO;
import it.getinsight.module.user.service.AuthenticationContextService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class InvitationNotificationService {

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter
        .ofPattern("dd/MM/yyyy HH:mm")
        .withZone(ZoneId.of("America/Sao_Paulo"));

    private final NotificationPublisher notificationPublisher;
    private final AuthenticationContextService authenticationContextService;

    public void publishInvitationCreated(InvitationEntity invitation, String token) {
        var payload = NotificationPayloadDTO.builder()
            .notificationType(RoutingKeys.NOTIFICATION_INVITATION_CREATED)
            .invitationId(invitation.getId())
            .invitationToken(token)
            .invitationEmail(invitation.getEmail())
            .invitationRoleLabel(invitation.getRole().getLabel())
            .invitationClientLabel(invitation.getRole().getClient().getLabel())
            .invitationLevelName(invitation.getRole().getLevel() != null
                ? invitation.getRole().getLevel().getName()
                : null)
            .invitationCodeItem(invitation.getCodeItem())
            .invitationExpiresAt(invitation.getExpiresAt() != null
                ? DATE_FORMATTER.format(invitation.getExpiresAt())
                : null)
            .build();

        var message = QueueMessageDTO.<NotificationPayloadDTO>builder()
            .messageId(UUID.randomUUID().toString())
            .userId(authenticationContextService.getCurrentUserId())
            .payload(payload)
            .build();

        notificationPublisher.publish(message);

        log.info("Invitation notification published: invitationId={}, email={}",
            invitation.getId(), invitation.getEmail());
    }
}
