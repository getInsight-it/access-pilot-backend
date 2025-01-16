package it.getinsight;

import it.getinsight.core.GetInsightSpringBootApp;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;


@EnableFeignClients
@SpringBootApplication
public class Application extends GetInsightSpringBootApp {

    public static void main(String[] args) {
        onApplicationLoaded(SpringApplication.run(Application.class, args));
    }

}
