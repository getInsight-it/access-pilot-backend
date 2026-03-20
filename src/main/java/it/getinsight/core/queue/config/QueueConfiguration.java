package it.getinsight.core.queue.config;

import it.getinsight.core.queue.QueueConfigProperties;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.AbstractExchange;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Declarable;
import org.springframework.amqp.core.Declarables;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.FanoutExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.QueueBuilder;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.config.RetryInterceptorBuilder;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.retry.RejectAndDontRequeueRecoverer;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.retry.interceptor.RetryOperationsInterceptor;

import java.net.URI;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

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
    public Declarables queuesAndExchanges(DirectExchange deadLetterExchange) {
        List<Declarable> declarables = new ArrayList<>();
        declarables.add(deadLetterExchange);

        String dlqSuffix = queueConfigProperties.getDlq().getSuffix();
        Set<String> declaredQueues = new HashSet<>();
        Map<String, Queue> queueRegistry = new HashMap<>();

        for (var exchangeDefinition : queueConfigProperties.getExchanges()) {
            AbstractExchange exchange = buildExchange(exchangeDefinition);
            declarables.add(exchange);

            for (var binding : exchangeDefinition.getBindings()) {
                String queueName = binding.getQueue();
                String dlqName = queueName + dlqSuffix;

                Queue mainQueue = queueRegistry.computeIfAbsent(queueName, name -> QueueBuilder.durable(name)
                        .withArgument("x-dead-letter-exchange", deadLetterExchange.getName())
                        .withArgument("x-dead-letter-routing-key", dlqName)
                        .build());

                if (declaredQueues.add(queueName)) {
                    Queue dlq = QueueBuilder.durable(dlqName).build();

                    Binding dlqBinding = BindingBuilder.bind(dlq)
                        .to(deadLetterExchange)
                        .with(dlqName);

                    declarables.add(mainQueue);
                    declarables.add(dlq);
                    declarables.add(dlqBinding);
                }

                Binding queueBinding = buildBinding(binding, exchange, mainQueue);
                declarables.add(queueBinding);

                log.info("Configured queue '{}' on exchange '{}' with pattern '{}' and DLQ '{}'",
                    queueName, exchange.getName(), binding.getRoutingPattern(), dlqName);
            }
        }

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

    private AbstractExchange buildExchange(QueueConfigProperties.ExchangeDefinition exchangeDefinition) {
        String type = exchangeDefinition.getType() == null ? "topic" : exchangeDefinition.getType().toLowerCase(Locale.ROOT);
        return switch (type) {
            case "topic" -> new TopicExchange(exchangeDefinition.getName());
            case "direct" -> new DirectExchange(exchangeDefinition.getName());
            case "fanout" -> new FanoutExchange(exchangeDefinition.getName());
            default -> throw new IllegalArgumentException("Unsupported exchange type: " + exchangeDefinition.getType());
        };
    }

    private Binding buildBinding(QueueConfigProperties.QueueBinding binding, AbstractExchange exchange, Queue queue) {
        if (exchange instanceof FanoutExchange fanoutExchange) {
            return BindingBuilder.bind(queue).to(fanoutExchange);
        }
        String routingPattern = binding.getRoutingPattern();
        if (routingPattern == null || routingPattern.isBlank()) {
            throw new IllegalArgumentException("Routing pattern is required for exchange: " + exchange.getName());
        }
        if (exchange instanceof TopicExchange topicExchange) {
            return BindingBuilder.bind(queue).to(topicExchange).with(routingPattern);
        }
        if (exchange instanceof DirectExchange directExchange) {
            return BindingBuilder.bind(queue).to(directExchange).with(routingPattern);
        }
        throw new IllegalArgumentException("Unsupported exchange type for binding: " + exchange.getType());
    }
}
