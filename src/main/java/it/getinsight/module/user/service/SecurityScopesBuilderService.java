package it.getinsight.module.user.service;

import it.getinsight.module.level.entity.LevelType;
import it.getinsight.module.level.repository.ItemRepository;
import it.getinsight.module.level.service.ItemService;
import it.getinsight.module.role.repository.RoleRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
@RequiredArgsConstructor
@Slf4j
public class SecurityScopesBuilderService {

    private final LevelAttributesExtractor extractor;
    private final RoleRepository roleRepository;
    private final ItemService itemService;
    private final ItemRepository itemRepository;

    private static final SecurityScopes.CachedScopes EMPTY = new SecurityScopes.CachedScopes(
        List.of(),
        List.of(),
        List.of(),
        List.of()
    );

    public SecurityScopes.CachedScopes buildScopes(Jwt jwt) {
        log.debug("Building scopes for new token...");
        List<ScopeRef> scopes = extractor.extract(jwt);
        log.debug("Extracted scopes: {}", scopes);

        if (scopes.isEmpty()) {
            log.debug("No scopes extracted, returning empty cached scopes");
            return EMPTY;
        }

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

        Set<String> roleIdsWithAccess = new HashSet<>();

        sameLevels.forEach(s -> {
            String[] parts = s.split(":");
            if (parts.length > 1) {
                roleIdsWithAccess.add(parts[1]);
            }
        });

        hierarchyLevels.forEach(h -> {
            String[] parts = h.split(":");
            if (parts.length > 1) {
                roleIdsWithAccess.add(parts[1]);
            }
        });

        List<String> rolesWithoutDirect = scopes.stream()
            .map(ScopeRef::roleId)
            .map(String::valueOf)
            .filter(roleId -> !roleIdsWithAccess.contains(roleId))
            .distinct()
            .toList();

        log.debug("rolesWithoutDirectAccess: {}", rolesWithoutDirect);

        SecurityScopes.CachedScopes cachedScopes = new SecurityScopes.CachedScopes(
            List.copyOf(scopes),
            List.copyOf(sameLevels),
            List.copyOf(hierarchyLevels),
            List.copyOf(rolesWithoutDirect)
        );
        log.debug("Built cached scopes successfully with {} scopes, {} direct, {} hierarchical, {} roles without access",
            cachedScopes.scopes().size(),
            cachedScopes.directScopes().size(),
            cachedScopes.hierarchicalScopes().size(),
            cachedScopes.rolesWithoutDirectAccess().size());
        return cachedScopes;
    }
}

