package it.getinsight.module.client.dto;

import it.getinsight.module.configuration.dto.ConfigurationDTO;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Builder;

import java.io.Serializable;

@Builder
public record ClientFullResponseDTO(

    Long id,

    @NotEmpty
    @Size(min = 3, max = 100)
    String name,

    String label,

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
    String baseUrl,

    ConfigurationDTO configuration

) implements Serializable {}
