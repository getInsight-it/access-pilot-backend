package it.getinsight.module.level.dto;

import lombok.Builder;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

@Builder
public record LevelImportSummaryDTO(
    long created,
    long updated,
    long ignored,
    long errors,
    long duration,
    List<LevelImportResultDTO> results
) implements Serializable {
    @Serial
    private static final long serialVersionUID = 1905422301457962526L;
}
