package it.getinsight.module.notification.repository;

import it.getinsight.module.notification.entity.NotificationEntity;
import it.getinsight.module.notification.enums.NotificationType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NotificationRepository extends JpaRepository<NotificationEntity, Long> {

    Long countByUser_ExternalIdAndIsOpenedAndType(String externalId, Boolean isOpened, NotificationType type);

}
