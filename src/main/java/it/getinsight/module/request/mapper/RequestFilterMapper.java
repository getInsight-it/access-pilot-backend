package it.getinsight.module.request.mapper;

import it.getinsight.core.dynamicquery.model.mapper.BaseGenericObjectMapper;
import it.getinsight.core.model.mapper.BaseMapper;
import it.getinsight.module.request.dto.RequestDTO;
import it.getinsight.module.request.dto.RequestFilterDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface RequestFilterMapper extends BaseMapper<RequestFilterDTO, RequestDTO>, BaseGenericObjectMapper<RequestDTO> {

    @Mapping(
        target = "role.name",
        source = "roleName"
    )
    @Mapping(
        target = "role.client.clientId",
        source = "clientId"
    )
    @Mapping(
        target = "requestingUser.id",
        source = "requestingUserId"
    )
    RequestDTO toDto(RequestFilterDTO filterDTO);


}
