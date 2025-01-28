package it.getinsight.module.request.repository;

import it.getinsight.module.request.entity.RequestEntity;
import org.springframework.data.jpa.domain.Specification;

public interface RequestRepositoryCustom {
    long countRequestingUserDistinct(Specification<RequestEntity> specification);

}
