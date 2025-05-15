package it.getinsight.module.request.repository;

import it.getinsight.core.dynamicquery.repository.DynamicNativeQueryRepository;
import it.getinsight.core.dynamicquery.repository.DynamicQueryRepository;
import it.getinsight.module.request.entity.RequestAttachmentEntity;
import it.getinsight.module.request.entity.RequestEntity;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface RequestAttachmentFileRepository extends JpaRepository<RequestAttachmentEntity, Long>, DynamicQueryRepository, DynamicNativeQueryRepository, JpaSpecificationExecutor<RequestAttachmentEntity> {

    long count(Specification<RequestAttachmentEntity> specification);

    @Query("SELECT r FROM RequestAttachmentEntity r " +
        "WHERE r.request = :request " +
        "and (r.active = true or r.active = false)" +
        "and (r.configuration.active = true or r.configuration.active = false)")
    List<RequestAttachmentEntity> findAllByRequest(RequestEntity request);

}
