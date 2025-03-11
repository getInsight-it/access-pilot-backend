package it.getinsight.module.level.dto;

import lombok.Builder;

import java.io.Serializable;


@Builder
public record LevelFilterDTO(
    String name,
    String description,
    String externalUrl
) implements Serializable{
}
