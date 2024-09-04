package it.getinsight.module.storage.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.List;

@Getter
@Setter
@Component
@ConfigurationProperties(prefix = "minio")
public class MinioConfigProperties {
    private String endpoint;
    private String accessKey;
    private String secretKey;
    private List<String> buckets;
}
