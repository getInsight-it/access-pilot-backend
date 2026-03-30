package it.getinsight.module.invitation.controller.v1;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import it.getinsight.module.invitation.dto.InvitationAuthIntentDTO;
import it.getinsight.module.invitation.dto.InvitationPublicDTO;
import it.getinsight.module.invitation.service.InvitationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/public/invitations")
@Tag(name = "Invitation (Public)", description = "Public operations for invitation links.")
@RequiredArgsConstructor
public class InvitationPublicController {

    private final InvitationService invitationService;

    @GetMapping(value = "/{invitationUuid}", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Get invitation status for the public page")
    public ResponseEntity<InvitationPublicDTO> getInvitation(@PathVariable String invitationUuid) {
        return ResponseEntity.ok(invitationService.getPublicInvitation(invitationUuid));
    }

    @GetMapping(value = "/{invitationUuid}/auth-intent", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Get auth intent for login/register decision")
    public ResponseEntity<InvitationAuthIntentDTO> getAuthIntent(@PathVariable String invitationUuid) {
        return ResponseEntity.ok(invitationService.getAuthIntent(invitationUuid));
    }
}
