package it.getinsight.module.level.dto;

import java.util.ArrayList;
import java.util.List;

import lombok.Data;

@Data
public final class ItemResponseNodeDTO {
    public final Long id;
    public final String name;
    public final LevelResumedDTO level;
    public final List<ItemResponseNodeDTO> items = new ArrayList<>();

    public ItemResponseNodeDTO(Long id, String name, LevelResumedDTO level) {
        this.id = id;
        this.name = name;
        this.level = level;
    }
}
