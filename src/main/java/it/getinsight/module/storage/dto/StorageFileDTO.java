package it.getinsight.module.storage.dto;


import lombok.Builder;

import java.io.Serializable;
import java.util.UUID;

@Builder
public record StorageFileDTO(
    Long id,
    Boolean excluded,
    String originalFilename,
    Long filesize,
    String mimeType,
    String bucket,
    Boolean isPublic,
    Boolean ephemeral,
    Long downloadCount,
    UUID ownerId,
    UUID fileId
) implements Serializable {}
