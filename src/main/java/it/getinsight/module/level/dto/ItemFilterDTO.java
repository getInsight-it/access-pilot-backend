package it.getinsight.module.level.dto;

import lombok.Builder;

import java.io.Serializable;


@Builder
public record ItemFilterDTO(
    String name,
    String description,
    String externalCode
) implements Serializable{
}
