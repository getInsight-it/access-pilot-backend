package it.getinsight.module.web_notification.repository;

import it.getinsight.core.dynamicquery.repository.DynamicNativeQueryRepository;
import it.getinsight.core.dynamicquery.repository.DynamicQueryRepository;
import it.getinsight.module.web_notification.entity.WebNotificationEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WebNotificationRepository extends JpaRepository<WebNotificationEntity, Long>, DynamicQueryRepository, DynamicNativeQueryRepository {
}
