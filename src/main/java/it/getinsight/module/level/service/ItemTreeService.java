package it.getinsight.module.level.service;

import it.getinsight.module.level.client.LevelClient;
import it.getinsight.module.level.dto.ItemResponseNodeDTO;
import it.getinsight.module.level.dto.LevelResumedDTO;
import it.getinsight.module.level.entity.ItemEntity;
import it.getinsight.module.level.entity.LevelEntity;
import it.getinsight.module.level.entity.LevelType;
import it.getinsight.module.level.mapper.ItemHierarchyResumedMapper;
import it.getinsight.module.level.repository.ItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@RequiredArgsConstructor
public class ItemTreeService {

    private final ItemRepository itemRepository;
    private final LevelClient levelClient;
    private final ItemHierarchyResumedMapper itemHierarchyResumedMapper;

    public List<ItemResponseNodeDTO> buildTreeFromScopeItemIds(LevelEntity levelEntity, Collection<String> codeItems) {
        if (codeItems == null || codeItems.isEmpty()) {
            return List.of();
        }

        List<ItemEntity> allItems = fetchItems(levelEntity, codeItems);
        if (allItems.isEmpty()) {
            return List.of();
        }

        Map<Long, ItemResponseNodeDTO> nodeById = createNodeMap(allItems);
        return buildTreeStructure(allItems, nodeById);
    }

    private List<ItemEntity> fetchItems(LevelEntity levelEntity, Collection<String> codeItems) {
        List<String> ids = codeItems.stream()
                .filter(Objects::nonNull)
                .distinct()
                .toList();

        if (levelEntity.getType() != null && LevelType.EXTERNAL.equals(levelEntity.getType())) {
            return fetchExternalItems(ids, levelEntity.getExternalUrl(), levelEntity.getApiKey());
        }else {
            return fetchInternalItems(ids);
        }
    }

    private List<ItemEntity> fetchInternalItems( List<String> ids) {
        return ids.stream()
                .map(itemRepository::findAscendantTreeByExternalCode)
                .flatMap(List::stream)
                .distinct()
                .toList();
    }

    private List<ItemEntity> fetchExternalItems(List<String> ids, String externalUrl, String apiKey) {
        return ids.stream()
                .map(o -> levelClient.getItemHierarchy(externalUrl, apiKey, o))
                .map(itemHierarchyResumedMapper::toEntity)
                .toList();
    }



    private Map<Long, ItemResponseNodeDTO> createNodeMap(List<ItemEntity> items) {
        Map<Long, ItemResponseNodeDTO> nodeById = HashMap.newHashMap(items.size());
        for (ItemEntity item : items) {
            LevelResumedDTO level = (item.getLevel() != null)
                    ? new LevelResumedDTO(item.getLevel().getId(), item.getLevel().getName())
                    : null;
            nodeById.put(item.getId(), new ItemResponseNodeDTO(item.getId(), item.getName(), level));
        }
        return nodeById;
    }

    private List<ItemResponseNodeDTO> buildTreeStructure(List<ItemEntity> items, Map<Long, ItemResponseNodeDTO> nodeById) {
        List<ItemResponseNodeDTO> roots = new ArrayList<>();
        for (ItemEntity item : items) {
            ItemResponseNodeDTO node = nodeById.get(item.getId());
            Long parentId = (item.getParent() != null) ? item.getParent().getId() : null;

            if (parentId != null && nodeById.containsKey(parentId)) {
                nodeById.get(parentId).getItems().add(node);
            } else {
                roots.add(node);
            }
        }
        return roots;
    }

}
