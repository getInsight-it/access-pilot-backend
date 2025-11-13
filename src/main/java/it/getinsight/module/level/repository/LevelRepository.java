package it.getinsight.module.level.repository;

import it.getinsight.core.dynamicquery.repository.DynamicNativeQueryRepository;
import it.getinsight.core.dynamicquery.repository.DynamicQueryRepository;
import it.getinsight.module.level.entity.LevelEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LevelRepository extends JpaRepository<LevelEntity, Long>, DynamicQueryRepository, DynamicNativeQueryRepository {

    boolean existsByName(String name);

    List<LevelEntity> findByNameIn(List<String> names);

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
