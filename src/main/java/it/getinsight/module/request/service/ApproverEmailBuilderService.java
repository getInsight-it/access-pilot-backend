package it.getinsight.module.request.service;

import it.getinsight.module.email.dto.EmailDTO;
import it.getinsight.module.notification.enums.NotificationType;
import it.getinsight.module.request.config.EmailNotificationProperties;
import it.getinsight.module.request.entity.RequestEntity;
import it.getinsight.module.role.service.RoleService;
import it.getinsight.module.user.dto.UserDTO;
import it.getinsight.module.user.mapper.UserMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
@RequiredArgsConstructor
@Slf4j
public class ApproverEmailBuilderService {

    private final RoleService roleService;
    private final UserMapper userMapper;
    private final RequestVariableService requestVariableService;
    private final EmailNotificationProperties emailNotificationProperties;

    public List<UserDTO> findApproversForRequest(RequestEntity requestEntity) {
        var approves = roleService.getOrImportApprovesByRequest(requestEntity);

        if (approves.isEmpty()) {
            logNoApproversForRequest(requestEntity);
        }

        return approves;
    }

    private void logNoApproversForRequest(RequestEntity requestEntity) {
        if (requestEntity == null || requestEntity.getRole() == null) {
            log.warn("No approvers found for request without role. Skipping approver notifications.");
            return;
        }

        var requestedRole = requestEntity.getRole();
        var parentRole = requestedRole.getRole();
        var requestedRoleName = requestedRole.getName();
        var parentRoleName = parentRole != null ? parentRole.getName() : "NONE";

        if (requestedRole.getRole() == null) {
            log.warn("No approvers found for request {}. requestedRole={}, mode=FALLBACK_TOP_ROLE, reason=ACCESS_PILOT_ADMIN_UNAVAILABLE",
                requestEntity.getId(), requestedRoleName);
            return;
        }

        if (requestedRole.getRole().getRole() != null) {
            log.warn("No approvers found for request {}. requestedRole={}, expectedParentRole={}, mode=IMMEDIATE_PARENT_ONLY, reason=PARENT_WITHOUT_ELIGIBLE_USERS_NON_TOP",
                requestEntity.getId(), requestedRoleName, parentRoleName);
            return;
        }

        log.warn("No approvers found for request {}. requestedRole={}, expectedParentRole={}, mode=FALLBACK_TOP_PARENT, reason=TOP_PARENT_WITHOUT_USERS_FALLBACK_UNAVAILABLE",
            requestEntity.getId(), requestedRoleName, parentRoleName);
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
