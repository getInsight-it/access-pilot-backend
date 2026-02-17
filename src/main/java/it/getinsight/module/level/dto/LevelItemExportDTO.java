package it.getinsight.module.level.dto;

import lombok.Builder;

import java.io.Serial;
import java.io.Serializable;

@Builder
public record LevelItemExportDTO(
    String name,
    String description,
    String externalCode,
    String parentName,
    String parentCode,
    String parentExternalCode
) implements Serializable {
    @Serial
    private static final long serialVersionUID = 5420218787404761149L;
}
