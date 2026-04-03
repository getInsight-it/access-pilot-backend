package it.getinsight.module.role.enuns;

import it.getinsight.core.message.CoreMessageSource;
import lombok.Getter;

@Getter
public enum ApprovalPolicyType {

    AUTO_APPROVAL("role.approval.policy.type.auto"),
    LATERAL_APPROVAL("role.approval.policy.type.lateral");

    private final String key;

    ApprovalPolicyType(String key) {
        this.key = key;
    }

    public String getDescription() {
        return CoreMessageSource.get().message(this.key);
    }
}
