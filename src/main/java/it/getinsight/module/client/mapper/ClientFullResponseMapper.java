package it.getinsight.module.client.mapper;

import it.getinsight.core.dynamicquery.model.mapper.BaseGenericObjectMapper;
import it.getinsight.core.model.mapper.BaseMapper;
import it.getinsight.module.client.dto.ClientFullResponseDTO;
import it.getinsight.module.client.entity.ClientEntity;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ClientFullResponseMapper extends BaseMapper<ClientEntity, ClientFullResponseDTO>, BaseGenericObjectMapper<ClientFullResponseDTO> {

}
