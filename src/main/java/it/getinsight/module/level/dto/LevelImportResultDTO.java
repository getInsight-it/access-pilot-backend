package it.getinsight.module.level.dto;

import lombok.Builder;

import java.io.Serial;
import java.io.Serializable;

@Builder
public record LevelImportResultDTO(
    String name,
    String status,
    String message
) implements Serializable {
    @Serial
    private static final long serialVersionUID = 5331379243333954568L;
}
