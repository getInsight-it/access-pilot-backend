package it.getinsight.module.configuration.dto;


import java.io.Serial;
import java.io.Serializable;

public record ConfigurationFilterDTO(
    String name,
    String description
) implements Serializable {
    @Serial
    private static final long serialVersionUID = -6731357049354425215L;
}
