package it.getinsight.module.domain.dto;

import lombok.Builder;

import java.io.Serializable;


@Builder
public record ItemFilterDTO(
    String name,
    String description,
    String externalCode
) implements Serializable{
}
