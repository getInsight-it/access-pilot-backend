package it.getinsight.module.web_notification.mapper;

import it.getinsight.core.dynamicquery.model.mapper.BaseGenericObjectMapper;
import it.getinsight.core.model.mapper.BaseMapper;
import it.getinsight.module.web_notification.dto.WebNotificationDTO;
import it.getinsight.module.web_notification.dto.WebNotificationFilterDTO;
import it.getinsight.module.web_notification.entity.WebNotificationEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface WebNotificationMapper extends BaseMapper<WebNotificationEntity, WebNotificationDTO>, BaseGenericObjectMapper<WebNotificationDTO> {

    @Mapping(source = "user.id", target = "userId")
    @Mapping(source = "request.id", target = "requestId")
    WebNotificationDTO toDto(WebNotificationEntity webNotificationEntity);

    @Mapping(source = "userId", target = "user.id")
    @Mapping(source = "externalId", target = "user.externalId")
    @Mapping(source = "requestId", target = "request.id")
    @Mapping(source = "title", target = "title")
    @Mapping(source = "description", target = "description")
    @Mapping(source = "type", target = "type")
    @Mapping(source = "priority", target = "priority")
    @Mapping(source = "isOpened", target = "isOpened")
    WebNotificationEntity toEntity(WebNotificationDTO webNotificationDTO);

    WebNotificationDTO fromFilter(WebNotificationFilterDTO dto);

}
