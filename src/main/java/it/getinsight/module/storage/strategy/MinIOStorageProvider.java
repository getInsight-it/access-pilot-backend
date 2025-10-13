package it.getinsight.module.storage.strategy;

import it.getinsight.module.storage.entity.StorageFileEntity;
import io.minio.*;
import io.minio.messages.Item;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static it.getinsight.message.MessageProperty.*;

@Component("minioStorageProvider")
@ConditionalOnProperty(name = "storage.provider", havingValue = "minio", matchIfMissing = true)
@RequiredArgsConstructor
@Slf4j
public class MinIOStorageProvider implements StorageProviderStrategy {

    private final MinioClient minioClient;

    @Override
    public StorageFileEntity upload(String bucket, Boolean isPublic, Boolean ephemeral,
                                 String fileId, String originalFilename, String contentType,
                                 Long size, InputStream inputStream) {
        log.debug("MinIO: Uploading file {} to bucket {}", originalFilename, bucket);

        try {
            boolean bucketExists = minioClient.bucketExists(BucketExistsArgs.builder().bucket(bucket).build());
            if (!bucketExists) {
                minioClient.makeBucket(MakeBucketArgs.builder().bucket(bucket).build());
            }

            minioClient.putObject(
                PutObjectArgs.builder()
                    .bucket(bucket)
                    .object(fileId)
                    .stream(inputStream, size, -1)
                    .contentType(contentType)
                    .build()
            );

            return StorageFileEntity.builder()
                .fileId(UUID.fromString(fileId))
                .bucket(bucket)
                .originalFilename(originalFilename)
                .mimeType(contentType)
                .filesize(size)
                .isPublic(isPublic)
                .ephemeral(ephemeral)
                .ownerId(UUID.fromString(fileId))
                .build();

        } catch (Exception e) {
            log.error("Error uploading file {} to bucket {}", originalFilename, bucket, e);
            throw FILE_UPLOAD_FAILED_ERROR.bind(originalFilename).infraException();
        }
    }

    @Override
    public List<StorageFileEntity> saveAll(List<org.springframework.web.multipart.MultipartFile> attachments,
                                          String bucket, Boolean isPublic, Boolean ephemeral, String ownerId) {
        log.debug("MinIO: Saving {} files to bucket {}", attachments.size(), bucket);

        return attachments.stream()
            .map(file -> {
                try {
                    return upload(bucket, isPublic, ephemeral, ownerId,
                                file.getOriginalFilename(), file.getContentType(),
                                file.getSize(), file.getInputStream());
                } catch (Exception e) {
                    log.error("Error saving file {}", file.getOriginalFilename(), e);
                    throw FILE_SAVE_FAILED_ERROR.bind(file.getOriginalFilename()).infraException();
                }
            })
            .toList();
    }

    @Override
    public InputStream download(String bucket, String filename) {
        log.debug("MinIO: Downloading file {} from bucket {}", filename, bucket);

        try {
            return minioClient.getObject(
                GetObjectArgs.builder()
                    .bucket(bucket)
                    .object(filename)
                    .build()
            );
        } catch (Exception e) {
            log.error("Error downloading file {} from bucket {}", filename, bucket, e);
            throw FILE_DOWNLOAD_FAILED_ERROR.bind(filename).infraException();
        }
    }

    @Override
    public void delete(String bucket, String filename) {
        log.debug("MinIO: Deleting file {} from bucket {}", filename, bucket);

        try {
            minioClient.removeObject(
                RemoveObjectArgs.builder()
                    .bucket(bucket)
                    .object(filename)
                    .build()
            );
        } catch (Exception e) {
            log.error("Error deleting file {} from bucket {}", filename, bucket, e);
            throw FILE_DELETE_FAILED_ERROR.bind(filename).infraException();
        }
    }

    @Override
    public boolean exists(String bucket, String filename) {
        log.debug("MinIO: Checking if file {} exists in bucket {}", filename, bucket);

        try {
            minioClient.statObject(
                StatObjectArgs.builder()
                    .bucket(bucket)
                    .object(filename)
                    .build()
            );
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public List<String> listFiles(String bucket, String prefix) {
        log.debug("MinIO: Listing files in bucket {} with prefix {}", bucket, prefix);

        try {
            Iterable<Result<Item>> results = minioClient.listObjects(
                ListObjectsArgs.builder()
                    .bucket(bucket)
                    .prefix(prefix)
                    .build()
            );

            List<String> files = new ArrayList<>();
            for (Result<Item> result : results) {
                files.add(result.get().objectName());
            }
            return files;
        } catch (Exception e) {
            log.error("Error listing files in bucket {} with prefix {}", bucket, prefix, e);
            throw FILE_LIST_FAILED_ERROR.bind(bucket).infraException();
        }
    }

    @Override
    public StorageFileEntity getFileInfo(String bucket, String filename) {
        log.debug("MinIO: Getting file info for {} in bucket {}", filename, bucket);

        try {
            var stat = minioClient.statObject(
                StatObjectArgs.builder()
                    .bucket(bucket)
                    .object(filename)
                    .build()
            );

            return StorageFileEntity.builder()
                .fileId(UUID.randomUUID())
                .bucket(bucket)
                .originalFilename(filename)
                .mimeType(stat.contentType())
                .filesize(stat.size())
                .build();
        } catch (Exception e) {
            log.error("Error getting file info for {} in bucket {}", filename, bucket, e);
            throw FILE_INFO_FAILED_ERROR.bind(filename).infraException();
        }
    }
}
