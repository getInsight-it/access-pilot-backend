package it.getinsight.module.level.dto;


import it.getinsight.module.level.entity.LevelType;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import org.apache.commons.text.WordUtils;

import java.io.Serializable;
import java.util.UUID;


@Builder
public record LevelDTO(
    Long id,
    UUID uuid,
    Long parentId,
    String sigla,
    String name,
    @NotBlank
    String description,
    String externalUrl,
    LevelType type,
    String icon,
    String apiKey
) implements Serializable {

    public String name() {
        return name != null ? WordUtils.capitalizeFully(name) : null;
    }
}
