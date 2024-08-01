package it.getinsight.module.erro.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.hibernate.validator.constraints.br.CPF;



public record ErrorDTO(
    @NotEmpty
    @Size(min = 3, max = 100)
    String name,

    @CPF
    @NotNull
    String cpf
) { }
