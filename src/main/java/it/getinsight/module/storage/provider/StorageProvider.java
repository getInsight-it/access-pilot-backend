package it.getinsight.module.storage.provider;


import it.getinsight.module.storage.entity.StorageFileEntity;
import org.springframework.core.io.InputStreamResource;

import java.io.InputStream;

public interface StorageProvider {

    void uploadFile(StorageFileEntity storageFileEntity, InputStream file);

    InputStreamResource download(StorageFileEntity storageFileEntity);
}
