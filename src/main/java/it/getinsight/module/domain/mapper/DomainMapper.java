package it.getinsight.module.domain.mapper;

import it.getinsight.core.dynamicquery.model.mapper.BaseGenericObjectMapper;
import it.getinsight.core.model.mapper.BaseMapper;
import it.getinsight.module.domain.dto.DomainDTO;
import it.getinsight.module.domain.entity.DomainEntity;
import org.mapstruct.*;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface DomainMapper extends BaseMapper<DomainEntity, DomainDTO>, BaseGenericObjectMapper<DomainDTO> {

    @Override
    @Mapping(target = "parent", expression = "java(mapParent(domainDTO.parentId()))")
    DomainEntity toEntity(DomainDTO domainDTO);

    @Override
    @Mapping(target = "parentId", source = "parent.id")
    DomainDTO toDto(DomainEntity domainEntity);


    default DomainEntity mapParent(Long parentId) {
        if (parentId == null) {
            return null;
        }
        return DomainEntity.builder().id(parentId).build();
    }

}
