package it.getinsight.module.request.repository;

import it.getinsight.core.dynamicquery.repository.DynamicNativeQueryRepository;
import it.getinsight.core.dynamicquery.repository.DynamicQueryRepository;
import it.getinsight.module.request.entity.RequestAttachmentEntity;
import it.getinsight.module.request.entity.RequestEntity;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface RequestAttachmentFileRepository extends JpaRepository<RequestAttachmentEntity, Long>, DynamicQueryRepository, DynamicNativeQueryRepository, JpaSpecificationExecutor<RequestAttachmentEntity> {

    long count(Specification<RequestAttachmentEntity> specification);


    List<RequestAttachmentEntity> findAllByRequest(RequestEntity request);
}
