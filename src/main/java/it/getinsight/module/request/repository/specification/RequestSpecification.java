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

import java.util.*;
import java.util.stream.Collectors;

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

        rolePredicate = addRoleFiltersByLabels(rolePredicate, root, cb,
            roles.stream().map(RoleEntity::getName).toList());

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

        return rolePredicate.getExpressions().isEmpty() ? null : rolePredicate;
    }

    public static Specification<RequestEntity> matchCustom(RequestEntitySpecificationFilter filter) {
        return (Root<RequestEntity> root, CriteriaQuery<?> query, CriteriaBuilder cb) -> {
            if (filter == null) {
                return cb.conjunction();
            }

            Predicate userPredicate = addRequestingUserFilters(root, cb, filter.getRequestingUser());

            List<Predicate> orParts = new ArrayList<>();

            Predicate rolePredicate = buildRoleFilters(filter, root, cb);
            if (rolePredicate != null && !rolePredicate.getExpressions().isEmpty()) {
                orParts.add(rolePredicate);
            }

            Predicate statusPredicate = addStatusFilter(cb.disjunction(), root, cb, filter.getStatus());
            if (statusPredicate != null && statusPredicate.getExpressions() != null && !statusPredicate.getExpressions().isEmpty()) {
                orParts.add(statusPredicate);
            }

            Predicate descriptionPredicate = addDescriptionFilter(cb.disjunction(), root, cb, filter.getDescription());
            if (descriptionPredicate != null && descriptionPredicate.getExpressions() != null && !descriptionPredicate.getExpressions().isEmpty()) {
                orParts.add(descriptionPredicate);
            }

            Predicate protocolPredicate = addProtocolCodeFilter(cb.disjunction(), root, cb, filter.getProtocolCode());
            if (protocolPredicate != null && protocolPredicate.getExpressions() != null && !protocolPredicate.getExpressions().isEmpty()) {
                orParts.add(protocolPredicate);
            }

            Predicate orBlock = orParts.isEmpty() ? null : cb.or(orParts.toArray(new Predicate[0]));


            if (userPredicate != null && orBlock != null) {
                return cb.and(userPredicate, orBlock);
            } else if (userPredicate != null) {
                return userPredicate;
            } else if (orBlock != null) {
                return orBlock;
            } else {
                return cb.conjunction();
            }
        };
    }




    public static Specification<RequestEntity> inTriples(Collection<String> triples) {
        return (root, query, cb) -> {
            if (triples == null || triples.isEmpty()) {
                return cb.conjunction();
            }


            Map<String, List<String>> groupedItems = triples.stream()
                .filter(StringUtils::isNotBlank)
                .map(triple -> {
                    String[] parts = triple.split(":");
                    if (parts.length != 4) return null;
                    return new Object[] {
                        parts[0],
                        parts[1],
                        parts[2],
                        parts[3]
                    };
                })
                .filter(Objects::nonNull)
                .collect(Collectors.groupingBy(
                    arr -> arr[0] + ":" + arr[1] + ":" + arr[2],
                    Collectors.mapping(arr -> (String) arr[3], Collectors.toList())
                ));

            if (groupedItems.isEmpty()) {
                return cb.conjunction();
            }


            List<Predicate> groupPredicates = new ArrayList<>();
            for (Map.Entry<String, List<String>> entry : groupedItems.entrySet()) {
                String[] keys = entry.getKey().split(":");
                String clientId = keys[0];
                String roleId = keys[1];
                String levelId = keys[2];
                List<String> codeItems = entry.getValue();

                Predicate clientPredicate = cb.equal(root.get("role").get("client").get("id"), clientId);
                Predicate rolePredicate = cb.equal(root.get("role").get("id"), roleId);
                Predicate levelPredicate = cb.equal(root.get("level").get("id"), levelId);
                Expression<String> codeItemExpr = root.get("codeItem");
                Predicate inClause = codeItemExpr.in(codeItems);

                groupPredicates.add(cb.and(
                    clientPredicate,
                    rolePredicate,
                    levelPredicate,
                    inClause
                ));
            }

            return cb.or(groupPredicates.toArray(new Predicate[0]));
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

    private static Predicate addRoleFiltersByLabels(Predicate p, Root<RequestEntity> root, CriteriaBuilder cb, List<String> roles) {
        if (roles != null && !roles.isEmpty()) {
            if (roles.size() == 1) {
                String role = roles.getFirst();
                p = addRoleFiltersByLabelWithLike(p, root, cb, role);
            } else {
                p = addRoleFiltersByLabelWithIn(p, root, cb, roles);
            }
        }
        return p;
    }

    private static String normalizeSearchTerm(String term) {
        if (term == null) {
            return "";
        }
        return java.text.Normalizer.normalize(term, java.text.Normalizer.Form.NFD)
            .replaceAll("\\p{InCombiningDiacriticalMarks}+", "");
    }

    private static Predicate addClientFiltersByClientIdWithLike(Predicate p, Root<RequestEntity> root, CriteriaBuilder cb, String clientId) {
        if (StringUtils.isNotBlank(clientId)) {
            Expression<String> field = cb.function("unaccent", String.class,
                cb.lower(root.get("role").get("client").get("clientId")));
            p = cb.or(p, cb.like(field, "%" + normalizeSearchTerm(clientId).toLowerCase() + "%"));
        }
        return p;
    }

    private static Predicate addClientFiltersByClientNameWithLike(Predicate p, Root<RequestEntity> root, CriteriaBuilder cb, String clientName) {
        if (StringUtils.isNotBlank(clientName)) {
            Expression<String> field = cb.function("unaccent", String.class,
                cb.lower(root.get("role").get("client").get("name")));
            p = cb.or(p, cb.like(field, "%" + normalizeSearchTerm(clientName).toLowerCase() + "%"));
        }
        return p;
    }

    private static Predicate addRoleFiltersByNameWithLike(Predicate p, Root<RequestEntity> root, CriteriaBuilder cb, String role) {
        if (StringUtils.isNotBlank(role)) {
            Expression<String> field = cb.function("unaccent", String.class,
                cb.lower(root.get("role").get("name")));
            p = cb.or(p, cb.like(field, "%" + normalizeSearchTerm(role).toLowerCase() + "%"));
        }
        return p;
    }

    private static Predicate addRoleFiltersByLabelWithLike(Predicate p, Root<RequestEntity> root, CriteriaBuilder cb, String role) {
        if (StringUtils.isNotBlank(role)) {
            Expression<String> field = cb.function("unaccent", String.class,
                cb.lower(root.get("role").get("label")));
            p = cb.or(p, cb.like(field, "%" + normalizeSearchTerm(role).toLowerCase() + "%"));
        }
        return p;
    }


    private static Predicate addRoleFiltersByLabelWithIn(Predicate p, Root<RequestEntity> root, CriteriaBuilder cb, List<String> rolesLabels) {
        if (!rolesLabels.isEmpty()) {
            Expression<String> campo = cb.lower(root.get("role").get("label"));
            p = cb.or(p, campo.in(rolesLabels.stream().map(String::toLowerCase).toList()));
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
        if (requestingUser == null) {
            return null;
        }

        List<Predicate> parts = new ArrayList<>();

        if (requestingUser.getId() != null) {
            parts.add(cb.equal(root.get("requestingUser").get("id"), requestingUser.getId()));
        }

        if (StringUtils.isNotBlank(requestingUser.getExternalId())) {
            Expression<String> field = cb.function("unaccent", String.class,
                cb.lower(root.get("requestingUser").get("externalId")));
            parts.add(cb.like(field, "%" + normalizeSearchTerm(requestingUser.getExternalId()).toLowerCase() + "%"));
        }

        if (parts.isEmpty()) {
            return null;
        }
        if (parts.size() == 1) {
            return parts.getFirst();
        }

        return cb.and(parts.toArray(new Predicate[0]));
    }


    private static Predicate addStatusFilter(Predicate p, Root<RequestEntity> root, CriteriaBuilder cb, RequestStatus status) {
        if (status != null) {
            p = cb.or(p, cb.equal(root.get("status"), status));
        }
        return p;
    }

    private static Predicate addDescriptionFilter(Predicate p, Root<RequestEntity> root, CriteriaBuilder cb, String description) {
        if (description != null) {
            Expression<String> field = cb.function("unaccent", String.class,
                cb.lower(root.get("description")));
            p = cb.or(p, cb.like(field, "%" + normalizeSearchTerm(description).toLowerCase() + "%"));
        }
        return p;
    }

    private static Predicate addProtocolCodeFilter(Predicate p, Root<RequestEntity> root, CriteriaBuilder cb, String protocolCode) {
        if (protocolCode != null) {
            Expression<String> field = cb.function("unaccent", String.class,
                cb.lower(root.get("protocolCode")));
            p = cb.or(p, cb.like(field, "%" + normalizeSearchTerm(protocolCode).toLowerCase() + "%"));
        }
        return p;
    }

    public static Specification<RequestEntity> rolesWithLevelIsNull(List<String> roleIds) {
        return (root, query, cb) -> {
            if (roleIds == null || roleIds.isEmpty()) {
                return cb.conjunction();
            }
            return cb.and(
                root.get("role").get("level").isNull(),
                root.get("role").get("id").in(roleIds)
            );
        };
    }
}
