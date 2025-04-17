package it.getinsight.module.configuration.mapper;

import it.getinsight.core.dynamicquery.model.mapper.BaseGenericObjectMapper;
import it.getinsight.core.model.mapper.BaseMapper;
import it.getinsight.module.configuration.dto.AttachmentConfigurationDTO;
import it.getinsight.module.configuration.dto.AttachmentConfigurationFilterDTO;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface AttachmentConfigurationFilterMapper extends BaseMapper<AttachmentConfigurationFilterDTO, AttachmentConfigurationDTO>, BaseGenericObjectMapper<AttachmentConfigurationFilterDTO> {}
