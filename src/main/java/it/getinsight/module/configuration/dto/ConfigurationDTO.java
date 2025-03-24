package it.getinsight.module.configuration.dto;

import com.fasterxml.jackson.databind.JsonNode;
import jakarta.validation.constraints.NotBlank;
import java.io.Serial;
import java.io.Serializable;
import java.util.Map;
import java.util.UUID;

public record ConfigurationDTO(
    Long id,
    UUID uuid,
    @NotBlank String name,
    String description,
    JsonNode value
) implements Serializable {
    @Serial
    private static final long serialVersionUID = -6731357049354425215L;
}
