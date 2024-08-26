package it.getinsight.module.request.controller.v1;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
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
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

@RestController
@RequestMapping("/v1/requests")
@Tag(name = "Request", description = "Operations on requests.")
@RequiredArgsConstructor
public class RequestController {

    private final RequestService requestService;

    @PostMapping
    @Operation(summary = "Creates a new request.",
        description = "Creates a new request with the given requestDTO"
    )
    public ResponseEntity<Void> create(@RequestBody RequestDTO requestDTO){
        var uri = ServletUriComponentsBuilder.fromCurrentRequest().path(
            "/{id}").buildAndExpand(requestService.createRequest(requestDTO).id()).toUri();
        return ResponseEntity.created(uri).build();
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
    public ResponseEntity<PageableResponseModel<RequestDTO>> findAllPaginated(
        @RequestParam(defaultValue = "0") Integer pageIndex,
        @RequestParam(defaultValue = "10") Integer pageSize,
        @RequestParam(defaultValue = "firstname") String sortField,
        @RequestParam(defaultValue = "ASC") String sortType,
        @RequestParam(required = false) String filter
    ) {
        final var pageRequest = PageableRequestModel.of(pageIndex, pageSize, sortType, sortField, filter);
        return ResponseEntity.ok(requestService.getAllRequestsByStatusDynamicQuery(pageRequest));
    }


}
