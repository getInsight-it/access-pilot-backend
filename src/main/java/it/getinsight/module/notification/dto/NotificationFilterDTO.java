package it.getinsight.module.notification.dto;

import it.getinsight.module.notification.enums.NotificationType;

import java.io.Serial;
import java.io.Serializable;

public record NotificationFilterDTO(
    NotificationType type,
    Long userId
) implements Serializable {
    @Serial
    private static final long serialVersionUID = -6731357049354425215L;
}
