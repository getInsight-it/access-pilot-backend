package it.getinsight.module.role.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;

import java.io.Serializable;

public record RoleDTO(

    Long id,

    String roleExternalId,

    @NotEmpty
    @Size(min = 3, max = 100)
    String name,

    @NotEmpty
    @Size(min = 3, max = 100)
    String description,

    Long idRoleParent,

    Long idClient
) implements Serializable {}
