package it.getinsight.module.request.service;


import it.getinsight.module.request.dto.RequestFilterDTO;
import it.getinsight.module.request.entity.RequestEntity;
import it.getinsight.module.request.repository.RequestRepository;
import it.getinsight.module.request.repository.specification.RequestSpecification;
import it.getinsight.module.role.entity.RoleEntity;
import it.getinsight.module.role.repository.RoleRepository;
import it.getinsight.module.role.service.ApprovalPolicyService;
import it.getinsight.module.role.service.RoleService;
import it.getinsight.module.user.entity.UserEntity;
import it.getinsight.module.user.service.AuthenticationContextService;
import it.getinsight.module.user.service.SecurityScopes;
import it.getinsight.module.user.service.ScopeRef;
import it.getinsight.module.user.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
@Slf4j
public class RequestQueryBuilderService {


    private final AuthenticationContextService authenticationContextService;
    private final SecurityScopes securityScopes;
    private final RequestRepository requestRepository;
    private final RoleRepository roleRepository;
    private final RoleService roleService;
    private final UserService userService;
    private final ApprovalPolicyService approvalPolicyService;


    public Specification<RequestEntity> buildCreatedRequestsSpecification(RequestEntity model, String currentUserId) {
        model.setRequestingUser(UserEntity.builder().externalId(currentUserId).build());
        return RequestSpecification.matchCustom(model);
    }

    public Specification<RequestEntity> buildAssignedRequestsSpecification(RequestEntity model) {
        var sameLevel = securityScopes.findDirectRoleAccessScopes();
        var hierarchyLevel = securityScopes.findHierarchicalRoleAccessScopes();
        var rolesWithoutLevel = securityScopes.findRolesWithoutDirectAccess();
        var lateralSpec = buildLateralAssignedSpecification(sameLevel, hierarchyLevel, rolesWithoutLevel);

        var spec = RequestSpecification.matchCustom(model);

        var triplesSpec = !sameLevel.isEmpty() ?
            RequestSpecification.inTriples(sameLevel) :
            null;

        var additionalTriplesSpec = !hierarchyLevel.isEmpty() ?
            RequestSpecification.inTriples(hierarchyLevel) :
            null;

        Specification<RequestEntity> specRolesWithLevelIsNull = !rolesWithoutLevel.isEmpty()
            ? RequestSpecification.rolesWithLevelIsNull(rolesWithoutLevel)
            : null;

        var fallbackSpec = buildFallbackApproversSpecification(model);
        if (triplesSpec == null && additionalTriplesSpec == null && specRolesWithLevelIsNull == null && lateralSpec == null && fallbackSpec == null) {
            return spec.and((root, query, cb) -> cb.disjunction());
        }

        Specification<RequestEntity> combinedSpec = Specification.anyOf(triplesSpec, additionalTriplesSpec, specRolesWithLevelIsNull, lateralSpec, fallbackSpec);
        return spec.and(combinedSpec);
    }

    private Specification<RequestEntity> buildLateralAssignedSpecification(List<String> directScopes,
                                                                          List<String> hierarchicalScopes,
                                                                          List<String> rolesWithoutLevelScopes) {
        var allLevelScopes = Stream.of(
                Optional.ofNullable(directScopes).orElse(List.of()),
                Optional.ofNullable(hierarchicalScopes).orElse(List.of()),
                toRawLevelScopes(securityScopes.all())
            )
            .flatMap(List::stream)
            .filter(StringUtils::hasText)
            .distinct()
            .toList();

        var scopedRoleIds = collectScopedRoleIds(allLevelScopes, rolesWithoutLevelScopes);
        if (scopedRoleIds.isEmpty()) {
            return null;
        }

        var sourceRolesByTargetRole = buildLateralSourceRolesMap(scopedRoleIds);
        if (sourceRolesByTargetRole.isEmpty()) {
            return null;
        }

        var mappedTriples = mapLateralLevelScopes(allLevelScopes, sourceRolesByTargetRole);
        var mappedRolesWithoutLevel = mapLateralRoleWithoutLevelScopes(rolesWithoutLevelScopes, sourceRolesByTargetRole);

        Specification<RequestEntity> lateralTriplesSpec = mappedTriples.isEmpty()
            ? null
            : RequestSpecification.inTriples(mappedTriples);
        Specification<RequestEntity> lateralRolesWithoutLevelSpec = mappedRolesWithoutLevel.isEmpty()
            ? null
            : RequestSpecification.rolesWithLevelIsNull(mappedRolesWithoutLevel);

        if (lateralTriplesSpec == null && lateralRolesWithoutLevelSpec == null) {
            return null;
        }

        return Specification.anyOf(lateralTriplesSpec, lateralRolesWithoutLevelSpec);
    }

    private Set<Long> collectScopedRoleIds(List<String> levelScopes, List<String> rolesWithoutLevelScopes) {
        var roleIds = new HashSet<Long>();
        Optional.ofNullable(levelScopes).orElse(List.of()).stream()
            .map(this::extractRoleIdFromScopeRef)
            .filter(Objects::nonNull)
            .forEach(roleIds::add);

        Optional.ofNullable(rolesWithoutLevelScopes).orElse(List.of()).stream()
            .map(this::extractRoleIdFromScopeRef)
            .filter(Objects::nonNull)
            .forEach(roleIds::add);

        return roleIds;
    }

    private Map<Long, Set<Long>> buildLateralSourceRolesMap(Set<Long> scopedRoleIds) {
        var sourceRolesByTargetRole = new HashMap<Long, Set<Long>>();
        var policies = approvalPolicyService.findEnabledLateralPoliciesByRoleIds(scopedRoleIds);
        for (var policy : policies) {
            if (policy == null || policy.getRole() == null || policy.getRole().getId() == null) {
                continue;
            }
            var sourceRoleId = policy.getRole().getId();
            for (var target : Optional.ofNullable(policy.getRoles()).orElse(List.of())) {
                if (target == null || !Boolean.TRUE.equals(target.getActive()) || target.getRole() == null || target.getRole().getId() == null) {
                    continue;
                }
                var targetRoleId = target.getRole().getId();
                if (!scopedRoleIds.contains(targetRoleId)) {
                    continue;
                }
                sourceRolesByTargetRole.computeIfAbsent(targetRoleId, ignored -> new HashSet<>()).add(sourceRoleId);
            }
        }
        return sourceRolesByTargetRole;
    }

    private List<String> mapLateralLevelScopes(List<String> levelScopes, Map<Long, Set<Long>> sourceRolesByTargetRole) {
        var mapped = new HashSet<String>();
        for (var scope : Optional.ofNullable(levelScopes).orElse(List.of())) {
            var parts = scope.split(":");
            if (parts.length != 4) {
                continue;
            }
            if ("*".equals(parts[2]) || "*".equals(parts[3])) {
                continue;
            }
            var targetRoleId = tryParseLong(parts[1]);
            if (targetRoleId == null) {
                continue;
            }
            var sourceRoleIds = sourceRolesByTargetRole.get(targetRoleId);
            if (sourceRoleIds == null || sourceRoleIds.isEmpty()) {
                continue;
            }
            for (Long sourceRoleId : sourceRoleIds) {
                mapped.add(parts[0] + ":" + sourceRoleId + ":" + parts[2] + ":" + parts[3]);
            }
        }
        return List.copyOf(mapped);
    }

    private List<String> mapLateralRoleWithoutLevelScopes(List<String> rolesWithoutLevelScopes, Map<Long, Set<Long>> sourceRolesByTargetRole) {
        var mapped = new HashSet<String>();
        for (var scope : Optional.ofNullable(rolesWithoutLevelScopes).orElse(List.of())) {
            if (!StringUtils.hasText(scope)) {
                continue;
            }
            var parts = scope.split(":");
            if (parts.length == 4) {
                var targetRoleId = tryParseLong(parts[1]);
                if (targetRoleId == null) {
                    continue;
                }
                var sourceRoleIds = sourceRolesByTargetRole.get(targetRoleId);
                if (sourceRoleIds == null || sourceRoleIds.isEmpty()) {
                    continue;
                }
                for (Long sourceRoleId : sourceRoleIds) {
                    mapped.add(parts[0] + ":" + sourceRoleId + ":*:*");
                }
                continue;
            }

            var targetRoleId = tryParseLong(scope);
            if (targetRoleId == null) {
                continue;
            }
            var sourceRoleIds = sourceRolesByTargetRole.get(targetRoleId);
            if (sourceRoleIds == null || sourceRoleIds.isEmpty()) {
                continue;
            }
            for (Long sourceRoleId : sourceRoleIds) {
                mapped.add(String.valueOf(sourceRoleId));
            }
        }
        return List.copyOf(mapped);
    }

    private List<String> toRawLevelScopes(List<ScopeRef> scopeRefs) {
        if (scopeRefs == null || scopeRefs.isEmpty()) {
            return List.of();
        }
        return scopeRefs.stream()
            .filter(Objects::nonNull)
            .filter(scope -> scope.clientId() != null && scope.roleId() != null && scope.levelId() != null && scope.codeItem() != null)
            .map(scope -> scope.clientId() + ":" + scope.roleId() + ":" + scope.levelId() + ":" + scope.codeItem())
            .toList();
    }

    private Long extractRoleIdFromScopeRef(String scope) {
        if (!StringUtils.hasText(scope)) {
            return null;
        }
        var parts = scope.split(":");
        if (parts.length == 4) {
            return tryParseLong(parts[1]);
        }
        if (parts.length == 1) {
            return tryParseLong(parts[0]);
        }
        return null;
    }

    private Long tryParseLong(String value) {
        try {
            return value == null ? null : Long.valueOf(value);
        } catch (NumberFormatException ex) {
            return null;
        }
    }

    private Specification<RequestEntity> buildFallbackApproversSpecification(RequestEntity model) {
        if (!userService.isUserLoggedAdmin()) {
            log.debug("Fallback spec skipped: user is not admin");
            return null;
        }

        var currentUserExternalId = authenticationContextService.getCurrentUserId();
        if (!roleService.isFallbackApproverExternalId(currentUserExternalId)) {
            log.debug("Fallback spec skipped: user is not accesspilot fallback approver. externalId={}", currentUserExternalId);
            return null;
        }

        var structurallyFallbackRoleIds = findStructurallyFallbackRoleIds();
        if (structurallyFallbackRoleIds.isEmpty()) {
            log.debug("Fallback spec skipped: no structurally fallback-eligible roles found");
            return null;
        }

        var candidateFallbackRequests = requestRepository.findAll(
            RequestSpecification.matchCustom(model)
                .and(RequestSpecification.requesterRoleIn(structurallyFallbackRoleIds))
        );
        log.debug("Fallback spec candidates: {}", candidateFallbackRequests.size());

        var fallbackRequestIds = candidateFallbackRequests.stream()
            .filter(roleService::isFallbackScenario)
            .map(RequestEntity::getId)
            .distinct()
            .toList();

        if (fallbackRequestIds.isEmpty()) {
            log.debug("Fallback spec result empty for externalId={}", currentUserExternalId);
            return null;
        }

        log.debug("Fallback spec request IDs for externalId={}: {}", currentUserExternalId, fallbackRequestIds);
        return RequestSpecification.requestIdIn(fallbackRequestIds);
    }

    private List<Long> findStructurallyFallbackRoleIds() {
        return roleRepository.findAll().stream()
            .filter(this::isStructurallyFallbackEligible)
            .map(RoleEntity::getId)
            .distinct()
            .toList();
    }

    private boolean isStructurallyFallbackEligible(RoleEntity role) {
        if (role == null) {
            return false;
        }
        if (role.getRole() == null) {
            return true;
        }
        return role.getRole().getRole() == null;
    }

    public boolean shouldExecuteQuery(Optional<RequestFilterDTO> filter) {
        if (filter.isEmpty()) {
            return false;
        }

        String type = filter.get().type();
        return "created".equalsIgnoreCase(type) || "assigned".equalsIgnoreCase(type);
    }

    public Specification<RequestEntity> buildFinalSpecification(RequestEntity model, Optional<RequestFilterDTO> filter) {
        var currentUserId = authenticationContextService.getCurrentUserId();

        if (filter.isPresent() && "created".equalsIgnoreCase(filter.get().type())) {
            return buildCreatedRequestsSpecification(model, currentUserId);
        }

        if (filter.isPresent() && "assigned".equalsIgnoreCase(filter.get().type())) {
            return buildAssignedRequestsSpecification(model);
        }

        return Specification.anyOf();
    }
}
