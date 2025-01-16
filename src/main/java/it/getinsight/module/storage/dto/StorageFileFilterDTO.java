package it.getinsight.module.storage.dto;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

import java.io.Serializable;
import java.util.UUID;

@Builder
public record StorageFileFilterDTO(

    @Schema(description = "File name")
    String originalFilename,

    @Schema(description = "File ID")
    UUID fileId,

    @Schema(description = "Owner ID")
    UUID ownerId
) implements Serializable {}
