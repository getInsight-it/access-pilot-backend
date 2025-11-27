package it.getinsight.module.client.repository.specification;

import org.springframework.data.jpa.domain.Specification;
import it.getinsight.module.client.entity.ClientEntity;

public class ClientSpecification  {

    private ClientSpecification() {}

    public static final String UNACCENT_FUNCTION = "unaccent";

    public static Specification<ClientEntity> nameContains(String value) {
        if (value == null || value.isBlank()) return null;

        return (root, query, cb) -> cb.like(
            cb.function(UNACCENT_FUNCTION, String.class, cb.lower(root.get("name"))),
            "%" + value.toLowerCase() + "%"
        );
    }

    public static Specification<ClientEntity> clientIdContains(String value) {
        if (value == null || value.isBlank()) return null;

        return (root, query, cb) -> cb.like(
            cb.function(UNACCENT_FUNCTION, String.class, cb.lower(root.get("clientId"))),
            "%" + value.toLowerCase() + "%"
        );
    }

    public static Specification<ClientEntity> descriptionContains(String value) {
        if (value == null || value.isBlank()) return null;

        return (root, query, cb) -> cb.like(
            cb.function(UNACCENT_FUNCTION, String.class, cb.lower(root.get("description"))),
            "%" + value.toLowerCase() + "%"
        );
    }
}
