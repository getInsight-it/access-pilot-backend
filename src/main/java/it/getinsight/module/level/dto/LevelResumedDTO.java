package it.getinsight.module.level.dto;

import org.apache.commons.text.WordUtils;

public record LevelResumedDTO(Long id, String name) {

    public String name() {
        return name != null ? WordUtils.capitalizeFully(name) : null;
    }
}
