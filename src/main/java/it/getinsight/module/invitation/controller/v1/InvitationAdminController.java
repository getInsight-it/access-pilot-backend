package it.getinsight.module.invitation.controller.v1;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import it.getinsight.core.pagination.PageableRequestModel;
import it.getinsight.core.pagination.PageableResponseModel;
import it.getinsight.module.invitation.dto.InvitationCreateRequestDTO;
import it.getinsight.module.invitation.dto.InvitationCreateResponseDTO;
import it.getinsight.module.invitation.dto.InvitationFilterDTO;
import it.getinsight.module.invitation.dto.InvitationListDTO;
import it.getinsight.module.invitation.service.InvitationService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1/invitations")
@Tag(name = "Invitation (Invite Sender)", description = "Invite-sender operations for invitations.")
@RequiredArgsConstructor
@PreAuthorize("hasRole('INVITE_SENDER')")
public class InvitationAdminController {

    private final InvitationService invitationService;

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "List invitations (paginated)")
    public ResponseEntity<PageableResponseModel<InvitationListDTO>> list(
        @Min(value = 1, message = "O índice da página deve ser no mínimo 1") @RequestParam(defaultValue = "1") Integer pageIndex,
        @RequestParam(defaultValue = "10") Integer pageSize,
        @RequestParam(defaultValue = "id") String sortField,
        @RequestParam(defaultValue = "ASC") String sortType,
        @ParameterObject InvitationFilterDTO filter
    ) {
        final var pageRequest = PageableRequestModel.of(pageIndex - 1, pageSize, sortType, sortField, filter);
        return ResponseEntity.ok(invitationService.getAllInvitations(pageRequest));
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Create invitations (returns tokens; email sending is out of scope)")
    public ResponseEntity<InvitationCreateResponseDTO> create(@Valid @RequestBody InvitationCreateRequestDTO request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(invitationService.createInvitations(request));
    }

    @PostMapping("/{id}/cancel")
    @Operation(summary = "Cancel an invitation")
    public ResponseEntity<Void> cancel(@PathVariable Long id) {
        invitationService.cancelInvitation(id);
        return ResponseEntity.noContent().build();
    }
}
