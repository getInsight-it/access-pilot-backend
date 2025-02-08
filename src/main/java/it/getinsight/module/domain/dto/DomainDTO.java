package it.getinsight.module.domain.dto;

import it.getinsight.module.domain.entity.DomainType;
import lombok.Builder;

import java.io.Serializable;
import java.util.UUID;


@Builder
public record DomainDTO(
    Long id,
    UUID uuid,
    Long parentId,
    String sigla,
    String name,
    String description,
    String externalUrl,
    DomainType type,
    String icon,
    String apiKey
) implements Serializable{
}
