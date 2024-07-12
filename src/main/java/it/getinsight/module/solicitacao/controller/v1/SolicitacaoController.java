package it.getinsight.module.solicitacao.controller.v1;

import io.swagger.v3.oas.annotations.tags.Tag;
import it.getinsight.core.pagination.PageableRequestModel;
import it.getinsight.core.pagination.PageableResponseModel;
import it.getinsight.module.solicitacao.dto.SolicitacaoDTO;
import it.getinsight.module.solicitacao.service.SolicitacaoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

@RestController
@RequestMapping("/v1/solicitacoes")
@Tag(name = "Solicitacao", description = "Operações sobre Solicitacao.")
@RequiredArgsConstructor
public class SolicitacaoController {

    private final SolicitacaoService solicitacaoService;

    @PostMapping
    public ResponseEntity<Void> create(@RequestBody SolicitacaoDTO solicitacaoDTO){
        var uri = ServletUriComponentsBuilder.fromCurrentRequest().path(
            "/{id}").buildAndExpand(solicitacaoService.createSolicitacao(solicitacaoDTO).id()).toUri();
        return ResponseEntity.created(uri).build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> publishSolicitacaoUpdateEvent(@PathVariable Long id, @RequestBody String status) {
        solicitacaoService.publishSolicitacaoUpdateEvent(id, status);
        return ResponseEntity.status(HttpStatus.ACCEPTED).build();
    }

    @GetMapping(path = "/me/paginado-por-nome", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<PageableResponseModel<SolicitacaoDTO>> recuperarTodosPaginado(
        @RequestParam(defaultValue = "0") Integer pageIndex,
        @RequestParam(defaultValue = "10") Integer pageSize,
        @RequestParam(defaultValue = "nome") String sortField,
        @RequestParam(defaultValue = "ASC") String sortType,
        @RequestParam(required = false) String filter
    ) {
        final var pageRequest = PageableRequestModel.of(pageIndex, pageSize, sortType, sortField, filter);
        return ResponseEntity.ok(solicitacaoService.getAllSolicitacoesByStatusDynamicQuery(pageRequest));
    }


}
