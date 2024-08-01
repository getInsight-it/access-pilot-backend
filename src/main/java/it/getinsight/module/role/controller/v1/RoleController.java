package it.getinsight.module.role.controller.v1;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import it.getinsight.core.pagination.PageableRequestModel;
import it.getinsight.core.pagination.PageableResponseModel;
import it.getinsight.module.role.dto.RoleDTO;
import it.getinsight.module.role.service.RoleService;
import it.getinsight.module.user.dto.UserDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/v1/roles")
@Tag(name = "Role", description = "Operations on roles.")
@RequiredArgsConstructor
public class RoleController {

    private final RoleService roleService;

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(
        summary = "Retrieve the list of roles",
        description = "Retrieve all roles",
        responses = {
            @ApiResponse(responseCode = "200", content = {
                @Content(schema = @Schema(implementation = RoleDTO[].class))
            })
        }
    )
    public ResponseEntity<List<RoleDTO>> getAllRoles() {
        return ResponseEntity.ok(roleService.getAllRolesDynamicQuery());
    }

    @Operation(
        summary = "Retrieve the paginated list of roles",
        description = "Retrieve a list of roles, with pagination, using a filter by name",
        responses = {
            @ApiResponse(responseCode = "200", content = {
                @Content(schema = @Schema(implementation = PageableResponseModel.class))
            })
        }
    )
    @Parameter(name = "name", description = "Filter by firstname", in = ParameterIn.QUERY, schema = @Schema(type = "string"))
    @Parameter(name = "filter", hidden = true)
    @GetMapping(path = "/paginated", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<PageableResponseModel<RoleDTO>> getAllPaginated(
        @RequestParam(defaultValue = "0") Integer pageIndex,
        @RequestParam(defaultValue = "10") Integer pageSize,
        @RequestParam(defaultValue = "name") String sortField,
        @RequestParam(defaultValue = "ASC") String sortType,
        RoleDTO filter
    ) {
        final var pageRequest = PageableRequestModel.of(pageIndex, pageSize, sortType, sortField, filter);
        return ResponseEntity.ok(roleService.getAllRolesPageable(pageRequest));
    }

    @Operation(
        summary = "Retrieve the paginated list of roles",
        description = "Retrieve a list of roles, with pagination, using a filter by name",
        responses = {
            @ApiResponse(responseCode = "200", content = {
                @Content(schema = @Schema(implementation = PageableResponseModel.class))
            })
        }
    )
    @GetMapping(path = "/paginated-by-name", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<PageableResponseModel<RoleDTO>> getAllPaginatedByName(
        @RequestParam(defaultValue = "0") Integer pageIndex,
        @RequestParam(defaultValue = "10") Integer pageSize,
        @RequestParam(defaultValue = "name") String sortField,
        @RequestParam(defaultValue = "ASC") String sortType,
        @RequestParam(required = false) String filter
    ) {
        final var pageRequest = PageableRequestModel.of(pageIndex, pageSize, sortType, sortField, filter);
        return ResponseEntity.ok(roleService.getAllRolesPageableByName(pageRequest));
    }

    @Operation(
        summary = "Retrieve a role by ID",
        description = "Retrieve a role by ID",
        responses = {
            @ApiResponse(responseCode = "200", content = {
                @Content(schema = @Schema(implementation = RoleDTO.class))
            })
        }
    )
    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<RoleDTO> getById(@PathVariable Long id) {
        return ResponseEntity.ok(roleService.getById(id));
    }

    @Operation(
        summary = "Retrieve the list of users by role ID",
        description = "Retrieve the list of users by role ID",
        responses = {
            @ApiResponse(responseCode = "200", content = {
                @Content(schema = @Schema(implementation = RoleDTO.class))
            })
        }
    )
    @GetMapping(value = "/{id}/approves", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<UserDTO>> getOrImportApprovesByRoleId(@PathVariable Long id) {
        return ResponseEntity.ok(roleService.getOrImportApprovesByRoleId(id));
    }

    @PostMapping("/synchronize")
    @Operation(summary = "Synchronize roles with IDP", description = "Synchronize roles with IDP")
    public ResponseEntity<Void> synchronizeRoles(@RequestBody List<String> clientIds) {
        roleService.synchronizeRoles(clientIds);
        return ResponseEntity.noContent().build();
    }

}
