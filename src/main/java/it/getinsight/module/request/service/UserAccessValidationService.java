package it.getinsight.module.request.service;

import it.getinsight.module.request.entity.RequestEntity;
import it.getinsight.module.role.repository.RoleRepository;
import it.getinsight.module.user.service.AuthenticationContextService;
import it.getinsight.module.user.service.SecurityScopes;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import static it.getinsight.message.MessageProperty.APPROVE_NOT_AUTHORIZED;
import static it.getinsight.message.MessageProperty.USER_NOT_AUTHORIZED;


@Service
@RequiredArgsConstructor
@Slf4j
public class UserAccessValidationService {

    private final RoleRepository roleRepository;
    private final AuthenticationContextService authenticationContextService;
    private final SecurityScopes securityScopes;


    public void validateUserAccessToRequest(Long requestId, RequestEntity requestEntity) {
        if (hasNoValidScopeWithHierarchyForRequest(requestEntity)) {
            log.warn("Unauthorized access attempt to request {} by user {}", requestId, authenticationContextService.getCurrentUserId());
            throw APPROVE_NOT_AUTHORIZED.accessForbiddenException();
        }
    }
    public boolean hasNoValidScopeWithHierarchyForRequest(RequestEntity requestEntity) {
        return securityScopes.all()
            .stream()
            .filter(s -> s.clientId().equals(requestEntity.getRole().getClient().getId()))
            .filter(s -> s.roleId().equals(requestEntity.getRole().getId()))
            .noneMatch(s -> {
                final var level = requestEntity.getLevel();
                final var levelId = level != null ? level.getId() : null;
                final var codeItem = requestEntity.getCodeItem();
                final var exactMatch = s.levelId().equals(levelId) && s.codeItem().equals(codeItem);

                if (!exactMatch && levelId != null) {
                    return roleRepository.findDescendantRoles(s.roleId(), s.clientId())
                        .stream()
                    .anyMatch(roleEntity ->
                               roleEntity != null
                            && roleEntity.getRole() != null
                            && roleEntity.getLevel() != null
                            && roleEntity.getRole().getLevel() != null
                            && !roleEntity.getRole().getLevel().equals(roleEntity.getLevel())
                            && roleEntity.getLevel().getId().equals(levelId)
                    );
            }
            return exactMatch;
        });
    }

    public void validateUserPermissionToUpdateToAllowOrDenyRequest(RequestEntity requestEntity) {
        if (hasNoValidScopeWithHierarchyForRequest(requestEntity)) {
            throw USER_NOT_AUTHORIZED.accessForbiddenException();
        }
    }
}
