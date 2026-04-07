package it.getinsight.module.role.dto;

import it.getinsight.module.client.dto.ClientDTO;
import it.getinsight.module.level.dto.LevelResponseDTO;
import it.getinsight.module.shared.enums.ColorPalette;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Builder;

import java.io.Serializable;
import java.util.List;

@Builder
public record RoleResponseDTO(

    Long id,

    String roleExternalId,

    @NotEmpty
    @Size(min = 3, max = 100)
    String name,

    String label,

    String icon,

    ColorPalette color,

    @NotEmpty
    @Size(min = 3, max = 100)
    String description,

    RoleResponseDTO roleParent,

    ClientDTO client,

    LevelResponseDTO level,

    List<ApprovalPolicyResponseDTO> approvalPolicies

) implements Serializable {}
