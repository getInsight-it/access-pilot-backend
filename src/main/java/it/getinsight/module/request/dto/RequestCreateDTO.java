package it.getinsight.module.request.dto;


import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Builder;

import java.io.Serial;
import java.io.Serializable;

@Builder
public record RequestCreateDTO(


    @NotNull
    Long roleId,

    @Size(max = 1000)
    String description

) implements Serializable {

    @Serial
    private static final long serialVersionUID = -6731357049354425215L;
}
