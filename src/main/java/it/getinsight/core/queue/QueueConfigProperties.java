package it.getinsight.core.queue;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Data
@Component
@ConfigurationProperties(prefix = "cali.queue")
public class QueueConfigProperties {

    private Connection connection = new Connection();
    private List<ExchangeDefinition> exchanges = new ArrayList<>();
    private Retry retry = new Retry();
    private Dlq dlq = new Dlq();

    @Data
    public static class Connection {
        private String url;
        private Credential credential = new Credential();
    }

    @Data
    public static class Credential {
        private String username;
        private String password;
    }

    @Data
    public static class Retry {
        private int maxAttempts;
        private long initialInterval;
        private double multiplier;
        private long maxInterval;
    }

    @Data
    public static class Dlq {
        private String suffix;
        private String exchange;
    }

    @Data
    public static class ExchangeDefinition {
        private String id;
        private String name;
        private String type = "topic";
        private List<QueueBinding> bindings = new ArrayList<>();
    }

    @Data
    public static class QueueBinding {
        private String queue;
        private String routingPattern;
        private String channel;
    }

    public ExchangeDefinition getExchangeById(String id) {
        return exchanges.stream()
            .filter(exchange -> id.equals(exchange.getId()))
            .findFirst()
            .orElseThrow(() -> new IllegalStateException("No exchange configured with id: " + id));
    }
}
