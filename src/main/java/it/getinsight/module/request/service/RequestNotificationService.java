package it.getinsight.module.request.service;

import it.getinsight.module.email.dto.EmailDTO;
import it.getinsight.module.notification.enums.NotificationType;
import it.getinsight.module.notification.service.NotificationService;
import it.getinsight.module.request.config.EmailNotificationProperties;
import it.getinsight.module.request.entity.RequestEntity;
import it.getinsight.module.request.repository.RequestRepository;
import it.getinsight.module.user.mapper.UserMapper;
import it.getinsight.module.web_notification.dto.WebNotificationDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class RequestNotificationService {

    private final NotificationService notificationService;
    private final RequestVariableService requestVariableService;
    private final ApproverEmailBuilderService approverEmailBuilderService;
    private final EmailNotificationProperties emailNotificationProperties;
    private final RequestRepository requestRepository;
    private final UserMapper userMapper;

    @Transactional
    public void sendNotificationsToApprovers(RequestEntity requestEntity) {
        var approvers = approverEmailBuilderService.findApproversForRole(requestEntity);
        approverEmailBuilderService.updateRequestStatusToPending(requestEntity);

        approvers.stream()
            .map(approver -> approverEmailBuilderService.buildEmailForApprover(requestEntity, approver))
            .forEach(emailDTO -> {
                requestRepository.save(requestEntity);
                notificationService.send(emailDTO);
            });

        var variables = requestVariableService.buildVariables(
            requestEntity.getRequestingUser(), null,
            requestEntity, requestEntity.getRole(),
            requestEntity.getRole().getClient()
        );
        sendNotificationStatusToUser(requestEntity, variables);
    }

    public void sendNotificationStatusToUser(RequestEntity requestEntity, Map<String, Object> variables) {
        var userDTO = userMapper.toDto(requestEntity.getRequestingUser());
        var emailDTO = EmailDTO.builder()
            .to(userDTO.email())
            .subject(emailNotificationProperties.getStatusRequest().getSubject())
            .templateName("status-request.html")
            .userId(userDTO.id())
            .isOpened(false)
            .uuid(UUID.randomUUID().toString())
            .type(NotificationType.EMAIL)
            .variables(variables)
            .isHtml(true)
            .build();
        notificationService.send(emailDTO);

        var webDTO = WebNotificationDTO.builder()
            .userId(userDTO.id())
            .title("protocolo: " + requestEntity.getProtocolCode())
            .uuid(UUID.randomUUID().toString())
            .requestId(requestEntity.getId())
            .isOpened(false)
            .type(NotificationType.WEB)
            .priority(1L)
            .description(requestEntity.getDescription())
            .build();
        notificationService.send(webDTO);
    }
}
