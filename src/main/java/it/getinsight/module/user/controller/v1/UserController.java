package it.getinsight.module.user.controller.v1;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import it.getinsight.core.pagination.PageableRequestModel;
import it.getinsight.core.pagination.PageableResponseModel;
import it.getinsight.module.request.service.RequestService;
import it.getinsight.module.user.dto.UserDTO;
import it.getinsight.module.user.service.UserService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/v1/users")
@Tag(name = "User", description = "Operations related to users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final RequestService requestService;

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(
        summary = "Retrieve the list of users",
        description = "Retrieve all users"
    )
    public ResponseEntity<List<UserDTO>> getAllUsers() {
        return ResponseEntity.ok(userService.getAllUsersDynamicQuery());
    }

    @Operation(
        summary = "Retrieve the paginated list of users",
        description = "Retrieve a list of users with pagination, using filters by key, status, quantity, and/or value"
    )
    @Parameter(name = "name", description = "Filter by name", in = ParameterIn.QUERY, schema = @Schema(type = "string"))
    @Parameter(name = "status", description = "Filter by status", in = ParameterIn.QUERY, schema = @Schema(type = "boolean"))
    @Parameter(name = "quantity", description = "Filter by quantity", in = ParameterIn.QUERY, schema = @Schema(type = "integer"))
    @Parameter(name = "value", description = "Filter by value", in = ParameterIn.QUERY, schema = @Schema(type = "float"))
    @Parameter(name = "filter", hidden = true)
    @GetMapping(path = "/paginated", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<PageableResponseModel<UserDTO>> getAllUsersPaginated(
        @Min(value = 1, message = "O índice da página deve ser no mínimo 1") @RequestParam(defaultValue = "1") Integer pageIndex,
        @RequestParam(defaultValue = "10") Integer pageSize,
        @RequestParam(defaultValue = "id") String sortField,
        @RequestParam(defaultValue = "ASC") String sortType,
        @ParameterObject UserDTO filter
    ) {
        final var pageRequest = PageableRequestModel.of(pageIndex - 1, pageSize, sortType, sortField, filter);
        return ResponseEntity.ok(userService.getAllUsersPageable(pageRequest));
    }

    @Operation(
        summary = "Retrieve the paginated list of users",
        description = "Retrieve a list of users with pagination, using a filter by key"
    )
    @GetMapping(path = "/paginated-by-name", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<PageableResponseModel<UserDTO>> getAllUsersPaginatedByName(
        @Min(value = 1, message = "O índice da página deve ser no mínimo 1") @RequestParam(defaultValue = "1") Integer pageIndex,
        @RequestParam(defaultValue = "10") Integer pageSize,
        @RequestParam(defaultValue = "id") String sortField,
        @RequestParam(defaultValue = "ASC") String sortType,
        @RequestParam(required = false) String filter
    ) {
        final var pageRequest = PageableRequestModel.of(pageIndex - 1, pageSize, sortType, sortField, filter);
        return ResponseEntity.ok(userService.getAllUsersPageableByName(pageRequest));
    }

    @Operation(
        summary = "Retrieve a user by ID",
        description =
            """
            # Example of documentation with Markdown
            ## Retrieve a user by ID

            > This method expects an **integer** parameter and returns a JSON of type `userDTO`.
            >
            > It is also possible to include an image via _markdown_ in the documentation, see:

            ![Image](https://www.rnp.br/sites/site-publico/themes/sitepublico/logo.png)

            We can include formatted code snippets via _markdown_. See the example:

            ```json
            {
              "ip": "string",
              "latitude": 0,
              "longitude": 0
            }
            ```
            """
    )
    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<UserDTO> getUserById(@PathVariable Long id) {
        return ResponseEntity.ok(userService.findById(id));
    }

    @PostMapping
    @Operation(summary = "Register user", description = "Register user")
    public ResponseEntity<Void> registerUser(
        @Parameter(name = "user", description = "Representation of the user to be added", required = true)
        @Valid @RequestBody UserDTO dto) {
        var createdUser = userService.addUser(dto);
        var location = ServletUriComponentsBuilder.fromCurrentRequest().path(
            "/{id}").buildAndExpand(createdUser.id()).toUri();
        return ResponseEntity.created(location).build();
    }

    @GetMapping("logged-in")
    public String getLoggedInUser(Principal principal) {
        return principal.getName();
    }

    @GetMapping("me")
    public ResponseEntity<UserDTO> getMe() {
        var user = userService.getMe();
        var isApprover = requestService.hasAssignedRequestsForCurrentUser();
        return ResponseEntity.ok(copyWithApproverFlag(user, isApprover));
    }

    private UserDTO copyWithApproverFlag(UserDTO user, boolean isApprover) {
        return UserDTO.builder()
            .id(user.id())
            .username(user.username())
            .firstName(user.firstName())
            .lastName(user.lastName())
            .email(user.email())
            .isApprover(isApprover)
            .externalId(user.externalId())
            .build();
    }

}
