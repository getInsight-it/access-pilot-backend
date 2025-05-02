package it.getinsight.module.storage.dto;


import lombok.Builder;

import java.io.Serializable;
import java.util.UUID;

@Builder
public record StorageFileResumedDTO(
    Long id,
    String originalFilename,
    Long filesize,
    String mimeType,
    UUID fileId
) implements Serializable {}
