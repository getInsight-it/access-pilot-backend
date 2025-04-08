package it.getinsight.module.level.mapper;

import it.getinsight.core.dynamicquery.model.mapper.BaseGenericObjectMapper;
import it.getinsight.core.model.mapper.BaseMapper;
import it.getinsight.module.level.dto.LevelHierarchyResumedDTO;
import it.getinsight.module.level.entity.LevelEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface LevelHierarchyResumedMapper extends BaseMapper<LevelEntity, LevelHierarchyResumedDTO>, BaseGenericObjectMapper<LevelHierarchyResumedDTO> {

    @Override
    LevelEntity toEntity(LevelHierarchyResumedDTO levelDTO);

    @Override
    @Mapping(target = "parent.parent", ignore = true)
    LevelHierarchyResumedDTO toDto(LevelEntity levelEntity);

}
