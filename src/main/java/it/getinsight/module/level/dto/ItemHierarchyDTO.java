package it.getinsight.module.level.dto;

import lombok.Builder;

import java.io.Serializable;
import java.util.UUID;

@Builder
public record ItemHierarchyDTO(Long id, UUID uuid, String description, String name, String externalCode, String status, LevelDTO level, ItemHierarchyDTO parent) implements Serializable {}
