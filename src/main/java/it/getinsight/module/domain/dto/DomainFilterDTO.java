package it.getinsight.module.domain.dto;

import lombok.Builder;

import java.io.Serializable;


@Builder
public record DomainFilterDTO(
    String name,
    String description,
    String externalUrl
) implements Serializable{
}
