package it.getinsight.module.request.config;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "email-notification")
@Getter
@Setter
public class EmailNotificationProperties {

    private Url url;
    private StatusRequest statusRequest;
    private Approver approver;

    @Data
    public static class Url {
        private String clientUrl;
        private String hint;
    }

    @Data
    public static class StatusRequest {
        private String subject;
    }

    @Data
    public static class Approver {
        private String subject;
    }


}
