package it.getinsight.module.client.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;

import java.io.Serializable;

public record ClienteDTO(

    Long id,

    @NotEmpty
    @Size(min = 3, max = 100)
    String clientId,

    @NotEmpty
    @Size(min = 3, max = 100)
    String clientUUID,

    @NotEmpty
    @Size(min = 3, max = 100)
    String descricao
) implements Serializable {}
