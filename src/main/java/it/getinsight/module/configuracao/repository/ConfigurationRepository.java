package it.getinsight.module.configuracao.repository;

import it.getinsight.core.dynamicquery.repository.DynamicNativeQueryRepository;
import it.getinsight.core.dynamicquery.repository.DynamicQueryRepository;
import it.getinsight.module.configuracao.entity.ConfigurationEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface ConfigurationRepository extends JpaRepository<ConfigurationEntity, Long>, DynamicQueryRepository, DynamicNativeQueryRepository {

    Optional<ConfigurationEntity> findByClientId(String clientId);

}
