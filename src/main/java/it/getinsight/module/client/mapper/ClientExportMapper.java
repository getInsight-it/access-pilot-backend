package it.getinsight.module.client.mapper;

import it.getinsight.module.client.dto.ClientExportClientDTO;
import it.getinsight.module.client.dto.ClientExportRoleDTO;
import it.getinsight.module.client.entity.ClientEntity;
import it.getinsight.module.role.entity.RoleEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ClientExportMapper {

    @Mapping(
        target = "status",
        expression = "java(entity.getStatus() != null ? entity.getStatus().name() : null)"
    )
    ClientExportClientDTO toExportClient(ClientEntity entity);

    @Mapping(target = "parentName", source = "role.name")
    @Mapping(target = "levelName", source = "level.name")
    @Mapping(
        target = "levelType",
        expression = "java(entity.getLevel() != null && entity.getLevel().getType() != null ? entity.getLevel().getType().name() : null)"
    )
    ClientExportRoleDTO toExportRole(RoleEntity entity);
}
