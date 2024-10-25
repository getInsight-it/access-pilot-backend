package it.getinsight.module.request.mapper;

import it.getinsight.core.dynamicquery.model.mapper.BaseGenericObjectMapper;
import it.getinsight.core.model.mapper.BaseMapper;
import it.getinsight.module.request.dto.RequestDTO;
import it.getinsight.module.request.entity.RequestEntity;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface RequestMapper extends BaseMapper<RequestEntity, RequestDTO>, BaseGenericObjectMapper<RequestDTO> {

    @InheritInverseConfiguration(name = "toDto")
    void fromDto(RequestDTO dto, @MappingTarget RequestEntity entity);

    @InheritInverseConfiguration(name = "toMap")
    List<RequestDTO> toList(List<Object> value);


    @Mapping(
        target = "id",
        expression = "java(toLong( value, 0 ))"
    )
    @Mapping(
        target = "status",
        expression = "java(it.getinsight.module.request.enuns.RequestStatus.valueOf(toString(value, 1 )))"
    )
    @Mapping(
        target = "description",
        expression = "java(toString(value, 2))"
    )
    @Mapping(
        target = "roleId",
        expression = "java(toLong(value, 3))"
    )
    @Mapping(
        target = "userId",
        expression = "java(toLong(value, 4))"
    )
    RequestDTO toMap(Object value);


}
