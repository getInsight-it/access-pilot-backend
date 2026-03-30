package it.getinsight.module.notification.service;

import it.getinsight.core.queue.RoutingKeys;
import it.getinsight.module.email.dto.EmailDTO;
import it.getinsight.module.notification.enums.NotificationType;
import it.getinsight.module.notification.queue.dto.NotificationPayloadDTO;
import it.getinsight.module.request.config.EmailNotificationProperties;
import it.getinsight.module.request.repository.RequestRepository;
import it.getinsight.module.request.service.RequestVariableService;
import it.getinsight.module.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;


@Service
@RequiredArgsConstructor
@Slf4j
public class NotificationEmailService {

    private final NotificationService notificationService;
    private final RequestRepository requestRepository;
    private final UserRepository userRepository;
    private final RequestVariableService requestVariableService;
    private final EmailNotificationProperties emailNotificationProperties;

    public void sendEmail(NotificationPayloadDTO payload) {
        if (RoutingKeys.NOTIFICATION_INVITATION_CREATED.equals(payload.getNotificationType())) {
            sendInvitationEmail(payload);
        } else {
            sendRequestEmail(payload);
        }
    }

    private void sendRequestEmail(NotificationPayloadDTO payload) {
        var request = requestRepository.findByIdWithRelationships(payload.getRequestId())
            .orElseThrow(() -> new IllegalStateException("Request not found: " + payload.getRequestId()));

        var recipient = userRepository.findById(payload.getRecipientId())
            .orElseThrow(() -> new IllegalStateException("Recipient not found: " + payload.getRecipientId()));

        var variables = requestVariableService.buildVariables(
            request.getRequestingUser(),
            request.getApprovingUser(),
            request,
            request.getRole(),
            request.getRole().getClient()
        );

        String templateName = resolveTemplateName(payload.getNotificationType());
        String subject = resolveSubject(payload.getNotificationType());

        var emailDTO = EmailDTO.builder()
            .to(recipient.getEmail())
            .subject(subject)
            .templateName(templateName)
            .userId(recipient.getId())
            .isOpened(false)
            .uuid(UUID.randomUUID().toString())
            .type(NotificationType.EMAIL)
            .variables(variables)
            .isHtml(true)
            .build();

        notificationService.send(emailDTO);

        log.info("Email sent: type={}, to={}, requestId={}",
            payload.getNotificationType(), recipient.getEmail(), payload.getRequestId());
    }

    private void sendInvitationEmail(NotificationPayloadDTO payload) {
        var variables = buildInvitationVariables(payload);

        var emailDTO = EmailDTO.builder()
            .to(payload.getInvitationEmail())
            .subject(emailNotificationProperties.getInvitation().getSubject())
            .templateName("invitation.html")
            .isOpened(false)
            .uuid(UUID.randomUUID().toString())
            .type(NotificationType.EMAIL)
            .variables(variables)
            .isHtml(true)
            .build();

        notificationService.send(emailDTO);

        log.info("Invitation email sent: invitationId={}, to={}",
            payload.getInvitationId(), payload.getInvitationEmail());
    }

    private Map<String, Object> buildInvitationVariables(NotificationPayloadDTO payload) {
        Map<String, Object> variables = new HashMap<>();
        variables.put("email", payload.getInvitationEmail());
        variables.put("roleLabel", payload.getInvitationRoleLabel());
        variables.put("clientLabel", payload.getInvitationClientLabel());
        variables.put("levelName", payload.getInvitationLevelName());
        variables.put("codeItem", payload.getInvitationCodeItem());
        variables.put("itemLabel", payload.getInvitationItemLabel());
        variables.put("expiresAt", payload.getInvitationExpiresAt());

        String invitationUrl = emailNotificationProperties.getUrl().getFrontendUrl()
            + "/invitation?invitationUuid=" + payload.getInvitationUuid();

        variables.put("link", Map.of(
            "invitationUrl", invitationUrl,
            "frontendUrl", emailNotificationProperties.getUrl().getFrontendUrl()
        ));

        return variables;
    }

    private String resolveTemplateName(String notificationType) {
        return switch (notificationType) {
            case RoutingKeys.NOTIFICATION_REQUEST_CREATED_APPROVER -> "request.html";
            case RoutingKeys.NOTIFICATION_REQUEST_CREATED_CONFIRMATION,
                 RoutingKeys.NOTIFICATION_REQUEST_STATUS_CHANGED -> "status-request.html";
            default -> "generic-notification.html";
        };
    }

    private String resolveSubject(String notificationType) {
        return switch (notificationType) {
            case RoutingKeys.NOTIFICATION_REQUEST_CREATED_APPROVER ->
                emailNotificationProperties.getApprover().getSubject();
            case RoutingKeys.NOTIFICATION_REQUEST_CREATED_CONFIRMATION,
                 RoutingKeys.NOTIFICATION_REQUEST_STATUS_CHANGED ->
                emailNotificationProperties.getStatusRequest().getSubject();
            default -> "Notificacao AccessPilot";
        };
    }
}
