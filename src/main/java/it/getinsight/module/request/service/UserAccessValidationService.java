package it.getinsight.module.request.service;

import it.getinsight.module.request.entity.RequestEntity;
import it.getinsight.module.request.enuns.RequestStatus;
import it.getinsight.module.role.entity.ApprovalPolicyRoleEntity;
import it.getinsight.module.role.service.ApprovalPolicyService;
import it.getinsight.module.role.service.RoleService;
import it.getinsight.module.user.service.AuthenticationContextService;
import it.getinsight.module.user.service.SecurityScopes;
import it.getinsight.module.user.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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
    private final ApprovalPolicyService approvalPolicyService;


    public void validateUserAccessToRequest(Long requestId, RequestEntity requestEntity) {
        if (!canViewRequest(requestEntity)) {
            log.warn("Unauthorized access attempt to request {} by user {}", requestId, authenticationContextService.getCurrentUserId());
            throw APPROVE_NOT_AUTHORIZED.accessForbiddenException();
        }
    }

    public boolean canViewRequest(RequestEntity requestEntity) {
        return hasHierarchyAccess(requestEntity) || hasLateralViewAccess(requestEntity);
    }

    public boolean canApproveRequest(RequestEntity requestEntity) {
        return hasHierarchyAccess(requestEntity) || hasLateralApproveAccess(requestEntity);
    }

    public boolean canRejectRequest(RequestEntity requestEntity) {
        return hasHierarchyAccess(requestEntity) || hasLateralRejectAccess(requestEntity);
    }

    public boolean canRevokeRequest(RequestEntity requestEntity) {
        return hasHierarchyAccess(requestEntity) || hasLateralRevokeAccess(requestEntity);
    }

    public boolean canTransitionToStatus(RequestEntity requestEntity, RequestStatus targetStatus) {
        return switch (targetStatus) {
            case APPROVED -> canApproveRequest(requestEntity);
            case REJECTED -> canRejectRequest(requestEntity);
            case REVOKED -> canRevokeRequest(requestEntity);
            default -> false;
        };
    }

    private boolean hasHierarchyAccess(RequestEntity requestEntity) {
        return !hasNoValidScopeWithHierarchyForRequest(requestEntity);
    }

    public boolean hasNoValidScopeWithHierarchyForRequest(RequestEntity requestEntity) {
        if (requestEntity == null || requestEntity.getRole() == null || requestEntity.getRole().getClient() == null) {
            return true;
        }

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
        if (scope == null || scope.isBlank() || roleId == null || levelId == null || codeItem == null) {
            return false;
        }
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

    private boolean hasLateralViewAccess(RequestEntity requestEntity) {
        return hasLateralPermission(requestEntity, target -> true);
    }

    private boolean hasLateralApproveAccess(RequestEntity requestEntity) {
        return hasLateralPermission(requestEntity, target -> Boolean.TRUE.equals(target.getCanApprove()));
    }

    private boolean hasLateralRejectAccess(RequestEntity requestEntity) {
        return hasLateralPermission(requestEntity, target -> Boolean.TRUE.equals(target.getCanReject()));
    }

    private boolean hasLateralRevokeAccess(RequestEntity requestEntity) {
        return hasLateralPermission(requestEntity, target -> Boolean.TRUE.equals(target.getCanRevoke()));
    }

    private boolean hasLateralPermission(RequestEntity requestEntity,
                                         java.util.function.Predicate<ApprovalPolicyRoleEntity> targetFilter) {
        if (requestEntity == null || requestEntity.getRole() == null || requestEntity.getRole().getClient() == null) {
            return false;
        }

        var lateralPolicy = approvalPolicyService.findEnabledLateralPolicy(requestEntity.getRole()).orElse(null);
        if (lateralPolicy == null || lateralPolicy.getRoles() == null || lateralPolicy.getRoles().isEmpty()) {
            return false;
        }

        Set<Long> targetRoleIds = lateralPolicy.getRoles().stream()
            .filter(target -> target != null && Boolean.TRUE.equals(target.getActive()))
            .filter(targetFilter)
            .map(ApprovalPolicyRoleEntity::getRole)
            .filter(java.util.Objects::nonNull)
            .map(targetRole -> targetRole.getId())
            .filter(java.util.Objects::nonNull)
            .collect(Collectors.toSet());

        if (targetRoleIds.isEmpty()) {
            return false;
        }

        Long clientId = requestEntity.getRole().getClient().getId();
        Long levelId = requestEntity.getLevel() != null ? requestEntity.getLevel().getId() : null;
        String codeItem = requestEntity.getCodeItem();

        if (levelId == null) {
            var roleScopesWithoutLevel = securityScopes.findRolesWithoutDirectAccess();
            return targetRoleIds.stream()
                .anyMatch(targetRoleId -> roleScopesWithoutLevel.stream()
                    .anyMatch(scope -> matchesRoleWithoutLevelScope(scope, clientId, targetRoleId)));
        }

        var levelScopes = Stream.of(
                securityScopes.findDirectRoleAccessScopes(),
                securityScopes.findHierarchicalRoleAccessScopes(),
                toRawLevelScopes(securityScopes.all())
            )
            .flatMap(List::stream)
            .distinct()
            .toList();

        return targetRoleIds.stream()
            .anyMatch(targetRoleId -> levelScopes.stream()
                .anyMatch(scope -> matchesScope(scope, clientId, targetRoleId, levelId, codeItem)));
    }

    private List<String> toRawLevelScopes(List<it.getinsight.module.user.service.ScopeRef> scopeRefs) {
        if (scopeRefs == null || scopeRefs.isEmpty()) {
            return List.of();
        }
        return scopeRefs.stream()
            .filter(java.util.Objects::nonNull)
            .filter(scope -> scope.clientId() != null && scope.roleId() != null && scope.levelId() != null && scope.codeItem() != null)
            .map(scope -> scope.clientId() + ":" + scope.roleId() + ":" + scope.levelId() + ":" + scope.codeItem())
            .toList();
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

        var currentUserExternalId = authenticationContextService.getCurrentUserId();
        return roleService.isFallbackApproverExternalId(currentUserExternalId);
    }

    public void validateUserPermissionToUpdateToAllowOrDenyRequest(RequestEntity requestEntity) {
        if (!(canApproveRequest(requestEntity) || canRejectRequest(requestEntity) || canRevokeRequest(requestEntity))) {
            throw USER_NOT_AUTHORIZED.accessForbiddenException();
        }
    }
}
