package it.getinsight.module.invitation.controller.v1;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import it.getinsight.core.pagination.PageableRequestModel;
import it.getinsight.core.pagination.PageableResponseModel;
import it.getinsight.module.invitation.dto.InvitationFilterDTO;
import it.getinsight.module.invitation.dto.InvitationListDTO;
import it.getinsight.module.invitation.dto.InvitationRequestContextDTO;
import it.getinsight.module.invitation.service.InvitationService;
import it.getinsight.module.user.service.AuthenticationContextService;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/invitations")
@Tag(name = "Invitation", description = "Authenticated operations for invitations.")
@RequiredArgsConstructor
public class InvitationController {

    private final InvitationService invitationService;
    private final AuthenticationContextService authenticationContextService;

    @GetMapping(value = "/{token}/request-context", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Get request-context for the existing request access screen")
    public ResponseEntity<InvitationRequestContextDTO> getRequestContext(@PathVariable String token) {
        return ResponseEntity.ok(invitationService.getRequestContext(token, authenticationContextService.getCurrentUserEmail()));
    }

    @GetMapping(value = "/me", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "List invitations for the authenticated user (paginated)")
    public ResponseEntity<PageableResponseModel<InvitationListDTO>> listMine(
        @Min(value = 1, message = "O índice da página deve ser no mínimo 1") @RequestParam(defaultValue = "1") Integer pageIndex,
        @RequestParam(defaultValue = "10") Integer pageSize,
        @RequestParam(defaultValue = "id") String sortField,
        @RequestParam(defaultValue = "ASC") String sortType,
        @ParameterObject InvitationFilterDTO filter
    ) {
        final var pageRequest = PageableRequestModel.of(pageIndex - 1, pageSize, sortType, sortField, filter);
        return ResponseEntity.ok(invitationService.getAllInvitationsMine(authenticationContextService.getCurrentUserEmail(), pageRequest));
    }
}
