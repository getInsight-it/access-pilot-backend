package it.getinsight.module.request.dto;

import it.getinsight.module.configuration.dto.AttachmentConfigurationDTO;
import it.getinsight.module.storage.dto.StorageFileResumedDTO;
import lombok.Builder;

import java.io.Serial;
import java.io.Serializable;
import java.util.UUID;

@Builder
public record RequestAttachmentDTO(
    Long id,
    UUID uuid,
    StorageFileResumedDTO file,
    AttachmentConfigurationDTO configuration,
    String fileType,
    String description
) implements Serializable {

    @Serial
    private static final long serialVersionUID = -6731357049354425215L;
}
