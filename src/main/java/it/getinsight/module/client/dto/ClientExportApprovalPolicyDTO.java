package it.getinsight.module.client.dto;

import it.getinsight.module.role.enuns.ApprovalPolicyType;
import lombok.Builder;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

@Builder
public record ClientExportApprovalPolicyDTO(
    ApprovalPolicyType type,
    Boolean enabled,
    List<ClientExportApprovalPolicyRoleDTO> roles
) implements Serializable {
    @Serial
    private static final long serialVersionUID = -333453760237285327L;
}
