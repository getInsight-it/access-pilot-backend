package it.getinsight.module.erro.controller.v1;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import it.getinsight.module.erro.dto.ErrorDTO;
import it.getinsight.module.erro.service.ErrorService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/v1/errors")
@Tag(name = "Error", description = "Operations on possible errors")
@RequiredArgsConstructor
public class ErrorController {

    private final ErrorService errorService;

    @GetMapping("/access-denied")
    @Operation(summary = "403", description = "AccessDeniedException")
    public ResponseEntity<Void> throwAccessDeniedException() {
        errorService.failWithAccessForbiddenException();
        return ResponseEntity.ok().build();
    }

    @GetMapping("/business/empty")
    @Operation(summary = "400", description = "BusinessException")
    public ResponseEntity<Void> throwBusinessExceptionEmpty() {
        errorService.failWithBusinessExceptionEmpty();
        return ResponseEntity.ok().build();
    }

    @GetMapping("/business/message")
    @Operation(summary = "400", description = "BusinessException")
    public ResponseEntity<Void> throwBusinessExceptionMessage() {
        errorService.failWithBusinessException();
        return ResponseEntity.ok().build();
    }

    @GetMapping("/business/message-parameterized/{message-parameter}")
    @Operation(summary = "400", description = "BusinessException")
    public ResponseEntity<Void> throwBusinessExceptionMessageParameter(@PathVariable("message-parameter") String parameter) {
        errorService.failWithBusinessExceptionMessage(parameter);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/feign-integration")
    @Operation(summary = "*", description = "FeignIntegrationException")
    public ResponseEntity<Void> throwFeignIntegrationException() {
        errorService.failWithFeignIntegrationException();
        return ResponseEntity.ok().build();
    }

    @GetMapping("/resource-not-found")
    @Operation(summary = "404", description = "ResourceNotFoundException")
    public ResponseEntity<Void> throwResourceNotFoundException() {
        errorService.failWithResourceNotFoundException();
        return ResponseEntity.ok().build();
    }

    @GetMapping("/security-validation")
    @Operation(summary = "401", description = "SecurityValidationException")
    public ResponseEntity<Void> throwSecurityValidationException() {
        errorService.failWithSecurityValidationException();
        return ResponseEntity.ok().build();
    }

    @PostMapping
    @Operation(summary = "400", description = "Bean validation error")
    public ResponseEntity<Void> throwValidationException(@Validated @RequestBody ErrorDTO errorDTO) {
        log.debug("Received: {}!", errorDTO);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/infra/message")
    @Operation(summary = "500", description = "InfraException")
    public ResponseEntity<Void> throwInfraExceptionMessage() {
        errorService.failWithInfraException();
        return ResponseEntity.ok().build();
    }

    @GetMapping("/infra/message-parameterized/{message-parameter}")
    @Operation(summary = "500", description = "InfraException")
    public ResponseEntity<Void> throwInfraExceptionMessageParameter(@PathVariable("message-parameter") String parameter) {
        errorService.failWithBusinessExceptionMessage(parameter);
        return ResponseEntity.ok().build();
    }
}
