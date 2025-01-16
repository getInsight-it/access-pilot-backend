package it.getinsight.module.storage.provider.impl;


import io.minio.BucketExistsArgs;
import io.minio.MakeBucketArgs;
import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import it.getinsight.core.exception.InfraException;
import it.getinsight.module.storage.entity.StorageFileEntity;
import it.getinsight.module.storage.provider.StorageProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.InputStreamResource;
import org.springframework.stereotype.Component;

import java.io.InputStream;

@Component
@RequiredArgsConstructor
public class MinioStorageProviderImpl implements StorageProvider {

    private final MinioClient minioClient;

    @Override
    public void uploadFile(StorageFileEntity storageFileEntity, InputStream file) {
        try {
            boolean bucketExists = minioClient.bucketExists(BucketExistsArgs.builder().bucket(storageFileEntity.getBucket()).build());
            if (!bucketExists) {
                minioClient.makeBucket(MakeBucketArgs.builder().bucket(storageFileEntity.getBucket()).build());
            }

            minioClient.putObject(
                    PutObjectArgs.builder()
                            .bucket(storageFileEntity.getBucket())
                            .object(storageFileEntity.getFileId().toString())
                            .stream(file, storageFileEntity.getFilesize(), -1)
                            .contentType(storageFileEntity.getMimeType())
                            .build()
            );
        } catch (Exception e) {
            throw new InfraException("Erro ao salvar arquivo", e);
        }
    }

    @Override
    public InputStreamResource download(StorageFileEntity storageFileEntity) {
        try {
            InputStream file = minioClient.getObject(
                    io.minio.GetObjectArgs.builder()
                            .bucket(storageFileEntity.getBucket())
                            .object(storageFileEntity.getFileId().toString())
                            .build()
            );
            return new InputStreamResource(file);
        } catch (Exception e) {
            throw new InfraException("Erro ao baixar arquivo", e);
        }
    }
}
