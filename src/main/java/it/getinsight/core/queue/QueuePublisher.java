package it.getinsight.core.queue;

import org.slf4j.MDC;
import org.springframework.amqp.rabbit.core.RabbitTemplate;


public interface QueuePublisher<T extends QueueMessageDTO<?>> {

    
    String exchangeId();

    
    RabbitTemplate rabbitTemplate();

    
    QueueConfigProperties queueConfigProperties();

    
    void publish(T message);

    
    default void publish(String routingKey, T message) {
        String exchangeName = queueConfigProperties().getExchangeById(exchangeId()).getName();

        rabbitTemplate().convertAndSend(exchangeName, routingKey, message, msg -> {
            msg.getMessageProperties().setMessageId(message.getMessageId());
            String correlationId = MDC.get("correlationId");
            if (correlationId != null) {
                msg.getMessageProperties().setCorrelationId(correlationId);
            }
            return msg;
        });
    }
}
