package it.getinsight.core.queue.config;

import it.getinsight.core.queue.QueueConfigProperties;
import it.getinsight.core.queue.QueueMessageHandler;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.beans.factory.SmartInitializingSingleton;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Configuration;

import java.util.List;


@Configuration
@RequiredArgsConstructor
@Slf4j
@ConditionalOnBean(QueueListenerFactory.class)
public class QueueListenerAutoConfiguration implements SmartInitializingSingleton {

    private final List<QueueMessageHandler<?>> handlers;
    private final QueueListenerFactory factory;
    private final QueueConfigProperties props;
    private final ConfigurableApplicationContext context;
    private final RabbitAdmin rabbitAdmin;

    @Override
    public void afterSingletonsInstantiated() {
        
        rabbitAdmin.initialize();

        log.info("Auto-configuring {} queue listeners", handlers.size());

        for (var handler : handlers) {
            String exchangeId = handler.exchangeId();
            String channel = handler.channel();
            String queueName = findQueue(exchangeId, channel);

            SimpleMessageListenerContainer container = factory.create(queueName, handler);

            String beanName = exchangeId + "-" + channel + "-listener";
            context.getBeanFactory().registerSingleton(beanName, container);
            container.start();

            log.info("Registered listener '{}' for queue '{}' (exchange={}, channel={})",
                beanName, queueName, exchangeId, channel);
        }
    }

    private String findQueue(String exchangeId, String channel) {
        return props.getExchangeById(exchangeId)
            .getBindings().stream()
            .filter(b -> channel.equals(b.getChannel()))
            .map(QueueConfigProperties.QueueBinding::getQueue)
            .findFirst()
            .orElseThrow(() -> new IllegalStateException(
                "No queue for exchange '" + exchangeId + "' channel '" + channel + "'"));
    }
}
