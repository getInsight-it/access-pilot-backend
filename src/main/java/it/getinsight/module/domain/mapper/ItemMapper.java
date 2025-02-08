package it.getinsight.module.domain.mapper;

import it.getinsight.core.dynamicquery.model.mapper.BaseGenericObjectMapper;
import it.getinsight.core.model.mapper.BaseMapper;
import it.getinsight.module.domain.dto.ItemDTO;
import it.getinsight.module.domain.entity.ItemEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ItemMapper extends BaseMapper<ItemEntity, ItemDTO>, BaseGenericObjectMapper<ItemDTO> {

    @Mapping(target = "parent", expression = "java(mapParent(itemDTO.parentId()))")
    @Mapping(target = "domain.id", source = "domainId")
    ItemEntity toEntity(ItemDTO itemDTO);

    @Mapping(target = "parentId", source = "parent.id")
    @Mapping(target = "domainId", source = "domain.id")
    ItemDTO toDto(ItemEntity itemEntity);



    default ItemEntity mapParent(Long parentId) {
        if (parentId == null) {
            return null;
        }
        return ItemEntity.builder().id(parentId).build();
    }

}
