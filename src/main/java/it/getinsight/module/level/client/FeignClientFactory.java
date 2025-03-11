package it.getinsight.module.level.client;

import feign.Feign;
import feign.jackson.JacksonDecoder;
import feign.jackson.JacksonEncoder;
import org.springframework.cloud.openfeign.support.SpringMvcContract;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FeignClientFactory {

    public LevelClient createClient(String baseUrl) {
        return Feign.builder()
                .decoder(new JacksonDecoder())
                .encoder(new JacksonEncoder())
                .contract(new SpringMvcContract())
                .target(LevelClient.class, baseUrl);
    }
}
