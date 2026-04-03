package it.getinsight.module.role.dto;

import lombok.Builder;

import java.io.Serial;
import java.io.Serializable;

@Builder
public record ApprovalPolicyRoleDTO(
    Long roleId,
    Boolean canApprove,
    Boolean canReject,
    Boolean canRevoke
) implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;
}
