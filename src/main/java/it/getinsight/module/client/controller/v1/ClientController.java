package it.getinsight.module.client.controller.v1;


import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import it.getinsight.core.pagination.PageableRequestModel;
import it.getinsight.core.pagination.PageableResponseModel;
import it.getinsight.module.client.dto.ClientDTO;
import it.getinsight.module.client.dto.ClientFilterDTO;
import it.getinsight.module.client.dto.ClientFullResponseDTO;
import it.getinsight.module.client.dto.ClientStatusUpdateDTO;
import it.getinsight.module.client.service.ClientService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/v1/clients")
@Tag(name = "Client", description = "Operations on clients.")
@RequiredArgsConstructor
public class ClientController {

    private final ClientService clientService;

    @GetMapping(path = "/publishes", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(
        summary = "Retrieve the list of clients",
        description = "Retrieve all clients"
    )
    public ResponseEntity<List<ClientDTO>> getAllClients() {
        return ResponseEntity.ok(clientService.getAllClientsPublished());
    }

    @Operation(
        summary = "Retrieve the list of clients",
        description = "Retrieve all clients"
    )
    @GetMapping(path = "/me/associations", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<ClientDTO>> getAssociateClients(
        @RequestParam() Boolean attached
        ) {
        return ResponseEntity.ok(clientService.getAssociateClients(attached));
    }

    @Operation(
        summary = "Retrieve the paginated list of clients",
        description = "Retrieve a list of clients, with pagination, using a name filter"
    )
    @GetMapping(path = "/paginated", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<PageableResponseModel<ClientDTO>> getAllClientsPaginated(
        @Min(value = 1, message = "O índice da página deve ser no mínimo 1")
        @RequestParam(defaultValue = "1") Integer pageIndex,
        @RequestParam(defaultValue = "10") Integer pageSize,
        @RequestParam(defaultValue = "id") String sortField,
        @RequestParam(defaultValue = "ASC") String sortType,
        @ParameterObject ClientFilterDTO filter
    ) {
        final var pageRequest = PageableRequestModel.of(pageIndex - 1, pageSize, sortType, sortField, filter);
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
    public ResponseEntity<ClientFullResponseDTO> getClientById(@PathVariable Long id) {
        return ResponseEntity.ok(clientService.findById(id));
    }

    @Operation(
        summary = "Synchronize clients",
        description = "Synchronize clients"
    )
    @PostMapping(value = "/synchronous", produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize(value = "hasRole('ADMIN')")
    public ResponseEntity<ClientDTO> synchronous(@RequestBody List<String> clientIds) {
        clientService.synchronizationClients(clientIds);
        return ResponseEntity.noContent().build();
    }


    @Operation(
        summary = "Import attachments configurations",
        description = """
        Imports attachment configurations from a CSV file.

        The CSV must contain the following headers:
        - name (required)
        - description (free text, may contain commas or line breaks)
        - required (true/false)
        - allowedExtensions (semicolon-separated values, e.g., PDF;JPG;PNG)

        ⚠️ Notes:
        - Fields containing commas or line breaks must be enclosed in double quotes.
        - The first line must be the header row with the column names.

        ✅ Recommended export instructions:
        - **Excel**: Use "Save As" and select **CSV UTF-8 (Comma delimited)** format.
        - **Google Sheets**: Go to "File" → "Download" → "Comma-separated values (.csv, current sheet)".
        - **LibreOffice**: Use "Save As" → "Text CSV (.csv)" and check the UTF-8 encoding option. Use `"` as text delimiter and `,` as field separator.

        Example CSV row:
        "doc_passport","Passport, driver's license.","true","PDF,JPG"
        """
    )
    @PostMapping(value = "{id}/import-attachments-configurations", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @PreAuthorize(value = "hasRole('ADMIN')")
    public ResponseEntity<Void> importConfigurations(@PathVariable Long id, @RequestParam("file") MultipartFile file) {
        clientService.importAttachmentConfigurations(id,file);
        return ResponseEntity.ok().build();
    }

    @Operation(
        summary = "Export attachments configurations",
        description = "Exports attachment configurations in CSV format for a given client."
    )
    @GetMapping(value = "{id}/export-attachments-configurations", produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<byte[]> exportConfigurations(@PathVariable Long id) {
        byte[] csv = clientService.exportAttachmentConfigurations(id);

        return ResponseEntity.ok()
            .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=attachments_configurations.csv")
            .body(csv);
    }

    @Operation(
        summary = "Create a new client",
        description = "Create a new client"
    )
    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize(value = "hasRole('ADMIN')")
    public ResponseEntity<ClientDTO> create(@RequestBody @Valid ClientDTO clientDTO) {
        return ResponseEntity.ok(clientService.create(clientDTO));
    }

    @Operation(
        summary = "Update client management",
        description = "Update client management"
    )
    @PutMapping(value = "{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize(value = "hasRole('ADMIN')")
    public ResponseEntity<ClientDTO> synchronous(@PathVariable Long id, @RequestBody ClientDTO clientDTO) {
        clientService.updateManaged(id,clientDTO);
        return ResponseEntity.noContent().build();
    }

    @Operation(
        summary = "Update status",
        description = "Update status"
    )
    @PatchMapping(value = "{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize(value = "hasRole('ADMIN')")
    public ResponseEntity<ClientDTO> updateStatus(@PathVariable Long id, @RequestBody ClientStatusUpdateDTO statusUpdateDTO) {
        return ResponseEntity.ok(clientService.update(id, statusUpdateDTO.status()));
    }


}
