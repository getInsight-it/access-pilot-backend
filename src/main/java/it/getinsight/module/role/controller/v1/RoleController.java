package it.getinsight.module.role.controller.v1;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import it.getinsight.core.pagination.PageableRequestModel;
import it.getinsight.core.pagination.PageableResponseModel;
import it.getinsight.module.role.dto.RoleDTO;
import it.getinsight.module.role.service.RoleService;
import it.getinsight.module.usuario.dto.UsuarioDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/v1/roles")
@Tag(name = "Role", description = "Operações sobre roles.")
@RequiredArgsConstructor
public class RoleController {

    private final RoleService roleService;

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(
        summary = "Recupera a lista de roles",
        description = "Recupera todos os roles",
        responses = {
            @ApiResponse(responseCode = "200", content = {
                @Content(schema = @Schema(implementation = RoleDTO[].class))
            })
        }
    )
    public ResponseEntity<List<RoleDTO>> recuperarTodosRoles() {
        return ResponseEntity.ok(roleService.getAllRolesDynamicQuery());
    }

    @Operation(
        summary = "Recupera a lista de roles paginada",
        description = "Recupera uma lista de roles, com paginação, utilizando um filtro por nome",
        responses = {
            @ApiResponse(responseCode = "200", content = {
                @Content(schema = @Schema(implementation = PageableResponseModel.class))
            })
        }
    )
    @Parameter(name = "nome", description = "Filtro por nome", in = ParameterIn.QUERY, schema = @Schema(type = "string"))
    @Parameter(name = "filter", hidden = true)
    @GetMapping(path = "/paginado", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<PageableResponseModel<RoleDTO>> recuperarTodosPaginado(
        @RequestParam(defaultValue = "0") Integer pageIndex,
        @RequestParam(defaultValue = "10") Integer pageSize,
        @RequestParam(defaultValue = "nome") String sortField,
        @RequestParam(defaultValue = "ASC") String sortType,
        RoleDTO filter
    ) {
        final var pageRequest = PageableRequestModel.of(pageIndex, pageSize, sortType, sortField, filter);
        return ResponseEntity.ok(roleService.getAllRolesPageable(pageRequest));
    }

    @Operation(
        summary = "Recupera a lista de roles paginada",
        description = "Recupera uma lista de roles, com paginação, utilizando um filtro por nome",
        responses = {
            @ApiResponse(responseCode = "200", content = {
                @Content(schema = @Schema(implementation = PageableResponseModel.class))
            })
        }
    )
    @GetMapping(path = "/paginado-por-nome", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<PageableResponseModel<RoleDTO>> recuperarTodosPaginado(
        @RequestParam(defaultValue = "0") Integer pageIndex,
        @RequestParam(defaultValue = "10") Integer pageSize,
        @RequestParam(defaultValue = "nome") String sortField,
        @RequestParam(defaultValue = "ASC") String sortType,
        @RequestParam(required = false) String filter
    ) {
        final var pageRequest = PageableRequestModel.of(pageIndex, pageSize, sortType, sortField, filter);
        return ResponseEntity.ok(roleService.getAllRolesPageableByName(pageRequest));
    }

    @Operation(
        summary = "Recupera um role por ID",
        description = "Recupera um role por ID",
        responses = {
            @ApiResponse(responseCode = "200", content = {
                @Content(schema = @Schema(implementation = RoleDTO.class))
            })
        }
    )
    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<RoleDTO> recuperarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(roleService.recuperarPorId(id));
    }


    @Operation(
        summary = "Recupera a lista de usuarios por ID da role",
        description = "Recupera a lista de usuarios por ID da role",
        responses = {
            @ApiResponse(responseCode = "200", content = {
                @Content(schema = @Schema(implementation = RoleDTO.class))
            })
        }
    )
    @GetMapping(value = "/{id}/aprovadores", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<UsuarioDTO>> recuperarOuImportarAprovadoresPorIdRole(@PathVariable Long id) {
        return ResponseEntity.ok(roleService.recuperarOuImportarAprovadoresPorIdRole(id));
    }


    @PostMapping("/synchronize")
    @Operation(summary = "synchronize roles com IDP", description = "synchronize roles com IDP")
    public ResponseEntity<Void> synchronizeRoles() {
        roleService.synchronizationRoles();
        return ResponseEntity.noContent().build();
    }

}
