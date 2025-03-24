package it.getinsight.module.level.mapper;

import it.getinsight.core.dynamicquery.model.mapper.BaseGenericObjectMapper;
import it.getinsight.core.model.mapper.BaseMapper;
import it.getinsight.module.level.dto.ItemHierarchyDTO;
import it.getinsight.module.level.entity.ItemEntity;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ItemHierarchyMapper extends BaseMapper<ItemEntity, ItemHierarchyDTO>, BaseGenericObjectMapper<ItemHierarchyDTO> {

    ItemEntity toEntity(ItemHierarchyDTO itemDTO);

    ItemHierarchyDTO toDto(ItemEntity itemEntity);




}
