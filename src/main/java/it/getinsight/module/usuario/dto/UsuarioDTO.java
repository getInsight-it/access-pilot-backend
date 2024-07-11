package it.getinsight.module.usuario.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.io.Serial;
import java.io.Serializable;

public record UsuarioDTO(
    Long id,

    @Size(min = 1, max = 255)
    @NotBlank
    String username,

    @Size(min = 1, max = 255)
    @NotBlank
    String nome,

    @Size(min = 1, max = 255)
    @NotBlank
    String sobrenome,

    @Size(min = 1, max = 255)
    @NotBlank
    String email,

    @NotBlank
    @Size(min = 1, max = 255)
    String idUsuarioExterno
) implements Serializable {

    @Serial
    private static final long serialVersionUID = -6731357049354425215L;
}
