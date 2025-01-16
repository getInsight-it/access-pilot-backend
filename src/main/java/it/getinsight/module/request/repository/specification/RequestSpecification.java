package it.getinsight.module.request.repository.specification;

import it.getinsight.module.request.entity.RequestEntity;
import it.getinsight.module.role.entity.RoleEntity;
import jakarta.persistence.criteria.*;
import org.springframework.data.jpa.domain.Specification;
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

    public static Specification<RequestEntity> matchCustom(RequestEntity example) {
        return (Root<RequestEntity> root, CriteriaQuery<?> query, CriteriaBuilder cb) -> {
            Predicate p = cb.conjunction();

            if (example.getRole() != null && example.getRole().getName() != null) {
                p = cb.and(p, cb.like(root.get("role").get("name"), "%" + example.getRole().getName() + "%"));
            }
            if (example.getRole() != null && example.getRole().getClient() != null) {
                if (example.getRole().getClient().getName() != null) {
                    p = cb.and(p, cb.like(root.get("role").get("client").get("name"), "%" + example.getRole().getClient().getName() + "%"));
                }
                if (example.getRole().getClient().getClientId() != null) {
                    p = cb.and(p, cb.like(root.get("role").get("client").get("clientId"), "%" + example.getRole().getClient().getClientId() + "%"));
                }
            }
            if (example.getRequestingUser() != null) {
                if (example.getRequestingUser().getId() != null) {
                    p = cb.and(p, cb.like(root.get("requestingUser").get("id"), "%" + example.getRequestingUser().getId() + "%"));
                }
                if (example.getRequestingUser().getExternalId() != null) {
                    p = cb.and(p, cb.like(root.get("requestingUser").get("externalId"), "%" + example.getRequestingUser().getExternalId() + "%"));
                }
            }
            if (example.getStatus() != null) {
                p = cb.and(p, cb.equal(root.get("status"), example.getStatus()));
            }
//            if (example.getRole() != null && example.getRole().getClient() != null) {
//                p = cb.and(p, cb.equal(root.get("managed"), example.getRole().getClient().getManaged()));
//            }
            if (example.getDescription() != null) {
                p = cb.and(p, cb.like(root.get("description"), "%" + example.getDescription() + "%"));
            }

            return p;
        };
    }
}
