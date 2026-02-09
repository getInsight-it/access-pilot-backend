package it.getinsight.module.level.specification;

import it.getinsight.module.level.entity.LevelEntity;
import it.getinsight.utilitario.SpecificationUtils;
import org.springframework.data.jpa.domain.Specification;

public class LevelSpecification {
    public static Specification<LevelEntity> nameContains(String value) {
        if (value == null) return null;
        return (root, query, cb) -> cb.like(
            SpecificationUtils.unaccentLower(cb, root.get("name")),
            SpecificationUtils.containsPatternUnaccentLower(cb, value)
        );
    }
    public static Specification<LevelEntity> descriptionContains(String value) {
        if (value == null) return null;
        return (root, query, cb) -> cb.like(
            SpecificationUtils.unaccentLower(cb, root.get("description")),
            SpecificationUtils.containsPatternUnaccentLower(cb, value)
        );
    }
    public static Specification<LevelEntity> externalUrlContains(String value) {
        if (value == null) return null;
        return (root, query, cb) -> cb.like(
            SpecificationUtils.unaccentLower(cb, root.get("externalUrl")),
            SpecificationUtils.containsPatternUnaccentLower(cb, value)
        );
    }
}
