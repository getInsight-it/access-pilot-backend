package it.getinsight.module.storage.repository;


import it.getinsight.core.dynamicquery.repository.DynamicNativeQueryRepository;
import it.getinsight.core.dynamicquery.repository.DynamicQueryRepository;
import it.getinsight.module.storage.entity.StorageFileEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface StorageFileRepository extends JpaRepository<StorageFileEntity, Long>, DynamicQueryRepository, DynamicNativeQueryRepository {
    Optional<StorageFileEntity> findByOriginalFilename(String name);
}
