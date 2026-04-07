package it.getinsight.module.client.dto;

import lombok.Builder;
import it.getinsight.module.shared.enums.ColorPalette;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

@Builder
public record ClientExportRoleDTO(
    String name,
    String label,
    String description,
    String icon,
    ColorPalette color,
    String parentName,
    String levelName,
    String levelType,
    List<ClientExportApprovalPolicyDTO> approvalPolicies
) implements Serializable {
    @Serial
    private static final long serialVersionUID = 1797640429329462746L;
}
