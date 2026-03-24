package it.getinsight.module.invitation.dto;

import jakarta.validation.constraints.*;
import lombok.Builder;

import java.io.Serial;
import java.io.Serializable;
import java.time.Instant;
import java.util.List;

@Builder
public record InvitationCreateRequestDTO(
    @NotEmpty
    @Size(max = 100, message = "Máximo de 100 emails por requisição")
    List<@NotBlank String> emails,

    @NotNull
    Long roleId,

    String codeItem,

    @Size(max = 1000)
    String description,

    @FutureOrPresent(message = "A data de expiração deve ser no futuro ou presente")
    Instant expiresAt,

    String protocolCode
) implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;
}
