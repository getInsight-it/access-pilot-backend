package it.getinsight.client;

import lombok.Builder;

import java.io.Serial;
import java.io.Serializable;

@Builder
public record EnderecoVO(
    String cep,
    String logradouro,
    String complemento,
    String bairro,
    String localidade,
    String uf

) implements Serializable {

    @Serial
    private static final long serialVersionUID = 5368569948794287726L;
}
