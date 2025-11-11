package it.getinsight.module.request.listener;

import it.getinsight.module.request.event.RequestStatusToUserEvent;
import it.getinsight.module.request.repository.RequestRepository;
import it.getinsight.module.request.service.RequestNotificationService;
import it.getinsight.module.request.service.RequestVariableService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
@RequiredArgsConstructor
public class RequestStatusToUserListener {

    private final RequestRepository requestRepository;
    private final RequestNotificationService requestNotificationService;
    private final RequestVariableService requestVariableService;

    @Async("asyncNotifyExecutor")
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT, fallbackExecution = true)
    public void onStatusToUser(RequestStatusToUserEvent event) {
        requestRepository.findByIdWithRelationships(event.requestId())
            .ifPresent(entity -> {
                var variables = requestVariableService.buildVariables(
                    entity.getRequestingUser(),
                    entity.getApprovingUser(),
                    entity,
                    entity.getRole(),
                    entity.getRole().getClient()
                );
                requestNotificationService.sendNotificationStatusToUser(entity, variables);
            });
    }
}


