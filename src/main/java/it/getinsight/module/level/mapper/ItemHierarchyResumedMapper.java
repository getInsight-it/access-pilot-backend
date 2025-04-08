package it.getinsight.module.level.mapper;

import it.getinsight.core.dynamicquery.model.mapper.BaseGenericObjectMapper;
import it.getinsight.core.model.mapper.BaseMapper;
import it.getinsight.module.level.dto.ItemHierarchyDTO;
import it.getinsight.module.level.dto.ItemHierarchyResumedDTO;
import it.getinsight.module.level.entity.ItemEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ItemHierarchyResumedMapper extends BaseMapper<ItemEntity, ItemHierarchyResumedDTO>, BaseGenericObjectMapper<ItemHierarchyResumedDTO> {

    @Mapping(target = "parent.parent", ignore = true)
    ItemHierarchyResumedDTO toDto(ItemEntity itemEntity);

    ItemEntity toEntity(ItemHierarchyDTO itemDTO);


}
