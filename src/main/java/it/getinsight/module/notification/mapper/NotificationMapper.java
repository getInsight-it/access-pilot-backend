package it.getinsight.module.notification.mapper;

import it.getinsight.module.email.dto.EmailFilterDTO;
import it.getinsight.module.email.entity.EmailSentEntity;
import it.getinsight.module.notification.dto.NotificationDTO;
import it.getinsight.module.notification.dto.NotificationFilterDTO;
import it.getinsight.module.notification.entity.NotificationEntity;
import it.getinsight.module.web_notification.dto.WebNotificationFilterDTO;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface NotificationMapper {

    NotificationDTO toDto(NotificationEntity notificationEntity);

    List<NotificationDTO> toDto(List<NotificationEntity> notificationEntity);

    EmailFilterDTO toEmailFilterDTO(NotificationFilterDTO filterDTO);

    WebNotificationFilterDTO toWebNotificationDTO(NotificationFilterDTO filterDTO);

    EmailSentEntity toEntityEmail(NotificationFilterDTO notificationDTO);

}
