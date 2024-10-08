package it.getinsight.module.client.mapper;

import it.getinsight.core.dynamicquery.model.mapper.BaseGenericObjectMapper;
import it.getinsight.core.model.mapper.BaseMapper;
import it.getinsight.module.client.dto.ClientDTO;
import it.getinsight.module.client.entity.ClientEntity;
import it.getinsight.module.keycloak.dto.ClientRepresentationDTO;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ClientRepresentationMapper extends BaseMapper<ClientRepresentationDTO, ClientDTO>, BaseGenericObjectMapper<ClientDTO> {

    @InheritInverseConfiguration(name = "toDto")
    void fromDto(ClientDTO dto, @MappingTarget ClientRepresentationDTO entity);

    @InheritInverseConfiguration(name = "toMap")
    List<ClientDTO> toList(List<Object> value);

    @Mapping(
        source = "obj.clientId",
        target = "clientId"
    )
    @Mapping(
        source = "obj.id",
        target = "clientUUID"
    )
    @Mapping(
        source = "entity.id",
        target = "id"
    )
    @Mapping(
        expression = "java(Boolean.valueOf(obj.attributes().get(\"acl.client.managed\")))",
        target = "managed"
    )
    @Mapping(
        source = "obj.description",
        target = "description"
    )
    @Mapping(
        source = "obj.baseUrl",
        target = "baseUrl"
    )
    ClientDTO toDto(ClientEntity entity, ClientRepresentationDTO obj);


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
