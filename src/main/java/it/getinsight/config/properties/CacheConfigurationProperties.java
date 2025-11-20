package it.getinsight.config.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

@Data
@Component
@ConfigurationProperties(prefix = "spring.cache.app")
public class CacheConfigurationProperties {
    private Duration defaultTtl = Duration.ofMinutes(1);
    private Map<String, Duration> ttl = new HashMap<>();
}
