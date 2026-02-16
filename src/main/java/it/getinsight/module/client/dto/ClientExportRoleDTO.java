package it.getinsight.module.client.dto;

import lombok.Builder;

import java.io.Serial;
import java.io.Serializable;

@Builder
public record ClientExportRoleDTO(
    String name,
    String label,
    String description,
    String icon,
    String parentName,
    String levelName,
    String levelType
) implements Serializable {
    @Serial
    private static final long serialVersionUID = 1797640429329462746L;
}
