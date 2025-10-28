package it.getinsight.config;

import feign.FeignException;
import io.github.resilience4j.retry.RetryConfig;
import io.github.resilience4j.retry.RetryRegistry;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.net.SocketTimeoutException;
import org.springframework.web.client.HttpServerErrorException;

@Configuration
public class Resilience4jConfig {

    @Bean
    public RetryRegistry retryRegistry() {
        RetryConfig config = RetryConfig.custom()
            .maxAttempts(3)
            .waitDuration(java.time.Duration.ofMillis(500))
            .retryExceptions(
                HttpServerErrorException.class,
                SocketTimeoutException.class,
                feign.RetryableException.class
            )
            .retryOnException(ex ->
                (ex instanceof FeignException feignException && feignException.status() == 404)
            )
            .build();

        return RetryRegistry.of(config);
    }
}
