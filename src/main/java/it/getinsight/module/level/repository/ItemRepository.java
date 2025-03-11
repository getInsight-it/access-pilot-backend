package it.getinsight.module.level.repository;

import it.getinsight.core.dynamicquery.repository.DynamicNativeQueryRepository;
import it.getinsight.core.dynamicquery.repository.DynamicQueryRepository;
import it.getinsight.module.level.entity.ItemEntity;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Repository
public interface ItemRepository extends JpaRepository<ItemEntity, Long>, DynamicQueryRepository, DynamicNativeQueryRepository {

    List<ItemEntity> findAllByLevelId(Long id);

    Page<ItemEntity> findAllByLevelId(Long id, Example<ItemEntity> example, Pageable pageable);

    Optional<ItemEntity> findByLevelIdAndId(Long id, Long itemId);


    List<ItemEntity> findAllByLevelIdAndParentId(Long id, Long itemId);

    Page<ItemEntity> findAllByLevelIdAndParentId(Long id, Long itemId, Example<ItemEntity> example, Pageable pageable);

}
