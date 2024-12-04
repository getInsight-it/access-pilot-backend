package it.getinsight.module.storage.controller.v1;


import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import it.getinsight.core.pagination.PageableRequestModel;
import it.getinsight.core.pagination.PageableResponseModel;
import it.getinsight.module.storage.dto.StorageFileDTO;
import it.getinsight.module.storage.dto.StorageFileFilterDTO;
import it.getinsight.module.storage.service.StorageFileService;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/v1/storages")
@Tag(name = "Storage", description = "Operations on storage files.")
@RequiredArgsConstructor
public class StorageFileController {

    private final StorageFileService storageFileService;

    @GetMapping("/{id}")
    @Operation(
        summary = "Retrieve a file by its ID",
        description = "Retrieve a file by its ID",
        responses = {
            @ApiResponse(responseCode = "200", content = {
                @Content(schema = @Schema(implementation = StorageFileDTO.class))
            })
        }
    )
    public ResponseEntity<StorageFileDTO> getFileById(@PathVariable Long id) {
        StorageFileDTO storageFile = storageFileService.findById(id);
        return ResponseEntity.ok(storageFile);
    }

    @DeleteMapping("/{id}")
    @Operation(
        summary = "Delete a file by its ID",
        description = "Delete a file by its ID",
        responses = {
            @ApiResponse(responseCode = "204")
        }
    )
    public ResponseEntity<Void> deleteFile(@PathVariable Long id) {
        storageFileService.delete(id);
        return ResponseEntity.noContent().build();
    }


    @GetMapping(value = "/paginated", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(
        summary = "retrieve all files paginated",
        description = "Retrieve all files paginated",
        responses = {
            @ApiResponse(responseCode = "200")
        }
    )
    public ResponseEntity<PageableResponseModel<StorageFileDTO>> getFilesPaginated(  @RequestParam(defaultValue = "1") Integer pageIndex,
                                                                                     @RequestParam(defaultValue = "10") Integer pageSize,
                                                                                     @RequestParam(defaultValue = "id") String sortField,
                                                                                     @RequestParam(defaultValue = "ASC") String sortType,
                                                                                     @ParameterObject StorageFileFilterDTO filter
    ) {
        return ResponseEntity.ok(storageFileService.getFilesPaginated(PageableRequestModel.of(pageIndex - 1, pageSize, sortType, sortField, filter)));
    }

    @GetMapping("/name/{name}")
    @Operation(
        summary = "Retrieve a file by its name",
        description = "Retrieve a file by its name",
        responses = {
            @ApiResponse(responseCode = "200", content = {
                @Content(schema = @Schema(implementation = StorageFileDTO.class))
            })
        }
    )
    public ResponseEntity<StorageFileDTO> getFileByName(@PathVariable String name) {
        StorageFileDTO storageFile = storageFileService.findByName(name);
        return ResponseEntity.ok(storageFile);
    }

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(
        summary = "Upload a file",
        description = "Upload a file",
        responses = {
            @ApiResponse(responseCode = "201")
        }
    )
    public ResponseEntity<StorageFileDTO> uploadFile(
                                                @RequestPart(name = "attachments")
                                                @Parameter(array = @ArraySchema(schema = @Schema(implementation = MultipartFile.class)))
                                                List<MultipartFile> attachments,
                                                @RequestParam String bucket,
                                                @RequestParam Boolean isPublic,
                                                @RequestParam Boolean ephemeral,
                                                @RequestParam(required = false) UUID ownerId) {
        storageFileService.save(attachments, bucket, isPublic, ephemeral, ownerId);
        return ResponseEntity.status(201).build();
    }

    @GetMapping("/download/{id}")
    @Operation(
        summary = "Download a file by its ID",
        description = "Download a file by its ID",
        responses = {
            @ApiResponse(responseCode = "200")
        }
    )
    public ResponseEntity<InputStreamResource> downloadFile(@PathVariable Long id,
                                                            @RequestParam(defaultValue = "true") Boolean registerDownload) {
        var storageFile = storageFileService.findById(id);
        var resource = storageFileService.download(storageFile.id(), registerDownload);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + storageFile.originalFilename() + "\"")
                .contentType(MediaType.parseMediaType(storageFile.mimeType()))
                .body(resource);
    }
}
