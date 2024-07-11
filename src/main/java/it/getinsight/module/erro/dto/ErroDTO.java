package it.getinsight.module.erro.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.hibernate.validator.constraints.br.CPF;



public record ErroDTO(
    @NotEmpty
    @Size(min = 3, max = 100)
    String nome,

    @CPF
    @NotNull
    String cpf
) { }
