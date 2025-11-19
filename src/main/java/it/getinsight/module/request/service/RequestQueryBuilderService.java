package it.getinsight.module.request.service;


import it.getinsight.module.request.dto.RequestFilterDTO;
import it.getinsight.module.request.entity.RequestEntity;
import it.getinsight.module.request.repository.specification.RequestSpecification;
import it.getinsight.module.user.entity.UserEntity;
import it.getinsight.module.user.service.AuthenticationContextService;
import it.getinsight.module.user.service.SecurityScopes;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class RequestQueryBuilderService {


    private final AuthenticationContextService authenticationContextService;
    private final SecurityScopes securityScopes;


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

        Specification<RequestEntity> specRolesWithLevelIsNull = RequestSpecification.rolesWithLevelIsNull(rolesWithoutLevel);

        Specification<RequestEntity> combinedSpec = Specification.anyOf(triplesSpec, additionalTriplesSpec, specRolesWithLevelIsNull);
        return spec.and(combinedSpec);
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
