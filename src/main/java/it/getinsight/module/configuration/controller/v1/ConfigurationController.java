package it.getinsight.module.configuration.controller.v1;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import it.getinsight.core.pagination.PageableRequestModel;
import it.getinsight.core.pagination.PageableResponseModel;
import it.getinsight.module.configuration.dto.ConfigurationDTO;
import it.getinsight.module.configuration.dto.ConfigurationFilterDTO;
import it.getinsight.module.configuration.service.ConfigurationService;
import it.getinsight.module.email.dto.EmailDTO;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.util.List;

@RestController
@RequestMapping("/v1/configurations")
@Tag(name = "Configurations", description = "Operations on configurations.")
@RequiredArgsConstructor
public class ConfigurationController {

    private final ConfigurationService configurationService;

    @PostMapping
    @Operation(summary = "Creates a new configuration.", responses = {
        @ApiResponse(responseCode = "201", description = "Configuration successfully created."),
        @ApiResponse(responseCode = "400", description = "Invalid request."),
        @ApiResponse(responseCode = "401", description = "Unauthorized."),
        @ApiResponse(responseCode = "403", description = "Forbidden."),
        @ApiResponse(responseCode = "500", description = "Internal server error.")
    })
    public ResponseEntity<Void> create(@Valid @RequestBody ConfigurationDTO configurationDTO) {
        var configuration = configurationService.create(configurationDTO);
        var uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(configuration.id()).toUri();
        return ResponseEntity.created(uri).build();
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Removes a configuration.", responses = {
        @ApiResponse(responseCode = "204", description = "Configuration successfully removed."),
        @ApiResponse(responseCode = "400", description = "Invalid request."),
        @ApiResponse(responseCode = "401", description = "Unauthorized."),
        @ApiResponse(responseCode = "403", description = "Forbidden."),
        @ApiResponse(responseCode = "404", description = "Configuration not found."),
        @ApiResponse(responseCode = "500", description = "Internal server error.")
    })
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        configurationService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}")
    @Operation(summary = "Fetches a configuration.", responses = {
        @ApiResponse(responseCode = "200", description = "Configuration found.", content = @Content(schema = @Schema(implementation = ConfigurationDTO.class))),
        @ApiResponse(responseCode = "400", description = "Invalid request."),
        @ApiResponse(responseCode = "401", description = "Unauthorized."),
        @ApiResponse(responseCode = "403", description = "Forbidden."),
        @ApiResponse(responseCode = "404", description = "Configuration not found."),
        @ApiResponse(responseCode = "500", description = "Internal server error.")
    })
    public ResponseEntity<ConfigurationDTO> findById(@PathVariable Long id) {
        var configurationDTO = configurationService.findById(id);
        return ResponseEntity.ok(configurationDTO);
    }

    @GetMapping
    @Operation(summary = "Fetches all configurations.", responses = {
        @ApiResponse(responseCode = "200", description = "Configurations found."),
        @ApiResponse(responseCode = "400", description = "Invalid request."),
        @ApiResponse(responseCode = "401", description = "Unauthorized."),
        @ApiResponse(responseCode = "403", description = "Forbidden."),
        @ApiResponse(responseCode = "404", description = "Configuration not found."),
        @ApiResponse(responseCode = "500", description = "Internal server error.")
    })
    public ResponseEntity<PageableResponseModel<ConfigurationDTO>> findConfigurations(@RequestParam(defaultValue = "1") Integer pageIndex,
                                                                            @RequestParam(defaultValue = "10") Integer pageSize,
                                                                            @RequestParam(defaultValue = "id") String sortField,
                                                                            @RequestParam(defaultValue = "ASC") String sortType,
                                                                          @ParameterObject ConfigurationFilterDTO filter
    ) {
        final var pageRequest = PageableRequestModel.of(pageIndex - 1, pageSize, sortType, sortField, filter);
        return ResponseEntity.ok(configurationService.getAllConfigurations(pageRequest));
    }

}
