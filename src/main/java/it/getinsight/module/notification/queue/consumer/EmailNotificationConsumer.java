package it.getinsight.module.notification.queue.consumer;

import it.getinsight.core.queue.QueueMessageDTO;
import it.getinsight.core.queue.QueueMessageHandler;
import it.getinsight.core.security.WithSecurityContext;
import it.getinsight.module.notification.queue.dto.NotificationPayloadDTO;
import it.getinsight.module.notification.service.NotificationEmailService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;


@Component
@RequiredArgsConstructor
@Slf4j
public class EmailNotificationConsumer implements QueueMessageHandler<NotificationPayloadDTO> {

    private static final String EXCHANGE_ID = "notifications";
    private static final String CHANNEL = "email";

    private final NotificationEmailService notificationEmailService;

    @Override
    public String exchangeId() {
        return EXCHANGE_ID;
    }

    @Override
    public String channel() {
        return CHANNEL;
    }

    @Override
    public Class<NotificationPayloadDTO> payloadType() {
        return NotificationPayloadDTO.class;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    @WithSecurityContext(userIdExpression = "#message.userId")
    public void onMessage(QueueMessageDTO<NotificationPayloadDTO> message, String routingKey) {
        var payload = message.getPayload();
        log.info("Sending email: type={}, recipientId={}, requestId={}",
            payload.getNotificationType(), payload.getRecipientId(), payload.getRequestId());

        notificationEmailService.sendEmail(payload);
    }
}
