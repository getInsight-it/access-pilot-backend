package it.getinsight.module.configuration.dto;

import it.getinsight.module.configuration.enums.FileExtensionType;
import it.getinsight.module.shared.enums.ColorPalette;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Builder;

import java.io.Serial;
import java.io.Serializable;
import java.util.Set;
import java.util.UUID;

@Builder
public record AttachmentConfigurationDTO(
    Long id,
    UUID uuid,
    @NotEmpty
    String name,
    String key,
    @Size(min = 3, max = 255)
    String description,
    @NotNull
    Boolean required,
    Set<FileExtensionType> allowedExtensions,
    String icon,
    ColorPalette color,
    Boolean active
) implements Serializable {
    @Serial
    private static final long serialVersionUID = -6731357049354425215L;
}
