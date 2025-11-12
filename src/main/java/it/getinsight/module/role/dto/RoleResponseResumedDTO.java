package it.getinsight.module.role.dto;


import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Builder;

import java.io.Serializable;

@Builder
public record RoleResponseResumedDTO(

    Long id,

    @NotEmpty
    @Size(min = 3, max = 100)
    String name,

    @NotEmpty
    @Size(min = 3, max = 100)
    String description

) implements Serializable {}
