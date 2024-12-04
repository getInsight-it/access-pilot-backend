package it.getinsight.module.keycloak.mapper;

import it.getinsight.core.dynamicquery.model.mapper.BaseGenericObjectMapper;
import it.getinsight.core.model.mapper.BaseMapper;
import it.getinsight.module.client.entity.ClientEntity;
import it.getinsight.module.keycloak.dto.ClientRepresentationDTO;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface KeycloakMapper extends BaseMapper<ClientEntity, ClientRepresentationDTO>, BaseGenericObjectMapper<ClientRepresentationDTO> {


}
