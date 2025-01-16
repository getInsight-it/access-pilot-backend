package it.getinsight.module.request.repository;

import it.getinsight.core.dynamicquery.repository.DynamicNativeQueryRepository;
import it.getinsight.core.dynamicquery.repository.DynamicQueryRepository;
import it.getinsight.module.request.entity.RequestEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;


@Repository
public interface RequestRepository extends JpaRepository<RequestEntity, Long>, DynamicQueryRepository, DynamicNativeQueryRepository, JpaSpecificationExecutor<RequestEntity> {

}
