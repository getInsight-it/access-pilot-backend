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

    @InheritInverseConfiguration(name = "toDto")
    @Mapping(ignore = true, target = "clientUUID")
    @Mapping(source = "configurationId" , target = "configuration.id")
    void fromDtoWithoutImmutableFields(ClientDTO dto, @MappingTarget ClientEntity entity);

    @Mapping(target = "configurationId", source = "configuration.id")
    ClientDTO toDto(ClientEntity entity);

    @InheritInverseConfiguration(name = "toMap")
    List<ClientDTO> toList(List<Object> value);

   @Mapping(
        target = "id",
        expression = "java(toLong( value, 0 ))"
    )
   @Mapping(
       target = "name",
       expression = "java(toString( value, 1 ))"
   )
   @Mapping(
       target = "clientId",
       expression = "java(toString( value, 2 ))"
   )
    @Mapping(
        target = "clientUUID",
        expression = "java(toString( value, 3 ))"
    )
   @Mapping(
       target = "managed",
       expression = "java(toBooleanFromString( value, 4, \"true\" ))"
    )
   @Mapping(
       target = "status",
       expression = "java(toString( value, 5 ))"
    )
    @Mapping(
        target = "description",
        expression = "java(toString( value, 6 ))"
    )
    @Mapping(
        target = "baseUrl",
        expression = "java(toString( value, 7 ))"
    )
   @Mapping(
       target = "configurationId",
       expression = "java(toLong( value, 8 ))"
   )
   ClientDTO toMap(Object value);
}
