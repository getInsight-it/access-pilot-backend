package it.getinsight.module.client.repository;

import it.getinsight.core.dynamicquery.repository.DynamicNativeQueryRepository;
import it.getinsight.core.dynamicquery.repository.DynamicQueryRepository;
import it.getinsight.module.client.entity.ClientEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ClientRepository extends JpaRepository<ClientEntity, Long>, DynamicQueryRepository, DynamicNativeQueryRepository {

    Optional<ClientEntity> findByClientId(String clientId);

    List<ClientEntity> findAllByClientIdIn(List<String> ids);
}
