package it.getinsight.module.user.service;

import it.getinsight.module.level.entity.LevelType;
import it.getinsight.module.level.repository.ItemRepository;
import it.getinsight.module.level.service.ItemService;
import it.getinsight.module.client.repository.ClientRepository;
import it.getinsight.module.role.repository.RoleRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Stream;

@Component
@RequiredArgsConstructor
@Slf4j
public class SecurityScopesBuilderService {

    private final LevelAttributesExtractor extractor;
    private final ClientRepository clientRepository;
    private final RoleRepository roleRepository;
    private final ItemService itemService;
    private final ItemRepository itemRepository;

    private static final SecurityScopes.CachedScopes EMPTY = new SecurityScopes.CachedScopes(
        List.of(),
        List.of(),
        List.of(),
        List.of()
    );
    private static final String WILDCARD = "*";

    public SecurityScopes.CachedScopes buildScopes(Jwt jwt) {
        log.debug("Building scopes for new token...");
        List<ScopeRef> scopes = extractor.extract(jwt);
        log.debug("Extracted scopes: {}", scopes);

        List<String> sameLevels = scopes.stream()
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
                    .map(roleEntity -> s.clientId()
                        + ":" + roleEntity.getId()
                        + ":" + s.levelId()
                        + ":" + s.codeItem())
            )
            .distinct()
            .toList();

        log.debug("sameLevels (direct role access by level): {}", sameLevels);

        List<String> hierarchyLevels = scopes.stream()
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
                        roleEntity.getLevel().getType() == LevelType.EXTERNAL
                            ? itemService.getAllSubitemCodes(roleEntity.getLevel().getId(), s.codeItem())
                                .stream()
                                .map(item -> s.clientId()
                                    + ":" + roleEntity.getId()
                                    + ":" + roleEntity.getLevel().getId()
                                    + ":" + item)
                            : itemRepository.findAllByLevelIdAndParentExternalCode(
                                    roleEntity.getLevel().getId(),
                                    s.codeItem()
                                )
                                .stream()
                                .map(item -> s.clientId()
                                    + ":" + roleEntity.getId()
                                    + ":" + roleEntity.getLevel().getId()
                                    + ":" + item.getExternalCode())
                    )
            )
            .distinct()
            .toList();

        log.debug("hierarchyLevels (hierarchical role access): {}", hierarchyLevels);

        Set<String> roleIdsWithLevelAccess = new HashSet<>(
            Stream.of(
                    scopes.stream()
                        .map(scope -> scope.clientId() + ":" + scope.roleId() + ":" + WILDCARD + ":" + WILDCARD),
                    sameLevels.stream().map(SecurityScopesBuilderService::toRoleWildcard),
                    hierarchyLevels.stream().map(SecurityScopesBuilderService::toRoleWildcard)
                )
                .flatMap(stream -> stream)
                .filter(Objects::nonNull)
                .toList()
        );

        List<String> rolesWithoutLevelFromResource = resolveRolesWithoutLevelFromResourceAccess(jwt);
        List<String> rolesWithDirectDirectAccess = rolesWithoutLevelFromResource.stream()
            .filter(triple -> {
                String[] parts = triple.split(":");
                if (parts.length != 4) {
                    return false;
                }
                return !roleIdsWithLevelAccess.contains(triple);
            })
            .distinct()
            .toList();

        log.debug("rolesWithoutDirectAccess (from resource_access, filtered): {}", rolesWithDirectDirectAccess);

        SecurityScopes.CachedScopes cachedScopes = new SecurityScopes.CachedScopes(
            List.copyOf(scopes),
            List.copyOf(sameLevels),
            List.copyOf(hierarchyLevels),
            List.copyOf(rolesWithDirectDirectAccess)
        );
        log.debug("Built cached scopes successfully with {} scopes, {} direct, {} hierarchical, {} roles without access",
            cachedScopes.scopes().size(),
            cachedScopes.directScopes().size(),
            cachedScopes.hierarchicalScopes().size(),
            cachedScopes.rolesWithoutDirectAccess().size());
        return cachedScopes;
    }

    /**
     * Derive roles without level from resource_access, excluding those already present in levelAttributes.
     */
    private List<String> resolveRolesWithoutLevelFromResourceAccess(Jwt jwt) {
        Object claim = jwt.getClaims().get("resource_access");
        if (!(claim instanceof Map<?, ?> resourceAccess)) {
            return List.of();
        }

        List<String> roleTriples = new ArrayList<>();
        for (Map.Entry<?, ?> entry : resourceAccess.entrySet()) {
            if (!(entry.getKey() instanceof String clientId)) {
                continue;
            }

            var clientOpt = clientRepository.findByClientId(clientId);
            if (clientOpt.isEmpty()) {
                continue;
            }

            Object rolesObj = entry.getValue();
            if (!(rolesObj instanceof Map<?, ?> rolesMap)) {
                continue;
            }

            Object rolesListObj = rolesMap.get("roles");
            if (!(rolesListObj instanceof Collection<?> rolesList)) {
                continue;
            }

            rolesList.stream()
                .map(Object::toString)
                .forEach(roleName ->
                    roleRepository.findByNameAndClient(roleName, clientOpt.get())
                        .filter(roleEntity -> roleEntity.getLevel() == null)
                        .map(roleEntity -> roleEntity.getClient().getId()
                            + ":" + roleEntity.getId()
                            + ":" + WILDCARD
                            + ":" + WILDCARD)
                        .ifPresent(roleTriples::add)
                );
        }

        return roleTriples.stream().distinct().toList();
    }

    private static String toRoleWildcard(String triple) {
        if (triple == null || triple.isBlank()) {
            return null;
        }
        String[] parts = triple.split(":");
        if (parts.length != 4 || parts[0].isBlank() || parts[1].isBlank()) {
            return null;
        }
        return parts[0] + ":" + parts[1] + ":" + WILDCARD + ":" + WILDCARD;
    }
}
