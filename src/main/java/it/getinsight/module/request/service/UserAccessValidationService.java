package it.getinsight.module.request.service;

import it.getinsight.module.request.entity.RequestEntity;
import it.getinsight.module.role.service.RoleService;
import it.getinsight.module.user.service.AuthenticationContextService;
import it.getinsight.module.user.service.SecurityScopes;
import it.getinsight.module.user.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

import static it.getinsight.message.MessageProperty.APPROVE_NOT_AUTHORIZED;
import static it.getinsight.message.MessageProperty.USER_NOT_AUTHORIZED;


@Service
@RequiredArgsConstructor
@Slf4j
public class UserAccessValidationService {

    private final AuthenticationContextService authenticationContextService;
    private final SecurityScopes securityScopes;
    private final RoleService roleService;
    private final UserService userService;


    public void validateUserAccessToRequest(Long requestId, RequestEntity requestEntity) {
        if (hasNoValidScopeWithHierarchyForRequest(requestEntity)) {
            log.warn("Unauthorized access attempt to request {} by user {}", requestId, authenticationContextService.getCurrentUserId());
            throw APPROVE_NOT_AUTHORIZED.accessForbiddenException();
        }
    }

    public boolean hasNoValidScopeWithHierarchyForRequest(RequestEntity requestEntity) {
        Long clientId = requestEntity.getRole().getClient().getId();
        Long roleId = requestEntity.getRole().getId();
        Long levelId = requestEntity.getLevel() != null ? requestEntity.getLevel().getId() : null;
        String codeItem = requestEntity.getCodeItem();

        List<String> directScopes = securityScopes.findDirectRoleAccessScopes();
        List<String> hierarchicalScopes = securityScopes.findHierarchicalRoleAccessScopes();
        List<String> rolesWithoutLevel = securityScopes.findRolesWithoutDirectAccess();

        boolean matchesDirect = directScopes.stream()
            .anyMatch(scope -> matchesScope(scope, clientId, roleId, levelId, codeItem));

        boolean matchesHierarchical = hierarchicalScopes.stream()
            .anyMatch(scope -> matchesScope(scope, clientId, roleId, levelId, codeItem));

        boolean matchesRoleWithoutLevel = levelId == null
            && rolesWithoutLevel.stream()
                .anyMatch(scope -> matchesRoleWithoutLevelScope(scope, clientId, roleId));

        boolean fallbackAllowed = isCurrentUserFallbackApproverForRequest(requestEntity);

        return !(matchesDirect || matchesHierarchical || matchesRoleWithoutLevel || fallbackAllowed);
    }

    private boolean matchesScope(String scope, Long clientId, Long roleId, Long levelId, String codeItem) {
        String[] parts = scope.split(":");
        if (parts.length != 4) return false;

        try {
            Long scopeClientId = Long.parseLong(parts[0]);
            Long scopeRoleId = Long.parseLong(parts[1]);
            Long scopeLevelId = Long.parseLong(parts[2]);
            String scopeCodeItem = parts[3];

            return scopeClientId.equals(clientId)
                && scopeRoleId.equals(roleId)
                && scopeLevelId.equals(levelId)
                && scopeCodeItem.equals(codeItem);
        } catch (NumberFormatException e) {
            log.warn("Invalid scope format: {}", scope);
            return false;
        }
    }

    private boolean matchesRoleWithoutLevelScope(String scope, Long clientId, Long roleId) {
        if (scope == null || scope.isBlank()) {
            return false;
        }
        String[] parts = scope.split(":");
        if (parts.length == 4) {
            Long scopeClientId = tryParseLong(parts[0]);
            Long scopeRoleId = tryParseLong(parts[1]);
            return scopeRoleId != null
                && scopeRoleId.equals(roleId)
                && (scopeClientId == null || scopeClientId.equals(clientId));
        }
        Long scopeRoleId = tryParseLong(scope);
        return scopeRoleId != null && scopeRoleId.equals(roleId);
    }

    private Long tryParseLong(String raw) {
        try {
            return raw == null ? null : Long.valueOf(raw);
        } catch (NumberFormatException e) {
            return null;
        }
    }

    public boolean isCurrentUserApproverForRequest(RequestEntity requestEntity) {
        var currentUserId = authenticationContextService.getCurrentUserId();
        var approvingUser = userService.findOrImportByExternalId(currentUserId);
        var approvingUsers = roleService.getOrImportApprovesByRequest(requestEntity);
        return approvingUsers.stream().anyMatch(user -> user.id().equals(approvingUser.id()));
    }

    private boolean isCurrentUserFallbackApproverForRequest(RequestEntity requestEntity) {
        if (!userService.isUserLoggedAdmin()) {
            return false;
        }

        if (!roleService.isFallbackScenario(requestEntity)) {
            return false;
        }
        return isCurrentUserApproverForRequest(requestEntity);
    }

    public void validateUserPermissionToUpdateToAllowOrDenyRequest(RequestEntity requestEntity) {
        if (hasNoValidScopeWithHierarchyForRequest(requestEntity)) {
            throw USER_NOT_AUTHORIZED.accessForbiddenException();
        }
    }
}
