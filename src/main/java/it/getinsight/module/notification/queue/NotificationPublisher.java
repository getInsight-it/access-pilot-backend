package it.getinsight.module.notification.queue;

import it.getinsight.core.queue.QueueConfigProperties;
import it.getinsight.core.queue.QueueMessageDTO;
import it.getinsight.core.queue.QueuePublisher;
import it.getinsight.core.queue.RoutingKeys;
import it.getinsight.module.notification.queue.dto.NotificationPayloadDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

import java.util.Objects;


@Component
@RequiredArgsConstructor
@Slf4j
public class NotificationPublisher implements QueuePublisher<QueueMessageDTO<NotificationPayloadDTO>> {

    private static final String NOTIFICATIONS_EXCHANGE = "notifications";

    private final RabbitTemplate rabbitTemplate;
    private final QueueConfigProperties queueConfigProperties;

    @Override
    public String exchangeId() {
        return NOTIFICATIONS_EXCHANGE;
    }

    @Override
    public RabbitTemplate rabbitTemplate() {
        return rabbitTemplate;
    }

    @Override
    public QueueConfigProperties queueConfigProperties() {
        return queueConfigProperties;
    }

    
    @Override
    public void publish(QueueMessageDTO<NotificationPayloadDTO> message) {
        Objects.requireNonNull(message, "message is required");
        Objects.requireNonNull(message.getPayload(), "payload is required");

        publish(RoutingKeys.NOTIFY_EMAIL, message);
        publish(RoutingKeys.NOTIFY_WEB, message);

        log.info("Notification published to all channels: recipientId={}, type={}, messageId={}",
            message.getPayload().getRecipientId(),
            message.getPayload().getNotificationType(),
            message.getMessageId());
    }
}
