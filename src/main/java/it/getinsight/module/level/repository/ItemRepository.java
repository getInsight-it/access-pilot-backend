package it.getinsight.module.level.repository;

import it.getinsight.core.dynamicquery.repository.DynamicNativeQueryRepository;
import it.getinsight.core.dynamicquery.repository.DynamicQueryRepository;
import it.getinsight.module.level.entity.ItemEntity;
import it.getinsight.module.level.entity.LevelEntity;
import it.getinsight.module.level.entity.LevelType;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Repository
public interface ItemRepository extends JpaRepository<ItemEntity, Long>, JpaSpecificationExecutor<ItemEntity>, DynamicQueryRepository, DynamicNativeQueryRepository {

    List<ItemEntity> findAllByLevelId(Long id);

    Page<ItemEntity> findAllByLevelId(Long id, Example<ItemEntity> example, Pageable pageable);

    Optional<ItemEntity> findByLevelIdAndId(Long id, Long itemId);

    List<ItemEntity> findAllByLevelNameIn(List<String> names);

    @Query("SELECT CASE WHEN i.level.type = :type THEN CAST(i.id AS string) ELSE i.externalCode END " +
            "FROM ItemEntity i " +
            "WHERE i.level.id = :levelId AND i.parent.id = :itemId")
    List<String> findAllSubItemCodesLevelIdAndId(@Param("levelId") Long id, @Param("itemId") String itemId, @Param("type") LevelType type);


    @Modifying
    @Query("UPDATE ItemEntity i SET i.active = false WHERE i.id = :id")
    void softDelete(@Param("id") Long id);

    Boolean existsByLevelIdAndId(Long id, Long itemId);

    Boolean existsItemEntityByActiveTrueAndLevel(LevelEntity level);

    boolean existsItemEntityByActiveTrueAndLevelAndName(LevelEntity level, String name);

    List<ItemEntity> findAllByLevelIdAndParentId(Long id, Long itemId);

    Page<ItemEntity> findAllByLevelIdAndParentId(Long id, Long itemId, Example<ItemEntity> example, Pageable pageable);

    @EntityGraph(attributePaths = {"level", "parent"})
    List<ItemEntity> findAllByIdIn(Collection<Long> ids);


    @Query(value = """
        WITH RECURSIVE item_ancestors AS (
            -- Base case: select the starting item
            SELECT i.*
            FROM TB_ESFERA_ITEM i
            WHERE i.ID = :itemId
            AND i.ATIVO = true

            UNION ALL

            -- Recursive case: find all parents
            SELECT i.*
            FROM TB_ESFERA_ITEM i
            INNER JOIN item_ancestors ia ON i.ID = ia.ID_PARENT
            WHERE i.ATIVO = true
        )
        SELECT DISTINCT ia.*
        FROM item_ancestors ia
        ORDER BY ia.ID ASC
        """, nativeQuery = true)
    List<ItemEntity> findAscendantTreeById(@Param("itemId") Long itemId);

    Integer countByLevel(LevelEntity level);
}
