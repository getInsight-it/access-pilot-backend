package it.getinsight.module.erro.controller.v1;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import it.getinsight.module.erro.dto.ErroDTO;
import it.getinsight.module.erro.service.ErroService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/v1/erros")
@Tag(name = "Erro", description = "Operações com os possíveis erros")
@RequiredArgsConstructor
public class ErroController {

    private final ErroService erroService;

    @GetMapping("/access-denied")
    @Operation(summary = "403", description = "AccessDeniedException")
    public ResponseEntity<Void> lancaExcessaoAccessDeniedException() {
        erroService.falharComAccessForbiddenException();

        return ResponseEntity.ok().build();
    }

    @GetMapping("/business/vazia")
    @Operation(summary = "400", description = "BusinessException")
    public ResponseEntity<Void> lancaExcessaoBusinessExceptionVazia() {
        erroService.falharComBusinessExceptionVazia();
        return ResponseEntity.ok().build();
    }

    @GetMapping("/business/mensagem")
    @Operation(summary = "400", description = "BusinessException")
    public ResponseEntity<Void> lancaExcessaoBusinessExceptionMensagem() {
        erroService.falharComBusinessException();
        return ResponseEntity.ok().build();
    }

    @GetMapping("/business/mensagem-parametrizada/{parametro-da-mensagem}")
    @Operation(summary = "400", description = "BusinessException")
    public ResponseEntity<Void> lancaExcessaoBusinessExceptionMensagemParametro(@PathVariable("parametro-da-mensagem") String parametro) {
        erroService.falharComBusinessExceptionMensagem(parametro);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/feign-integration")
    @Operation(summary = "*", description = "FeignIntegrationException")
    public ResponseEntity<Void> lancaExcessaoFeignIntegrationException() {
        erroService.falharComFeignIntegrationException();
        return ResponseEntity.ok().build();
    }

    @GetMapping("/resource-not-found")
    @Operation(summary = "404", description = "ResourceNotFoundException")
    public ResponseEntity<Void> lancaExcessaoResourceNotFoundException() {
        erroService.falharComResourceNotFoundException();
        return ResponseEntity.ok().build();
    }

    @GetMapping("/security-validation")
    @Operation(summary = "401", description = "SecurityValidationException")
    public ResponseEntity<Void> lancaExcessaoSecurityValidationException() {
        erroService.falharComSecurityValidationException();
        return ResponseEntity.ok().build();
    }

    @PostMapping
    @Operation(summary = "400", description = "Erro de validação de Bean")
    public ResponseEntity<Void> lancarExcessaoValidacao(@Validated @RequestBody ErroDTO erroDTO) {
        log.debug("Chegou: {}!", erroDTO);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/infra/mensagem")
    @Operation(summary = "500", description = "InfraException")
    public ResponseEntity<Void> lancaExcessaoInfraExceptionMensagem() {
        erroService.falharComInfraException();
        return ResponseEntity.ok().build();
    }

    @GetMapping("/infra/mensagem-parametrizada/{parametro-da-mensagem}")
    @Operation(summary = "500", description = "InfraException")
    public ResponseEntity<Void> lancaExcessaoInfraExceptionMensagemParametro(@PathVariable("parametro-da-mensagem") String parametro) {
        erroService.falharComBusinessExceptionMensagem(parametro);
        return ResponseEntity.ok().build();
    }

}
