package it.getinsight.module.invitation.service;

import it.getinsight.core.queue.QueueMessageDTO;
import it.getinsight.core.queue.RoutingKeys;
import it.getinsight.module.invitation.entity.InvitationEntity;
import it.getinsight.module.level.client.LevelClient;
import it.getinsight.module.level.entity.LevelEntity;
import it.getinsight.module.level.entity.ItemEntity;
import it.getinsight.module.level.repository.ItemRepository;
import it.getinsight.module.notification.queue.NotificationPublisher;
import it.getinsight.module.notification.queue.dto.NotificationPayloadDTO;
import it.getinsight.module.user.service.AuthenticationContextService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
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
    private final ItemRepository itemRepository;
    private final LevelClient levelClient;

    public void publishInvitationCreated(InvitationEntity invitation, String invitationUuid) {

        var payload = NotificationPayloadDTO.builder()
            .notificationType(RoutingKeys.NOTIFICATION_INVITATION_CREATED)
            .invitationId(invitation.getId())
            .invitationUuid(invitationUuid)
            .invitationEmail(invitation.getEmail())
            .invitationRoleLabel(invitation.getRole().getLabel())
            .invitationClientLabel(invitation.getRole().getClient().getLabel())
            .invitationLevelName(invitation.getRole().getLevel() != null
                ? invitation.getRole().getLevel().getName()
                : null)
            .invitationCodeItem(invitation.getCodeItem())
            .invitationItemLabel(resolveInvitationItemLabel(invitation))
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

    private String resolveInvitationItemLabel(InvitationEntity invitation) {
        String codeItem = invitation.getCodeItem();
        if (StringUtils.isBlank(codeItem) || invitation.getRole() == null || invitation.getRole().getLevel() == null) {
            return null;
        }

        LevelEntity level = invitation.getRole().getLevel();
        try {
            return switch (level.getType()) {
                case EXTERNAL -> resolveExternalItemLabel(level, codeItem);
                case BUILT_IN, BUSINESS -> itemRepository.findByLevelIdAndExternalCode(level.getId(), codeItem)
                    .map(this::extractItemLabel)
                    .orElse(null);
            };
        } catch (Exception ex) {
            log.warn("Could not resolve invitation item label for invitationId={} codeItem={}",
                invitation.getId(), codeItem, ex);
            return null;
        }
    }

    private String resolveExternalItemLabel(LevelEntity level, String codeItem) {
        var item = levelClient.getItemByExternalCode(level.getExternalUrl(), level.getApiKey(), codeItem);
        if (item == null) {
            return null;
        }

        if (StringUtils.isNotBlank(item.name())) {
            return item.name();
        }
        if (StringUtils.isNotBlank(item.description())) {
            return item.description();
        }
        return null;
    }

    private String extractItemLabel(ItemEntity item) {
        if (StringUtils.isNotBlank(item.getName())) {
            return item.getName();
        }
        if (StringUtils.isNotBlank(item.getDescription())) {
            return item.getDescription();
        }
        return null;
    }
}
