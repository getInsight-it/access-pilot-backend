package it.getinsight.module.client.dto;

import lombok.Builder;

import java.io.Serial;
import java.io.Serializable;

@Builder
public record ClientExportApprovalPolicyRoleDTO(
    String roleName,
    Boolean canApprove,
    Boolean canReject,
    Boolean canRevoke
) implements Serializable {
    @Serial
    private static final long serialVersionUID = -3822646694997403539L;
}
