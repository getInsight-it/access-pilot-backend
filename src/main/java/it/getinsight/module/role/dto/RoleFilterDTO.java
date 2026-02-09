package it.getinsight.module.role.dto;

import lombok.Builder;

import java.io.Serializable;

@Builder
public record RoleFilterDTO(

    String name,
    String label,

    String description,

    String clientName,

    String clientId

) implements Serializable {}
