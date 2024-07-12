package it.getinsight.module.client.v1;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import it.getinsight.core.pagination.PageableRequestModel;
import it.getinsight.core.pagination.PageableResponseModel;
import it.getinsight.module.client.dto.ClienteDTO;
import it.getinsight.module.client.service.ClienteService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/v1/clientes")
@Tag(name = "Cliente", description = "Operações sobre clientes.")
@RequiredArgsConstructor
public class ClienteController {

    private final ClienteService clienteService;

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(
        summary = "Recupera a lista de clientes",
        description = "Recupera todos os clientes",
        responses = {
            @ApiResponse(responseCode = "200", content = {
                @Content(schema = @Schema(implementation = ClienteDTO[].class))
            })
        }
    )
    public ResponseEntity<List<ClienteDTO>> recuperarTodosClientes() {
        return ResponseEntity.ok(clienteService.getAllClientesDynamicQuery());
    }

    @Operation(
        summary = "Recupera a lista de clientes paginada",
        description = "Recupera uma lista de clientes, com paginação, utilizando um filtro por nome",
        responses = {
            @ApiResponse(responseCode = "200", content = {
                @Content(schema = @Schema(implementation = PageableResponseModel.class))
            })
        }
    )
    @Parameter(name = "nome", description = "Filtro por nome", in = ParameterIn.QUERY, schema = @Schema(type = "string"))
    @Parameter(name = "filter", hidden = true)
    @GetMapping(path = "/paginado", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<PageableResponseModel<ClienteDTO>> recuperarTodosPaginado(
        @RequestParam(defaultValue = "0") Integer pageIndex,
        @RequestParam(defaultValue = "10") Integer pageSize,
        @RequestParam(defaultValue = "nome") String sortField,
        @RequestParam(defaultValue = "ASC") String sortType,
        ClienteDTO filter
    ) {
        final var pageRequest = PageableRequestModel.of(pageIndex, pageSize, sortType, sortField, filter);
        return ResponseEntity.ok(clienteService.getAllClientesPageable(pageRequest));
    }

    @Operation(
        summary = "Recupera a lista de clientes paginada",
        description = "Recupera uma lista de clientes, com paginação, utilizando um filtro por nome",
        responses = {
            @ApiResponse(responseCode = "200", content = {
                @Content(schema = @Schema(implementation = PageableResponseModel.class))
            })
        }
    )
    @GetMapping(path = "/paginado-por-nome", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<PageableResponseModel<ClienteDTO>> recuperarTodosPaginado(
        @RequestParam(defaultValue = "0") Integer pageIndex,
        @RequestParam(defaultValue = "10") Integer pageSize,
        @RequestParam(defaultValue = "nome") String sortField,
        @RequestParam(defaultValue = "ASC") String sortType,
        @RequestParam(required = false) String filter
    ) {
        final var pageRequest = PageableRequestModel.of(pageIndex, pageSize, sortType, sortField, filter);
        return ResponseEntity.ok(clienteService.getAllClientesPageableByName(pageRequest));
    }

    @Operation(
        summary = "Recupera um role por ID",
        description = "Recupera um role por ID",
        responses = {
            @ApiResponse(responseCode = "200", content = {
                @Content(schema = @Schema(implementation = ClienteDTO.class))
            })
        }
    )
    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ClienteDTO> recuperarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(clienteService.recuperarPorId(id));
    }



    @Operation(
        summary = "Sincroniza os clientes",
        description = "Sincroniza os clientes",
        responses = {
            @ApiResponse(responseCode = "200", content = {
                @Content(schema = @Schema(implementation = ClienteDTO.class))
            })
        }
    )
    @PostMapping(value = "/synchronize", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ClienteDTO> synchronize() {
        clienteService.synchronizationClients();
        return ResponseEntity.noContent().build();
    }


}
