package it.getinsight.module.client.dto;

import lombok.Builder;

import java.io.Serializable;

@Builder
public record ClientFilterDTO(

    String clientId,
    String description

) implements Serializable {}
