package it.getinsight.core.config;

import it.getinsight.core.config.properties.CacheConfigurationProperties;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.cache.CacheProperties;
import org.springframework.boot.autoconfigure.cache.RedisCacheManagerBuilderCustomizer;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;

import java.time.Duration;



@Slf4j
@Configuration
@EnableCaching
@RequiredArgsConstructor
public class CacheConfig {

    private final CacheConfigurationProperties cacheProperties;
    private final CacheProperties springCacheProperties;

    @Bean
    public RedisCacheManagerBuilderCustomizer redisCacheManagerBuilderCustomizer() {
        log.info("Configuring Redis cache manager with properties: {}", cacheProperties);

        return builder -> {

            var keySer = RedisSerializationContext.SerializationPair.fromSerializer(new StringRedisSerializer());
            var valSer = RedisSerializationContext.SerializationPair.fromSerializer(new GenericJackson2JsonRedisSerializer());

            var defaultConfig = createCacheConfiguration(
                keySer,
                valSer,
                cacheProperties.getDefaultTtl()
            );

            builder.cacheDefaults(defaultConfig);

            cacheProperties.getTtl().forEach((cacheName, ttl) -> {
                log.debug("Configuring cache '{}' with TTL: {}", cacheName, ttl);
                builder.withCacheConfiguration(
                    cacheName,
                    createCacheConfiguration(keySer, valSer, ttl)
                );
            });


        };
    }

    private RedisCacheConfiguration createCacheConfiguration(
            RedisSerializationContext.SerializationPair<String> keySerializer,
            RedisSerializationContext.SerializationPair<Object> valueSerializer,
            Duration ttl) {

        var config = RedisCacheConfiguration.defaultCacheConfig()
            .serializeKeysWith(keySerializer)
            .serializeValuesWith(valueSerializer);


        if (ttl != null && !ttl.isZero() && !ttl.isNegative()) {
            config = config.entryTtl(ttl);
        } else if (springCacheProperties.getRedis().getTimeToLive() != null) {
            config = config.entryTtl(springCacheProperties.getRedis().getTimeToLive());
        }

        var redisProps = springCacheProperties.getRedis();

        if (!redisProps.isCacheNullValues()) {
            config = config.disableCachingNullValues();
        }

        if (redisProps.getKeyPrefix() != null) {
            config = config.computePrefixWith(cacheName ->
                redisProps.getKeyPrefix().concat(":").concat(cacheName).concat(":")
            );
        } else if (!redisProps.isUseKeyPrefix()) {
            config = config.disableKeyPrefix();
        }

        return config;
    }
}
