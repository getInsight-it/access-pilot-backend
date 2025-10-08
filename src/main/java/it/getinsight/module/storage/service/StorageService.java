package it.getinsight.module.storage.service;

import it.getinsight.module.storage.entity.StorageFileEntity;
import it.getinsight.module.storage.strategy.StorageProviderStrategy;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.util.List;

/**
 * Service genérico que escolhe automaticamente qual implementação Strategy usar
 * baseado na configuração do application.yaml.
 * 
 * A estratégia é escolhida via:
 * - storage.provider=minio (padrão) -> MinIOStorageProvider
 * - storage.provider=s3 -> S3StorageProvider
 * - storage.provider=local -> LocalStorageProvider
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class StorageService {

    private final StorageProviderStrategy storageProviderStrategy;

    /**
     * Faz upload de um arquivo para o armazenamento.
     */
    public StorageFileEntity upload(String bucket, Boolean isPublic, Boolean ephemeral, 
                                  String ownerId, String originalFilename, String contentType, 
                                  Long size, InputStream inputStream) {
        log.debug("Using {} to upload file {} to bucket {}", 
                 storageProviderStrategy.getClass().getSimpleName(), originalFilename, bucket);
        return storageProviderStrategy.upload(bucket, isPublic, ephemeral, ownerId, 
                                            originalFilename, contentType, size, inputStream);
    }

    /**
     * Salva múltiplos arquivos.
     */
    public List<StorageFileEntity> saveAll(List<org.springframework.web.multipart.MultipartFile> attachments, 
                                          String bucket, Boolean isPublic, Boolean ephemeral, String ownerId) {
        log.debug("Using {} to save {} files to bucket {}", 
                 storageProviderStrategy.getClass().getSimpleName(), attachments.size(), bucket);
        return storageProviderStrategy.saveAll(attachments, bucket, isPublic, ephemeral, ownerId);
    }

    /**
     * Faz download de um arquivo.
     */
    public InputStream download(String bucket, String filename) {
        log.debug("Using {} to download file {} from bucket {}", 
                 storageProviderStrategy.getClass().getSimpleName(), filename, bucket);
        return storageProviderStrategy.download(bucket, filename);
    }

    /**
     * Deleta um arquivo do armazenamento.
     */
    public void delete(String bucket, String filename) {
        log.debug("Using {} to delete file {} from bucket {}", 
                 storageProviderStrategy.getClass().getSimpleName(), filename, bucket);
        storageProviderStrategy.delete(bucket, filename);
    }

    /**
     * Verifica se um arquivo existe.
     */
    public boolean exists(String bucket, String filename) {
        log.debug("Using {} to check if file {} exists in bucket {}", 
                 storageProviderStrategy.getClass().getSimpleName(), filename, bucket);
        return storageProviderStrategy.exists(bucket, filename);
    }

    /**
     * Lista arquivos em um bucket.
     */
    public List<String> listFiles(String bucket, String prefix) {
        log.debug("Using {} to list files in bucket {} with prefix {}", 
                 storageProviderStrategy.getClass().getSimpleName(), bucket, prefix);
        return storageProviderStrategy.listFiles(bucket, prefix);
    }

    /**
     * Obtém informações de um arquivo.
     */
    public StorageFileEntity getFileInfo(String bucket, String filename) {
        log.debug("Using {} to get file info for {} in bucket {}", 
                 storageProviderStrategy.getClass().getSimpleName(), filename, bucket);
        return storageProviderStrategy.getFileInfo(bucket, filename);
    }

    /**
     * Obtém o nome da implementação Strategy atual.
     */
    public String getCurrentProviderName() {
        return storageProviderStrategy.getClass().getSimpleName();
    }
}
