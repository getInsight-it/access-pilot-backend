package it.getinsight.common.queue.dto;

import lombok.Builder;

import java.io.Serial;
import java.io.Serializable;

@Builder
public record NotificationQueueMessage(
    Long requestId,
    NotificationType notificationType,
    String userId,
    String correlationId
) implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    public enum NotificationType {
        REQUEST_CREATED,
        REQUEST_STATUS_CHANGED
    }
}
