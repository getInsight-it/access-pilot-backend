package it.getinsight.module.role.repository.specification;

import it.getinsight.module.role.entity.RoleEntity;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class RoleSpecification {

    public static Specification<RoleEntity> byResourceAccess(Map<String, List<String>> resourceAccess) {
        return (root, query, builder) -> {

            List<Predicate> predicates = resourceAccess.entrySet()
                .stream()
                .map(entry -> {
                    String clientId = entry.getKey();

                    List<String> roleNames = extractRoles(entry.getValue());
                    if (roleNames == null || roleNames.isEmpty()) {
                        return null;
                    }
                    return builder.and(
                        builder.equal(root.get("client").get("clientId"), clientId),
                        root.get("name").in(roleNames)
                    );
                })
                .filter(Objects::nonNull)
                .toList();

            return builder.or(predicates.toArray(new Predicate[0]));
        };
    }

    private static List<String> extractRoles(Object value) {
        if (value instanceof Map) {
            Object roles = ((Map<?, ?>) value).get("roles");
            if (roles instanceof List) {
                return ((List<?>) roles).stream()
                    .filter(String.class::isInstance)
                    .map(String.class::cast)
                    .toList();
            }
        }
        return Collections.emptyList();
    }
}
