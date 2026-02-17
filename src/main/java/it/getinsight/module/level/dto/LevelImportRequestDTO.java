package it.getinsight.module.level.dto;

import lombok.Builder;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

@Builder
public record LevelImportRequestDTO(
    List<LevelExportDTO> exports
) implements Serializable {
    @Serial
    private static final long serialVersionUID = 3855891568971545839L;
}
