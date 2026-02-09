package it.getinsight.module.client.repository.specification;

import it.getinsight.module.client.entity.ClientStatus;
import it.getinsight.utilitario.SpecificationUtils;
import org.springframework.data.jpa.domain.Specification;
import it.getinsight.module.client.entity.ClientEntity;

public class ClientSpecification  {

    private ClientSpecification() {}

    public static Specification<ClientEntity> nameContains(String value) {
        if (value == null || value.isBlank()) return null;

        return (root, query, cb) -> cb.like(
            SpecificationUtils.unaccentLower(cb, root.get("name")),
            SpecificationUtils.containsPatternUnaccentLower(cb, value)
        );
    }

    public static Specification<ClientEntity> clientIdContains(String value) {
        if (value == null || value.isBlank()) return null;

        return (root, query, cb) -> cb.like(
            SpecificationUtils.unaccentLower(cb, root.get("clientId")),
            SpecificationUtils.containsPatternUnaccentLower(cb, value)
        );
    }

    public static Specification<ClientEntity> descriptionContains(String value) {
        if (value == null || value.isBlank()) return null;

        return (root, query, cb) -> cb.like(
            SpecificationUtils.unaccentLower(cb, root.get("description")),
            SpecificationUtils.containsPatternUnaccentLower(cb, value)
        );
    }

    public static Specification<ClientEntity> statusContains(ClientStatus status) {
        if (status == null) return null;
        return (root, query,    cb) -> cb.equal(root.get("status"), status);
    }



}
