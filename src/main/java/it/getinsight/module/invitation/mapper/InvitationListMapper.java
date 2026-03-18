package it.getinsight.module.invitation.mapper;

import it.getinsight.core.dynamicquery.model.mapper.BaseGenericObjectMapper;
import it.getinsight.core.model.mapper.BaseMapper;
import it.getinsight.module.invitation.dto.InvitationListDTO;
import it.getinsight.module.invitation.entity.InvitationEntity;
import it.getinsight.module.invitation.enuns.InvitationStatus;
import org.mapstruct.Context;
import org.mapstruct.IterableMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.ReportingPolicy;

import java.time.Instant;
import java.util.Collections;
import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface InvitationListMapper extends BaseMapper<InvitationEntity, InvitationListDTO>, BaseGenericObjectMapper<InvitationListDTO> {

    @Override
    default InvitationListDTO toDto(InvitationEntity entity) {
        return toDtoWithContext(entity, Instant.now());
    }

    @Override
    default List<InvitationListDTO> toDto(List<InvitationEntity> entities) {
        if (entities == null || entities.isEmpty()) {
            return Collections.emptyList();
        }
        return toDto(entities, Instant.now());
    }

    @Override
    default List<InvitationListDTO> toDto(Iterable<InvitationEntity> entities) {
        if (entities == null) {
            return Collections.emptyList();
        }
        List<InvitationEntity> list = new java.util.ArrayList<>();
        for (InvitationEntity entity : entities) {
            list.add(entity);
        }
        return toDto(list, Instant.now());
    }

    @Override
    default List<InvitationListDTO> toList(List<Object> values) {
        if (values == null || values.isEmpty()) {
            return Collections.emptyList();
        }
        return values.stream().map(this::toMap).toList();
    }

    @Override
    default InvitationListDTO toMap(Object value) {
        throw new UnsupportedOperationException("Dynamic query mapping is not supported for InvitationListDTO.");
    }

    @Named("toDtoWithContext")
    @Mapping(target = "roleId", source = "entity.role.id")
    @Mapping(target = "roleLabel", source = "entity.role.label")
    @Mapping(target = "clientId", source = "entity.role.client.clientId")
    @Mapping(target = "clientLabel", source = "entity.role.client.label")
    @Mapping(target = "status", expression = "java(resolveEffectiveStatus(entity, now))")
    InvitationListDTO toDtoWithContext(InvitationEntity entity, @Context Instant now);

    @IterableMapping(qualifiedByName = "toDtoWithContext")
    List<InvitationListDTO> toDto(List<InvitationEntity> entities, @Context Instant now);

    default InvitationStatus resolveEffectiveStatus(InvitationEntity invitation, Instant now) {
        if (InvitationStatus.CANCELLED.equals(invitation.getStatus())) {
            return InvitationStatus.CANCELLED;
        }
        if (InvitationStatus.CONSUMED.equals(invitation.getStatus())) {
            return InvitationStatus.CONSUMED;
        }
        if (InvitationStatus.EXPIRED.equals(invitation.getStatus())) {
            return InvitationStatus.EXPIRED;
        }
        if (isExpired(invitation, now)) {
            return InvitationStatus.EXPIRED;
        }
        return InvitationStatus.PENDING;
    }

    default boolean isExpired(InvitationEntity invitation, Instant now) {
        return invitation.getExpiresAt() != null && invitation.getExpiresAt().isBefore(now);
    }
}
