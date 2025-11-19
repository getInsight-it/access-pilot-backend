package it.getinsight.module.client.service;


import it.getinsight.module.client.entity.ClientEntity;
import it.getinsight.module.level.dto.ItemResponseNodeDTO;
import it.getinsight.module.level.service.ItemTreeService;
import it.getinsight.module.role.entity.RoleEntity;
import it.getinsight.module.role.repository.RoleRepository;
import it.getinsight.module.user.service.ScopeRef;
import it.getinsight.module.user.service.SecurityScopes;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class ClientItemTreeService {

    private final SecurityScopes securityScopes;
    private final RoleRepository roleRepository;
    private final ItemTreeService itemTreeService;

    @Transactional(readOnly = true)
    public List<ItemResponseNodeDTO> buildTreeForClient(ClientEntity client) {
        log.debug("Building tree for client: {}", client.getId());
        List<ScopeRef> scopes = securityScopes.all();


        List<ScopeRef> clientScopes = scopes.stream()
            .filter(scope -> scope.clientId().equals(client.getId())
                && scope.roleId() != null
                && scope.codeItem() != null)
            .toList();

        if (clientScopes.isEmpty()) {
            log.debug("No scopes found for client: {}", client.getId());
            return Collections.emptyList();
        }


        Set<Long> roleIds = clientScopes.stream()
            .map(ScopeRef::roleId)
            .collect(Collectors.toSet());

        List<RoleEntity> roles = roleRepository.findAllById(roleIds);
        Map<Long, RoleEntity> rolesById = roles.stream()
            .collect(Collectors.toMap(RoleEntity::getId, r -> r));


        Map<RoleEntity, Set<String>> itemsByRole = clientScopes.stream()
            .map(scope -> {
                RoleEntity role = rolesById.get(scope.roleId());
                return role != null ? new AbstractMap.SimpleEntry<>(role, scope) : null;
            })
            .filter(Objects::nonNull)
            .collect(Collectors.groupingBy(
                Map.Entry::getKey,
                Collectors.mapping(e -> e.getValue().codeItem(), Collectors.toSet())
            ));

        log.debug("itemsByRole: {}", itemsByRole);

        if (itemsByRole.isEmpty()) {
            log.debug("No items found for client after role mapping: {}", client.getId());
            return Collections.emptyList();
        }

        try {
            return itemsByRole.entrySet().stream()
                .map(o -> itemTreeService.buildTreeFromScopeItemIds(o.getKey(), o.getValue()))
                .flatMap(List::stream)
                .toList();
        } catch (Exception e) {
            log.error("Error building tree for client: {}", client.getId(), e);
            return Collections.emptyList();
        }
    }
}
