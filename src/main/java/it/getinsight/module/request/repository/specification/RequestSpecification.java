package it.getinsight.module.request.repository.specification;

import it.getinsight.module.request.entity.RequestEntity;
import it.getinsight.module.request.enuns.RequestStatus;
import it.getinsight.module.role.entity.RoleEntity;
import it.getinsight.module.user.entity.UserEntity;
import jakarta.persistence.criteria.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.Assert;

import java.util.List;

@Slf4j
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
        return matchCustom(RequestEntitySpecificationFilter.of(example));
    }

    public static Specification<RequestEntity> matchCustom(RequestEntitySpecificationFilter filter) {
        return (Root<RequestEntity> root, CriteriaQuery<?> query, CriteriaBuilder cb) -> {
            Predicate p = cb.conjunction();
            if (filter == null) return p;
            if (CollectionUtils.isNotEmpty(filter.getRoles())){
                p = addRoleFiltersByNames(p, root, cb, filter.getRoles().stream().map(RoleEntity::getName).toList());
                p = addRoleFiltersByIds(p, root, cb, filter.getRoles().stream().map(RoleEntity::getId).toList());
            }
            p = addRequestingUserFilters(p, root, cb, filter.getRequestingUser());
            p = addStatusFilter(p, root, cb, filter.getStatus());
            p = addDescriptionFilter(p, root, cb, filter.getDescription());

            return p;
        };
    }

    private static Predicate addRoleFiltersByIds(Predicate p, Root<RequestEntity> root, CriteriaBuilder cb, List<Long> roles) {
        if (roles != null && !roles.isEmpty()) {
            if (roles.size() == 1) {
                Long role = roles.getFirst();
                p = addRoleFiltersByIdWithEqual(p, root, cb, role);
            } else {
                p = addRoleFiltersByIdWithIn(p, root, cb, roles);
            }
            roles.forEach(o -> log.info("Role: {}", o));
        }
        return p;
    }

    private static Predicate addRoleFiltersByIdWithEqual(Predicate p, Root<RequestEntity> root, CriteriaBuilder cb, Long role) {
        if (role != null){
            p = cb.and(p, cb.equal(root.get("role").get("id"),  role ));
        }
        return p;
    }

    private static Predicate addRoleFiltersByIdWithIn(Predicate p, Root<RequestEntity> root, CriteriaBuilder cb, List<Long> rolesId) {
        if (!rolesId.isEmpty()) {
            p = cb.and(p, root.get("role").get("id").in(rolesId));
        }
        return p;
    }

    private static Predicate addRoleFiltersByNames(Predicate p, Root<RequestEntity> root, CriteriaBuilder cb, List<String> roles) {
        if (roles != null && !roles.isEmpty()) {
            if (roles.size() == 1) {
                String role = roles.getFirst();
                p = addRoleFiltersByNameWithLike(p, root, cb, role);
            } else {
                p = addRoleFiltersByNameWithIn(p, root, cb, roles);
            }
        }
        return p;
    }

    private static Predicate addRoleFiltersByNameWithLike(Predicate p, Root<RequestEntity> root, CriteriaBuilder cb, String role) {
        if (StringUtils.isNotBlank(role)){
            p = cb.and(p, cb.like(root.get("role").get("name"), "%" + role + "%"));
        }
        return p;
    }

    private static Predicate addRoleFiltersByNameWithIn(Predicate p, Root<RequestEntity> root, CriteriaBuilder cb, List<String> rolesNames) {
        if (!rolesNames.isEmpty()) {
            p = cb.and(p, root.get("role").get("name").in(rolesNames));
        }
        return p;
    }


    private static Predicate addRequestingUserFilters(Predicate p, Root<RequestEntity> root, CriteriaBuilder cb, UserEntity requestingUser) {
        if (requestingUser != null) {
            if (requestingUser.getId() != null) {
                p = cb.and(p, cb.like(root.get("requestingUser").get("id"), "%" + requestingUser.getId() + "%"));
            }
            if (requestingUser.getExternalId() != null) {
                p = cb.and(p, cb.like(root.get("requestingUser").get("externalId"), "%" + requestingUser.getExternalId() + "%"));
            }
        }
        return p;
    }

    private static Predicate addStatusFilter(Predicate p, Root<RequestEntity> root, CriteriaBuilder cb, RequestStatus status) {
        if (status != null) {
            p = cb.and(p, cb.equal(root.get("status"), status));
        }
        return p;
    }

    private static Predicate addDescriptionFilter(Predicate p, Root<RequestEntity> root, CriteriaBuilder cb, String description) {
        if (description != null) {
            p = cb.and(p, cb.like(root.get("description"), "%" + description + "%"));
        }
        return p;
    }

}
