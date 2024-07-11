package it.getinsight.module.role.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;

import java.io.Serializable;

public record RoleDTO(

    Long id,

    String idRoleExterno,

    @NotEmpty
    @Size(min = 3, max = 100)
    String nome,

    @NotEmpty
    @Size(min = 3, max = 100)
    String descricao,

    Long idRoleParent,

    Long idClient
) implements Serializable {}
