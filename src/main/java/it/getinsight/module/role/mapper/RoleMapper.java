package it.getinsight.module.role.mapper;

import it.getinsight.core.dynamicquery.model.mapper.BaseGenericObjectMapper;
import it.getinsight.core.model.mapper.BaseMapper;
import it.getinsight.module.role.dto.RoleDTO;
import it.getinsight.module.role.entity.RoleEntity;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface RoleMapper extends BaseMapper<RoleEntity, RoleDTO>, BaseGenericObjectMapper<RoleDTO> {

    @InheritInverseConfiguration(name = "toDto")
    void fromDto(RoleDTO dto, @MappingTarget RoleEntity entity);

    @InheritInverseConfiguration(name = "toMap")
    List<RoleDTO> toList(List<Object> value);

   @Mapping(
        target = "id",
        expression = "java(toLong( value, 0 ))"
    )
   @Mapping(
       target = "idRoleExterno",
       expression = "java(toString( value, 1 ))"
   )
    @Mapping(
        target = "nome",
        expression = "java(toString( value, 2 ))"
    )
    @Mapping(
        target = "descricao",
        expression = "java(toString( value, 3 ))"
    )
   @Mapping(
       target = "idRoleParent",
       expression = "java(toLong( value, 4 ))"
   )
   @Mapping(
       target = "idClient",
       expression = "java(toLong( value, 5 ))"
   )
   RoleDTO toMap(Object value);
}
