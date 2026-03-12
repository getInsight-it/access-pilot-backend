package it.getinsight.module.invitation.dto;

import it.getinsight.module.invitation.enuns.InvitationPublicStatus;
import lombok.Builder;

import java.io.Serial;
import java.io.Serializable;
import java.time.Instant;

@Builder
public record InvitationPublicDTO(
    InvitationPublicStatus status,
    Instant expiresAt,
    String emailMasked,
    String codeItem,
    String description,
    RoleResumedDTO role,
    ClientResumedDTO client,
    LevelResumedDTO level
) implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Builder
    public record RoleResumedDTO(
        Long id,
        String name,
        String label
    ) implements Serializable {

        @Serial
        private static final long serialVersionUID = 1L;
    }

    @Builder
    public record ClientResumedDTO(
        Long id,
        String name,
        String label
    ) implements Serializable {

        @Serial
        private static final long serialVersionUID = 1L;
    }

    @Builder
    public record LevelResumedDTO(
        Long id,
        String name
    ) implements Serializable {

        @Serial
        private static final long serialVersionUID = 1L;
    }
}
