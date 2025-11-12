package it.getinsight.module.level.service;

import it.getinsight.module.level.client.LevelClient;
import it.getinsight.module.level.dto.ItemResponseNodeDTO;
import it.getinsight.module.level.dto.LevelResumedDTO;
import it.getinsight.module.level.entity.ItemEntity;
import it.getinsight.module.level.entity.LevelEntity;
import it.getinsight.module.level.entity.LevelType;
import it.getinsight.module.level.mapper.ItemHierarchyResumedMapper;
import it.getinsight.module.level.repository.ItemRepository;
import it.getinsight.module.role.dto.RoleDTO;
import it.getinsight.module.role.entity.RoleEntity;
import it.getinsight.module.role.mapper.RoleMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
public class ItemTreeService {

    private final ItemRepository itemRepository;
    private final LevelClient levelClient;
    private final ItemHierarchyResumedMapper itemHierarchyResumedMapper;
    private final RoleMapper roleMapper;

    public List<ItemResponseNodeDTO> buildTreeFromScopeItemIds(RoleEntity roleEntity, Collection<String> codeItems) {
        var levelEntity = roleEntity.getLevel();
        var roleDTO = roleMapper.toDto(roleEntity);
        if (codeItems == null || codeItems.isEmpty()) {
            return List.of();
        }

        List<ItemEntity> fetchItems = fetchItems(levelEntity, codeItems).stream()
            .filter(it -> it.getParent() != null)
            .toList();
        if (fetchItems.isEmpty()) {
            return List.of();
        }

        Set<ItemEntity> allItemsWithParent = new LinkedHashSet<>();
        if (levelEntity.getType() != null && LevelType.EXTERNAL.equals(levelEntity.getType()) && levelEntity.getParent() != null) {
            var parentCodeItems = fetchItems.stream()
                .map(ItemEntity::getParent)
                .filter(Objects::nonNull)
                .map(ItemEntity::getExternalCode)
                .toList();
            allItemsWithParent.addAll(fetchItems(levelEntity.getParent(), parentCodeItems));

            Set<ItemEntity> itemsToProcess = new LinkedHashSet<>(Stream.concat(fetchItems.stream(), allItemsWithParent.stream()).toList());
            for (ItemEntity item : itemsToProcess) {
                ItemEntity current = item;
                while (current != null) {
                    allItemsWithParent.add(current);
                    current = current.getParent();
                }
            }
        }

        var allItems = Stream.concat(fetchItems.stream(), allItemsWithParent.stream()).toList();
        return buildTreeSimpleByExternalCode(allItems, roleDTO);
    }

    private List<ItemEntity> fetchItems(LevelEntity levelEntity, Collection<String> codeItems) {
        List<String> ids = codeItems.stream()
                .filter(Objects::nonNull)
                .distinct()
                .toList();

        if (levelEntity.getType() != null && LevelType.EXTERNAL.equals(levelEntity.getType())) {
            var items = fetchExternalItems(ids, levelEntity.getExternalUrl(), levelEntity.getApiKey());
            items.forEach(item -> {
                item.setLevel(levelEntity);
                if (item.getParent() != null && item.getParent().getLevel() == null) {
                    item.getParent().setLevel(levelEntity.getParent());
                }
            });
            return items;
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
                .map(o -> levelClient.getItemsHierarchy(externalUrl, apiKey, o))
                .flatMap(List::stream)
                .map(itemHierarchyResumedMapper::toEntity)
                .toList();
    }

    private List<ItemResponseNodeDTO> buildTreeSimpleByExternalCode(List<ItemEntity> items, RoleDTO roleDTO) {
        if (items == null || items.isEmpty()) {
            return List.of();
        }

        Map<String, ItemResponseNodeDTO> nodeByCode = new LinkedHashMap<>();
        Set<String> children = new HashSet<>();

        for (ItemEntity item : items) {
            ItemResponseNodeDTO node = getOrCreateNode(item, nodeByCode, roleDTO);

            if (item.getParent() != null) {
                attachToParent(item, nodeByCode, node, children, roleDTO);
            }
        }

        return extractRootNodes(nodeByCode, children);
    }

    private ItemResponseNodeDTO getOrCreateNode(ItemEntity item, Map<String, ItemResponseNodeDTO> nodeByCode, RoleDTO roleDTO) {
        String code = getCode(item);
        return nodeByCode.computeIfAbsent(code, c -> createNode(item, roleDTO));
    }

    private void attachToParent(ItemEntity item, Map<String, ItemResponseNodeDTO> nodeByCode,
                                ItemResponseNodeDTO node, Set<String> children, RoleDTO roleDTO) {
        String parentCode = item.getParent().getExternalCode();
        if (parentCode == null) {
            return;
        }

        ItemResponseNodeDTO parent = nodeByCode.computeIfAbsent(parentCode, c -> createNode(item.getParent(), roleDTO));

        boolean alreadyAdded = parent.getItems().stream()
            .anyMatch(child -> Objects.equals(child.getId(), node.getId()));

        if (!alreadyAdded) {
            parent.getItems().add(node);
            children.add(getCode(item));
        }
    }

    private List<ItemResponseNodeDTO> extractRootNodes(Map<String, ItemResponseNodeDTO> nodeByCode, Set<String> children) {
        return nodeByCode.entrySet().stream()
            .filter(entry -> !children.contains(entry.getKey()))
            .map(Map.Entry::getValue)
            .toList();
    }

    private String getCode(ItemEntity item) {
        return item.getExternalCode() != null ? item.getExternalCode() : "TMP-" + item.getId();
    }

    private ItemResponseNodeDTO createNode(ItemEntity item, RoleDTO roleDTO) {
        LevelResumedDTO level = (item.getLevel() != null)
            ? new LevelResumedDTO(item.getLevel().getId(), item.getLevel().getName())
            : null;
        return new ItemResponseNodeDTO(item.getId(), item.getName(), level, roleDTO);
    }


}
