package it.getinsight.module.client.mapper;

import it.getinsight.core.dynamicquery.model.mapper.BaseGenericObjectMapper;
import it.getinsight.core.model.mapper.BaseMapper;
import it.getinsight.module.client.dto.ClientDTO;
import it.getinsight.module.client.entity.ClientEntity;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ClientMapper extends BaseMapper<ClientEntity, ClientDTO>, BaseGenericObjectMapper<ClientDTO> {

    @InheritInverseConfiguration(name = "toDto")
    void fromDto(ClientDTO dto, @MappingTarget ClientEntity entity);

    @InheritInverseConfiguration(name = "toMap")
    List<ClientDTO> toList(List<Object> value);

   @Mapping(
        target = "id",
        expression = "java(toLong( value, 0 ))"
    )
   @Mapping(
       target = "clientId",
       expression = "java(toString( value, 1 ))"
   )
    @Mapping(
        target = "clientUUID",
        expression = "java(toString( value, 2 ))"
    )
   @Mapping(
       target = "managed",
       expression = "java(toBooleanFromString( value, 3, \"true\" ))"
    )
    @Mapping(
        target = "description",
        expression = "java(toString( value, 4 ))"
    )
    @Mapping(
        target = "baseUrl",
        expression = "java(toString( value, 5 ))"
    )
   ClientDTO toMap(Object value);
}
