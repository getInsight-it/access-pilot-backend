package it.getinsight.module.storage.mapper;

import it.getinsight.core.dynamicquery.model.mapper.BaseGenericObjectMapper;
import it.getinsight.core.model.mapper.BaseMapper;
import it.getinsight.module.storage.dto.StorageFileDTO;
import it.getinsight.module.storage.dto.StorageFileFilterDTO;
import org.mapstruct.*;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface StorageFileFilterMapper extends BaseMapper<StorageFileFilterDTO, StorageFileDTO>, BaseGenericObjectMapper<StorageFileDTO> {


}
