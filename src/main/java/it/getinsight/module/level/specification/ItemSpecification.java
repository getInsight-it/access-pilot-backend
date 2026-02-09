package it.getinsight.module.level.specification;

import it.getinsight.module.level.entity.ItemEntity;
import it.getinsight.utilitario.SpecificationUtils;
import org.springframework.data.jpa.domain.Specification;

public class ItemSpecification {
    public static Specification<ItemEntity> nameContains(String value) {
        if (value == null) return null;
        return (root, query, cb) -> cb.like(
            SpecificationUtils.unaccentLower(cb, root.get("name")),
            SpecificationUtils.containsPatternUnaccentLower(cb, value)
        );
    }
    public static Specification<ItemEntity> descriptionContains(String value) {
        if (value == null) return null;
        return (root, query, cb) -> cb.like(
            SpecificationUtils.unaccentLower(cb, root.get("description")),
            SpecificationUtils.containsPatternUnaccentLower(cb, value)
        );
    }
    public static Specification<ItemEntity> externalCodeContains(String value) {
        if (value == null) return null;
        return (root, query, cb) -> cb.like(cb.lower(root.get("externalCode")), "%" + value.toLowerCase() + "%");
    }
    public static Specification<ItemEntity> levelEquals(Long levelId) {
        if (levelId == null) return null;
        return (root, query, cb) -> cb.equal(root.get("level").get("id"), levelId);
    }
    public static Specification<ItemEntity> parentEquals(String parentId) {
        if (parentId == null) return null;
        return (root, query, cb) -> cb.equal(root.get("parent").get("id"), parentId);
    }
}
