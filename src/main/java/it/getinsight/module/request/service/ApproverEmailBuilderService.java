package it.getinsight.module.request.service;

import it.getinsight.module.email.dto.EmailDTO;
import it.getinsight.module.request.config.EmailNotificationProperties;
import it.getinsight.module.keycloak.service.IdentityProviderService;
import it.getinsight.module.notification.enums.NotificationType;
import it.getinsight.module.request.entity.RequestEntity;
import it.getinsight.module.request.enuns.RequestStatus;
import it.getinsight.module.request.repository.RequestRepository;
import it.getinsight.module.user.mapper.UserMapper;
import it.getinsight.module.user.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static it.getinsight.message.MessageProperty.*;

/**
 * Serviço responsável exclusivamente por construir emails para aprovadores.
 * Aplica SRP de forma agressiva - apenas construção de DTOs de email.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class ApproverEmailBuilderService {

    private final IdentityProviderService identityProviderService;
    private final UserService userService;
    private final UserMapper userMapper;
    private final RequestVariableService requestVariableService;
    private final EmailNotificationProperties emailNotificationProperties;

    /**
     * Busca todos os aprovadores para um role específico.
     */
    public java.util.List<it.getinsight.module.user.dto.UserDTO> findApproversForRole(RequestEntity requestEntity) {
        var roleEntity = requestEntity.getRole().getRole();
        var approves = identityProviderService.getUsersByClientUUIDAndRoleName(roleEntity.getClient().getClientUUID(), roleEntity.getName())
            .stream()
            .map(user -> userService.findOrImportByExternalId(user.id()))
            .toList();

        if (approves.isEmpty()) {
            log.warn("No approvers found for role {}", roleEntity.getName());
            throw APPROVERS_NOT_FOUND_ERROR.businessException();
        }

        return approves;
    }

    /**
     * Constrói um EmailDTO para um aprovador específico.
     */
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

    /**
     * Atualiza o status do request para PENDING.
     */
    public void updateRequestStatusToPending(RequestEntity requestEntity) {
        requestEntity.setStatus(RequestStatus.PENDING);
    }
}
