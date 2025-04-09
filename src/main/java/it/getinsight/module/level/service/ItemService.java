package it.getinsight.module.level.service;

import it.getinsight.core.helper.PaginationHelper;
import it.getinsight.core.message.CoreMessageSource;
import it.getinsight.core.pagination.PageableRequestModel;
import it.getinsight.core.pagination.PageableResponseModel;
import it.getinsight.module.level.client.FeignClientFactory;
import it.getinsight.module.level.dto.*;
import it.getinsight.module.level.entity.ItemEntity;
import it.getinsight.module.level.entity.LevelEntity;
import it.getinsight.module.level.entity.LevelType;
import it.getinsight.module.level.mapper.*;
import it.getinsight.module.level.repository.ItemRepository;
import it.getinsight.module.level.repository.LevelRepository;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.text.MessageFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

import static it.getinsight.message.MessageProperty.*;


@Service
@RequiredArgsConstructor
@Slf4j
public class ItemService {

    private final LevelRepository levelRepository;
    private final ItemRepository itemRepository;
    private final FeignClientFactory feignClientFactory;
    private final ItemMapper itemMapper;
    private final ItemHierarchyResumedMapper itemHierarchyResumedMapper;
    private final ItemFilterMapper itemFilterMapper;
    private final LevelHierarchyResumedMapper levelHierarchyResumedMapper;

    public PageableResponseModel<ItemHierarchyResumedDTO> getItemsPaginatedByLevel(Long levelId, PageableRequestModel<ItemFilterDTO> configPage) {
        var levelEntity = levelRepository.findById(levelId).orElseThrow(LEVEL_NOT_FOUND_ERROR::businessException);

        if (LevelType.EXTERNAL.equals(levelEntity.getType())) {
            var client = feignClientFactory.createClient(levelEntity.getExternalUrl());
            var page = client.getItems(levelEntity.getApiKey(), configPage.getPageNumber() + 1, configPage.getPageSize(), configPage.getSortField(), configPage.getSortType(), configPage.getFilter().orElse(null));
            var itemsFormated = page.getItems()
                .stream().map(o -> formatExternalItem(o, levelEntity)).toList();
            page.setItems(itemsFormated);
            return page;
        }

        var filter = configPage.getFilter();
        var model = filter
            .map(itemFilterMapper::toDto)
            .map(itemMapper::toEntity)
            .orElse(new ItemEntity());

        final var matcher = ExampleMatcher
            .matchingAny()
            .withIgnoreNullValues()
            .withMatcher("name", ExampleMatcher.GenericPropertyMatcher::contains)
            .withMatcher("description", ExampleMatcher.GenericPropertyMatcher::contains)
            .withMatcher("externalCode", ExampleMatcher.GenericPropertyMatcher::contains)
            .withMatcher("level.id", ExampleMatcher.GenericPropertyMatcher::exact)
            .withMatcher("level.active", ExampleMatcher.GenericPropertyMatcher::exact);
        model.setLevel(LevelEntity.builder().id(levelId).build());
        model.setActive(true);
        final var example = Example.of(model, matcher);


        final var page = itemRepository.findAll(example, PaginationHelper.toPageable(configPage));
        return PaginationHelper.toPageResponse(itemHierarchyResumedMapper.toDto(page.getContent()), page.getTotalElements());
    }


    private ItemHierarchyResumedDTO formatExternalItem(ItemHierarchyResumedDTO o, LevelEntity levelEntity) {
        return o.withLevel(levelHierarchyResumedMapper.toDto(levelEntity)).withParent(o.parent().withLevel(levelHierarchyResumedMapper.toDto(levelEntity.getParent())));
    }


    @Transactional(propagation = Propagation.REQUIRED)
    public void importLevels(MultipartFile file) {
        try {
            var itemsMap = new HashMap<Long, ItemEntity>();
            var levelsMap = new HashMap<Long, LevelEntity>();

            List<ItemEntity> items = new BufferedReader(new InputStreamReader(file.getInputStream(), StandardCharsets.UTF_8))
                .lines()
                .skip(1)
                .map(line -> {
                    String[] values = line.split(",");
                    Long levelId = Long.parseLong(values[0]);
                    Long id = Long.parseLong(values[1]);
                    Long parentId = values[2].isEmpty() ? null : Long.parseLong(values[2]);
                    var parent = parentId != null ? itemsMap.get(parentId) : null;
                    var level = levelsMap.getOrDefault(levelId, levelRepository.findById(levelId).orElseThrow(LEVEL_NOT_FOUND_ERROR::businessException));


                    var item = ItemEntity.builder()
                        .parent(parent)
                        .level(level)
                        .name(values[2])
                        .description(values[4])
                        .externalCode(values.length > 5 ? values[5] : null)
                        .build();

                    itemsMap.put(id, item);
                    levelsMap.put(levelId, level);
                    return item;
                }).toList();
            itemRepository.saveAll(items);
        } catch (Exception e) {
            log.error(CoreMessageSource.get().message(ERROR_IMPORT_CSV.key()), e);
            throw ERROR_IMPORT_CSV.businessException();
        }
    }

    public void exportItems(ExportationFilterDTO filter, HttpServletResponse response) {
        try {
            var items = itemRepository.findAllByLevelNameIn(filter.namesLevels());
            OutputStreamWriter writer = new OutputStreamWriter(response.getOutputStream(), StandardCharsets.UTF_8);
            writer.write("id,uuid,parentId,levelId,name,description,externalCode\n");

            for (ItemEntity item : items) {
                writer.write(MessageFormat.format("{0},{1},{2},{3},{4},{5},{6}\n",
                    item.getId(),
                    item.getUuid(),
                    item.getParent() != null ? item.getParent().getId() : "",
                    item.getLevel() != null ? item.getLevel().getId() : "",
                    item.getName(),
                    item.getDescription(),
                    item.getExternalCode() != null ? item.getExternalCode() : ""));
            }

            writer.flush();
        } catch (Exception e) {
            log.error(CoreMessageSource.get().message(ERROR_EXPORT_CSV.key()), e);
            throw ERROR_EXPORT_CSV.businessException();
        }
    }


    public Optional<ItemHierarchyResumedDTO> findByTypeAndCodeItem(LevelEntity level, String codeItem) {
        var levelType = level.getType();
        if (levelType == LevelType.BUILT_IN || levelType == LevelType.BUSINESS) {
            var entityFound = itemRepository.findById(Long.parseLong(codeItem))
                .orElseThrow(ITEM_NOT_FOUND_ERROR::businessException);
            log.info("Item found for request: {}", entityFound);
            return Optional.of(itemHierarchyResumedMapper.toDto(entityFound));
        }

        if (levelType == LevelType.EXTERNAL) {
            var opItemDtoFound = feignClientFactory.createClient(level.getExternalUrl())
                .getItemByExternalCode(level.getApiKey(), codeItem).orElseThrow(ITEM_NOT_FOUND_ERROR::businessException);
            log.info("Item found for request: {}", opItemDtoFound);
            return Optional.of(opItemDtoFound);
        }

        return Optional.empty();
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public void updateItem(Long id, String itemId, ItemDTO itemDTO) {
        var level = levelRepository.findById(id).orElseThrow(LEVEL_NOT_FOUND_ERROR::businessException);
        if (LevelType.BUILT_IN.equals(level.getType())) {
            throw CREATE_BUILT_IN_ITEM.businessException();
        }
        var itemEntity = itemRepository.findByLevelIdAndId(id, Long.parseLong(itemId)).orElseThrow(ITEM_NOT_FOUND_ERROR::businessException);
        itemMapper.fromDto(itemDTO, itemEntity);
        itemMapper.toDto(itemRepository.save(itemEntity));
    }

    public Integer getCountItemsByLevel(Long id) {
        var level = levelRepository.findById(id).orElseThrow(LEVEL_NOT_FOUND_ERROR::businessException);
        if (level.getType() == LevelType.EXTERNAL) {
            return feignClientFactory.createClient(level.getExternalUrl())
                .getCount(level.getApiKey());
        }
        return itemRepository.countByLevel(level);
    }

    public ItemHierarchyResumedDTO getItemById(Long id, String itemId) {
        var levelEntity = levelRepository.findById(id).orElseThrow(LEVEL_NOT_FOUND_ERROR::businessException);
        if (LevelType.EXTERNAL.equals(levelEntity.getType())) {
            var itemExternal = feignClientFactory.createClient(levelEntity.getExternalUrl()).getItemByExternalCode(levelEntity.getApiKey(), itemId);
            var itemHierarchyDTO = itemExternal.orElseThrow(ITEM_NOT_FOUND_ERROR::businessException);
            return itemHierarchyDTO.withLevel(levelHierarchyResumedMapper.toDto(levelEntity));
        }
        var entity = itemRepository.findByLevelIdAndId(id, Long.parseLong(itemId)).orElseThrow(ITEM_NOT_FOUND_ERROR::businessException);
        return itemHierarchyResumedMapper.toDto(entity);
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public ItemDTO createItem(Long id, ItemDTO itemDTO) {
        var level = levelRepository.findById(id).orElseThrow(LEVEL_NOT_FOUND_ERROR::businessException);
        if (LevelType.BUILT_IN.equals(level.getType())) {
            throw CREATE_BUILT_IN_ITEM.businessException();
        }
        var entity = itemMapper.toEntity(itemDTO);
        entity.setLevel(level);
        return itemMapper.toDto(itemRepository.save(entity));
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public void deleteItem(Long id, String itemId) {
        var levelEntity = levelRepository.findById(id).orElseThrow(LEVEL_NOT_FOUND_ERROR::businessException);
        if (!LevelType.EXTERNAL.equals(levelEntity.getType())) {
            var itemEntity = itemRepository.findByLevelIdAndId(id, Long.parseLong(itemId)).orElseThrow(ITEM_NOT_FOUND_ERROR::businessException);
            itemRepository.softDelete(itemEntity.getId());
        }
    }
}

