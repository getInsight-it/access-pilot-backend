package it.getinsight.module.level.repository;

import it.getinsight.core.dynamicquery.repository.DynamicNativeQueryRepository;
import it.getinsight.core.dynamicquery.repository.DynamicQueryRepository;
import it.getinsight.module.level.entity.ItemEntity;
import it.getinsight.module.level.entity.LevelEntity;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ItemRepository extends JpaRepository<ItemEntity, Long>, JpaSpecificationExecutor<ItemEntity>, DynamicQueryRepository, DynamicNativeQueryRepository {

    List<ItemEntity> findAllByLevelId(Long id);

    Page<ItemEntity> findAllByLevelId(Long id, Example<ItemEntity> example, Pageable pageable);

    Optional<ItemEntity> findByLevelIdAndId(Long id, Long itemId);

    List<ItemEntity> findAllByLevelNameIn(List<String> names);

    @Modifying
    @Query("UPDATE ItemEntity i SET i.active = false WHERE i.id = :id")
    void softDelete(@Param("id") Long id);

    Boolean existsByLevelIdAndId(Long id, Long itemId);

    Boolean existsItemEntityByActiveTrueAndLevel(LevelEntity level);

    boolean existsItemEntityByActiveTrueAndLevelAndName(LevelEntity level, String name);

    List<ItemEntity> findAllByLevelIdAndParentId(Long id, Long itemId);

    Page<ItemEntity> findAllByLevelIdAndParentId(Long id, Long itemId, Example<ItemEntity> example, Pageable pageable);

    Integer countByLevel(LevelEntity level);
}
