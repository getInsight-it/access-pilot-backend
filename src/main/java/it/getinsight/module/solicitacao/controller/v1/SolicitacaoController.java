package it.getinsight.module.solicitacao.controller.v1;

import io.swagger.v3.oas.annotations.tags.Tag;
import it.getinsight.module.solicitacao.dto.SolicitacaoDTO;
import it.getinsight.module.solicitacao.service.SolicitacaoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.util.List;

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

    @GetMapping
    public ResponseEntity<List<SolicitacaoDTO>> getAllSolicitacao() {
        return ResponseEntity.ok(solicitacaoService.getAllConfigurationsDynamicQuery());
    }


}
