package it.getinsight.module.role.dto;

import it.getinsight.module.client.dto.ClientDTO;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Builder;

import java.io.Serializable;

@Builder
public record RoleDTO(

    Long id,

    String roleExternalId,

    @NotEmpty
    @Size(min = 3, max = 100)
    String name,

    String label,

    String icon,

    @NotEmpty
    @Size(min = 3, max = 100)
    String description,

    RoleDTO roleParent,

    ClientDTO client,

    Long levelId

) implements Serializable {}
