package it.getinsight.module.domain.mapper;

import it.getinsight.core.dynamicquery.model.mapper.BaseGenericObjectMapper;
import it.getinsight.core.model.mapper.BaseMapper;
import it.getinsight.module.domain.dto.DomainDTO;
import it.getinsight.module.domain.dto.DomainFilterDTO;
import it.getinsight.module.domain.dto.ItemDTO;
import it.getinsight.module.domain.dto.ItemFilterDTO;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ItemFilterMapper extends BaseMapper<ItemFilterDTO, ItemDTO>, BaseGenericObjectMapper<ItemFilterDTO> {}
