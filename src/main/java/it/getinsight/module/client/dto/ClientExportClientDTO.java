package it.getinsight.module.client.dto;

import lombok.Builder;

import java.io.Serial;
import java.io.Serializable;

@Builder
public record ClientExportClientDTO(
    String clientId,
    String name,
    String label,
    String description,
    String baseUrl,
    Boolean managed,
    String status
) implements Serializable {
    @Serial
    private static final long serialVersionUID = 1297314927782641440L;
}
