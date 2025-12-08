package it.getinsight.module.client.dto;

import it.getinsight.module.client.entity.ClientStatus;
import lombok.Builder;

import java.io.Serializable;

@Builder
public record ClientFilterDTO(

    String clientId,
    String name,
    String description,
    ClientStatus status

) implements Serializable {}
