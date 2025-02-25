package it.getinsight.module.domain.controller.v1;


import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import it.getinsight.core.pagination.PageableRequestModel;
import it.getinsight.core.pagination.PageableResponseModel;
import it.getinsight.module.domain.dto.DomainDTO;
import it.getinsight.module.domain.dto.DomainFilterDTO;
import it.getinsight.module.domain.dto.ItemDTO;
import it.getinsight.module.domain.dto.ItemFilterDTO;
import it.getinsight.module.domain.service.DomainService;
import it.getinsight.module.domain.service.ItemService;
import jakarta.annotation.security.PermitAll;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@RestController
@RequestMapping("/v1/domains")
@Tag(name = "Domain", description = "Operations on domains.")
@RequiredArgsConstructor
@PermitAll
public class DomainController {

    private final DomainService domainService;
    private final ItemService itemService;


    @Operation(summary = "Retrieve the list of domains", description = "Retrieve all domains")
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<PageableResponseModel<DomainDTO>> getPaginatedAllDomains(
        @RequestParam(defaultValue = "1") Integer pageIndex,
        @RequestParam(defaultValue = "10") Integer pageSize,
        @RequestParam(defaultValue = "id") String sortField,
        @RequestParam(defaultValue = "ASC") String sortType,
        @ParameterObject DomainFilterDTO filterDTO) {
        final var pageRequest = PageableRequestModel.of(pageIndex - 1, pageSize, sortType, sortField, filterDTO);
        return ResponseEntity.ok(domainService.getAllPaginatedDomains(pageRequest));
    }

    @Operation(summary = "Retrieve a domain by ID", description = "Retrieve a domain by ID")
    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<DomainDTO> getDomainById(@PathVariable Long id,
                                                   @RequestHeader(required = false) @SuppressWarnings("unused") String apiKey
    ) {
        return ResponseEntity.ok(domainService.findById(id));
    }

    @Operation(summary = "Create a new domain", description = "Create a new domain")
    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize(value = "hasRole('ADMIN')")
    public ResponseEntity<DomainDTO> create(@RequestBody DomainDTO domainDTO) {
        return ResponseEntity.ok(domainService.create(domainDTO));
    }

    @Operation(summary = "Update domain information", description = "Update domain information")
    @PutMapping(value = "{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize(value = "hasRole('ADMIN')")
    public ResponseEntity<Void> update(@PathVariable Long id, @RequestBody DomainDTO domainDTO) {
        domainService.update(id, domainDTO);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Import domains", description = "Import domains")
    @PostMapping(value = "/importation", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<String> importDomains(@RequestParam("file") MultipartFile file) {
        domainService.importDomains(file);
        return ResponseEntity.ok("Importação realizada com sucesso.");
    }

    @Operation(summary = "Import items", description = "Import items")
    @PostMapping(value = "/items/importation", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<String> importItems(@RequestParam("file") MultipartFile file) {
        itemService.importDomains(file);
        return ResponseEntity.ok("Importação realizada com sucesso.");
    }

    @Operation(summary = "Export domains", description = "Export domains")
    @GetMapping(value = "/exportation", produces = "text/csv")
    public void exportDomains(HttpServletResponse response) {
        response.setContentType("text/csv");
        response.setHeader("Content-Disposition", "attachment; filename=domains.csv");
        domainService.exportDomains(response);
    }

    @Operation(summary = "Retrieve items of a domain", description = "Retrieve items related to a domain")
    @GetMapping(value = "{id}/items", produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<PageableResponseModel<ItemDTO>> getItemsPaginatedByDomain(@PathVariable Long id,
                                                                   @RequestParam(defaultValue = "1") Integer pageIndex,
                                                                   @RequestParam(defaultValue = "10") Integer pageSize,
                                                                   @RequestParam(defaultValue = "id") String sortField,
                                                                   @RequestParam(defaultValue = "ASC") String sortType,
                                                                   @RequestHeader(required = false) @SuppressWarnings("unused") String apiKey,
                                                                   @ParameterObject ItemFilterDTO filterDTO) {
        final var pageRequest = PageableRequestModel.of(pageIndex - 1, pageSize, sortType, sortField, filterDTO);
        return ResponseEntity.ok(domainService.getItemsPaginatedByDomain(id, pageRequest));
    }

    @Operation(summary = "Retrieve items of a domain", description = "Retrieve items related to a domain")
    @GetMapping(value = "{id}/items/{itemId}", produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ItemDTO> getItemById(@PathVariable Long id, @PathVariable String itemId,
                                               @RequestHeader(required = false) @SuppressWarnings("unused") String apiKey
    ) {
        return ResponseEntity.ok(domainService.getItemById(id, itemId));
    }


    @Operation(summary = "Create a new item", description = "Create a new item")
    @PostMapping(value = "{id}/items", produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ItemDTO> createItem(@PathVariable Long id,
                                              @RequestHeader(required = false) @SuppressWarnings("unused") String apiKey,
                                              @RequestBody ItemDTO itemDTO) {
        return ResponseEntity.ok(domainService.createItem(id, itemDTO));
    }

    @Operation(summary = "Retrieve subitems of an item", description = "Retrieve subitems related to an item")
    @GetMapping(value = "{id}/items/{itemId}/subitems", produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<PageableResponseModel<ItemDTO>> getSubItemsPaginatedByItem(@PathVariable Long id, @PathVariable Long itemId,
                                                                    @RequestParam(defaultValue = "1") Integer pageIndex,
                                                                    @RequestParam(defaultValue = "10") Integer pageSize,
                                                                    @RequestParam(defaultValue = "id") String sortField,
                                                                    @RequestParam(defaultValue = "ASC") String sortType,
                                                                    @RequestHeader(required = false) @SuppressWarnings("unused") String apiKey,
                                                                    @ParameterObject ItemFilterDTO filterDTO
                                                                    ) {
        final var pageRequest = PageableRequestModel.of(pageIndex - 1, pageSize, sortType, sortField, filterDTO);
        return ResponseEntity.ok(domainService.getSubItemsPaginatedByDomain(id,itemId, pageRequest));
    }
}
