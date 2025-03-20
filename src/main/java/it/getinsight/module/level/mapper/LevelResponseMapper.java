package it.getinsight.module.level.mapper;

import it.getinsight.core.dynamicquery.model.mapper.BaseGenericObjectMapper;
import it.getinsight.core.model.mapper.BaseMapper;
import it.getinsight.module.level.dto.LevelResponseDTO;
import it.getinsight.module.level.entity.LevelEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface LevelResponseMapper extends BaseMapper<LevelEntity, LevelResponseDTO>, BaseGenericObjectMapper<LevelResponseDTO> {

    @Override
    LevelEntity toEntity(LevelResponseDTO levelDTO);

    @Override
    @Mapping(target = "parent.parent", ignore = true)
    LevelResponseDTO toDto(LevelEntity levelEntity);

}
