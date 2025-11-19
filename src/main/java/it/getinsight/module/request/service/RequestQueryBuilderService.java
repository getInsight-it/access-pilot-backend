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

import java.util.List;
import java.util.Optional;

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
        log.debug("Iniciando buildSameLevelFromToken");
        List<String> sameLevels = securityScopes.all().stream()
            .peek(s -> log.debug("Processando scope: roleId={}, clientId={}, levelId={}, codeItem={}", 
                s.roleId(), s.clientId(), s.levelId(), s.codeItem()))
            .flatMap(s -> {
                log.debug("Buscando roles descendentes para roleId: {}, clientId: {}", s.roleId(), s.clientId());
                return roleRepository.findDescendantRoles(s.roleId(), s.clientId())
                    .stream()
                    .peek(role -> log.debug("Role encontrada: {}", role != null ? 
                        String.format("id=%s, role=%s, level=%s", 
                            role.getId(),
                            role.getRole() != null ? role.getRole().getName() : "null",
                            role.getLevel() != null ? role.getLevel().getName() : "null") : "null"))
                    .filter(roleEntity -> {
                        boolean isValid = roleEntity != null
                            && roleEntity.getRole() != null
                            && roleEntity.getLevel() != null
                            && roleEntity.getRole().getLevel() != null
                            && roleEntity.getRole().getLevel().equals(roleEntity.getLevel());
                        
                        if (log.isDebugEnabled()) {
                            if (!isValid) {
                                log.debug("Role inválida ou níveis não correspondentes: {}", 
                                    roleEntity != null ? 
                                        String.format("roleLevel=%s, entityLevel=%s",
                                            roleEntity.getRole() != null && roleEntity.getRole().getLevel() != null ? 
                                                roleEntity.getRole().getLevel().getId() : "null",
                                            roleEntity.getLevel() != null ? 
                                                roleEntity.getLevel().getId() : "null") : "null");
                            } else {
                                log.debug("Role válida com níveis correspondentes: roleLevel={}, entityLevel={}",
                                    roleEntity.getRole().getLevel().getId(),
                                    roleEntity.getLevel().getId());
                            }
                        }
                        return isValid;
                    })
                    .peek(role -> log.debug("Role após filtro: {} (roleId: {}, levelId: {})", 
                        role.getRole().getName(), role.getId(), role.getLevel().getId()))
                    .map(roleEntity -> {
                        String result = s.clientId() + ":" + roleEntity.getId() + ":" + s.levelId() + ":" + s.codeItem();
                        log.debug("Mapeando para formato de saída: {}", result);
                        return result;
                    });
            })
            .peek(item -> log.debug("Item antes do distinct: {}", item))
            .distinct()
            .toList();
            
        log.debug("Total de sameLevels gerados: {}", sameLevels.size());
        log.debug("sameLevels: {}", sameLevels);
        return sameLevels;
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
        log.debug("Iniciando buildHierarchyLevelFromToken");
        List<String> hierarchyLevels = securityScopes.all().stream()
            .peek(s -> log.debug("Processando scope: roleId={}, clientId={}, codeItem={}", 
                s.roleId(), s.clientId(), s.codeItem()))
            .flatMap(s -> {
                log.debug("Buscando roles descendentes para roleId: {}, clientId: {}", s.roleId(), s.clientId());
                return roleRepository.findDescendantRoles(s.roleId(), s.clientId())
                    .stream()
                    .peek(role -> log.debug("Role encontrada: {}", role != null ? role.getId() : "null"))
                    .filter(roleEntity -> {
                        boolean isValid = roleEntity != null
                            && roleEntity.getRole() != null
                            && roleEntity.getLevel() != null
                            && roleEntity.getRole().getLevel() != null
                            && !roleEntity.getRole().getLevel().equals(roleEntity.getLevel());
                        if (log.isDebugEnabled() && !isValid) {
                            log.debug("Role inválida ou sem nível associado: {}", 
                                roleEntity != null ? roleEntity.toString() : "null");
                        }
                        return isValid;
                    })
                    .peek(role -> log.debug("Role após filtro: {} (roleId: {}, levelId: {})",
                        role.getRole().getName(), role.getRole().getId(), role.getLevel().getId()))
                    .flatMap(roleEntity -> {
                        log.debug("Processando role: {} (nivel: {})",
                            roleEntity.getRole().getName(), roleEntity.getLevel().getType());

                        if (roleEntity.getLevel().getType() == LevelType.EXTERNAL) {
                            log.debug("Processando nível EXTERNO para levelId: {}, codeItem: {}",
                                roleEntity.getLevel().getId(), s.codeItem());
                            return itemService.getAllSubitemCodes(roleEntity.getLevel().getId(), s.codeItem())
                                .stream()
                                .peek(item -> log.debug("Subitem encontrado: {}", item))
                                .map(item -> s.clientId() + ":" + roleEntity.getId() + ":" + roleEntity.getLevel().getId() + ":" + item);
                        } else {
                            log.debug("Processando nível INTERNO para levelId: {}, codeItem: {}",
                                roleEntity.getLevel().getId(), s.codeItem());
                            return itemRepository.findAllByLevelIdAndParentExternalCode(roleEntity.getLevel().getId(), s.codeItem())
                                .stream()
                                .peek(item -> log.debug("Item encontrado: {}", item.getExternalCode()))
                                .map(item -> s.clientId() + ":" + roleEntity.getId() + ":" + roleEntity.getLevel().getId() + ":" + item.getExternalCode());
                        }
                    });
            })
            .peek(item -> log.debug("Item processado: {}", item))
            .distinct()
            .toList();
        
        log.debug("Total de hierarchyLevels gerados: {}", hierarchyLevels.size());
        log.debug("hierarchyLevels: {}", hierarchyLevels);
        return hierarchyLevels;
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
