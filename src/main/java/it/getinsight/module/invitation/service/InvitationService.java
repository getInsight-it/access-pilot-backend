package it.getinsight.module.invitation.service;

import it.getinsight.core.helper.PaginationHelper;
import it.getinsight.core.pagination.PageableRequestModel;
import it.getinsight.core.pagination.PageableResponseModel;
import it.getinsight.core.util.EmailNormalizationUtil;
import it.getinsight.module.invitation.dto.*;
import it.getinsight.module.invitation.entity.InvitationEntity;
import it.getinsight.module.invitation.enuns.InvitationNextStep;
import it.getinsight.module.invitation.enuns.InvitationPublicStatus;
import it.getinsight.module.invitation.enuns.InvitationStatus;
import it.getinsight.module.invitation.mapper.InvitationListMapper;
import it.getinsight.module.invitation.repository.InvitationRepository;
import it.getinsight.module.invitation.repository.specification.InvitationSpecification;
import it.getinsight.module.keycloak.client.KeycloakClient;
import it.getinsight.module.request.entity.RequestEntity;
import it.getinsight.module.role.repository.RoleRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Optional;

import static it.getinsight.message.MessageProperty.*;

@Service
@RequiredArgsConstructor
@Slf4j
public class InvitationService {

    private final InvitationRepository invitationRepository;
    private final RoleRepository roleRepository;
    private final InvitationListMapper invitationListMapper;
    private final InvitationTokenService invitationTokenService;
    private final InvitationProtocolCodeService invitationProtocolCodeService;
    private final KeycloakClient keycloakClient;

    @Transactional(readOnly = true)
    public InvitationPublicDTO getPublicInvitation(String token) {
        Instant now = Instant.now();
        var invitation = findByToken(token);
        if (invitation.isEmpty()) {
            return new InvitationPublicDTO(InvitationPublicStatus.NOT_FOUND, null, null, null, null, null, null, null);
        }

        var entity = invitation.get();
        var status = computePublicStatus(entity, now);

        var role = new InvitationPublicDTO.RoleResumedDTO(entity.getRole().getId(), entity.getRole().getName(), entity.getRole().getLabel());
        var client = new InvitationPublicDTO.ClientResumedDTO(entity.getRole().getClient().getId(), entity.getRole().getClient().getName(), entity.getRole().getClient().getLabel());
        var level = entity.getRole().getLevel() != null
            ? new InvitationPublicDTO.LevelResumedDTO(entity.getRole().getLevel().getId(), entity.getRole().getLevel().getName())
            : null;

        return new InvitationPublicDTO(
            status,
            entity.getExpiresAt(),
            maskEmail(entity.getEmail()),
            entity.getCodeItem(),
            entity.getDescription(),
            role,
            client,
            level
        );
    }

    @Transactional(readOnly = true)
    public InvitationAuthIntentDTO getAuthIntent(String token) {
        Instant now = Instant.now();
        var invitation = findByToken(token);
        if (invitation.isEmpty()) {
            return new InvitationAuthIntentDTO(InvitationPublicStatus.NOT_FOUND, null, null, null);
        }

        var entity = invitation.get();
        var status = computePublicStatus(entity, now);
        if (!InvitationPublicStatus.VALID.equals(status)) {
            return new InvitationAuthIntentDTO(status, null, null, null);
        }

        InvitationNextStep nextStep = resolveNextStep(entity.getEmail());
        return new InvitationAuthIntentDTO(status, nextStep, entity.getEmail(), token);
    }

    @Transactional(readOnly = true)
    public InvitationRequestContextDTO getRequestContext(String token, String currentUserEmail) {
        var invitation = getValidatedInvitation(token, currentUserEmail);
        var level = invitation.getRole().getLevel();

        return new InvitationRequestContextDTO(
            token,
            invitation.getRole().getClient().getClientId(),
            invitation.getRole().getId(),
            invitation.getRole().getLabel(),
            invitation.getRole().getClient().getLabel(),
            level != null ? level.getId() : null,
            level != null ? level.getName() : null,
            invitation.getCodeItem(),
            invitation.getDescription()
        );
    }

    @Transactional(readOnly = true)
    public InvitationEntity getValidatedInvitation(String token, String currentUserEmail) {
        Instant now = Instant.now();
        var invitation = findByToken(token).orElseThrow(INVITATION_NOT_FOUND_ERROR::resourceNotFoundException);

        validatePending(invitation, now);
        validateEmailBinding(invitation.getEmail(), currentUserEmail);

        return invitation;
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public InvitationEntity getValidatedInvitationForUpdate(String token, String currentUserEmail) {
        Instant now = Instant.now();
        String tokenHash = invitationTokenService.hashToken(token);
        var invitation = invitationRepository.findByTokenHashForUpdate(tokenHash)
            .orElseThrow(INVITATION_NOT_FOUND_ERROR::resourceNotFoundException);

        validatePending(invitation, now);
        validateEmailBinding(invitation.getEmail(), currentUserEmail);

        return invitation;
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public void consumeInvitation(Long invitationId, RequestEntity request) {
        var invitation = invitationRepository.findById(invitationId)
            .orElseThrow(INVITATION_NOT_FOUND_ERROR::resourceNotFoundException);

        if (!InvitationStatus.PENDING.equals(invitation.getStatus())) {
            log.info("Invitation {} already in status {}, skipping consumption", invitationId, invitation.getStatus());
            return;
        }

        invitation.setStatus(InvitationStatus.CONSUMED);
        invitation.setRequest(request);
        invitationRepository.save(invitation);
    }

    @Transactional(readOnly = true)
    public PageableResponseModel<InvitationListDTO> getAllInvitations(PageableRequestModel<InvitationFilterDTO> configPage) {
        Instant now = Instant.now();
        Specification<InvitationEntity> spec = buildSpecification(configPage.getFilter().orElse(null), null, now);
        Page<InvitationEntity> page = invitationRepository.findAll(spec, PaginationHelper.toPageable(configPage));
        return toPageableResponse(page, now);
    }

    @Transactional(readOnly = true)
    public PageableResponseModel<InvitationListDTO> getAllInvitationsMine(String currentUserEmail, PageableRequestModel<InvitationFilterDTO> configPage) {
        Instant now = Instant.now();
        String normalizedEmail = EmailNormalizationUtil.normalize(currentUserEmail);
        Specification<InvitationEntity> spec = buildSpecification(configPage.getFilter().orElse(null), normalizedEmail, now);
        Page<InvitationEntity> page = invitationRepository.findAll(spec, PaginationHelper.toPageable(configPage));
        return toPageableResponse(page, now);
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public void cancelInvitation(Long invitationId) {
        var invitation = invitationRepository.findById(invitationId).orElseThrow(INVITATION_NOT_FOUND_ERROR::resourceNotFoundException);

        if (InvitationStatus.CANCELLED.equals(invitation.getStatus())) {
            return;
        }
        if (InvitationStatus.CONSUMED.equals(invitation.getStatus())) {
            throw INVITATION_CONSUMED_ERROR.businessException();
        }
        if (!InvitationStatus.PENDING.equals(invitation.getStatus())) {
            throw INVITATION_CANCELLED_ERROR.businessException();
        }

        invitation.setStatus(InvitationStatus.CANCELLED);
        invitationRepository.save(invitation);
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public InvitationCreateResponseDTO createInvitations(@Valid InvitationCreateRequestDTO request) {
        var role = roleRepository.findById(request.roleId()).orElseThrow(ROLE_NOT_FOUND_ERROR::resourceNotFoundException);
        var protocolCode = StringUtils.isNotBlank(request.protocolCode())
            ? request.protocolCode()
            : invitationProtocolCodeService.generateProtocolCode();

        var created = new ArrayList<InvitationCreateResponseDTO.InvitationCreatedDTO>();
        for (String rawEmail : request.emails()) {
            String email = EmailNormalizationUtil.normalize(rawEmail);
            String token = invitationTokenService.generateToken();

            InvitationEntity entity = InvitationEntity.builder()
                .tokenHash(invitationTokenService.hashToken(token))
                .email(email)
                .role(role)
                .codeItem(request.codeItem())
                .description(request.description())
                .status(InvitationStatus.PENDING)
                .expiresAt(request.expiresAt())
                .protocolCode(protocolCode)
                .build();

            token = saveWithRetryOnTokenCollision(entity, token);
            created.add(new InvitationCreateResponseDTO.InvitationCreatedDTO(email, token, request.expiresAt()));
        }

        return new InvitationCreateResponseDTO(protocolCode, created);
    }

    private PageableResponseModel<InvitationListDTO> toPageableResponse(Page<InvitationEntity> page, Instant now) {
        var items = invitationListMapper.toDto(page.getContent(), now);
        return PaginationHelper.toPageResponse(items, page.getTotalElements());
    }

    private Specification<InvitationEntity> buildSpecification(InvitationFilterDTO filter, String email, Instant now) {
        return Specification.where(InvitationSpecification.emailEquals(email))
            .and(InvitationSpecification.hasStatus(filter != null ? filter.status() : null, now));
    }

    private Optional<InvitationEntity> findByToken(String token) {
        if (StringUtils.isBlank(token)) {
            return Optional.empty();
        }
        String tokenHash = invitationTokenService.hashToken(token);
        return invitationRepository.findByTokenHash(tokenHash);
    }

    private InvitationPublicStatus computePublicStatus(InvitationEntity invitation, Instant now) {
        if (InvitationStatus.CANCELLED.equals(invitation.getStatus())) {
            return InvitationPublicStatus.CANCELLED;
        }
        if (InvitationStatus.CONSUMED.equals(invitation.getStatus())) {
            return InvitationPublicStatus.CONSUMED;
        }
        if (isExpired(invitation, now)) {
            return InvitationPublicStatus.EXPIRED;
        }
        return InvitationPublicStatus.VALID;
    }

    private boolean isExpired(InvitationEntity invitation, Instant now) {
        return invitation.getExpiresAt() != null && invitation.getExpiresAt().isBefore(now);
    }

    private void validatePending(InvitationEntity invitation, Instant now) {
        if (InvitationStatus.CANCELLED.equals(invitation.getStatus())) {
            throw INVITATION_CANCELLED_ERROR.businessException();
        }
        if (InvitationStatus.CONSUMED.equals(invitation.getStatus())) {
            throw INVITATION_CONSUMED_ERROR.businessException();
        }
        if (isExpired(invitation, now)) {
            throw INVITATION_EXPIRED_ERROR.businessException();
        }
        if (!InvitationStatus.PENDING.equals(invitation.getStatus())) {
            throw INVITATION_NOT_FOUND_ERROR.resourceNotFoundException();
        }
    }

    private void validateEmailBinding(String invitationEmail, String currentUserEmail) {
        if (!EmailNormalizationUtil.normalize(invitationEmail).equals(EmailNormalizationUtil.normalize(currentUserEmail))) {
            throw INVITATION_EMAIL_MISMATCH_ERROR.accessForbiddenException();
        }
    }

    private String maskEmail(String email) {
        if (StringUtils.isBlank(email) || !email.contains("@")) {
            return null;
        }
        String[] parts = email.split("@", 2);
        String local = parts[0];
        String domain = parts[1];
        int visibleChars = Math.min(3, local.length());
        String visible = local.isEmpty() ? "" : local.substring(0, visibleChars);
        return "%s***@%s".formatted(visible, domain);
    }

    private InvitationNextStep resolveNextStep(String email) {
        try {
            var users = keycloakClient.getUsersByEmail(email, true);
            return (users == null || users.isEmpty())
                ? InvitationNextStep.REDIRECT_TO_REGISTER
                : InvitationNextStep.REDIRECT_TO_LOGIN;
        } catch (Exception e) {
            log.warn("Keycloak lookup failed; falling back to login", e);
            return InvitationNextStep.REDIRECT_TO_LOGIN;
        }
    }

    private String saveWithRetryOnTokenCollision(InvitationEntity entity, String initialToken) {
        String token = initialToken;
        for (int attempt = 0; attempt < 3; attempt++) {
            try {
                invitationRepository.save(entity);
                return token;
            } catch (DataIntegrityViolationException e) {
                if (attempt == 2) {
                    throw e;
                }
                token = invitationTokenService.generateToken();
                entity.setTokenHash(invitationTokenService.hashToken(token));
            }
        }
        return token;
    }
}
