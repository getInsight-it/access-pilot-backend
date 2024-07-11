package it.getinsight.module.solicitacao.dto;

import it.getinsight.module.solicitacao.enuns.SolicitacaoStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.io.Serial;
import java.io.Serializable;

public record SolicitacaoDTO(

    @Size(min = 1, max = 100)
    @NotBlank
    Long id,
    @NotBlank
    SolicitacaoStatus status,
    @NotBlank
    String userId,
    @NotBlank
    String roleId
) implements Serializable {

    @Serial
    private static final long serialVersionUID = -6731357049354425215L;
}
