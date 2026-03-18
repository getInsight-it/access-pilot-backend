package it.getinsight.module.request.service;

import it.getinsight.module.email.dto.EmailDTO;
import it.getinsight.module.keycloak.service.IdentityProviderService;
import it.getinsight.module.notification.enums.NotificationType;
import it.getinsight.module.request.config.EmailNotificationProperties;
import it.getinsight.module.request.entity.RequestEntity;
import it.getinsight.module.request.enuns.RequestStatus;
import it.getinsight.module.user.dto.UserDTO;
import it.getinsight.module.user.mapper.UserMapper;
import it.getinsight.module.user.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
@RequiredArgsConstructor
@Slf4j
public class ApproverEmailBuilderService {

    private final IdentityProviderService identityProviderService;
    private final UserService userService;
    private final UserMapper userMapper;
    private final RequestVariableService requestVariableService;
    private final EmailNotificationProperties emailNotificationProperties;

    public List<UserDTO> findApproversForRole(RequestEntity requestEntity) {
        var roleEntity = requestEntity.getRole().getRole();

        var approves = identityProviderService.getUsersByClientUUIDAndRoleName(roleEntity.getClient().getClientUUID(), roleEntity.getName())
            .stream()
            .map(user -> userService.findOrImportByExternalId(user.id()))
            .toList();

        if (approves.isEmpty()) {
            log.warn("No approvers found for role {}. Skipping approver notifications.", roleEntity.getName());
        }

        return approves;
    }


    public EmailDTO buildEmailForApprover(RequestEntity requestEntity, it.getinsight.module.user.dto.UserDTO approverDTO) {
        return EmailDTO.builder()
            .to(approverDTO.email())
            .userId(approverDTO.id())
            .isOpened(false)
            .type(NotificationType.EMAIL)
            .subject(emailNotificationProperties.getApprover().getSubject())
            .templateName("request.html")
            .variables(requestVariableService.buildVariables(
                requestEntity.getRequestingUser(),
                userMapper.toEntity(approverDTO),
                requestEntity,
                requestEntity.getRole(),
                requestEntity.getRole().getClient()
            ))
            .isHtml(true)
            .build();
    }
}
