package it.getinsight.module.client.controller.v1;

import io.minio.MinioClient;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import it.getinsight.core.pagination.PageableRequestModel;
import it.getinsight.core.pagination.PageableResponseModel;
import it.getinsight.module.client.dto.ClientDTO;
import it.getinsight.module.client.service.ClientService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/v1/clients")
@Tag(name = "Client", description = "Operations on clients.")
@RequiredArgsConstructor
public class ClientController {

    private final ClientService clientService;

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(
        summary = "Retrieve the list of clients",
        description = "Retrieve all clients",
        responses = {
            @ApiResponse(responseCode = "200", content = {
                @Content(schema = @Schema(implementation = ClientDTO[].class))
            })
        }
    )
    public ResponseEntity<List<ClientDTO>> getAllClients() {
        return ResponseEntity.ok(clientService.getAllClientsDynamicQuery());
    }

    @Operation(
        summary = "Retrieve the paginated list of clients",
        description = "Retrieve a list of clients, with pagination, using a name filter",
        responses = {
            @ApiResponse(responseCode = "200", content = {
                @Content(schema = @Schema(implementation = PageableResponseModel.class))
            })
        }
    )
    @Parameter(name = "name", description = "Filter by name", in = ParameterIn.QUERY, schema = @Schema(type = "string"))
    @Parameter(name = "filter", hidden = true)
    @GetMapping(path = "/paginated", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<PageableResponseModel<ClientDTO>> getAllClientsPaginated(
        @RequestParam(defaultValue = "0") Integer pageIndex,
        @RequestParam(defaultValue = "10") Integer pageSize,
        @RequestParam(defaultValue = "name") String sortField,
        @RequestParam(defaultValue = "ASC") String sortType,
        ClientDTO filter
    ) {
        final var pageRequest = PageableRequestModel.of(pageIndex, pageSize, sortType, sortField, filter);
        return ResponseEntity.ok(clientService.getAllClientsPageable(pageRequest));
    }

    @Operation(
        summary = "Retrieve the paginated list of clients",
        description = "Retrieve a list of clients, with pagination, using a name filter",
        responses = {
            @ApiResponse(responseCode = "200", content = {
                @Content(schema = @Schema(implementation = PageableResponseModel.class))
            })
        }
    )
    @GetMapping(path = "/paginated-by-name", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<PageableResponseModel<ClientDTO>> getAllClientsPaginatedByName(
        @RequestParam(defaultValue = "0") Integer pageIndex,
        @RequestParam(defaultValue = "10") Integer pageSize,
        @RequestParam(defaultValue = "name") String sortField,
        @RequestParam(defaultValue = "ASC") String sortType,
        @RequestParam(required = false) String filter
    ) {
        final var pageRequest = PageableRequestModel.of(pageIndex, pageSize, sortType, sortField, filter);
        return ResponseEntity.ok(clientService.getAllClientsPageableByName(pageRequest));
    }

    @Operation(
        summary = "Retrieve a client by ID",
        description = "Retrieve a client by ID",
        responses = {
            @ApiResponse(responseCode = "200", content = {
                @Content(schema = @Schema(implementation = ClientDTO.class))
            })
        }
    )
    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ClientDTO> getClientById(@PathVariable Long id) {
        return ResponseEntity.ok(clientService.findById(id));
    }

    @Operation(
        summary = "Synchronize clients",
        description = "Synchronize clients",
        responses = {
            @ApiResponse(responseCode = "200", content = {
                @Content(schema = @Schema(implementation = ClientDTO.class))
            })
        }
    )
    @PostMapping(value = "/synchronize", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ClientDTO> synchronize(@RequestBody List<Long> clientIds) {
        clientService.synchronizationClients(clientIds);
        return ResponseEntity.noContent().build();
    }

    @Operation(
        summary = "Create a new client",
        description = "Create a new client",
        responses = {
            @ApiResponse(responseCode = "200", content = {
                @Content(schema = @Schema(implementation = ClientDTO.class))
            })
        }
    )
    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ClientDTO> create(@RequestBody ClientDTO clientDTO) {
        return ResponseEntity.ok(clientService.create(clientDTO));
    }

    @Operation(
        summary = "Update client management",
        description = "Update client management",
        responses = {
            @ApiResponse(responseCode = "200", content = {
                @Content(schema = @Schema(implementation = ClientDTO.class))
            })
        }
    )
    @PutMapping(value = "{id}/management", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ClientDTO> synchronize(@PathVariable Long id, @RequestBody ClientDTO clientDTO) {
        clientService.updateManaged(id,clientDTO.managed());
        return ResponseEntity.noContent().build();
    }
}
