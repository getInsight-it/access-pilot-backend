package it.getinsight.module.request.service;

import it.getinsight.module.configuration.entity.AttachmentConfigurationEntity;
import it.getinsight.module.invitation.entity.InvitationEntity;
import it.getinsight.module.invitation.service.InvitationService;
import it.getinsight.module.request.dto.RequestDTO;
import it.getinsight.module.request.entity.RequestEntity;
import it.getinsight.module.request.enuns.RequestStatus;
import it.getinsight.module.request.mapper.RequestMapper;
import it.getinsight.module.request.repository.RequestRepository;
import it.getinsight.module.role.entity.RoleEntity;
import it.getinsight.module.role.repository.RoleRepository;
import it.getinsight.module.role.service.RoleLevelPolicyService;
import it.getinsight.module.user.entity.UserEntity;
import it.getinsight.module.user.repository.UserRepository;
import it.getinsight.module.user.service.AuthenticationContextService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.MultiValueMap;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

import static it.getinsight.message.MessageProperty.REQUEST_NOT_FOUND_ERROR;

@Service
@RequiredArgsConstructor
@Slf4j
public class RequestCreationService {

    private final RequestRepository requestRepository;
    private final RequestMapper requestMapper;
    private final RoleRepository roleRepository;
    private final UserRepository userRepository;
    private final AuthenticationContextService authenticationContextService;

    private final RequestValidationService requestValidationService;
    private final RequestAttachmentService requestAttachmentService;
    private final ProtocolGeneratorService protocolGeneratorService;
    private final RoleLevelPolicyService roleLevelPolicyService;
    private final RequestNotificationService requestNotificationService;
    private final InvitationService invitationService;

    @Transactional(propagation = Propagation.REQUIRED)
    public RequestDTO createRequest(RequestDTO requestDTO, MultiValueMap<String, MultipartFile> attachments) {
        var invitation = processInvitationIfPresent(requestDTO);

        log.debug("Starting request creation process for role: {}", requestDTO.role().id());

        var roleEntity = findAndValidateRole(requestDTO);
        var configurations = roleEntity.getClient().getConfigurations();

        validateRequestCreation(requestDTO, roleEntity, configurations, attachments);

        var currentUserId = authenticationContextService.getCurrentUserId();
        var user = findOrCreateUser(currentUserId);

        var entity = buildRequestEntityWithPendingStatus(requestDTO, user, roleEntity);

        requestRepository.save(entity);
        log.info("Request created with protocol: {} for user: {}", entity.getProtocolCode(), user.getEmail());

        requestAttachmentService.saveRequestFiles(attachments, configurations, entity);

        requestNotificationService.publishRequestCreated(entity, user.getId().toString());

        if (invitation != null) {
            invitationService.consumeInvitation(invitation.getId(), entity);
        }

        return requestMapper.toDto(entity);
    }

    private InvitationEntity processInvitationIfPresent(RequestDTO requestDTO) {
        if (StringUtils.isBlank(requestDTO.invitationUuid())) {
            return null;
        }
        String currentUserEmail = authenticationContextService.getCurrentUserEmail();
        return invitationService.getValidatedInvitationForUpdate(requestDTO.invitationUuid(), currentUserEmail);
    }

    private RoleEntity findAndValidateRole(RequestDTO requestDTO) {
        return roleRepository.findById(requestDTO.role().id())
            .orElseThrow(REQUEST_NOT_FOUND_ERROR::resourceNotFoundException);
    }

    private void validateRequestCreation(RequestDTO requestDTO, RoleEntity roleEntity,
                                        List<AttachmentConfigurationEntity> configurations,
                                        MultiValueMap<String, MultipartFile> attachments) {
        requestValidationService.validateItemExistence(requestDTO.codeItem(), roleEntity);
        requestValidationService.validateAttachments(configurations, attachments);

        if (roleEntity.getRole() != null) {
            Long parentLevelId = roleEntity.getRole().getLevel() != null ? roleEntity.getRole().getLevel().getId() : null;
            Long childLevelId = roleEntity.getLevel() != null ? roleEntity.getLevel().getId() : null;

            roleLevelPolicyService.validateChildLevelAssignment(parentLevelId,childLevelId);
        }
    }

    private RequestEntity buildRequestEntityWithPendingStatus(RequestDTO requestDTO, UserEntity user, RoleEntity roleEntity) {
        var entity = requestMapper.toEntity(requestDTO);
        entity.setRequestingUser(user);
        entity.setRole(roleEntity);
        entity.setLevel(roleEntity.getLevel());
        entity.setCodeItem(requestDTO.codeItem());
        entity.setStatus(RequestStatus.PENDING);
        entity.setProtocolCode(protocolGeneratorService.generateUniqueProtocolCode());
        return entity;
    }

    private UserEntity findOrCreateUser(String userId) {
        return userRepository.findByExternalId(userId).orElseGet(() -> {
            var jwt = authenticationContextService.getCurrentJwt();
            UserEntity user = new UserEntity();
            user.setExternalId(userId);
            user.setFirstName(jwt.getClaimAsString("given_name"));
            user.setLastName(jwt.getClaimAsString("family_name"));
            user.setEmail(jwt.getClaimAsString("email"));
            log.info("Creating new user in database: {}", user.getEmail());
            return userRepository.save(user);
        });
    }
}
