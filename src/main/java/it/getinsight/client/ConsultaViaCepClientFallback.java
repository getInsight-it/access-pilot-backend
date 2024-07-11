package it.getinsight.client;

import feign.hystrix.FallbackFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class ConsultaViaCepClientFallback implements FallbackFactory<ConsultaViaCepClient> {


    /**
     * Aqui você deve implementar um outro meio de se obter o dado.
     * Se não for possível, deve lançar a excessão assim:
     *
     * <pre>
     *     {@code
     *      import it.getinsight.core.exception.FeignIntegrationException;
     *     }
     * </pre>
     *
     * <pre>
     *     {@code
     *      throw new FeignIntegrationException(cause);
     *     }
     * </pre>
     *
     * @param cause - O motivo de o Feign Client ter falhado
     * @return Uma implementação de fallback.
     */
    @Override
    public ConsultaViaCepClient create(Throwable cause) {
        return cep -> {
            log.error("Ocorreu um erro com o Feign Client [ ConsultaViaCepClient ]. Retornando fallback.", cause);
            final var end = new EnderecoVO(null, null, "Fallback: " + cep, null, null, null);
            return ResponseEntity.ok(end);
        };
    }

}
