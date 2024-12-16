package it.getinsight.module.user.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Builder;

import java.io.Serial;
import java.io.Serializable;

@Builder
public record UserDTO(
    Long id,

    @Size(min = 1, max = 255)
    @NotBlank
    String username,

    @Size(min = 1, max = 255)
    @NotBlank
    String firstName,

    @Size(min = 1, max = 255)
    @NotBlank
    String lastName,

    @Size(min = 1, max = 255)
    @NotBlank
    String email,

    @Size(min = 1, max = 255)
    @NotBlank
    Boolean isApprover,

    @NotBlank
    @Size(min = 1, max = 255)
    String externalId
) implements Serializable {

    @Serial
    private static final long serialVersionUID = -6731357049354425215L;
}
