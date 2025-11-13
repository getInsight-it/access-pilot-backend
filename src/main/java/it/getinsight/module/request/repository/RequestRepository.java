package it.getinsight.module.request.repository;

import it.getinsight.core.dynamicquery.repository.DynamicNativeQueryRepository;
import it.getinsight.core.dynamicquery.repository.DynamicQueryRepository;
import it.getinsight.module.request.entity.RequestEntity;
import it.getinsight.module.request.enuns.RequestStatus;
import it.getinsight.module.role.entity.RoleEntity;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


@Repository
public interface RequestRepository extends JpaRepository<RequestEntity, Long>, DynamicQueryRepository, DynamicNativeQueryRepository, JpaSpecificationExecutor<RequestEntity>, RequestRepositoryCustom {

    long count(Specification<RequestEntity> specification);

    long countByStatusAndRole(RequestStatus status, RoleEntity role);


    @Query("""
        SELECT DISTINCT r FROM RequestEntity r
        LEFT JOIN FETCH r.role role
        LEFT JOIN FETCH role.client client
        LEFT JOIN FETCH role.level
        LEFT JOIN FETCH r.requestingUser
        LEFT JOIN FETCH r.approvingUser
        LEFT JOIN FETCH r.level
        WHERE r.id IN :ids
    """)
    List<RequestEntity> findAllByIdWithRelationships(@Param("ids") List<Long> ids);


    @Query("""
        SELECT r FROM RequestEntity r
        LEFT JOIN FETCH r.role role
        LEFT JOIN FETCH role.client client
        LEFT JOIN FETCH role.level
        LEFT JOIN FETCH r.requestingUser
        LEFT JOIN FETCH r.approvingUser
        LEFT JOIN FETCH r.level
        WHERE r.id = :id
    """)
    Optional<RequestEntity> findByIdWithRelationships(@Param("id") Long id);

    boolean existsByLevelIdAndStatusIn(Long id, List<RequestStatus> created);
}
