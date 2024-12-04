package it.getinsight.module.client.controller.v1;


import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import it.getinsight.core.pagination.PageableRequestModel;
import it.getinsight.core.pagination.PageableResponseModel;
import it.getinsight.module.client.dto.ClientDTO;
import it.getinsight.module.client.dto.ClientStatusUpdateDTO;
import it.getinsight.module.client.service.ClientService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springdoc.core.annotations.ParameterObject;
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
        description = "Retrieve all clients"
    )
    public ResponseEntity<List<ClientDTO>> getAllClients() {
        return ResponseEntity.ok(clientService.getAllClientsDynamicQuery());
    }

    @Operation(
        summary = "Retrieve the paginated list of clients",
        description = "Retrieve a list of clients, with pagination, using a name filter"
    )
    @GetMapping(path = "/paginated", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<PageableResponseModel<ClientDTO>> getAllClientsPaginated(
        @RequestParam(defaultValue = "1") Integer pageIndex,
        @RequestParam(defaultValue = "10") Integer pageSize,
        @RequestParam(defaultValue = "id") String sortField,
        @RequestParam(defaultValue = "ASC") String sortType,
        @ParameterObject ClientDTO filter
    ) {
        final var pageRequest = PageableRequestModel.of(pageIndex, pageSize, sortType, sortField, filter);
        return ResponseEntity.ok(clientService.getAllClientsPageable(pageRequest));
    }

    @Operation(
        summary = "Retrieve a client by client ID",
        description = "Retrieve a client by client ID"
    )
    @GetMapping(path = "/client-id/{clientId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ClientDTO> getByClientId(@PathVariable String clientId) {
        return ResponseEntity.ok(clientService.findByClientId(clientId));
    }

    @Operation(
        summary = "Retrieve a client by ID",
        description = "Retrieve a client by ID"
    )
    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ClientDTO> getClientById(@PathVariable Long id) {
        return ResponseEntity.ok(clientService.findById(id));
    }

    @Operation(
        summary = "Synchronize clients",
        description = "Synchronize clients"
    )
    @PostMapping(value = "/synchronous", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ClientDTO> synchronous(@RequestBody List<String> clientIds) {
        clientService.synchronizationClients(clientIds);
        return ResponseEntity.noContent().build();
    }

    @Operation(
        summary = "Create a new client",
        description = "Create a new client"
    )
    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ClientDTO> create(@RequestBody ClientDTO clientDTO) {
        return ResponseEntity.ok(clientService.create(clientDTO));
    }

    @Operation(
        summary = "Update client management",
        description = "Update client management"
    )
    @PutMapping(value = "{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ClientDTO> synchronous(@PathVariable Long id, @RequestBody ClientDTO clientDTO) {
        clientService.updateManaged(id,clientDTO);
        return ResponseEntity.noContent().build();
    }

    @Operation(
        summary = "Update status",
        description = "Update status"
    )
    @PatchMapping(value = "{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ClientDTO> updateStatus(@PathVariable Long id, @RequestBody ClientStatusUpdateDTO statusUpdateDTO) {
        return ResponseEntity.ok(clientService.update(id, statusUpdateDTO.status()));
    }


}
