package it.getinsight.module.level.dto;

import java.util.ArrayList;
import java.util.List;

import lombok.Data;
import org.apache.commons.text.WordUtils;

@Data
public final class ItemResponseNodeDTO {
    private final Long id;
    private final String name;
    private final LevelResumedDTO level;
    private final List<ItemResponseNodeDTO> items = new ArrayList<>();

    public ItemResponseNodeDTO(Long id, String name, LevelResumedDTO level) {
        this.id = id;
        this.name = name != null ? WordUtils.capitalizeFully(name) : null;
        this.level = level;
    }


}
