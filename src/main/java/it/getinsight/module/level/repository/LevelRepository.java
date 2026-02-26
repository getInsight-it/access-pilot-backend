package it.getinsight.module.level.repository;

import it.getinsight.core.dynamicquery.repository.DynamicNativeQueryRepository;
import it.getinsight.core.dynamicquery.repository.DynamicQueryRepository;
import it.getinsight.module.level.entity.LevelEntity;
import it.getinsight.module.level.entity.LevelType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface LevelRepository extends JpaRepository<LevelEntity, Long>, JpaSpecificationExecutor<LevelEntity>, DynamicQueryRepository, DynamicNativeQueryRepository {

    boolean existsByNameIgnoreCaseAndActiveTrue(String name);

    List<LevelEntity> findByNameIn(List<String> names);

    @Query("SELECT l FROM LevelEntity l WHERE lower(l.name) IN :names")
    List<LevelEntity> findByNameIgnoreCaseIn(@Param("names") List<String> names);

    Optional<LevelEntity> findByNameIgnoreCaseAndTypeAndActiveTrue(String name, LevelType type);

    @Modifying
    @Query("UPDATE LevelEntity l SET l.active = false WHERE l.id = :id")
    void softDelete(@Param("id") Long id);


    @Query(value = """
        WITH RECURSIVE level_ancestors AS (
            SELECT l.*
            FROM TB_ESFERA l
            WHERE l.ID = :childLevelId
            AND l.ATIVO = true

            UNION ALL

            SELECT l.*
            FROM TB_ESFERA l
            INNER JOIN level_ancestors la ON l.ID = la.ID_PARENT
            WHERE l.ATIVO = true
        )
        SELECT DISTINCT la.*
        FROM level_ancestors la
        """, nativeQuery = true)
    List<LevelEntity> findAncestorLevels(@Param("childLevelId") Long childLevelId);

    boolean existsByParentIdAndActiveTrue(Long id);
}
