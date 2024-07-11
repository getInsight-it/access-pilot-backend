package it.getinsight.module.usuario.mapper;

import it.getinsight.core.dynamicquery.model.mapper.BaseGenericObjectMapper;
import it.getinsight.core.model.mapper.BaseMapper;
import it.getinsight.module.usuario.dto.UsuarioDTO;
import it.getinsight.module.usuario.entity.UsuarioEntity;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface UsuarioMapper extends BaseMapper<UsuarioEntity, UsuarioDTO>, BaseGenericObjectMapper<UsuarioDTO> {

    @InheritInverseConfiguration(name = "toDto")
    void fromDto(UsuarioDTO dto, @MappingTarget UsuarioEntity entity);

    @InheritInverseConfiguration(name = "toMap")
    List<UsuarioDTO> toList(List<Object> value);

    @Mapping(
        target = "id",
        expression = "java(toLong( value, 0 ))"
    )
    @Mapping(
        target = "username",
        expression = "java(toString( value, 1 ))"
    )
    @Mapping(
        target = "nome",
        expression = "java(toString( value, 2 ))"
    )
    @Mapping(
        target = "sobrenome",
        expression = "java(toString( value, 3 ))"
    )
    @Mapping(
        target = "email",
        expression = "java(toString( value, 4 ))"
    )
    @Mapping(
        target = "idUsuarioExterno",
        expression = "java(toString( value, 5 ))"
    )
    UsuarioDTO toMap(Object value);
}
