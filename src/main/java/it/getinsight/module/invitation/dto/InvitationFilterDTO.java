package it.getinsight.module.invitation.dto;

import it.getinsight.module.invitation.enuns.InvitationStatus;
import lombok.Builder;

import java.io.Serial;
import java.io.Serializable;

@Builder
public record InvitationFilterDTO(
    InvitationStatus status
) implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;
}
