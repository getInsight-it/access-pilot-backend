package it.getinsight.module.level.dto;

import lombok.Builder;

import java.io.Serializable;
import java.util.UUID;

@Builder
public record ItemHierarchyResumedDTO(Long id, UUID uuid, String description, String name, String externalCode, String status, LevelHierarchyResumedDTO level, ItemHierarchyResumedDTO parent) implements Serializable {
    public ItemHierarchyResumedDTO withLevel(LevelHierarchyResumedDTO newLevel) {
        return new ItemHierarchyResumedDTO(
            this.id,
            this.uuid,
            this.description,
            this.name,
            this.externalCode,
            this.status,
            newLevel,
            this.parent
        );
    }

    public ItemHierarchyResumedDTO withParent(ItemHierarchyResumedDTO newParent) {
        return new ItemHierarchyResumedDTO(
            this.id,
            this.uuid,
            this.description,
            this.name,
            this.externalCode,
            this.status,
            this.level,
            newParent
        );
    }
}
