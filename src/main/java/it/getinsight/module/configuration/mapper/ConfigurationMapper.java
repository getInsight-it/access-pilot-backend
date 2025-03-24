package it.getinsight.module.configuration.mapper;

import com.fasterxml.jackson.databind.JsonNode;
import it.getinsight.core.dynamicquery.model.mapper.BaseGenericObjectMapper;
import it.getinsight.core.model.mapper.BaseMapper;
import it.getinsight.module.configuration.dto.ConfigurationDTO;
import it.getinsight.module.configuration.entity.ConfigurationEntity;
import org.mapstruct.*;

import java.sql.Clob;
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
        target = "value",
        expression = "java(toJsonNode( value, 3))"
    )
    ConfigurationDTO toMap(Object value);



    default JsonNode toJsonNode(Object valor, int pos) {
        Object[] value = (Object[]) valor;
        return (JsonNode) value[pos];
    }


}
