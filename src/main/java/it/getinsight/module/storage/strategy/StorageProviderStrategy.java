package it.getinsight.module.storage.strategy;

import it.getinsight.module.storage.entity.StorageFileEntity;

import java.io.InputStream;
import java.util.List;

/**
 * Strategy interface para provedores de armazenamento.
 * Permite diferentes implementações de armazenamento de arquivos.
 * 
 * Implementações possíveis:
 * - MinIOStorageProvider (atual)
 * - S3StorageProvider (futura)
 * - LocalStorageProvider (desenvolvimento)
 */
public interface StorageProviderStrategy {

    /**
     * Faz upload de um arquivo para o armazenamento.
     */
    StorageFileEntity upload(String bucket, Boolean isPublic, Boolean ephemeral, 
                           String ownerId, String originalFilename, String contentType, 
                           Long size, InputStream inputStream);

    /**
     * Salva múltiplos arquivos.
     */
    List<StorageFileEntity> saveAll(List<org.springframework.web.multipart.MultipartFile> attachments, 
                                  String bucket, Boolean isPublic, Boolean ephemeral, String ownerId);

    /**
     * Faz download de um arquivo.
     */
    InputStream download(String bucket, String filename);

    /**
     * Deleta um arquivo do armazenamento.
     */
    void delete(String bucket, String filename);

    /**
     * Verifica se um arquivo existe.
     */
    boolean exists(String bucket, String filename);

    /**
     * Lista arquivos em um bucket.
     */
    List<String> listFiles(String bucket, String prefix);

    /**
     * Obtém informações de um arquivo.
     */
    StorageFileEntity getFileInfo(String bucket, String filename);
}
