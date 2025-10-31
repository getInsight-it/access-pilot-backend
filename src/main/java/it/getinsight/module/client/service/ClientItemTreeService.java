package it.getinsight.module.client.service;

import it.getinsight.module.client.entity.ClientEntity;
import it.getinsight.module.level.dto.ItemResponseNodeDTO;
import it.getinsight.module.level.entity.LevelEntity;
import it.getinsight.module.level.repository.LevelRepository;
import it.getinsight.module.level.service.ItemTreeService;
import it.getinsight.module.user.service.SecurityScopes;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ClientItemTreeService {

    private final SecurityScopes securityScopes;
    private final ItemTreeService itemTreeService;
    private final LevelRepository levelRepository;

    @Transactional(readOnly = true)
    public List<ItemResponseNodeDTO> buildTreeForClient(ClientEntity client) {
        Map<LevelEntity, Set<String>> itemsByLevel = securityScopes.all().stream()
                .filter(scope -> scope.clientId().equals(client.getId())
                    && scope.levelId() != null
                    && scope.codeItem() != null)
                .map(scope -> {
                    Optional<LevelEntity> level = levelRepository.findById(scope.levelId());
                    return level.map(l -> new AbstractMap.SimpleEntry<>(l, scope));
                })
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(Collectors.groupingBy(
                    Map.Entry::getKey,
                    Collectors.mapping(e -> e.getValue().codeItem(), Collectors.toSet())
                ));

        if (itemsByLevel.isEmpty()) {
            return Collections.emptyList();
        }


        return itemsByLevel.entrySet().stream()
                .map(o -> itemTreeService.buildTreeFromScopeItemIds(o.getKey(),o.getValue()))
                .flatMap(List::stream)
                .toList();
    }
}
