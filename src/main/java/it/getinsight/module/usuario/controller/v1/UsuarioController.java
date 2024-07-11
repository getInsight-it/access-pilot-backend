package it.getinsight.module.usuario.controller.v1;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import it.getinsight.core.pagination.PageableRequestModel;
import it.getinsight.core.pagination.PageableResponseModel;
import it.getinsight.module.usuario.dto.UsuarioDTO;
import it.getinsight.module.usuario.service.UsuarioService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/v1/usuarios")
@Tag(name = "Usuário", description = "Operações sobre usuários.")
@RequiredArgsConstructor
public class UsuarioController {

    private final UsuarioService usuarioService;

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(
        summary = "Recupera a lista de usuários",
        description = "Recupera todos os usuários",
        responses = {
            @ApiResponse(responseCode = "200", content = {
                @Content(schema = @Schema(implementation = UsuarioDTO[].class))
            })
        }
    )
    public ResponseEntity<List<UsuarioDTO>> recuperarTodosUsuarios() {
        return ResponseEntity.ok(usuarioService.getAllUsersDynamicQuery());
    }

    @Operation(
        summary = "Recupera a lista de usuários paginada",
        description = "Recupera uma lista de usuários, com paginação, utilizando um filtro por nome, situação, quantidade e/ou valor",
        responses = {
            @ApiResponse(responseCode = "200", content = {
                @Content(schema = @Schema(implementation = PageableResponseModel.class))
            })
        }
    )
    @Parameter(name = "nome", description = "Filtro por nome", in = ParameterIn.QUERY, schema = @Schema(type = "string"))
    @Parameter(name = "situacao", description = "Filtro por situação", in = ParameterIn.QUERY, schema = @Schema(type = "boolean"))
    @Parameter(name = "quantidade", description = "Filtro por quantidade", in = ParameterIn.QUERY, schema = @Schema(type = "integer"))
    @Parameter(name = "valor", description = "Filtro por valor", in = ParameterIn.QUERY, schema = @Schema(type = "float"))
    @Parameter(name = "filter", hidden = true)
    @GetMapping(path = "/paginado", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<PageableResponseModel<UsuarioDTO>> recuperarTodosPaginado(
        @RequestParam(defaultValue = "0") Integer pageIndex,
        @RequestParam(defaultValue = "10") Integer pageSize,
        @RequestParam(defaultValue = "nome") String sortField,
        @RequestParam(defaultValue = "ASC") String sortType,
        UsuarioDTO filter
    ) {
        final var pageRequest = PageableRequestModel.of(pageIndex, pageSize, sortType, sortField, filter);
        return ResponseEntity.ok(usuarioService.getAllUsersPageable(pageRequest));
    }

    @Operation(
        summary = "Recupera a lista de usuários paginada",
        description = "Recupera uma lista de usuários, com paginação, utilizando um filtro por nome",
        responses = {
            @ApiResponse(responseCode = "200", content = {
                @Content(schema = @Schema(implementation = PageableResponseModel.class))
            })
        }
    )
    @GetMapping(path = "/paginado-por-nome", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<PageableResponseModel<UsuarioDTO>> recuperarTodosPaginado(
        @RequestParam(defaultValue = "0") Integer pageIndex,
        @RequestParam(defaultValue = "10") Integer pageSize,
        @RequestParam(defaultValue = "nome") String sortField,
        @RequestParam(defaultValue = "ASC") String sortType,
        @RequestParam(required = false) String filter
    ) {
        final var pageRequest = PageableRequestModel.of(pageIndex, pageSize, sortType, sortField, filter);
        return ResponseEntity.ok(usuarioService.getAllUsersPageableByName(pageRequest));
    }

    @Operation(
        summary = "Recupera um usuário por ID",
        description =
            """
            # Exemplo de documentação com Markdown
            ## Recupera um usuário por ID

            > Este método espera um parâmetro **inteiro** e retorna um JSON do tipo `UsuarioDTO`.
            >
            > Também é possível colocar uma imagem via _markdown_ na documentação, veja:

            ![Imagem](https://www.rnp.br/sites/site-publico/themes/sitepublico/logo.png)

            Podemos colocar trechos de código formatados via _markdown_. Veja o exemplo:

            ```json
            {
              "ip": "string",
              "latitude": 0,
              "longitude": 0
            }
            ```
            """,
        responses = {
            @ApiResponse(responseCode = "200", content = {
                @Content(schema = @Schema(implementation = UsuarioDTO.class))
            })
        }
    )
    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<UsuarioDTO> recuperarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(usuarioService.recuperarPorId(id));
    }


    @PostMapping
    @Operation(summary = "Cadastrar usuário", description = "Cadastrar usuário")
    public ResponseEntity<Void> cadastrarUsuario(
        @Parameter(name = "usuario", description = "Representação do usuário a ser adicionado", required = true)
        @Valid @RequestBody UsuarioDTO dto) {
        var colaboradorCriado = usuarioService.adicionarUsuario(dto);
        var location = ServletUriComponentsBuilder.fromCurrentRequest().path(
            "/{id}").buildAndExpand(colaboradorCriado.id()).toUri();
        return ResponseEntity.created(location).build();
    }

    @GetMapping("logado")
    public String getUsuarioLogado(Principal principal) {
        return principal.getName();
    }

}
