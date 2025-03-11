package it.getinsight.module.level.mapper;

import it.getinsight.core.dynamicquery.model.mapper.BaseGenericObjectMapper;
import it.getinsight.core.model.mapper.BaseMapper;
import it.getinsight.module.level.dto.ItemDTO;
import it.getinsight.module.level.entity.ItemEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ItemMapper extends BaseMapper<ItemEntity, ItemDTO>, BaseGenericObjectMapper<ItemDTO> {

    @Mapping(target = "parent", expression = "java(mapParent(itemDTO.parentId()))")
    @Mapping(target = "level.id", source = "levelId")
    ItemEntity toEntity(ItemDTO itemDTO);

    @Mapping(target = "parentId", source = "parent.id")
    @Mapping(target = "levelId", source = "level.id")
    ItemDTO toDto(ItemEntity itemEntity);



    default ItemEntity mapParent(Long parentId) {
        if (parentId == null) {
            return null;
        }
        return ItemEntity.builder().id(parentId).build();
    }

}
