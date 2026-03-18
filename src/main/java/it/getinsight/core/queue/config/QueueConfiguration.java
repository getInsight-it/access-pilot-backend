package it.getinsight.core.queue.config;

import it.getinsight.core.queue.QueueConfigProperties;
import it.getinsight.core.queue.QueueConsumer;
import it.getinsight.core.queue.QueueMessageDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Declarable;
import org.springframework.amqp.core.Declarables;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.QueueBuilder;
import org.springframework.amqp.rabbit.config.RetryInterceptorBuilder;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.amqp.rabbit.retry.RejectAndDontRequeueRecoverer;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.retry.interceptor.RetryOperationsInterceptor;

import org.slf4j.MDC;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

@Configuration
@ConditionalOnProperty(prefix = "cali.queue.connection", name = "url")
@RequiredArgsConstructor
@Slf4j
public class QueueConfiguration {

    private final QueueConfigProperties queueConfigProperties;

    @Bean
    public ConnectionFactory connectionFactory() {
        String url = queueConfigProperties.getConnection().getUrl();
        String username = queueConfigProperties.getConnection().getCredential().getUsername();
        String password = queueConfigProperties.getConnection().getCredential().getPassword();

        log.info("Configuring RabbitMQ connection to: {}", url);

        CachingConnectionFactory factory = new CachingConnectionFactory();

        try {
            URI uri = new URI(url);
            factory.setHost(uri.getHost());
            factory.setPort(uri.getPort() > 0 ? uri.getPort() : 5672);
            if (uri.getPath() != null && uri.getPath().length() > 1) {
                factory.setVirtualHost(uri.getPath().substring(1));
            }
        } catch (Exception e) {
            log.warn("Could not parse URL {}, using default host/port", url);
            factory.setHost("localhost");
            factory.setPort(5672);
        }

        factory.setUsername(username);
        factory.setPassword(password);

        return factory;
    }

    @Bean
    public MessageConverter jsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory, MessageConverter messageConverter) {
        RabbitTemplate template = new RabbitTemplate(connectionFactory);
        template.setMessageConverter(messageConverter);
        return template;
    }

    @Bean
    public RabbitAdmin rabbitAdmin(ConnectionFactory connectionFactory) {
        RabbitAdmin admin = new RabbitAdmin(connectionFactory);
        admin.setAutoStartup(true);
        return admin;
    }

    @Bean
    public DirectExchange deadLetterExchange() {
        return new DirectExchange(queueConfigProperties.getDlq().getExchange());
    }

    @Bean
    public Declarables queues(DirectExchange deadLetterExchange) {
        List<String> queueNames = new ArrayList<>(queueConfigProperties.getListenTo());
        queueConfigProperties.getPublishTo().stream()
            .map(QueueConfigProperties.QueueDefinition::getName)
            .filter(name -> !queueNames.contains(name))
            .forEach(queueNames::add);

        String dlqSuffix = queueConfigProperties.getDlq().getSuffix();
        String dlxExchange = queueConfigProperties.getDlq().getExchange();

        List<Declarable> declarables = new ArrayList<>();
        declarables.add(deadLetterExchange);

        for (String queueName : queueNames) {
            String dlqName = queueName + dlqSuffix;

            Queue mainQueue = QueueBuilder.durable(queueName)
                .withArgument("x-dead-letter-exchange", dlxExchange)
                .withArgument("x-dead-letter-routing-key", dlqName)
                .build();

            Queue dlq = QueueBuilder.durable(dlqName).build();
            Binding dlqBinding = BindingBuilder.bind(dlq).to(deadLetterExchange).with(dlqName);

            declarables.add(mainQueue);
            declarables.add(dlq);
            declarables.add(dlqBinding);
        }

        log.info("Declaring queues with DLQ: {}", queueNames);
        return new Declarables(declarables);
    }

    @Bean
    public RetryOperationsInterceptor retryInterceptor() {
        var retry = queueConfigProperties.getRetry();
        return RetryInterceptorBuilder.stateless()
            .maxAttempts(retry.getMaxAttempts())
            .backOffOptions(retry.getInitialInterval(), retry.getMultiplier(), retry.getMaxInterval())
            .recoverer(new RejectAndDontRequeueRecoverer())
            .build();
    }

    @Bean
    public SimpleMessageListenerContainer messageListenerContainer(
            ConnectionFactory connectionFactory,
            MessageConverter messageConverter,
            RetryOperationsInterceptor retryInterceptor,
            @Autowired ApplicationContext applicationContext) {

        List<String> queueNames = queueConfigProperties.getListenTo();
        if (queueNames.isEmpty()) {
            log.info("No queues configured to listen to");
            return new SimpleMessageListenerContainer();
        }

        log.info("Configuring listener for queues: {}", queueNames);

        SimpleMessageListenerContainer container = new SimpleMessageListenerContainer();
        container.setConnectionFactory(connectionFactory);
        container.setQueueNames(queueNames.toArray(new String[0]));
        container.setMissingQueuesFatal(false);
        container.setDefaultRequeueRejected(false);
        container.setAdviceChain(retryInterceptor);

        container.setMessageListener(message -> {
            String correlationId = message.getMessageProperties().getCorrelationId();
            try {
                if (correlationId != null) {
                    MDC.put("correlationId", correlationId);
                }

                QueueMessageDTO dto = (QueueMessageDTO) messageConverter.fromMessage(message);
                String beanName = dto.getListenerBeanName();

                if (beanName == null || beanName.isBlank()) {
                    log.error("Message received without listenerBeanName, cannot route");
                    return;
                }

                Object bean = applicationContext.getBean(beanName);
                if (bean instanceof QueueConsumer consumer) {
                    log.debug("Routing message to consumer: {}, correlationId: {}", beanName, correlationId);
                    consumer.onMessage(dto);
                } else {
                    log.error("Bean {} is not a QueueConsumer", beanName);
                }
            } catch (Exception e) {
                log.error("Error processing queue message, attempt will be retried", e);
                throw new RuntimeException(e);
            } finally {
                MDC.remove("correlationId");
            }
        });

        return container;
    }
}
