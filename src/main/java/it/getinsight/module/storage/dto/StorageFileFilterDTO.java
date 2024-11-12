package it.getinsight.module.storage.dto;


import lombok.Builder;

import java.io.Serializable;
import java.util.UUID;

@Builder
public record StorageFileFilterDTO(
    String originalFilename,
    UUID fileId,
    Long requestId
) implements Serializable {}
