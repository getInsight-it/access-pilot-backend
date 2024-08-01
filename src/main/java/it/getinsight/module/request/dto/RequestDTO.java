package it.getinsight.module.request.dto;

import it.getinsight.module.request.enuns.RequestStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.io.Serial;
import java.io.Serializable;

public record RequestDTO(

    @Size(min = 1, max = 100)
    @NotBlank
    Long id,
    RequestStatus status,
    @NotBlank
    String userId,
    @NotBlank
    String roleId
) implements Serializable {

    @Serial
    private static final long serialVersionUID = -6731357049354425215L;
}
