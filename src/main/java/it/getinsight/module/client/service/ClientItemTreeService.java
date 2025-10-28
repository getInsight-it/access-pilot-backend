package it.getinsight.module.client.service;

import it.getinsight.module.client.entity.ClientEntity;
import it.getinsight.module.level.dto.ItemResponseNodeDTO;
import it.getinsight.module.level.service.ItemTreeService;
import it.getinsight.module.user.service.ScopeRef;
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

    @Transactional(readOnly = true)
    public List<ItemResponseNodeDTO> buildTreeForClient(ClientEntity client) {
        Set<Long> itemIds = securityScopes.all().stream()
                .filter(scope -> scope.clientId().equals(client.getId()))
                .map(ScopeRef::itemId)
                .collect(Collectors.toSet());

        if (itemIds.isEmpty()) {
            return Collections.emptyList();
        }

        return itemTreeService.buildTreeFromScopeItemIds(itemIds);
    }
}
