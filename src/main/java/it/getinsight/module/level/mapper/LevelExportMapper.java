package it.getinsight.module.level.mapper;

import it.getinsight.module.level.dto.LevelExportDTO;
import it.getinsight.module.level.entity.LevelEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface LevelExportMapper {

    @Mapping(target = "parentName", expression = "java(level.getParent() != null ? level.getParent().getName() : null)")
    LevelExportDTO toExport(LevelEntity level);
}
