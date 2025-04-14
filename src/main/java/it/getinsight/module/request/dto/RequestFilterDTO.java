package it.getinsight.module.request.dto;

import it.getinsight.module.request.enuns.RequestStatus;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;

import java.io.Serial;
import java.io.Serializable;

@Builder
public record RequestFilterDTO(

    RequestStatus status,

    String description,

    String roleName,

    String protocolCode,

    String clientId,

    String requestingUserId,

    Boolean onlyMine,

    @NotNull
    String type
) implements Serializable {

    @Serial
    private static final long serialVersionUID = -6731357049354425215L;
}
