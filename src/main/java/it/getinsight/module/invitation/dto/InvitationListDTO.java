package it.getinsight.module.invitation.dto;

import it.getinsight.module.invitation.enuns.InvitationStatus;
import lombok.Builder;

import java.io.Serial;
import java.io.Serializable;
import java.time.Instant;

@Builder
public record InvitationListDTO(
    Long id,
    String invitationUuid,
    String protocolCode,
    InvitationStatus status,
    Instant expiresAt,
    String email,
    Long roleId,
    String roleLabel,
    String clientId,
    String clientLabel,
    String codeItem,
    String description
) implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;
}
