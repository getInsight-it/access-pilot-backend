package it.getinsight.module.request.controller.v1;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import it.getinsight.core.pagination.PageableRequestModel;
import it.getinsight.core.pagination.PageableResponseModel;
import it.getinsight.module.request.dto.RequestCreateDTO;
import it.getinsight.module.request.dto.RequestDTO;
import it.getinsight.module.request.dto.RequestFilterDTO;
import it.getinsight.module.request.dto.RequestUpdateDTO;
import it.getinsight.module.request.service.RequestService;
import it.getinsight.module.role.dto.RoleDTO;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
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
                                       @Parameter(schema = @Schema(implementation = RequestCreateDTO.class))
                                       String request) {
        try {
            final var requestCreateDTO = objectMapper.readValue(request, RequestCreateDTO.class);
            RequestDTO requestDTO = RequestDTO.builder().description(requestCreateDTO.description()).role(RoleDTO.builder().id(requestCreateDTO.roleId()).build()).build();
            var uri = ServletUriComponentsBuilder.fromCurrentRequest().path(
                "/{id}").buildAndExpand(requestService.createRequest(requestDTO, attachments).id()).toUri();
            return ResponseEntity.created(uri).build();
        } catch (JsonProcessingException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "JSON mal formado", e);
        }
    }

    @PutMapping(path = "/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(summary = "Updates a request.",
        description = "Updates a request with the given id and requestDTO"
    )
    public ResponseEntity<Void> publishRequestUpdateEvent(@PathVariable Long id,
                                                          @RequestPart(name = "attachments", required = false) List<MultipartFile> attachments,
                                                          @RequestPart(name = "request")
                                                              @Parameter(schema = @Schema(implementation = RequestUpdateDTO.class))
                                                              String request) {

        try {
            final var requestUpdateDTO = objectMapper.readValue(request, RequestUpdateDTO.class);
            requestService.publishRequestUpdateEvent(id, requestUpdateDTO);
            return ResponseEntity.status(HttpStatus.ACCEPTED).build();
        } catch (JsonProcessingException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "JSON mal formado", e);
        }
    }

    @GetMapping(path = "/me/paginated", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(
        summary = "Retrieve the paginated list of requests associated with the authenticated user",
        description = "Retrieve a list of requests, with pagination, using a filter by name"
    )
    public ResponseEntity<PageableResponseModel<RequestDTO>> findAllByMePaginated(
        @RequestParam(defaultValue = "1") Integer pageIndex,
        @RequestParam(defaultValue = "10") Integer pageSize,
        @RequestParam(defaultValue = "id") String sortField,
        @RequestParam(defaultValue = "ASC") String sortType
    ) {
        final var pageRequest = PageableRequestModel.of(pageIndex, pageSize, sortType, sortField, "");
        return ResponseEntity.ok(requestService.getAllRequestsMine(pageRequest));
    }

    @GetMapping(path = "/paginated-by-roles", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(
        summary = "Retrieve the paginated list of requests associated with the authenticated user",
        description = "Retrieve a list of requests, with pagination, using a filter by name"
    )
    public ResponseEntity<PageableResponseModel<RequestDTO>> findAllPaginatedByRole(
        @RequestParam(defaultValue = "1") Integer pageIndex,
        @RequestParam(defaultValue = "10") Integer pageSize,
        @RequestParam(defaultValue = "id") String sortField,
        @RequestParam(defaultValue = "ASC") String sortType,
        @RequestParam String roles
    ) {
        final var pageRequest = PageableRequestModel.of(pageIndex, pageSize, sortType, sortField, roles);
        return ResponseEntity.ok(requestService.getAllRequestsByRolesDynamicQuery(pageRequest));
    }

    @GetMapping(path = "/paginated", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(
        summary = "Retrieve the paginated list of requests associated with the authenticated user",
        description = "Retrieve a list of requests, with pagination, using a filter by name"
    )
    public ResponseEntity<PageableResponseModel<RequestDTO>> findAllPaginated(
        @RequestParam(defaultValue = "1") Integer pageIndex,
        @RequestParam(defaultValue = "10") Integer pageSize,
        @RequestParam(defaultValue = "id") String sortField,
        @RequestParam(defaultValue = "ASC") String sortType,
        @ParameterObject RequestFilterDTO filter
    ) {
        final var pageRequest = PageableRequestModel.of(pageIndex - 1, pageSize, sortType, sortField, filter);
        return ResponseEntity.ok(requestService.getAllRequests(pageRequest));
    }
}
