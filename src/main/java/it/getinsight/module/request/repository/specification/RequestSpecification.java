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

import java.util.Collection;
import java.util.List;
import java.util.Objects;

@Slf4j
public class RequestSpecification {

    private RequestSpecification() {
    }

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

    private static Predicate buildRoleFilters(RequestEntitySpecificationFilter filter, Root<RequestEntity> root, CriteriaBuilder cb) {
        if (CollectionUtils.isEmpty(filter.getRoles())) {
            return null;
        }

        Predicate rolePredicate = cb.disjunction();
        List<RoleEntity> roles = filter.getRoles();


        rolePredicate = addRoleFiltersByNames(rolePredicate, root, cb,
            roles.stream().map(RoleEntity::getName).toList());


        rolePredicate = addRoleFiltersByIds(rolePredicate, root, cb,
            roles.stream().map(RoleEntity::getId).toList());


        RoleEntity firstRole = roles.get(0);
        if (firstRole.getClient() != null) {
            String clientId = firstRole.getClient().getClientId();
            String clientName = firstRole.getClient().getName();

            if (StringUtils.isNotBlank(clientId)) {
                rolePredicate = addClientFiltersByClientIdWithLike(rolePredicate, root, cb, clientId);
            }
            if (StringUtils.isNotBlank(clientName)) {
                rolePredicate = addClientFiltersByClientNameWithLike(rolePredicate, root, cb, clientName);
            }
        }

        return rolePredicate;
    }

    public static Specification<RequestEntity> matchCustom(RequestEntitySpecificationFilter filter) {
        return (Root<RequestEntity> root, CriteriaQuery<?> query, CriteriaBuilder cb) -> {
            if (filter == null) {
                return cb.conjunction();
            }

            Predicate combinedPredicate = cb.conjunction();
            Predicate rolePredicate = buildRoleFilters(filter, root, cb);

            if (rolePredicate != null) {
                combinedPredicate = cb.and(combinedPredicate, rolePredicate);
            }


            Predicate statusPredicate = addStatusFilter(cb.disjunction(), root, cb, filter.getStatus());
            Predicate descriptionPredicate = addDescriptionFilter(cb.disjunction(), root, cb, filter.getDescription());
            Predicate protocolPredicate = addProtocolCodeFilter(cb.disjunction(), root, cb, filter.getProtocolCode());
            Predicate userPredicate = addRequestingUserFilters(root, cb, filter.getRequestingUser());


            Predicate orPredicate = cb.or(
                statusPredicate,
                descriptionPredicate,
                protocolPredicate
            );

            if (userPredicate != null) {
                combinedPredicate = cb.and(combinedPredicate, userPredicate);
            }

            return orPredicate.getExpressions().isEmpty()
                ? combinedPredicate
                : cb.and(combinedPredicate, orPredicate);
        };
    }

    private static void processTriple(String triple, boolean isAdditionalTriples, Root<RequestEntity> root, CriteriaBuilder cb, Predicate or) {
        final int LENGTH_LEVEL_WITHOUT_ITEM = 2;
        final int LENGTH_LEVEL_WITH_ITEM = 3;
        final int expectedParts = isAdditionalTriples ? LENGTH_LEVEL_WITHOUT_ITEM : LENGTH_LEVEL_WITH_ITEM;

        String[] parts = triple.split(":");
        if (parts.length != expectedParts) {
            return;
        }

        try {
            Long clientId = Long.valueOf(parts[0]);
            Long levelId = Long.valueOf(parts[1]);
            Predicate and = cb.and(
                cb.equal(root.get("role").get("client").get("id"), clientId),
                cb.equal(root.get("level").get("id"), levelId)
            );

            if (!isAdditionalTriples) {
                long itemId = Long.parseLong(parts[2]);
                and = cb.and(and, cb.equal(root.get("codeItem"), itemId));
            }

            or.getExpressions().add(and);
        } catch (NumberFormatException numberFormatExceptionIgnored) {
            log.warn("Invalid triple: {}", numberFormatExceptionIgnored.getMessage());
        }
    }

    public static Specification<RequestEntity> inTriples(Collection<String> triples, boolean isAdditionalTriples) {
        return (root, query, cb) -> {
            if (triples == null || triples.isEmpty()) {
                return cb.conjunction();
            }

            Predicate or = cb.disjunction();
            triples.stream()
                .filter(StringUtils::isNotBlank)
                .forEach(triple -> processTriple(triple, isAdditionalTriples, root, cb, or));

            return or.getExpressions().isEmpty() ? cb.conjunction() : or;
        };
    }

    public static Specification<RequestEntity> requesterRoleIn(Collection<Long> roleIds) {
        return (root, query, cb) -> {
            if (roleIds == null || roleIds.isEmpty()) {
                return cb.conjunction();
            }
            return root.get("role").get("id").in(roleIds);
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
        if (role != null) {
            p = cb.or(p, cb.equal(root.get("role").get("id"), role));
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
        if (StringUtils.isNotBlank(clientId)) {
            Expression<String> expression = cb.lower(root.get("role").get("client").get("clientId"));
            p = cb.or(p, cb.like(expression, "%" + clientId.toLowerCase() + "%"));
        }
        return p;
    }

    private static Predicate addClientFiltersByClientNameWithLike(Predicate p, Root<RequestEntity> root, CriteriaBuilder cb, String clientName) {
        if (StringUtils.isNotBlank(clientName)) {
            Expression<String> campo = cb.lower(root.get("role").get("client").get("name"));
            p = cb.or(p, cb.like(campo, "%" + clientName.toLowerCase() + "%"));
        }
        return p;
    }

    private static Predicate addRoleFiltersByNameWithLike(Predicate p, Root<RequestEntity> root, CriteriaBuilder cb, String role) {
        if (StringUtils.isNotBlank(role)) {
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
