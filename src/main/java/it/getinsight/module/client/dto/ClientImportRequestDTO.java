package it.getinsight.module.client.dto;

import lombok.Builder;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

@Builder
public record ClientImportRequestDTO(
    Boolean force,
    Boolean importRoles,
    Boolean importConfigurations,
    List<ClientExportDTO> exports
) implements Serializable {
    @Serial
    private static final long serialVersionUID = 2869473661665022465L;
}
