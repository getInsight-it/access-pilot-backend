package it.getinsight.module.role.controller.v1;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import it.getinsight.core.pagination.PageableRequestModel;
import it.getinsight.core.pagination.PageableResponseModel;
import it.getinsight.module.role.dto.RoleDTO;
import it.getinsight.module.role.dto.RoleFilterDTO;
import it.getinsight.module.role.service.RoleService;
import it.getinsight.module.user.dto.UserDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

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
        description = "Retrieve all roles"
    )
    @Parameter(name = "clientId", description = "Filter by client id", in = ParameterIn.QUERY, schema = @Schema(type = "string"))
    public ResponseEntity<List<RoleDTO>> getAllRoles(@RequestParam(required = false) String clientId) {
        return ResponseEntity.ok(roleService.getAllRoles(clientId));
    }

    @Operation(
        summary = "Retrieve the paginated list of roles",
        description = "Retrieve a list of roles, with pagination, using a filter by name"
    )
    @GetMapping(path = "/paginated", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<PageableResponseModel<RoleDTO>> getAllPaginated(
        @RequestParam(defaultValue = "1") Integer pageIndex,
        @RequestParam(defaultValue = "10") Integer pageSize,
        @RequestParam(defaultValue = "id") String sortField,
        @RequestParam(defaultValue = "ASC") String sortType,
        @ParameterObject RoleFilterDTO filter
    ) {
        final var pageRequest = PageableRequestModel.of(pageIndex -1, pageSize, sortType, sortField, filter);
        return ResponseEntity.ok(roleService.getAllRolesPageable(pageRequest));
    }

    @Operation(
        summary = "Retrieve the paginated list of roles",
        description = "Retrieve a list of roles, with pagination, using a filter by name"
    )
    @GetMapping(path = "/paginated-by-name", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<PageableResponseModel<RoleDTO>> getAllPaginatedByName(
        @RequestParam(defaultValue = "1") Integer pageIndex,
        @RequestParam(defaultValue = "10") Integer pageSize,
        @RequestParam(defaultValue = "id") String sortField,
        @RequestParam(defaultValue = "ASC") String sortType,
        @RequestParam(required = false) String filter
    ) {
        final var pageRequest = PageableRequestModel.of(pageIndex, pageSize, sortType, sortField, filter);
        return ResponseEntity.ok(roleService.getAllRolesPageableByName(pageRequest));
    }

    @Operation(
        summary = "Retrieve a roleParent by ID",
        description = "Retrieve a roleParent by ID"
    )
    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<RoleDTO> getById(@PathVariable Long id) {
        return ResponseEntity.ok(roleService.getById(id));
    }

    @Operation(
        summary = "Retrieve the list of users by roleParent ID",
        description = "Retrieve the list of users by roleParent ID"
    )
    @GetMapping(value = "/{id}/approves", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<UserDTO>> getOrImportApprovesByRoleId(@PathVariable Long id) {
        return ResponseEntity.ok(roleService.getOrImportApprovesByRoleId(id));
    }

    @PostMapping("/synchronous")
    @Operation(summary = "Synchronize roles with IDP", description = "Synchronize roles with IDP")
    @PreAuthorize(value = "hasRole('ADMIN')")
    public ResponseEntity<Void> synchronizeRoles(@RequestBody List<String> clientIds) {
        roleService.synchronizeRoles(clientIds);
        return ResponseEntity.noContent().build();
    }

    @PutMapping
    @Operation(
        summary = "Update roles",
        description = "Update roles",
        responses = {
            @ApiResponse(responseCode = "204")
        }
    )
    @PreAuthorize(value = "hasRole('ADMIN')")
    public ResponseEntity<Void> updateRoles(@RequestBody List<RoleDTO> roles) {
        roleService.updateRoles(roles);
        return ResponseEntity.noContent().build();
    }

    @PostMapping
    @Operation(
        summary = "Create a new roleParent",
        description = "Create a new roleParent"
    )
    @PreAuthorize(value = "hasRole('ADMIN')")
    public ResponseEntity<RoleDTO> create(@RequestBody RoleDTO roleDTO) {
        final var roleSavedDTO = roleService.createRole(roleDTO);
        var location = ServletUriComponentsBuilder.fromCurrentRequest().path(
            "/{id}").buildAndExpand(roleSavedDTO.name()).toUri();
        return ResponseEntity.created(location).body(roleSavedDTO);
    }

    @PutMapping("/{id}")
    @Operation(
        summary = "Update a role",
        description = "Update a role"
    )
    @PreAuthorize(value = "hasRole('ADMIN')")
    public ResponseEntity<RoleDTO> update(@PathVariable Long id, @RequestBody RoleDTO roleDTO) {
        return ResponseEntity.ok(roleService.update(id, roleDTO));
    }

    @DeleteMapping("/{id}")
    @Operation(
        summary = "Delete a role",
        description = "Delete a role"
    )
    @PreAuthorize(value = "hasRole('ADMIN')")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        roleService.delete(id);
        return ResponseEntity.noContent().build();
    }

}
