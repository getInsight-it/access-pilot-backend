package it.getinsight.module.role.mapper;

import it.getinsight.core.dynamicquery.model.mapper.BaseGenericObjectMapper;
import it.getinsight.core.model.mapper.BaseMapper;
import it.getinsight.module.role.dto.RoleDTO;
import it.getinsight.module.role.dto.RoleResponseDTO;
import it.getinsight.module.role.entity.RoleEntity;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface RoleResponseMapper extends BaseMapper<RoleEntity, RoleResponseDTO>, BaseGenericObjectMapper<RoleResponseDTO> {

    @InheritInverseConfiguration(name = "toDto")
    void fromDto(RoleDTO dto, @MappingTarget RoleEntity entity);

    @InheritInverseConfiguration(name = "toMap")
    List<RoleResponseDTO> toList(List<Object> value);

    @Mapping(
        target = "roleParent",
        source = "role"
    )
    @Mapping(target = "roleParent.roleParent", ignore = true)
    RoleResponseDTO toDto(RoleEntity entity);

    @Mapping(
        target = "id",
        expression = "java(toLong( value, 0 ))"
    )
    @Mapping(
        target = "roleExternalId",
        expression = "java(toString( value, 1 ))"
    )
    @Mapping(
        target = "name",
        expression = "java(toString( value, 2 ))"
    )
    @Mapping(
        target = "description",
        expression = "java(toString( value, 3 ))"
    )
    RoleResponseDTO toMap(Object value);
}
