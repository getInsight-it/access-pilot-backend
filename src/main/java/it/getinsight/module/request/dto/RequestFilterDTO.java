package it.getinsight.module.request.dto;

import it.getinsight.module.request.enuns.RequestStatus;
import lombok.Builder;

import java.io.Serial;
import java.io.Serializable;

@Builder
public record RequestFilterDTO(

    RequestStatus status,

    Boolean managed,

    String description,

    String finalReason,

    String roleName,

    String clientId,

    String clientName,

    String requestingUserId,

    Boolean onlyMine,

    String type
) implements Serializable {

    @Serial
    private static final long serialVersionUID = -6731357049354425215L;
}
