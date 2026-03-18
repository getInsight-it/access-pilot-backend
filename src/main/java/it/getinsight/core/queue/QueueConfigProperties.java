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
    private List<String> listenTo = new ArrayList<>();
    private List<QueueDefinition> publishTo = new ArrayList<>();
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
    public static class QueueDefinition {
        private String name;
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
}
