package it.getinsight.module.level.dto;

import lombok.Builder;
import org.apache.commons.text.WordUtils;

import java.io.Serializable;
import java.util.UUID;

@Builder
public record ItemHierarchyDTO(Long id, UUID uuid, String description, String name, String externalCode, String status, LevelResponseDTO level, ItemHierarchyDTO parent) implements Serializable {

    public String name() {
        return name != null ? WordUtils.capitalizeFully(name) : null;
    }
}
