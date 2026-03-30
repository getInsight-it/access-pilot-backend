package it.getinsight.core.queue.config;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import it.getinsight.core.queue.QueueMessageDTO;
import it.getinsight.core.queue.QueueMessageHandler;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.retry.interceptor.RetryOperationsInterceptor;
import org.springframework.retry.support.RetrySynchronizationManager;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
@ConditionalOnBean(ConnectionFactory.class)
public class QueueListenerFactory {

    private final ConnectionFactory connectionFactory;
    private final ObjectMapper objectMapper;
    private final RetryOperationsInterceptor retryInterceptor;

    public <T> SimpleMessageListenerContainer create(String queueName, QueueMessageHandler<T> handler) {
        log.info("Creating listener container for queue: {}", queueName);

        SimpleMessageListenerContainer container = new SimpleMessageListenerContainer();
        container.setConnectionFactory(connectionFactory);
        container.setQueueNames(queueName);
        container.setMissingQueuesFatal(false);
        container.setDefaultRequeueRejected(false);
        container.setAdviceChain(retryInterceptor);

        container.setMessageListener(message -> {
            String correlationId = message.getMessageProperties().getCorrelationId();
            String routingKey = message.getMessageProperties().getReceivedRoutingKey();
            String messageId = message.getMessageProperties().getMessageId();

            try {
                if (correlationId != null) {
                    MDC.put("correlationId", correlationId);
                }

                QueueMessageDTO<T> dto = deserialize(message.getBody(), handler.payloadType());

                if (dto.getMessageId() == null && messageId != null) {
                    dto.setMessageId(messageId);
                }

                handler.onMessage(dto, routingKey);

            } catch (Exception e) {
                var ctx = RetrySynchronizationManager.getContext();
                int attempt = ctx != null ? ctx.getRetryCount() + 1 : 1;
                log.warn("Retry attempt {} failed on queue {}: routingKey={}, messageId={}, error={}",
                    attempt, queueName, routingKey, messageId, e.getMessage());
                if (e instanceof RuntimeException re) throw re;
                throw new RuntimeException(e);
            } finally {
                MDC.remove("correlationId");
            }
        });

        return container;
    }

    private <T> QueueMessageDTO<T> deserialize(byte[] body, Class<T> payloadType) throws Exception {
        JavaType type = objectMapper.getTypeFactory()
            .constructParametricType(QueueMessageDTO.class, payloadType);
        return objectMapper.readValue(body, type);
    }
}
