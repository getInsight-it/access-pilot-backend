package it.getinsight.module.domain.mapper;

import it.getinsight.core.dynamicquery.model.mapper.BaseGenericObjectMapper;
import it.getinsight.core.model.mapper.BaseMapper;
import it.getinsight.module.domain.dto.DomainDTO;
import it.getinsight.module.domain.dto.DomainFilterDTO;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface DomainFilterMapper extends BaseMapper< DomainFilterDTO , DomainDTO>, BaseGenericObjectMapper<DomainFilterDTO> {}
