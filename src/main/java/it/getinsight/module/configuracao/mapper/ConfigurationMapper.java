package it.getinsight.module.configuracao.mapper;

import it.getinsight.core.dynamicquery.model.mapper.BaseGenericObjectMapper;
import it.getinsight.core.model.mapper.BaseMapper;
import it.getinsight.module.configuracao.dto.ConfigurationDTO;
import it.getinsight.module.configuracao.entity.ConfigurationEntity;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ConfigurationMapper extends BaseMapper<ConfigurationEntity, ConfigurationDTO>, BaseGenericObjectMapper<ConfigurationDTO> {

    @InheritInverseConfiguration(name = "toDto")
    void fromDto(ConfigurationDTO dto, @MappingTarget ConfigurationEntity entity);

    @InheritInverseConfiguration(name = "toMap")
    List<ConfigurationDTO> toList(List<Object> value);

    @Mapping(
        target = "id",
        expression = "java(toLong( value, 0 ))"
    )
    @Mapping(
        target = "name",
        expression = "java(toString( value, 1 ))"
    )
    @Mapping(
        target = "description",
        expression = "java(toString( value, 2 ))"
    )
    @Mapping(
        target = "icon",
        expression = "java(toString( value, 3 ))"
    )
    @Mapping(
        target = "authority",
        expression = "java(toString( value, 4 ))"
    )
    @Mapping(
        target = "redirectUrl",
        expression = "java(toString( value, 5 ))"
    )
    @Mapping(
        target = "clientId",
        expression = "java(toString( value, 6 ))"
    )
    @Mapping(
        target = "responseType",
        expression = "java(toString( value, 7 ))"
    )
    @Mapping(
        target = "scope",
        expression = "java(toString( value, 8 ))"
    )
    @Mapping(
        target = "postLogoutRedirectUri",
        expression = "java(toString( value, 9 ))"
    )
    @Mapping(
        target = "startChecksession",
        expression = "java(toBooleanFromSimNao( value, 10 ))"
    )
    @Mapping(
        target = "silentRenew",
        expression = "java(toBooleanFromSimNao( value, 11 ))"
    )
    @Mapping(
        target = "startupRoute",
        expression = "java(toString( value, 12 ))"
    )
    @Mapping(
        target = "forbiddenRoute",
        expression = "java(toString( value, 13 ))"
    )
    @Mapping(
        target = "unauthorizedRoute",
        expression = "java(toString( value, 14 ))"
    )
    @Mapping(
        target = "logLevel",
        expression = "java(toInteger( value, 15 ))"
    )
    @Mapping(
        target = "maxIdTokenIatOffsetAllowedInSeconds",
        expression = "java(toInteger( value, 16 ))"
    )
    @Mapping(
        target = "historyCleanupOff",
        expression = "java(toBooleanFromSimNao( value, 17 ))"
    )
    ConfigurationDTO toMap(Object value);
}
