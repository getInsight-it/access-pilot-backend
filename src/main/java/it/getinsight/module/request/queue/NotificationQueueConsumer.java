package it.getinsight.module.request.queue;

import it.getinsight.core.queue.QueueConsumer;
import it.getinsight.core.queue.QueueMessageDTO;
import it.getinsight.core.security.WithSecurityContext;
import it.getinsight.module.request.repository.RequestRepository;
import it.getinsight.module.request.service.RequestNotificationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Component("requestCreatedQueueConsumer")
@RequiredArgsConstructor
@Slf4j
public class NotificationQueueConsumer implements QueueConsumer {

    private final RequestRepository requestRepository;
    private final RequestNotificationService requestNotificationService;

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    @WithSecurityContext(userIdExpression = "#message.userId")
    public void onMessage(QueueMessageDTO message) {
        String correlationId = MDC.get("correlationId");
        Long requestId = Long.parseLong(message.getObjectIdentifier());

        log.info("Processing REQUEST_CREATED notification: requestId={}, correlationId={}",
            requestId, correlationId);

        requestRepository.findByIdWithRelationships(requestId)
            .ifPresentOrElse(
                request -> {
                    requestNotificationService.sendNotificationsToApprovers(request);
                    log.info("REQUEST_CREATED notification sent: requestId={}, correlationId={}",
                        requestId, correlationId);
                },
                () -> log.warn("Request not found: requestId={}, correlationId={}", requestId, correlationId)
            );
    }
}
