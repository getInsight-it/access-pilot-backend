package it.getinsight.utilitario;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.Expression;

public final class SpecificationUtils {
    private static final String UNACCENT_FUNCTION = "unaccent";

    private SpecificationUtils() {}

    public static Expression<String> unaccentLower(CriteriaBuilder cb, Expression<String> expression) {
        return cb.function(UNACCENT_FUNCTION, String.class, cb.lower(expression));
    }

    public static Expression<String> unaccentLowerLiteral(CriteriaBuilder cb, String value) {
        return cb.function(UNACCENT_FUNCTION, String.class, cb.lower(cb.literal(value)));
    }

    public static Expression<String> containsPatternUnaccentLower(CriteriaBuilder cb, String value) {
        return cb.concat("%", cb.concat(unaccentLowerLiteral(cb, value), "%"));
    }
}
