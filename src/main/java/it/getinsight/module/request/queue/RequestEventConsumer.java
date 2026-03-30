package it.getinsight.module.request.queue;

import it.getinsight.core.queue.QueueMessageDTO;
import it.getinsight.core.queue.QueueMessageHandler;
import it.getinsight.core.queue.RoutingKeys;
import it.getinsight.core.security.WithSecurityContext;
import it.getinsight.module.request.queue.dto.RequestEventPayloadDTO;
import it.getinsight.module.request.repository.RequestRepository;
import it.getinsight.module.request.service.RequestNotificationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import static it.getinsight.message.MessageProperty.REQUEST_NOT_FOUND_ERROR;


@Component
@RequiredArgsConstructor
@Slf4j
public class RequestEventConsumer implements QueueMessageHandler<RequestEventPayloadDTO> {

    private static final String EXCHANGE_ID = "request-events";
    private static final String CHANNEL = "request-notification";

    private final RequestRepository requestRepository;
    private final RequestNotificationService requestNotificationService;

    @Override
    public String exchangeId() {
        return EXCHANGE_ID;
    }

    @Override
    public String channel() {
        return CHANNEL;
    }

    @Override
    public Class<RequestEventPayloadDTO> payloadType() {
        return RequestEventPayloadDTO.class;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    @WithSecurityContext(userIdExpression = "#message.userId")
    public void onMessage(QueueMessageDTO<RequestEventPayloadDTO> message, String routingKey) {
        var payload = message.getPayload();
        log.info("Processing request event: routingKey={}, requestId={}, eventType={}",
            routingKey, payload.getRequestId(), payload.getEventType());

        var request = requestRepository.findByIdWithRelationships(payload.getRequestId())
            .orElseThrow(REQUEST_NOT_FOUND_ERROR::resourceNotFoundException);

        switch (routingKey) {
            case RoutingKeys.REQUEST_CREATED ->
                requestNotificationService.notifyRequestCreated(request, message.getUserId());
            case RoutingKeys.REQUEST_STATUS_CHANGED ->
                requestNotificationService.notifyStatusChanged(request, message.getUserId());
            default ->
                log.warn("Unknown routing key: {}", routingKey);
        }
    }
}
