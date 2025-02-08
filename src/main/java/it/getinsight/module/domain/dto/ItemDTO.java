package it.getinsight.module.domain.dto;

import lombok.Builder;

import java.io.Serializable;
import java.util.UUID;

@Builder
public record ItemDTO(Long id, UUID uuid, String description, String name, String externalCode, String status, Long domainId, Long parentId) implements Serializable {}
