package it.getinsight.module.web_notification.dto;


import it.getinsight.module.notification.dto.Notification;
import it.getinsight.module.notification.enums.NotificationType;
import lombok.Builder;

import java.util.Date;

@Builder
public record WebNotificationDTO(
    Long id,
    String uuid,
    String criadoPor,
    Date criacao,
    String modificadoPor,
    Date ultimaAlteracao,
    Long userId,
    String externalId,
    Long requestId,
    Long priority,
    Boolean isOpened,
    String title,
    String description,
    NotificationType type

) implements Notification {}










