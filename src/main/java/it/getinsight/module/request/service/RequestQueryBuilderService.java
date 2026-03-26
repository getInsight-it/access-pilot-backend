package it.getinsight.module.request.service;


import it.getinsight.module.request.dto.RequestFilterDTO;
import it.getinsight.module.request.entity.RequestEntity;
import it.getinsight.module.request.repository.RequestRepository;
import it.getinsight.module.request.repository.specification.RequestSpecification;
import it.getinsight.module.role.entity.RoleEntity;
import it.getinsight.module.role.repository.RoleRepository;
import it.getinsight.module.role.service.RoleService;
import it.getinsight.module.user.entity.UserEntity;
import it.getinsight.module.user.service.AuthenticationContextService;
import it.getinsight.module.user.service.SecurityScopes;
import it.getinsight.module.user.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

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


    public Specification<RequestEntity> buildCreatedRequestsSpecification(RequestEntity model, String currentUserId) {
        model.setRequestingUser(UserEntity.builder().externalId(currentUserId).build());
        return RequestSpecification.matchCustom(model);
    }

    public Specification<RequestEntity> buildAssignedRequestsSpecification(RequestEntity model) {
        var sameLevel = securityScopes.findDirectRoleAccessScopes();
        var hierarchyLevel = securityScopes.findHierarchicalRoleAccessScopes();
        var rolesWithoutLevel = securityScopes.findRolesWithoutDirectAccess();

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
        if (triplesSpec == null && additionalTriplesSpec == null && specRolesWithLevelIsNull == null && fallbackSpec == null) {
            return spec.and((root, query, cb) -> cb.disjunction());
        }

        Specification<RequestEntity> combinedSpec = Specification.anyOf(triplesSpec, additionalTriplesSpec, specRolesWithLevelIsNull, fallbackSpec);
        return spec.and(combinedSpec);
    }

    private Specification<RequestEntity> buildFallbackApproversSpecification(RequestEntity model) {
        if (!userService.isUserLoggedAdmin()) {
            return null;
        }

        var currentUser = userService.findOrImportByExternalId(authenticationContextService.getCurrentUserId());
        var structurallyFallbackRoleIds = findStructurallyFallbackRoleIds();
        if (structurallyFallbackRoleIds.isEmpty()) {
            return null;
        }

        var candidateFallbackRequests = requestRepository.findAll(
            RequestSpecification.matchCustom(model)
                .and(RequestSpecification.requesterRoleIn(structurallyFallbackRoleIds))
        );

        var fallbackRequestIds = candidateFallbackRequests.stream()
            .filter(roleService::isFallbackScenario)
            .filter(request ->
                roleService.getOrImportApprovesByRequest(request).stream()
                    .anyMatch(user -> user.id().equals(currentUser.id()))
            )
            .map(RequestEntity::getId)
            .distinct()
            .toList();

        if (fallbackRequestIds.isEmpty()) {
            return null;
        }

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

    // Methods moved to SecurityScopes class





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
