package it.getinsight.module.solicitacao.repository;

import it.getinsight.core.dynamicquery.repository.DynamicNativeQueryRepository;
import it.getinsight.core.dynamicquery.repository.DynamicQueryRepository;
import it.getinsight.module.solicitacao.entity.SolicitacaoEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface SolicitacaoRepository extends JpaRepository<SolicitacaoEntity, Long>, DynamicQueryRepository, DynamicNativeQueryRepository {

}
