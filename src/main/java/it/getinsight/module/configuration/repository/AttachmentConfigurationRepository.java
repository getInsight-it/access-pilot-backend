package it.getinsight.module.configuration.repository;

import it.getinsight.core.dynamicquery.repository.DynamicNativeQueryRepository;
import it.getinsight.core.dynamicquery.repository.DynamicQueryRepository;
import it.getinsight.module.client.entity.ClientEntity;
import it.getinsight.module.configuration.entity.AttachmentConfigurationEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AttachmentConfigurationRepository extends JpaRepository<AttachmentConfigurationEntity, Long>, DynamicQueryRepository, DynamicNativeQueryRepository {

    List<AttachmentConfigurationEntity> findAllByClient(ClientEntity client);

    @Query(value = """
        SELECT *
        FROM TB_CONFIGURACAO_ANEXO c
        WHERE c.CLIENTE_ID = :clientId
          AND LOWER(c.NOME) = LOWER(:name)
          AND c.ATIVO = true
        """, nativeQuery = true)
    List<AttachmentConfigurationEntity> findByClientIdAndNameIgnoreCase(@Param("clientId") Long clientId, @Param("name") String name);

    @Modifying
    @Query("UPDATE AttachmentConfigurationEntity l SET l.active = false WHERE l.id = :id")
    void softDelete(@Param("id") Long id);
}
