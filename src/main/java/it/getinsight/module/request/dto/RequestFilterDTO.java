package it.getinsight.module.request.dto;

import it.getinsight.module.request.enuns.RequestStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Builder;

import java.io.Serial;
import java.io.Serializable;

@Builder
public record RequestFilterDTO(

    RequestStatus status,

    Boolean managed,

    @Size(max = 255)
    String description,

    @NotBlank
    String roleName,

    @NotBlank
    String clientId,

    String clientName
) implements Serializable {

    @Serial
    private static final long serialVersionUID = -6731357049354425215L;
}
