package it.getinsight.module.request.dto;

import it.getinsight.module.client.dto.ClientDTO;
import it.getinsight.module.level.dto.LevelHierarchyResumedDTO;
import it.getinsight.module.request.enuns.RequestStatus;
import it.getinsight.module.role.dto.RoleDTO;
import it.getinsight.module.user.dto.UserDTO;
import jakarta.validation.constraints.Size;
import lombok.Builder;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;
import java.util.UUID;

@Builder
public record RequestDTO(

    Long id,

    UUID uuid,

    RequestStatus status,

    @Size(max = 255)
    String description,

    String finalReason,

    String revocationReason,

    String protocolCode,

    String codeItem,

    RoleDTO role,

    LevelHierarchyResumedDTO level,

    ClientDTO client,

    UserDTO requestingUser,

    UserDTO approvingUser,

    UserDTO revokingUser,

    Date criacao,

    String invitationUuid
) implements Serializable {

    @Serial
    private static final long serialVersionUID = -6731357049354425215L;
}
