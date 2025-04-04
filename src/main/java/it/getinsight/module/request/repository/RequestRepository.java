package it.getinsight.module.request.repository;

import it.getinsight.core.dynamicquery.repository.DynamicNativeQueryRepository;
import it.getinsight.core.dynamicquery.repository.DynamicQueryRepository;
import it.getinsight.module.request.entity.RequestEntity;
import it.getinsight.module.request.enuns.RequestStatus;
import it.getinsight.module.role.entity.RoleEntity;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;


@Repository
public interface RequestRepository extends JpaRepository<RequestEntity, Long>, DynamicQueryRepository, DynamicNativeQueryRepository, JpaSpecificationExecutor<RequestEntity>, RequestRepositoryCustom {

    long count(Specification<RequestEntity> specification);


    long countByStatusAndRole(RequestStatus status, RoleEntity role);


}
