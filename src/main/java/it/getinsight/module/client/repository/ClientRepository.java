package it.getinsight.module.client.repository;

import it.getinsight.core.dynamicquery.repository.DynamicNativeQueryRepository;
import it.getinsight.core.dynamicquery.repository.DynamicQueryRepository;
import it.getinsight.module.client.entity.ClientEntity;
import it.getinsight.module.client.entity.ClientStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ClientRepository extends JpaRepository<ClientEntity, Long>, JpaSpecificationExecutor<ClientEntity>, DynamicQueryRepository, DynamicNativeQueryRepository {

    Optional<ClientEntity> findByClientId(String clientId);

    List<ClientEntity> findAllByClientIdIn(List<String> ids);

    @Query("SELECT c FROM ClientEntity c WHERE lower(c.clientId) IN :ids")
    List<ClientEntity> findByClientIdIgnoreCaseIn(@Param("ids") List<String> ids);

    boolean existsByClientId(String clientId);

    List<ClientEntity> findAllByStatus(ClientStatus clientStatus);

    List<ClientEntity> findAllByManaged(Boolean attached);

    List<ClientEntity> findAllByManagedAndStatus(Boolean managed, ClientStatus clientStatus);
}
