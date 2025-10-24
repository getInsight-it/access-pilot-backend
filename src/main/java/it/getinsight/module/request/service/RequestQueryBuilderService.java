package it.getinsight.module.request.service;

import it.getinsight.module.request.dto.RequestFilterDTO;
import it.getinsight.module.request.entity.RequestEntity;
import it.getinsight.module.request.repository.specification.RequestSpecification;
import it.getinsight.module.role.entity.RoleEntity;
import it.getinsight.module.role.repository.RoleRepository;
import it.getinsight.module.user.entity.UserEntity;
import it.getinsight.module.user.service.AuthenticationContextService;
import it.getinsight.module.user.service.SecurityScopes;
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

    private final RoleRepository roleRepository;
    private final AuthenticationContextService authenticationContextService;
    private final SecurityScopes securityScopes;

    public Specification<RequestEntity> buildCreatedRequestsSpecification(RequestEntity model, String currentUserId) {
        model.setRequestingUser(UserEntity.builder().externalId(currentUserId).build());
        return RequestSpecification.matchCustom(model);
    }

    public Specification<RequestEntity> buildAssignedRequestsSpecification(RequestEntity model) {
        var triples = buildTriplesFromToken();
        var additionalTriples = buildAdditionalTriples();
        var approvablesScoped = buildApprovablesFromToken();

        Specification<RequestEntity> spec = RequestSpecification.matchCustom(model);

        if (!approvablesScoped.isEmpty()) {
            spec = spec.and(RequestSpecification.requesterRoleIn(approvablesScoped));
        }

        if (!triples.isEmpty()) {
            spec = spec.and(RequestSpecification.inTriples(triples, false));
            // TODO: Verificar quando a role nao tem esfera associada
            //  Specification<RequestEntity> levelIsNull = (root, query, cb) -> cb.isNull(root.get("level"));
            //  spec = spec.or(levelIsNull);
        } else {
            Specification<RequestEntity> levelIsNull = (root, query, cb) -> cb.isNull(root.get("level"));
            spec = spec.and(levelIsNull);
        }

        if (!additionalTriples.isEmpty()) {
            spec = spec.or(RequestSpecification.inTriples(additionalTriples, true));
        }

        return spec;
    }

    private List<String> buildTriplesFromToken() {
        return  securityScopes.all().stream()
            .map(s -> s.clientId() + ":" + s.levelId() + ":" + s.itemId())
            .toList();
    }

    private List<String> buildAdditionalTriples() {
        return securityScopes.all().stream()
            .flatMap(s -> roleRepository.findDescendantRoles(s.roleId(), s.clientId())
                .stream()
                .filter(roleEntity ->
                    roleEntity != null
                    && roleEntity.getRole() != null
                    && roleEntity.getLevel() != null
                    && roleEntity.getRole().getLevel() != null
                    && !roleEntity.getRole().getLevel().equals(roleEntity.getLevel()))
                .map(roleEntity -> s.clientId() + ":" + roleEntity.getLevel().getId())
            )
            .distinct()
            .toList();
    }

    private List<Long> buildApprovablesFromToken() {
        return securityScopes.all().stream()
            .flatMap(s -> {
                List<RoleEntity> descendants = roleRepository.findDescendantRoles(s.roleId(), s.clientId());
                return java.util.stream.Stream.concat(
                    java.util.stream.Stream.of(s.roleId()),
                    descendants.stream().map(RoleEntity::getId)
                );
            })
            .distinct()
            .toList();
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
