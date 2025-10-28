package it.getinsight.module.level.service;

import it.getinsight.module.level.dto.ItemResponseNodeDTO;
import it.getinsight.module.level.dto.LevelResumedDTO;
import it.getinsight.module.level.entity.ItemEntity;
import it.getinsight.module.level.repository.ItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;



@Service
@RequiredArgsConstructor
public class ItemTreeService {
    private final ItemRepository itemRepository;

    public List<ItemResponseNodeDTO> buildTreeFromScopeItemIds(Collection<Long> itemIds) {
        if (itemIds == null || itemIds.isEmpty()) return List.of();


        List<Long> ids = itemIds.stream().filter(Objects::nonNull).distinct().toList();
        List<ItemEntity> allItems = ids.stream().map(itemRepository::findAscendantTreeById).flatMap(List::stream).distinct().toList();
        if (allItems.isEmpty()) return List.of();


        Map<Long, ItemResponseNodeDTO> nodeById = HashMap.newHashMap(allItems.size());
        for (ItemEntity item : allItems) {
            LevelResumedDTO level = (item.getLevel() != null)
                ? new LevelResumedDTO(item.getLevel().getId(), item.getLevel().getName())
                : null;

            nodeById.put(item.getId(), new ItemResponseNodeDTO(item.getId(), item.getName(), level));
        }


        List<ItemResponseNodeDTO> roots = new ArrayList<>();
        for (ItemEntity item : allItems) {
            ItemResponseNodeDTO node = nodeById.get(item.getId());
            Long parentId = (item.getParent() != null) ? item.getParent().getId() : null;

            if (parentId != null && nodeById.containsKey(parentId)) {
                ItemResponseNodeDTO parent = nodeById.get(parentId);
                parent.getItems().add(node);
            } else {

                roots.add(node);
            }
        }

        return roots;
    }


}
