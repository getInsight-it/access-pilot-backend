package it.getinsight.module.level.dto;

import it.getinsight.module.level.entity.LevelType;
import lombok.Builder;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;
import java.util.UUID;

@Builder
public record LevelExportDTO(
    String name,
    String sigla,
    String description,
    LevelType type,
    String parentName,
    String externalUrl,
    String icon,
    String apiKey,
    UUID uuid,
    List<LevelItemExportDTO> items
) implements Serializable {
    @Serial
    private static final long serialVersionUID = 1179118314468812578L;
}
