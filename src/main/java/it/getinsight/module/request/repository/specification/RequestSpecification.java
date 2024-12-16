package it.getinsight.module.request.repository.specification;

import org.springframework.data.jpa.domain.Specification;
import it.getinsight.module.request.entity.RequestEntity;
import it.getinsight.module.role.entity.RoleEntity;

import jakarta.persistence.criteria.Subquery;
import org.springframework.util.Assert;

import java.util.List;

public class RequestSpecification {

    public static Specification<RequestEntity> byRolesParent(List<Long> rolesParentIds) {
        return (root, query, builder) -> {
            if (rolesParentIds == null || rolesParentIds.isEmpty()) {
                return builder.conjunction();
            }

            Assert.notNull(query, "The query must not be null");
            Subquery<Long> subquery = query.subquery(Long.class);
            var roleRoot = subquery.from(RoleEntity.class);

            subquery.select(roleRoot.get("id"))
                .where(roleRoot.get("role").get("id").in(rolesParentIds));

            return root.get("role").get("id").in(subquery);
        };
    }
}
