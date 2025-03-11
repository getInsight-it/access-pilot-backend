package it.getinsight.module.level.mapper;

import it.getinsight.core.dynamicquery.model.mapper.BaseGenericObjectMapper;
import it.getinsight.core.model.mapper.BaseMapper;
import it.getinsight.module.level.dto.LevelDTO;
import it.getinsight.module.level.entity.LevelEntity;
import org.mapstruct.*;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface LevelMapper extends BaseMapper<LevelEntity, LevelDTO>, BaseGenericObjectMapper<LevelDTO> {

    @Override
    @Mapping(target = "parent", expression = "java(mapParent(levelDTO.parentId()))")
    LevelEntity toEntity(LevelDTO levelDTO);

    @Override
    @Mapping(target = "parentId", source = "parent.id")
    LevelDTO toDto(LevelEntity levelEntity);


    default LevelEntity mapParent(Long parentId) {
        if (parentId == null) {
            return null;
        }
        return LevelEntity.builder().id(parentId).build();
    }

}
