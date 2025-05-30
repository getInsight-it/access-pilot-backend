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
import java.util.Objects;

@Slf4j
public class RequestSpecification {

    private RequestSpecification() {}

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
            Predicate filtroAnd = cb.conjunction();
            Predicate filtrosOr = cb.disjunction();

            if (filter == null) return filtroAnd;

            if (CollectionUtils.isNotEmpty(filter.getRoles())) {
                filtrosOr = addRoleFiltersByNames(filtrosOr, root, cb, filter.getRoles().stream().map(RoleEntity::getName).toList());
                filtrosOr = addRoleFiltersByIds(filtrosOr, root, cb, filter.getRoles().stream().map(RoleEntity::getId).toList());

                var client = filter.getRoles().getFirst().getClient();
                if (client != null && StringUtils.isNotBlank(client.getClientId())) {
                    filtrosOr = addClientFiltersByClientIdWithLike(filtrosOr, root, cb, client.getClientId());
                }
                if (client != null && StringUtils.isNotBlank(client.getName())) {
                    filtrosOr = addClientFiltersByClientNameWithLike(filtrosOr, root, cb, client.getName());
                }
            }

            filtrosOr = addStatusFilter(filtrosOr, root, cb, filter.getStatus());
            filtrosOr = addDescriptionFilter(filtrosOr, root, cb, filter.getDescription());
            filtrosOr = addProtocolCodeFilter(filtrosOr, root, cb, filter.getProtocolCode());

            Predicate pRequestingUser = addRequestingUserFilters(root, cb, filter.getRequestingUser());
            if (pRequestingUser != null) {
                filtroAnd = cb.and(filtroAnd, pRequestingUser);
            }

            return filtrosOr.getExpressions().isEmpty() ? filtroAnd : cb.and(filtroAnd, filtrosOr);
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
            roles.stream().filter(Objects::nonNull).forEach(o -> log.info("Role: {}", o));
        }
        return p;
    }

    private static Predicate addRoleFiltersByIdWithEqual(Predicate p, Root<RequestEntity> root, CriteriaBuilder cb, Long role) {
        if (role != null){
            p = cb.or(p, cb.equal(root.get("role").get("id"),  role ));
        }
        return p;
    }

    private static Predicate addRoleFiltersByIdWithIn(Predicate p, Root<RequestEntity> root, CriteriaBuilder cb, List<Long> rolesId) {
        if (!rolesId.isEmpty()) {
            p = cb.or(p, root.get("role").get("id").in(rolesId));
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



    private static Predicate addClientFiltersByClientIdWithLike(Predicate p, Root<RequestEntity> root, CriteriaBuilder cb, String clientId) {
        if (StringUtils.isNotBlank(clientId)){
            Expression<String> expression = cb.lower(root.get("role").get("client").get("clientId"));
            p = cb.or(p, cb.like(expression, "%" + clientId.toLowerCase() + "%"));
        }
        return p;
    }

    private static Predicate addClientFiltersByClientNameWithLike(Predicate p, Root<RequestEntity> root, CriteriaBuilder cb, String clientName) {
        if (StringUtils.isNotBlank(clientName)){
            Expression<String> campo = cb.lower(root.get("role").get("client").get("name"));
            p = cb.or(p, cb.like(campo, "%" + clientName.toLowerCase() + "%"));
        }
        return p;
    }

    private static Predicate addRoleFiltersByNameWithLike(Predicate p, Root<RequestEntity> root, CriteriaBuilder cb, String role) {
        if (StringUtils.isNotBlank(role)){
            Expression<String> campo = cb.lower(root.get("role").get("name"));
            p = cb.or(p, cb.like(campo, "%" + role.toLowerCase() + "%"));
        }
        return p;
    }

    private static Predicate addRoleFiltersByNameWithIn(Predicate p, Root<RequestEntity> root, CriteriaBuilder cb, List<String> rolesNames) {
        if (!rolesNames.isEmpty()) {
            Expression<String> campo = cb.lower(root.get("role").get("name"));
            p = cb.or(p, campo.in(rolesNames.stream().map(String::toLowerCase).toList()));
        }
        return p;
    }


    private static Predicate addRequestingUserFilters(Root<RequestEntity> root, CriteriaBuilder cb, UserEntity requestingUser) {
        Predicate requestingPredicate = null;
        if (requestingUser != null) {
        requestingPredicate = cb.conjunction();
            if (requestingUser.getId() != null) {
                requestingPredicate = cb.and(requestingPredicate, cb.like(root.get("requestingUser").get("id"), "%" + requestingUser.getId() + "%"));
            }
            if (requestingUser.getExternalId() != null) {
                requestingPredicate = cb.and(requestingPredicate, cb.like(root.get("requestingUser").get("externalId"), "%" + requestingUser.getExternalId() + "%"));
            }
        }
        return requestingPredicate;
    }

    private static Predicate addStatusFilter(Predicate p, Root<RequestEntity> root, CriteriaBuilder cb, RequestStatus status) {
        if (status != null) {
            p = cb.or(p, cb.equal(root.get("status"), status));
        }
        return p;
    }

    private static Predicate addDescriptionFilter(Predicate p, Root<RequestEntity> root, CriteriaBuilder cb, String description) {
        if (description != null) {
            Expression<String> field = cb.lower(root.get("description"));
            p = cb.or(p, cb.like(field, "%" + description.toLowerCase() + "%"));
        }
        return p;
    }

    private static Predicate addProtocolCodeFilter(Predicate p, Root<RequestEntity> root, CriteriaBuilder cb, String protocolCode) {
        if (protocolCode != null) {
            Expression<String> field = cb.lower(root.get("protocolCode"));
            p = cb.or(p, cb.like(field, "%" + protocolCode.toLowerCase() + "%"));
        }
        return p;
    }

}
