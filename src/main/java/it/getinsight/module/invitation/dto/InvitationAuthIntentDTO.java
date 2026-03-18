package it.getinsight.module.invitation.dto;

import it.getinsight.module.invitation.enuns.InvitationNextStep;
import it.getinsight.module.invitation.enuns.InvitationPublicStatus;
import lombok.Builder;

import java.io.Serial;
import java.io.Serializable;

@Builder
public record InvitationAuthIntentDTO(
    InvitationPublicStatus status,
    InvitationNextStep nextStep,
    String loginHint,
    String invitationToken
) implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;
}
