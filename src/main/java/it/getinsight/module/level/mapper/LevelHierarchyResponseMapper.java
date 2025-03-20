package it.getinsight.module.level.mapper;

import it.getinsight.core.dynamicquery.model.mapper.BaseGenericObjectMapper;
import it.getinsight.core.model.mapper.BaseMapper;
import it.getinsight.module.level.dto.LevelResponseDTO;
import it.getinsight.module.level.entity.LevelEntity;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface LevelHierarchyResponseMapper extends BaseMapper<LevelEntity, LevelResponseDTO>, BaseGenericObjectMapper<LevelResponseDTO> {

    @Override
    LevelEntity toEntity(LevelResponseDTO levelDTO);

    @Override
    LevelResponseDTO toDto(LevelEntity levelEntity);

}
