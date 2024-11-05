package it.getinsight.module.client.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Builder;

import java.io.Serializable;

@Builder
public record ClientDTO(

    Long id,

    @NotEmpty
    @Size(min = 3, max = 100)
    String name,

    @NotEmpty
    @Size(min = 3, max = 100)
    String clientId,

    @NotEmpty
    @Size(min = 3, max = 100)
    String clientUUID,

    @NotEmpty
    Boolean managed,

    String status,

    @NotEmpty
    @Size(min = 3, max = 100)
    String description,

    @NotEmpty
    @Size(min = 3, max = 255)
    String baseUrl

) implements Serializable {}
