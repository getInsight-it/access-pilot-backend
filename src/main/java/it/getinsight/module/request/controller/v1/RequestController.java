package it.getinsight.module.request.controller.v1;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import it.getinsight.core.pagination.PageableRequestModel;
import it.getinsight.core.pagination.PageableResponseModel;
import it.getinsight.module.request.dto.RequestDTO;
import it.getinsight.module.request.service.RequestService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.util.List;

@RestController
@RequestMapping("/v1/requests")
@Tag(name = "Request", description = "Operations on requests.")
@RequiredArgsConstructor
public class RequestController {

    private final RequestService requestService;
    private final ObjectMapper objectMapper;

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(summary = "Creates a new request.",
        description = "Creates a new request with the given requestDTO"
    )
    public ResponseEntity<Void> create(@RequestPart(name = "attachments", required = false)
                                       List<MultipartFile> attachments,
                                       @RequestPart(name = "request")
                                       @Parameter(schema = @Schema(implementation = RequestDTO.class))
                                       String request) {
        try {
            final var requestDTO = objectMapper.readValue(request, RequestDTO.class);
            var uri = ServletUriComponentsBuilder.fromCurrentRequest().path(
                "/{id}").buildAndExpand(requestService.createRequest(requestDTO, attachments).id()).toUri();
            return ResponseEntity.created(uri).build();
        } catch (JsonProcessingException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "JSON mal formado", e);
        }
    }

    @PutMapping("/{id}")
    @Operation(summary = "Updates a request.",
        description = "Updates a request with the given id and requestDTO"
    )
    public ResponseEntity<Void> publishRequestUpdateEvent(@PathVariable Long id, @RequestBody String status) {
        requestService.publishRequestUpdateEvent(id, status);
        return ResponseEntity.status(HttpStatus.ACCEPTED).build();
    }

    @GetMapping(path = "/me/paginated-by-name", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(
        summary = "Retrieve the paginated list of requests associated with the authenticated user",
        description = "Retrieve a list of requests, with pagination, using a filter by name"
    )
    public ResponseEntity<PageableResponseModel<RequestDTO>> findAllByMePaginated(
        @RequestParam(defaultValue = "0") Integer pageIndex,
        @RequestParam(defaultValue = "10") Integer pageSize,
        @RequestParam(defaultValue = "id") String sortField,
        @RequestParam(defaultValue = "ASC") String sortType,
        @RequestParam(required = false) String status
    ) {
        final var pageRequest = PageableRequestModel.of(pageIndex, pageSize, sortType, sortField, status);
        return ResponseEntity.ok(requestService.getAllRequestsByStatusDynamicQuery(pageRequest));
    }

    @GetMapping(path = "/paginated-by-name", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(
        summary = "Retrieve the paginated list of requests associated with the authenticated user",
        description = "Retrieve a list of requests, with pagination, using a filter by name"
    )
    public ResponseEntity<PageableResponseModel<RequestDTO>> findAllPaginated(
        @RequestParam(defaultValue = "0") Integer pageIndex,
        @RequestParam(defaultValue = "10") Integer pageSize,
        @RequestParam(defaultValue = "id") String sortField,
        @RequestParam(defaultValue = "ASC") String sortType,
        @RequestParam String roles
    ) {
        final var pageRequest = PageableRequestModel.of(pageIndex, pageSize, sortType, sortField, roles);
        return ResponseEntity.ok(requestService.getAllRequestsByRolesDynamicQuery(pageRequest));
    }

}
