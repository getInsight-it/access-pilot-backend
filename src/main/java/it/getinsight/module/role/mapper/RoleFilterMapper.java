package it.getinsight.module.role.mapper;

import it.getinsight.core.dynamicquery.model.mapper.BaseGenericObjectMapper;
import it.getinsight.core.model.mapper.BaseMapper;
import it.getinsight.module.role.dto.RoleDTO;
import it.getinsight.module.role.dto.RoleFilterDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface RoleFilterMapper extends BaseMapper<RoleFilterDTO, RoleDTO>, BaseGenericObjectMapper<RoleDTO> {

    @Mapping(
        target = "client.name",
        source = "clientName"
    )
    @Mapping(
        target = "client.clientId",
        source = "clientId"
    )
    RoleDTO toDto(RoleFilterDTO entity);

}
