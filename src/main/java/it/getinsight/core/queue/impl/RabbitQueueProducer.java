package it.getinsight.core.queue.impl;

import it.getinsight.core.queue.QueueMessageDTO;
import it.getinsight.core.queue.QueueProducer;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

import java.util.UUID;


@Component
@RequiredArgsConstructor
@Slf4j
public class RabbitQueueProducer implements QueueProducer {

    private final RabbitTemplate rabbitTemplate;

    @Override
    public void publish(QueueMessageDTO message, String queueName) {
        String correlationId = UUID.randomUUID().toString();

        log.debug("Publishing message to queue {}: objectId={}, listener={}, correlationId={}",
            queueName, message.getObjectIdentifier(), message.getListenerBeanName(), correlationId);

        rabbitTemplate.convertAndSend(queueName, message, msg -> {
            msg.getMessageProperties().setCorrelationId(correlationId);
            return msg;
        });

        log.debug("Message published successfully to queue {}", queueName);
    }
}
