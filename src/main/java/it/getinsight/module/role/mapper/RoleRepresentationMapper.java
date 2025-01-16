package it.getinsight.module.role.mapper;

import it.getinsight.core.dynamicquery.model.mapper.BaseGenericObjectMapper;
import it.getinsight.core.model.mapper.BaseMapper;
import it.getinsight.module.keycloak.dto.RoleRepresentationDTO;
import it.getinsight.module.role.dto.RoleDTO;
import it.getinsight.module.role.entity.RoleEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface RoleRepresentationMapper extends BaseMapper<RoleRepresentationDTO, RoleDTO>, BaseGenericObjectMapper<RoleDTO> {


    @Mapping(
        source = "obj.id",
        target = "id"
    )
    @Mapping(
        source = "obj.name",
        target = "name"
    )
    @Mapping(
        source = "obj.description",
        target = "description"
    )
    RoleDTO toDto(RoleEntity entity, RoleRepresentationDTO obj);

    @Mapping(
        source = "obj.id",
        target = "id"
    )
    @Mapping(
        source = "obj.name",
        target = "name"
    )
    @Mapping(
        source = "obj.description",
        target = "description"
    )
    @Named("toRoleRepresentationDTO")
    RoleRepresentationDTO toRoleRepresentationDTO(RoleDTO obj);


}
