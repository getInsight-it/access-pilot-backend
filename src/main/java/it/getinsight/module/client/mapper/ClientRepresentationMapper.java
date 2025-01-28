package it.getinsight.module.client.mapper;

import it.getinsight.core.dynamicquery.model.mapper.BaseGenericObjectMapper;
import it.getinsight.core.model.mapper.BaseMapper;
import it.getinsight.module.client.dto.ClientDTO;
import it.getinsight.module.client.entity.ClientEntity;
import it.getinsight.module.keycloak.dto.ClientRepresentationDTO;
import org.mapstruct.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ClientRepresentationMapper extends BaseMapper<ClientRepresentationDTO, ClientDTO>, BaseGenericObjectMapper<ClientDTO> {

    @InheritInverseConfiguration(name = "toDto")
    void fromDto(ClientDTO dto, @MappingTarget ClientRepresentationDTO entity);

    @Mapping(source = "description", target = "description")
    @Mapping(source = "baseUrl", target = "baseUrl")
    default void fromDtoRepresentation(ClientDTO dto, ClientRepresentationDTO obj) {
        obj.setName(dto.name() );
        obj.setClientId(dto.clientId() );
        obj.setDescription(dto.description() );
        obj.setBaseUrl(dto.baseUrl() );
        if (obj.getAttributes() != null) {
            obj.getAttributes().put("acl.client.managed", String.valueOf(dto.managed()));
        }
    }


    @InheritInverseConfiguration(name = "toMap")
    List<ClientDTO> toList(List<Object> value);


    @Mapping(
        source = "obj.clientId",
        target = "clientId"
    )
    @Mapping(
        source = "entity.name",
        target = "name"
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
        expression = "java(Boolean.valueOf(obj.getAttributes().get(\"acl.client.managed\")))",
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
