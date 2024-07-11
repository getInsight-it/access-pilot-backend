package it.getinsight.module.configuracao.controller.v1;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import it.getinsight.module.configuracao.dto.ConfigurationDTO;
import it.getinsight.module.configuracao.service.ConfigurationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.util.List;

@RestController
@RequestMapping("/v1/configurations")
@Tag(name = "Configurações", description = "Operações sobre configurações.")
@RequiredArgsConstructor
public class ConfigurationController {

    private final ConfigurationService configurationService;

    @PostMapping
    @Operation(summary = "Cria uma nova configuração.", responses = {
            @ApiResponse(responseCode = "201", description = "Configuração criada com sucesso."),
            @ApiResponse(responseCode = "400", description = "Requisição inválida."),
            @ApiResponse(responseCode = "401", description = "Não autorizado."),
            @ApiResponse(responseCode = "403", description = "Proibido."),
            @ApiResponse(responseCode = "500", description = "Erro interno.")
    })
    public ResponseEntity<Void> create(@Valid @RequestBody ConfigurationDTO configurationDTO) {
        var configuration = configurationService.create(configurationDTO);
        var uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(configuration.id()).toUri();
        return ResponseEntity.created(uri).build();
    }

    @PutMapping("/{id}")
    @Operation(summary = "Atualiza uma configuração.", responses = {
            @ApiResponse(responseCode = "204", description = "Configuração atualizada com sucesso."),
            @ApiResponse(responseCode = "400", description = "Requisição inválida."),
            @ApiResponse(responseCode = "401", description = "Não autorizado."),
            @ApiResponse(responseCode = "403", description = "Proibido."),
            @ApiResponse(responseCode = "404", description = "Configuração não encontrada."),
            @ApiResponse(responseCode = "500", description = "Erro interno.")
    })
    public ResponseEntity<Void> update(@PathVariable Long id, @Valid @RequestBody ConfigurationDTO configurationDTO) {
        configurationService.update(id, configurationDTO);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Remove uma configuração.", responses = {
            @ApiResponse(responseCode = "204", description = "Configuração removida com sucesso."),
            @ApiResponse(responseCode = "400", description = "Requisição inválida."),
            @ApiResponse(responseCode = "401", description = "Não autorizado."),
            @ApiResponse(responseCode = "403", description = "Proibido."),
            @ApiResponse(responseCode = "404", description = "Configuração não encontrada."),
            @ApiResponse(responseCode = "500", description = "Erro interno.")
    })
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        configurationService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}")
    @Operation(summary = "Busca uma configuração.", responses = {
            @ApiResponse(responseCode = "200", description = "Configuração encontrada.", content = @Content(schema = @Schema(implementation = ConfigurationDTO.class))),
            @ApiResponse(responseCode = "400", description = "Requisição inválida."),
            @ApiResponse(responseCode = "401", description = "Não autorizado."),
            @ApiResponse(responseCode = "403", description = "Proibido."),
            @ApiResponse(responseCode = "404", description = "Configuração não encontrada."),
            @ApiResponse(responseCode = "500", description = "Erro interno.")
    })
    public ResponseEntity<ConfigurationDTO> findById(@PathVariable Long id) {
        var configurationDTO = configurationService.findById(id);
        return ResponseEntity.ok(configurationDTO);
    }

    @GetMapping
    @Operation(summary = "Busca todas as configurações.", responses = {
            @ApiResponse(responseCode = "200", description = "Configurações encontradas.", content = @Content(schema = @Schema(implementation = ConfigurationDTO.class))),
            @ApiResponse(responseCode = "400", description = "Requisição inválida."),
            @ApiResponse(responseCode = "401", description = "Não autorizado."),
            @ApiResponse(responseCode = "403", description = "Proibido."),
            @ApiResponse(responseCode = "500", description = "Erro interno.")
    })
    public ResponseEntity<List<ConfigurationDTO>> findAll() {
        var configurations = configurationService.getAllConfigurationsDynamicQuery();
        return ResponseEntity.ok(configurations);
    }
}
