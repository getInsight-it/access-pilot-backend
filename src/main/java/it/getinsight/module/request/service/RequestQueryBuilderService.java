package it.getinsight.module.request.service;

import it.getinsight.module.request.dto.RequestFilterDTO;
import it.getinsight.module.request.entity.RequestEntity;
import it.getinsight.module.request.repository.specification.RequestSpecification;
import it.getinsight.module.role.entity.RoleEntity;
import it.getinsight.module.role.repository.RoleRepository;
import it.getinsight.module.role.repository.specification.RoleSpecification;
import it.getinsight.module.user.entity.UserEntity;
import it.getinsight.module.user.service.AuthenticationContextService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class RequestQueryBuilderService {

    private final RoleRepository roleRepository;
    private final AuthenticationContextService authenticationContextService;

    public Specification<RequestEntity> buildCreatedRequestsSpecification(RequestEntity model, String currentUserId) {
        model.setRequestingUser(UserEntity.builder().externalId(currentUserId).build());
        return RequestSpecification.matchCustom(model);
    }

    public Specification<RequestEntity> buildAssignedRequestsSpecification(RequestEntity model) {
        var resourceAccess = authenticationContextService.getCurrentUserResourceAccess();
        List<Long> rolesParentIds = roleRepository.findAll(RoleSpecification.byResourceAccess(resourceAccess))
            .stream()
            .map(RoleEntity::getId)
            .toList();
        
        if (rolesParentIds.isEmpty()) {
        }
        
        return RequestSpecification.byRolesParent(rolesParentIds)
            .and(RequestSpecification.matchCustom(model));
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
        
        return Specification.where(null);
    }
}
