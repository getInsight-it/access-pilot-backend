package it.getinsight.module.email.repository;

import it.getinsight.core.dynamicquery.repository.DynamicNativeQueryRepository;
import it.getinsight.core.dynamicquery.repository.DynamicQueryRepository;
import it.getinsight.module.email.entity.EmailSentEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EmailRepository extends JpaRepository<EmailSentEntity, Long>, DynamicQueryRepository, DynamicNativeQueryRepository {
}
