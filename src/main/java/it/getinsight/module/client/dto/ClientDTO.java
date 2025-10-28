package it.getinsight.module.client.dto;

import it.getinsight.module.configuration.dto.AttachmentConfigurationDTO;
import it.getinsight.module.level.dto.ItemResponseNodeDTO;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Builder;

import java.io.Serializable;
import java.util.List;

@Builder
public record ClientDTO(

    Long id,

    @NotEmpty
    @Size(min = 3, max = 100)
    String name,

    String label,

    @NotEmpty
    @Size(min = 3, max = 100)
    String clientId,

    String clientUUID,

    @NotNull(message = "managed is required")
    Boolean managed,

    String status,

    @NotEmpty
    @Size(min = 3, max = 100)
    String description,

    @Size(min = 3, max = 255)
    String baseUrl,

    List<AttachmentConfigurationDTO> configurations,

    List<ItemResponseNodeDTO> allowedItemsHierarchy

) implements Serializable {

    public ClientDTO withAllowedItemsHierarchy(List<ItemResponseNodeDTO> allowedItemsHierarchy) {
        return ClientDTO.builder()
            .id(this.id)
            .name(this.name)
            .label(this.label)
            .clientId(this.clientId)
            .clientUUID(this.clientUUID)
            .managed(this.managed)
            .status(this.status)
            .description(this.description)
            .baseUrl(this.baseUrl)
            .configurations(this.configurations)
            .allowedItemsHierarchy(allowedItemsHierarchy)
            .build();
    }
}
