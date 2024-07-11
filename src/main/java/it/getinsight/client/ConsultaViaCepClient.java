package it.getinsight.client;

import io.swagger.v3.oas.annotations.ExternalDocumentation;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Tag(name = "🔗 Feign - ViaCep", description = "Cliente para o ViaCep", externalDocs = @ExternalDocumentation(description = "Mais informações", url = "https://viacep.com.br"))
@FeignClient(url = "https://viacep.com.br", path = "/ws", name = "viacep", fallbackFactory = ConsultaViaCepClientFallback.class)
public interface ConsultaViaCepClient {

    @Operation(
        summary = "Recupera endereço por CEP",
        description = "Método usado para recuperar endereço por CEP.",
        hidden = true
    )
    @GetMapping("{cep}/json")
    ResponseEntity<EnderecoVO> obterEnderecoPorCep(@PathVariable("cep") String cep);
}
