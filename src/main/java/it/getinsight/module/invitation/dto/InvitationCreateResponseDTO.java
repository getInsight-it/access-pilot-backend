package it.getinsight.module.invitation.dto;

import lombok.Builder;

import java.io.Serial;
import java.io.Serializable;
import java.time.Instant;
import java.util.List;

@Builder
public record InvitationCreateResponseDTO(
    String protocolCode,
    List<InvitationCreatedDTO> invitations
) implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Builder
    public record InvitationCreatedDTO(
        String email,
        String token,
        Instant expiresAt
    ) implements Serializable {

        @Serial
        private static final long serialVersionUID = 1L;
    }
}
