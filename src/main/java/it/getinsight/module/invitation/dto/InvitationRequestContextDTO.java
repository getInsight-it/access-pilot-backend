package it.getinsight.module.invitation.dto;

import lombok.Builder;

import java.io.Serial;
import java.io.Serializable;

@Builder
public record InvitationRequestContextDTO(
    String invitationToken,
    String clientId,
    Long roleId,
    String roleLabel,
    String clientLabel,
    Long levelId,
    String levelName,
    String codeItem,
    String description
) implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;
}
