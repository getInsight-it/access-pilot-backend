package it.getinsight.module.storage.strategy;

import it.getinsight.module.storage.entity.StorageFileEntity;

import java.io.InputStream;
import java.util.List;


public interface StorageProviderStrategy {


    StorageFileEntity upload(String bucket, Boolean isPublic, Boolean ephemeral,
                           String ownerId, String originalFilename, String contentType,
                           Long size, InputStream inputStream);


    List<StorageFileEntity> saveAll(List<org.springframework.web.multipart.MultipartFile> attachments,
                                  String bucket, Boolean isPublic, Boolean ephemeral, String ownerId);


    InputStream download(String bucket, String filename);


    void delete(String bucket, String filename);


    boolean exists(String bucket, String filename);


    List<String> listFiles(String bucket, String prefix);


    StorageFileEntity getFileInfo(String bucket, String filename);
}
