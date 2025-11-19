package it.getinsight.module.client.service;


import it.getinsight.module.client.entity.ClientEntity;
import it.getinsight.module.level.dto.ItemResponseNodeDTO;
import it.getinsight.module.level.service.ItemTreeService;
import it.getinsight.module.role.entity.RoleEntity;
import it.getinsight.module.role.repository.RoleRepository;
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
        Map<RoleEntity, Set<String>> itemsByRole = securityScopes.all().stream()
                .filter(scope -> scope.clientId().equals(client.getId())
                    && scope.roleId() != null
                    && scope.codeItem() != null)
                .map(scope -> {
                    Optional<RoleEntity> role = roleRepository.findById(scope.roleId());
                    return role.map(l -> new AbstractMap.SimpleEntry<>(l, scope));
                })
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(Collectors.groupingBy(
                    Map.Entry::getKey,
                    Collectors.mapping(e -> e.getValue().codeItem(), Collectors.toSet())
                ));
        log.debug("itemsByRole: {}", itemsByRole);

        if (itemsByRole.isEmpty()) {
            log.debug("No items found for client: {}", client.getId());
            return Collections.emptyList();
        }

        try {
        return itemsByRole.entrySet().stream()
                .map(o -> itemTreeService.buildTreeFromScopeItemIds(o.getKey(),o.getValue()))
                .flatMap(List::stream)
                .toList();
        }catch (Exception e){
            log.error("Error building tree for client: {}", client.getId(), e);
            return  Collections.emptyList();
        }
    }
}
