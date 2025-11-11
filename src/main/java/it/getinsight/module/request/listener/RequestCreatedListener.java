package it.getinsight.module.request.listener;

import it.getinsight.module.request.event.RequestCreatedEvent;
import it.getinsight.module.request.repository.RequestRepository;
import it.getinsight.module.request.service.RequestNotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
@RequiredArgsConstructor
public class RequestCreatedListener {

    private final RequestRepository requestRepository;
    private final RequestNotificationService requestNotificationService;

    @Async("asyncNotifyExecutor")
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT, fallbackExecution = true)
    public void onRequestCreated(RequestCreatedEvent event) {
        requestRepository.findByIdWithRelationships(event.requestId())
            .ifPresent(requestNotificationService::sendNotificationsToApprovers);
    }
}


