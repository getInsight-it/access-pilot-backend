package it.getinsight.module.client.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;

import java.io.Serializable;

public record ClientDTO(

    Long id,

    @NotEmpty
    @Size(min = 3, max = 100)
    String clientId,

    @NotEmpty
    @Size(min = 3, max = 100)
    String clientUUID,

    @NotEmpty
    Boolean managed,

    @NotEmpty
    @Size(min = 3, max = 100)
    String description
) implements Serializable {}
