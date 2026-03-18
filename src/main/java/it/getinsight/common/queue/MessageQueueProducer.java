package it.getinsight.common.queue;

import it.getinsight.common.queue.dto.NotificationQueueMessage;
import it.getinsight.core.queue.QueueConfigProperties;
import it.getinsight.core.queue.QueueMessageDTO;
import it.getinsight.core.queue.QueueProducer;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class MessageQueueProducer {

    private static final String REQUEST_CREATED_CONSUMER = "requestCreatedQueueConsumer";
    private static final String REQUEST_STATUS_CONSUMER = "requestStatusQueueConsumer";

    private final QueueProducer queueProducer;
    private final QueueConfigProperties queueConfigProperties;

    public void publishNotification(Long requestId, NotificationQueueMessage.NotificationType notificationType, Long userId) {
        String consumerBeanName = resolveConsumerBeanName(notificationType);

        publishToQueue(requestId, consumerBeanName, userId != null ? userId.toString() : null, notificationType);
    }

    private String resolveConsumerBeanName(NotificationQueueMessage.NotificationType notificationType) {
        return switch (notificationType) {
            case REQUEST_CREATED -> REQUEST_CREATED_CONSUMER;
            case REQUEST_STATUS_CHANGED -> REQUEST_STATUS_CONSUMER;
        };
    }

    private void publishToQueue(Long requestId, String consumerBeanName, String userId,
                                NotificationQueueMessage.NotificationType notificationType) {
        queueConfigProperties.getPublishTo().stream()
            .findFirst()
            .ifPresent(queue -> {
                try {
                    QueueMessageDTO message = QueueMessageDTO.builder()
                        .objectIdentifier(requestId.toString())
                        .listenerBeanName(consumerBeanName)
                        .userId(userId)
                        .dlq(false)
                        .build();

                    queueProducer.publish(message, queue.getName());

                    log.info("Notification message published: requestId={}, type={}, consumer={}",
                        requestId, notificationType, consumerBeanName);

                } catch (Exception e) {
                    log.error("Failed to publish notification message: requestId={}, type={}",
                        requestId, notificationType, e);
                    throw new RuntimeException("Failed to publish notification message", e);
                }
            });
    }
}
