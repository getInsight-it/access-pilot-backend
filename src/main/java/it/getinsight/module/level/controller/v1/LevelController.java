package it.getinsight.module.level.controller.v1;


import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import it.getinsight.core.pagination.PageableRequestModel;
import it.getinsight.core.pagination.PageableResponseModel;
import it.getinsight.module.level.dto.LevelDTO;
import it.getinsight.module.level.dto.LevelFilterDTO;
import it.getinsight.module.level.dto.ItemDTO;
import it.getinsight.module.level.dto.ItemFilterDTO;
import it.getinsight.module.level.service.LevelService;
import it.getinsight.module.level.service.ItemService;
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

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/v1/levels")
@Tag(name = "Level", description = "Operations on levels.")
@RequiredArgsConstructor
@PermitAll
public class LevelController {

    private final LevelService levelService;
    private final ItemService itemService;


    @Operation(summary = "Retrieve the list of levels", description = "Retrieve all levels")
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<PageableResponseModel<LevelDTO>> getPaginatedAllLevels(
        @RequestParam(defaultValue = "1") Integer pageIndex,
        @RequestParam(defaultValue = "10") Integer pageSize,
        @RequestParam(defaultValue = "id") String sortField,
        @RequestParam(defaultValue = "ASC") String sortType,
        @ParameterObject LevelFilterDTO filterDTO) {
        final var pageRequest = PageableRequestModel.of(pageIndex - 1, pageSize, sortType, sortField, filterDTO);
        return ResponseEntity.ok(levelService.getAllPaginatedLevels(pageRequest));
    }

    @Operation(summary = "Retrieve a level by ID", description = "Retrieve a level by ID")
    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<LevelDTO> getLevelById(@PathVariable Long id
    ) {
        return ResponseEntity.ok(levelService.findById(id));
    }

    @GetMapping("/{id}/hierarchy")
    public ResponseEntity<List<LevelDTO>> getHierarchy(@PathVariable Long id) {
        return ResponseEntity.ok(levelService.getHierarchy(id));
    }

    @Operation(summary = "Create a new level", description = "Create a new level")
    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize(value = "hasRole('ADMIN')")
    public ResponseEntity<LevelDTO> create(@RequestBody LevelDTO levelDTO) {
        return ResponseEntity.ok(levelService.create(levelDTO));
    }

    @Operation(summary = "Update level information", description = "Update level information")
    @PutMapping(value = "{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize(value = "hasRole('ADMIN')")
    public ResponseEntity<Void> update(@PathVariable Long id, @RequestBody LevelDTO levelDTO) {
        levelService.update(id, levelDTO);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Import levels", description = "Import levels")
    @PostMapping(value = "/importation", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<String> importLevels(@RequestParam("file") MultipartFile file) {
        levelService.importLevels(file);
        return ResponseEntity.ok("Importação realizada com sucesso.");
    }

    @Operation(summary = "Import items", description = "Import items")
    @PostMapping(value = "/items/importation", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<String> importItems(@RequestParam("file") MultipartFile file) {
        itemService.importLevels(file);
        return ResponseEntity.ok("Importação realizada com sucesso.");
    }

    @Operation(summary = "Export levels", description = "Export levels")
    @GetMapping(value = "/exportation", produces = "text/csv")
    public void exportLevels(HttpServletResponse response) {
        response.setContentType("text/csv");
        response.setHeader("Content-Disposition", "attachment; filename=levels.csv");
        levelService.exportLevels(response);
    }

    @Operation(summary = "Retrieve items of a level", description = "Retrieve items related to a level")
    @GetMapping(value = "{id}/items", produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<PageableResponseModel<ItemDTO>> getItemsPaginatedByLevel(@PathVariable Long id,
                                                                   @RequestParam(defaultValue = "1") Integer pageIndex,
                                                                   @RequestParam(defaultValue = "10") Integer pageSize,
                                                                   @RequestParam(defaultValue = "id") String sortField,
                                                                   @RequestParam(defaultValue = "ASC") String sortType,
                                                                   @ParameterObject ItemFilterDTO filterDTO) {
        final var pageRequest = PageableRequestModel.of(pageIndex - 1, pageSize, sortType, sortField, filterDTO);
        return ResponseEntity.ok(levelService.getItemsPaginatedByLevel(id, pageRequest));
    }

    @Operation(summary = "Retrieve items of a level", description = "Retrieve items related to a level")
    @GetMapping(value = "{id}/items/{itemId}", produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ItemDTO> getItemById(@PathVariable Long id, @PathVariable String itemId
    ) {
        return ResponseEntity.ok(levelService.getItemById(id, itemId));
    }


    @Operation(summary = "Create a new item", description = "Create a new item")
    @PostMapping(value = "{id}/items", produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ItemDTO> createItem(@PathVariable Long id,
                                              @RequestBody ItemDTO itemDTO) {
        return ResponseEntity.ok(levelService.createItem(id, itemDTO));
    }

    @Operation(summary = "Retrieve subitems of an item", description = "Retrieve subitems related to an item")
    @GetMapping(value = "{id}/items/{itemId}/subitems", produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<PageableResponseModel<ItemDTO>> getSubItemsPaginatedByItem(@PathVariable Long id, @PathVariable Long itemId,
                                                                    @RequestParam(defaultValue = "1") Integer pageIndex,
                                                                    @RequestParam(defaultValue = "10") Integer pageSize,
                                                                    @RequestParam(defaultValue = "id") String sortField,
                                                                    @RequestParam(defaultValue = "ASC") String sortType,
                                                                    @ParameterObject ItemFilterDTO filterDTO
                                                                    ) {
        final var pageRequest = PageableRequestModel.of(pageIndex - 1, pageSize, sortType, sortField, filterDTO);
        return ResponseEntity.ok(levelService.getSubItemsPaginatedByLevel(id,itemId, pageRequest));
    }
}
