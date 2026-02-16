package it.getinsight.module.client.dto;

import it.getinsight.module.configuration.dto.AttachmentConfigurationDTO;
import lombok.Builder;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

@Builder
public record ClientExportDTO(
    ClientExportClientDTO client,
    List<AttachmentConfigurationDTO> configurations,
    List<ClientExportRoleDTO> roles
) implements Serializable {
    @Serial
    private static final long serialVersionUID = 7032953144175152566L;
}
