package it.getinsight.module.client.dto;

import lombok.Builder;

import java.io.Serial;
import java.io.Serializable;

@Builder
public record ClientImportResultDTO(
    String clientId,
    String status,
    long rolesCreated,
    long rolesUpdated,
    long rolesDeleted,
    long configurationsCreated,
    long configurationsUpdated,
    long configurationsDeleted,
    String message
) implements Serializable {
    @Serial
    private static final long serialVersionUID = 1433919729037932790L;
}
