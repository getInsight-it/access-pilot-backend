package it.getinsight.module.request.repository;

import it.getinsight.module.request.entity.RequestEntity;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Repository;

@Repository
public class RequestRepositoryImpl implements RequestRepositoryCustom {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public long countRequestingUserDistinct(Specification<RequestEntity> specification) {
        var builder = entityManager.getCriteriaBuilder();
        var query = builder.createQuery(Long.class);
        var root = query.from(RequestEntity.class);

        query.select(builder.countDistinct(root.get("requestingUser")));

        if (specification != null) {
            query.where(specification.toPredicate(root, query, builder));
        }

        return entityManager.createQuery(query).getSingleResult();
    }
}
