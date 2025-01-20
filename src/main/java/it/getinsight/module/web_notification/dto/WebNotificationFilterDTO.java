package it.getinsight.module.web_notification.dto;


import it.getinsight.module.notification.enums.NotificationType;
import lombok.Builder;

import java.io.Serializable;

@Builder
public record WebNotificationFilterDTO(
    Long id,
    String uuid,
    Long userId,
    Long requestId,
    Long priority,
    Boolean isOpened,
    String title,
    String description,
    String externalId,
    NotificationType type
) implements Serializable {}










