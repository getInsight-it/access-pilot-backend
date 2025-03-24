package it.getinsight.module.configuration.repository;

import it.getinsight.core.dynamicquery.repository.DynamicNativeQueryRepository;
import it.getinsight.core.dynamicquery.repository.DynamicQueryRepository;
import it.getinsight.module.configuration.entity.ConfigurationEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;


@Repository
public interface ConfigurationRepository extends JpaRepository<ConfigurationEntity, Long>, DynamicQueryRepository, DynamicNativeQueryRepository {

    @Modifying
    @Query("UPDATE ConfigurationEntity l SET l.active = false WHERE l.id = :id")
    void softDelete(@Param("id") Long id);

}
