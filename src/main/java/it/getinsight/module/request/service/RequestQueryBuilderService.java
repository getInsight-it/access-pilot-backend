package it.getinsight.module.request.service;

import it.getinsight.module.level.entity.LevelType;
import it.getinsight.module.level.repository.ItemRepository;
import it.getinsight.module.level.service.ItemService;
import it.getinsight.module.request.dto.RequestFilterDTO;
import it.getinsight.module.request.entity.RequestEntity;
import it.getinsight.module.request.repository.specification.RequestSpecification;
import it.getinsight.module.role.repository.RoleRepository;
import it.getinsight.module.user.entity.UserEntity;
import it.getinsight.module.user.service.AuthenticationContextService;
import it.getinsight.module.user.service.ScopeRef;
import it.getinsight.module.user.service.SecurityScopes;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@RequiredArgsConstructor
@Slf4j
public class RequestQueryBuilderService {

    private final RoleRepository roleRepository;
    private final AuthenticationContextService authenticationContextService;
    private final SecurityScopes securityScopes;
    private final ItemService itemService;
    private final ItemRepository itemRepository;

    public Specification<RequestEntity> buildCreatedRequestsSpecification(RequestEntity model, String currentUserId) {
        model.setRequestingUser(UserEntity.builder().externalId(currentUserId).build());
        return RequestSpecification.matchCustom(model);
    }

    public Specification<RequestEntity> buildAssignedRequestsSpecification(RequestEntity model) {
        var sameLevel = buildSameLevelFromToken();
        var hierarchyLevel = buildHierarchyLevelFromToken();
        var rolesWithoutLevel = buildRolesWithoutLevelFromToken();


        var spec = RequestSpecification.matchCustom(model);

        var triplesSpec = !sameLevel.isEmpty() ?
            RequestSpecification.inTriples(sameLevel) :
            null;

        var additionalTriplesSpec = !hierarchyLevel.isEmpty() ?
            RequestSpecification.inTriples(hierarchyLevel) :
            null;

        Specification<RequestEntity>  specRolesWithLevelIsNull = RequestSpecification.rolesWithLevelIsNull(rolesWithoutLevel);


        Specification<RequestEntity> combinedSpec = Specification.anyOf(triplesSpec, additionalTriplesSpec, specRolesWithLevelIsNull);
        return spec.and(combinedSpec);
    }

    private List<String> buildSameLevelFromToken() {
        return securityScopes.all().stream()
            .flatMap(s ->
                roleRepository.findDescendantRoles(s.roleId(), s.clientId())
                    .stream()
                    .filter(roleEntity ->
                        roleEntity != null
                            && roleEntity.getRole() != null
                            && roleEntity.getLevel() != null
                            && roleEntity.getRole().getLevel() != null
                            && roleEntity.getRole().getLevel().equals(roleEntity.getLevel())
                    )
                    .map(roleEntity -> s.clientId() + ":"+ roleEntity.getId() + ":" + s.levelId() + ":" + s.codeItem() )

            )
            .distinct()
            .toList();
    }

    private List<String> buildRolesWithoutLevelFromToken() {
        return securityScopes.all().stream()
            .map(ScopeRef::roleId)
            .map(String::valueOf)
            .filter(roleId -> buildSameLevelFromToken().stream().noneMatch(s -> s.split(":")[1].equals(roleId)) &&
                buildHierarchyLevelFromToken().stream().noneMatch(h -> h.split(":")[1].equals(roleId)))
            .toList();
    }


    private List<String> buildHierarchyLevelFromToken() {
        return securityScopes.all().stream()
            .flatMap(s ->
                roleRepository.findDescendantRoles(s.roleId(), s.clientId())
                    .stream()
                    .filter(roleEntity ->
                        roleEntity != null
                            && roleEntity.getRole() != null
                            && roleEntity.getLevel() != null
                            && roleEntity.getRole().getLevel() != null
                            && !roleEntity.getRole().getLevel().equals(roleEntity.getLevel())
                    )
                    .flatMap(roleEntity ->
                        roleEntity.getLevel().getType() == LevelType.EXTERNAL ?
                            itemService.getAllSubitemCodes(roleEntity.getLevel().getId(), s.codeItem())
                                .stream()
                            .map(itemId -> s.clientId() + ":" + roleEntity.getId() + ":" + roleEntity.getLevel().getId() + ":" + itemId)

                        : itemRepository.findAllByLevelIdAndParentId(roleEntity.getLevel().getId(), Long.parseLong(s.codeItem()))
                        .stream()
                        .map(item -> s.clientId() + ":" + roleEntity.getId() + ":" + roleEntity.getLevel().getId() + ":" + item.getId())
                    )
            )
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

        return Specification.anyOf();
    }
}
