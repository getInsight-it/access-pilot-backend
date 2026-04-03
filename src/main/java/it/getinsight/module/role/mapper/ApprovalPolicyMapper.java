package it.getinsight.module.role.mapper;

import it.getinsight.core.model.mapper.BaseMapper;
import it.getinsight.module.role.dto.ApprovalPolicyDTO;
import it.getinsight.module.role.dto.ApprovalPolicyResponseDTO;
import it.getinsight.module.role.dto.ApprovalPolicyRoleDTO;
import it.getinsight.module.role.dto.ApprovalPolicyRoleResponseDTO;
import it.getinsight.module.role.entity.ApprovalPolicyEntity;
import it.getinsight.module.role.entity.ApprovalPolicyRoleEntity;
import it.getinsight.module.role.enuns.ApprovalPolicyType;
import org.mapstruct.*;

import java.util.Arrays;
import java.util.EnumMap;
import java.util.List;
import java.util.Objects;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ApprovalPolicyMapper extends BaseMapper<ApprovalPolicyEntity, ApprovalPolicyResponseDTO> {

    @Override
    @Mapping(target = "enabled", expression = "java(Boolean.TRUE.equals(entity.getEnabled()))")
    @Mapping(target = "roles", source = "roles", qualifiedByName = "toTargetDtoList")
    ApprovalPolicyResponseDTO toDto(ApprovalPolicyEntity entity);

    @Named("toTargetDtoList")
    default List<ApprovalPolicyRoleResponseDTO> toTargetDtoList(List<ApprovalPolicyRoleEntity> entities) {
        if (entities == null || entities.isEmpty()) {
            return List.of();
        }
        return entities.stream()
            .map(this::toTargetDto)
            .toList();
    }

    @Mapping(target = "roleId", source = "role.id")
    @Mapping(target = "roleName", source = "role.name")
    @Mapping(target = "roleLabel", source = "role.label")
    @Mapping(target = "canApprove", expression = "java(Boolean.TRUE.equals(entity.getCanApprove()))")
    @Mapping(target = "canReject", expression = "java(Boolean.TRUE.equals(entity.getCanReject()))")
    @Mapping(target = "canRevoke", expression = "java(Boolean.TRUE.equals(entity.getCanRevoke()))")
    ApprovalPolicyRoleResponseDTO toTargetDto(ApprovalPolicyRoleEntity entity);

    @Mapping(target = "enabled", expression = "java(Boolean.TRUE.equals(entity.getEnabled()))")
    @Mapping(target = "roles", source = "roles", qualifiedByName = "toRequestTargetDtoList")
    ApprovalPolicyDTO toRequestDto(ApprovalPolicyEntity entity);

    @Named("toRequestTargetDtoList")
    default List<ApprovalPolicyRoleDTO> toRequestTargetDtoList(List<ApprovalPolicyRoleEntity> entities) {
        if (entities == null || entities.isEmpty()) {
            return List.of();
        }
        return entities.stream()
            .map(this::toRequestTargetDto)
            .toList();
    }

    @Mapping(target = "roleId", source = "role.id")
    @Mapping(target = "canApprove", expression = "java(Boolean.TRUE.equals(entity.getCanApprove()))")
    @Mapping(target = "canReject", expression = "java(Boolean.TRUE.equals(entity.getCanReject()))")
    @Mapping(target = "canRevoke", expression = "java(Boolean.TRUE.equals(entity.getCanRevoke()))")
    ApprovalPolicyRoleDTO toRequestTargetDto(ApprovalPolicyRoleEntity entity);

    /**
     * Converts a list of entities to response DTOs, normalizing to include all policy types.
     */
    default List<ApprovalPolicyResponseDTO> toDtoList(List<ApprovalPolicyEntity> entities) {
        return normalizeEntities(entities).stream()
            .map(this::toDto)
            .toList();
    }

    /**
     * Converts a list of entities to request DTOs, normalizing to include all policy types.
     */
    default List<ApprovalPolicyDTO> toRequestDtoList(List<ApprovalPolicyEntity> entities) {
        return normalizeEntities(entities).stream()
            .map(this::toRequestDto)
            .toList();
    }

    /**
     * Normalizes the list of policies to ensure all policy types are represented.
     * Missing types are filled with default disabled policies.
     */
    private List<ApprovalPolicyEntity> normalizeEntities(List<ApprovalPolicyEntity> entities) {
        var byType = new EnumMap<ApprovalPolicyType, ApprovalPolicyEntity>(ApprovalPolicyType.class);
        if (entities != null) {
            entities.stream()
                .filter(Objects::nonNull)
                .filter(policy -> policy.getType() != null)
                .forEach(policy -> byType.put(policy.getType(), policy));
        }

        return Arrays.stream(ApprovalPolicyType.values())
            .map(type -> byType.getOrDefault(type, ApprovalPolicyEntity.builder()
                .type(type)
                .enabled(false)
                .roles(List.of())
                .build()))
            .toList();
    }
}
