package it.getinsight.module.invitation.repository.specification;

import it.getinsight.core.util.EmailNormalizationUtil;
import it.getinsight.module.invitation.entity.InvitationEntity;
import it.getinsight.module.invitation.enuns.InvitationStatus;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.jpa.domain.Specification;

import java.time.Instant;

public class InvitationSpecification {

    private InvitationSpecification() {
    }

    public static Specification<InvitationEntity> emailEquals(String email) {
        return (root, query, cb) -> {
            if (StringUtils.isBlank(email)) {
                return null;
            }
            return cb.equal(root.get("email"), EmailNormalizationUtil.normalize(email));
        };
    }

    public static Specification<InvitationEntity> hasStatus(InvitationStatus status, Instant now) {
        return (root, query, cb) -> {
            if (status == null) {
                return null;
            }

            if (InvitationStatus.PENDING.equals(status)) {
                return cb.and(
                    cb.equal(root.get("status"), InvitationStatus.PENDING),
                    cb.or(
                        cb.isNull(root.get("expiresAt")),
                        cb.greaterThan(root.get("expiresAt"), now)
                    )
                );
            }

            if (InvitationStatus.EXPIRED.equals(status)) {
                return cb.or(
                    cb.equal(root.get("status"), InvitationStatus.EXPIRED),
                    cb.and(
                        cb.equal(root.get("status"), InvitationStatus.PENDING),
                        cb.isNotNull(root.get("expiresAt")),
                        cb.lessThanOrEqualTo(root.get("expiresAt"), now)
                    )
                );
            }

            return cb.equal(root.get("status"), status);
        };
    }
}
