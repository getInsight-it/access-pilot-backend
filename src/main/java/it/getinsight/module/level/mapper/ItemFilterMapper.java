package it.getinsight.module.level.mapper;

import it.getinsight.core.dynamicquery.model.mapper.BaseGenericObjectMapper;
import it.getinsight.core.model.mapper.BaseMapper;
import it.getinsight.module.level.dto.LevelDTO;
import it.getinsight.module.level.dto.LevelFilterDTO;
import it.getinsight.module.level.dto.ItemDTO;
import it.getinsight.module.level.dto.ItemFilterDTO;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ItemFilterMapper extends BaseMapper<ItemFilterDTO, ItemDTO>, BaseGenericObjectMapper<ItemFilterDTO> {}
