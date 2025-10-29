package it.getinsight.module.request.service;

import it.getinsight.module.request.entity.RequestEntity;
import it.getinsight.module.role.entity.RoleEntity;
import it.getinsight.module.role.repository.RoleRepository;
import it.getinsight.module.user.service.SecurityScopes;
import it.getinsight.module.user.service.AuthenticationContextService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import static it.getinsight.message.MessageProperty.*;


@Service
@RequiredArgsConstructor
@Slf4j
public class UserAccessValidationService {

    private final RoleRepository roleRepository;
    private final AuthenticationContextService authenticationContextService;
    private final SecurityScopes securityScopes;


    public void validateUserAccessToRequest(Long requestId, RequestEntity requestEntity) {
        var currentUserId = authenticationContextService.getCurrentUserId();
        boolean isRequestingUser = requestEntity.getRequestingUser().getExternalId().equals(currentUserId);
        if (!isRequestingUser && !(isSameScope(requestEntity) && isHierarchyOk(requestEntity))) {
            log.warn("Unauthorized access attempt to request {} by user {}", requestId, currentUserId);
            throw APPROVE_NOT_AUTHORIZED.accessForbiddenException();
        }
    }

    private boolean isSameScope(RequestEntity requestEntity) {
        return securityScopes.all().stream().anyMatch(s -> {
            Long clientId = requestEntity.getRole().getClient().getId();
            Long levelId = requestEntity.getLevel() != null ? requestEntity.getLevel().getId() : null;
            Long itemId = requestEntity.getCodeItem() != null ? Long.parseLong(requestEntity.getCodeItem()) : null;

            boolean exactMatch = s.clientId().equals(clientId)
                && s.levelId().equals(levelId)
                && s.itemId().equals(itemId);

            if (!exactMatch && levelId != null) {
                return roleRepository.findDescendantRoles(s.roleId(), s.clientId())
                    .stream()
                    .anyMatch(roleEntity ->
                        roleEntity != null
                            && roleEntity.getRole() != null
                            && roleEntity.getLevel() != null
                            && roleEntity.getRole().getLevel() != null
                            && !roleEntity.getRole().getLevel().equals(roleEntity.getLevel())
                            && s.clientId().equals(clientId)
                            && roleEntity.getLevel().getId().equals(levelId)
                    );
            }
            return exactMatch;
        });
    }

    private boolean isHierarchyOk(RequestEntity requestEntity) {
        return securityScopes.all().stream().anyMatch(s -> {
            var approvables = roleRepository.findDescendantRoles(s.roleId(), s.clientId())
                .stream().map(RoleEntity::getId).toList();
            return approvables.contains(requestEntity.getRole().getId()) || requestEntity.getRole().getId().equals(s.roleId());
        });
    }

    public void validateUserPermissionToUpdateRequest(RequestEntity requestEntity) {
        var currentUserId = authenticationContextService.getCurrentUserId();
        boolean isRequestingUser = requestEntity.getRequestingUser().getExternalId().equals(currentUserId);
        if (!isRequestingUser && !(isSameScope(requestEntity) && isHierarchyOk(requestEntity))) {
            throw USER_NOT_AUTHORIZED.accessForbiddenException();
        }
    }
}
