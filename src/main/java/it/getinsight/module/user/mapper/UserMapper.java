package it.getinsight.module.user.mapper;

import it.getinsight.core.dynamicquery.model.mapper.BaseGenericObjectMapper;
import it.getinsight.core.model.mapper.BaseMapper;
import it.getinsight.module.user.dto.UserDTO;
import it.getinsight.module.user.entity.UserEntity;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface UserMapper extends BaseMapper<UserEntity, UserDTO>, BaseGenericObjectMapper<UserDTO> {

    @InheritInverseConfiguration(name = "toDto")
    void fromDto(UserDTO dto, @MappingTarget UserEntity entity);

    @InheritInverseConfiguration(name = "toMap")
    List<UserDTO> toList(List<Object> value);

    @Mapping(
        target = "id",
        expression = "java(toLong( value, 0 ))"
    )
    @Mapping(
        target = "username",
        expression = "java(toString( value, 1 ))"
    )
    @Mapping(
        target = "firstName",
        expression = "java(toString( value, 2 ))"
    )
    @Mapping(
        target = "lastName",
        expression = "java(toString( value, 3 ))"
    )
    @Mapping(
        target = "email",
        expression = "java(toString( value, 4 ))"
    )
    @Mapping(
        target = "externalId",
        expression = "java(toString( value, 5 ))"
    )
    UserDTO toMap(Object value);
}
