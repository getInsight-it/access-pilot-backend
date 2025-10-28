package it.getinsight.module.storage.config;

import io.minio.MakeBucketArgs;
import io.minio.MinioClient;
import io.minio.SetBucketPolicyArgs;
import io.minio.messages.Bucket;
import it.getinsight.core.exception.InfraException;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

import static it.getinsight.message.MessageProperty.*;

@Configuration
@EnableConfigurationProperties({MinioConfigProperties.class})
public class MinioConfig {

    @Bean
    public MinioClient minioClient(MinioConfigProperties minioConfigProperties) {
        MinioClient minioClient = MinioClient.builder()
            .endpoint(minioConfigProperties.getEndpoint())
            .credentials(minioConfigProperties.getAccessKey(), minioConfigProperties.getSecretKey())
            .build();
        try {
            List<Bucket> bucketList = minioClient.listBuckets();
            for (String bucket : minioConfigProperties.getBuckets()) {
                boolean bucketExists = bucketList.stream()
                    .anyMatch(b -> b.name().equals(bucket));
                if (!bucketExists) {
                    minioClient.makeBucket(MakeBucketArgs.builder().bucket(bucket).objectLock(true).build());
                    var policy = "{\"Version\":\"2012-10-17\",\"Statement\":[{\"Effect\":\"Allow\",\"Principal\":\"*\",\"Action\":[\"s3:GetObject\",\"s3:PutObject\"],\"Resource\":\"arn:aws:s3:::%s/*\"}]}".formatted(bucket);
                    minioClient.setBucketPolicy(SetBucketPolicyArgs.builder()
                        .bucket(bucket)
                        .config(policy)
                        .build());
                }
            }
        } catch (Exception e) {
            throw STORAGE_BUCKET_CREATION_ERROR.bind(e.getMessage()).infraException();
        }

        return minioClient;
    }

}
