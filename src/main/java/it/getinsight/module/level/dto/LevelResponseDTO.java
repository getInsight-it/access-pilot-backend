package it.getinsight.module.level.dto;

import it.getinsight.module.level.entity.LevelType;
import lombok.Builder;

import java.io.Serializable;
import java.util.UUID;


@Builder
public record LevelResponseDTO(
    Long id,
    UUID uuid,
    LevelResponseDTO parent,
    String sigla,
    String name,
    String description,
    String externalUrl,
    LevelType type
) implements Serializable{
}
