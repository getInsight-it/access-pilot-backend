package it.getinsight.module.request.queue;

import it.getinsight.core.queue.QueueConfigProperties;
import it.getinsight.core.queue.QueueMessageDTO;
import it.getinsight.core.queue.QueuePublisher;
import it.getinsight.module.request.queue.dto.RequestEventPayloadDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

import java.util.Objects;


@Component
@RequiredArgsConstructor
@Slf4j
public class RequestEventPublisher implements QueuePublisher<QueueMessageDTO<RequestEventPayloadDTO>> {

    private static final String REQUEST_EVENTS_EXCHANGE = "request-events";

    private final RabbitTemplate rabbitTemplate;
    private final QueueConfigProperties queueConfigProperties;

    @Override
    public String exchangeId() {
        return REQUEST_EVENTS_EXCHANGE;
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
    public void publish(QueueMessageDTO<RequestEventPayloadDTO> message) {
        Objects.requireNonNull(message, "message is required");
        Objects.requireNonNull(message.getPayload(), "payload is required");

        String routingKey = message.getPayload().getEventType();
        publish(routingKey, message);

        log.info("Request event published: routingKey={}, requestId={}, messageId={}",
            routingKey, message.getPayload().getRequestId(), message.getMessageId());
    }
}
