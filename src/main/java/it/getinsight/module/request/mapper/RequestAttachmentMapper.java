package it.getinsight.module.request.mapper;

import it.getinsight.core.dynamicquery.model.mapper.BaseGenericObjectMapper;
import it.getinsight.core.model.mapper.BaseMapper;
import it.getinsight.module.request.dto.RequestAttachmentDTO;
import it.getinsight.module.request.entity.RequestAttachmentEntity;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface RequestAttachmentMapper extends BaseMapper<RequestAttachmentEntity, RequestAttachmentDTO>, BaseGenericObjectMapper<RequestAttachmentDTO> {


}
