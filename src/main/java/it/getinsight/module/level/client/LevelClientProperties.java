package it.getinsight.module.level.client;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties(prefix = "level-client")
public class LevelClientProperties {

    private Timeout timeout = new Timeout();
    private Retry retry = new Retry();
    private Cache cache = new Cache();

    @Data
    public static class Timeout {
        private int connectMs = 10_000;
        private int readMs = 30_000;
    }

    @Data
    public static class Retry {
        private boolean enabled = true;
        private long periodMs = 100;
        private long maxPeriodMs = 1_000;
        private int maxAttempts = 2;
    }

    @Data
    public static class Cache {
        private int maxEntries = 20;
    }
}
