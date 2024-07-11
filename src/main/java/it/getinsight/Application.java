package it.getinsight;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.camunda.zeebe.spring.client.annotation.Deployment;
import it.getinsight.core.GetInsightSpringBootApp;
import it.getinsight.module.solicitacao.process.SolicitacaoProcess;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Lazy;


@EnableFeignClients
@SpringBootApplication
@Deployment(resources = "classpath*:/bpmn/*.bpmn")
public class Application extends GetInsightSpringBootApp {

    public static void main(String[] args) {
        onApplicationLoaded(SpringApplication.run(Application.class, args));
    }


    @Bean
    @Lazy(false)
    public SolicitacaoProcess SolicitacaoProcess() {
        return new SolicitacaoProcess();
    }

    @Bean
    public ObjectMapper objectMapper() {
        return new ObjectMapper();
    }

}
