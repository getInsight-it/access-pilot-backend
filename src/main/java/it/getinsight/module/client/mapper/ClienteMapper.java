package it.getinsight.module.client.mapper;

import it.getinsight.core.dynamicquery.model.mapper.BaseGenericObjectMapper;
import it.getinsight.core.model.mapper.BaseMapper;
import it.getinsight.module.client.dto.ClienteDTO;
import it.getinsight.module.client.entity.ClienteEntity;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ClienteMapper extends BaseMapper<ClienteEntity, ClienteDTO>, BaseGenericObjectMapper<ClienteDTO> {

    @InheritInverseConfiguration(name = "toDto")
    void fromDto(ClienteDTO dto, @MappingTarget ClienteEntity entity);

    @InheritInverseConfiguration(name = "toMap")
    List<ClienteDTO> toList(List<Object> value);

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
        target = "descricao",
        expression = "java(toString( value, 3 ))"
    )
   ClienteDTO toMap(Object value);
}
