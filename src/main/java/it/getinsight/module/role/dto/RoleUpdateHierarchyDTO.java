package it.getinsight.module.role.dto;

import lombok.Builder;

import java.io.Serializable;

@Builder
public record RoleUpdateHierarchyDTO(

    Long id,

    Long parentId,

    Long clientId

) implements Serializable {}
