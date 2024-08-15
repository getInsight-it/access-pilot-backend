package it.getinsight;

import com.fasterxml.jackson.databind.ObjectMapper;
import it.getinsight.core.GetInsightSpringBootApp;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;


@EnableFeignClients
@SpringBootApplication
public class Application extends GetInsightSpringBootApp {

    public static void main(String[] args) {
        onApplicationLoaded(SpringApplication.run(Application.class, args));
    }

    @Bean
    public ObjectMapper objectMapper() {
        return new ObjectMapper();
    }

}
