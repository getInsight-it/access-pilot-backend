package it.getinsight.module.level.service;

import feign.FeignException;
import it.getinsight.core.helper.PaginationHelper;
import it.getinsight.core.message.CoreMessageSource;
import it.getinsight.core.pagination.PageableRequestModel;
import it.getinsight.core.pagination.PageableResponseModel;
import it.getinsight.module.level.client.ItemQueryParams;
import it.getinsight.module.level.client.LevelClient;
import it.getinsight.module.level.dto.ExportationFilterDTO;
import it.getinsight.module.level.dto.ItemDTO;
import it.getinsight.module.level.dto.ItemFilterDTO;
import it.getinsight.module.level.dto.ItemHierarchyResumedDTO;
import it.getinsight.module.level.entity.ItemEntity;
import it.getinsight.module.level.entity.LevelEntity;
import it.getinsight.module.level.entity.LevelType;
import it.getinsight.module.level.mapper.ItemFilterMapper;
import it.getinsight.module.level.mapper.ItemHierarchyResumedMapper;
import it.getinsight.module.level.mapper.ItemMapper;
import it.getinsight.module.level.mapper.LevelHierarchyResumedMapper;
import it.getinsight.module.level.repository.ItemRepository;
import it.getinsight.module.level.repository.LevelRepository;
import it.getinsight.utilitario.PropertyPathConstants;
import jakarta.persistence.criteria.Predicate;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.apache.commons.csv.QuoteMode;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.jpa.convert.QueryByExamplePredicateBuilder;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.text.MessageFormat;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static it.getinsight.message.MessageProperty.*;

@Service
@RequiredArgsConstructor
@Slf4j
public class ItemService {

    private final LevelRepository levelRepository;
    private final ItemRepository itemRepository;
    private final LevelClient levelClient;
    private final ItemMapper itemMapper;
    private final ItemResolverService itemResolverService;
    private final ItemHierarchyResumedMapper itemHierarchyResumedMapper;
    private final ItemFilterMapper itemFilterMapper;
    private final LevelHierarchyResumedMapper levelHierarchyResumedMapper;


    public PageableResponseModel<ItemHierarchyResumedDTO> getItemsPaginatedByLevel(Long levelId, PageableRequestModel<ItemFilterDTO> configPage) {
        var levelEntity = levelRepository.findById(levelId).orElseThrow(LEVEL_NOT_FOUND_ERROR::resourceNotFoundException);

        if (LevelType.EXTERNAL.equals(levelEntity.getType())) {
            var page = levelClient.getItems(levelEntity.getExternalUrl(), levelEntity.getApiKey(), configPage.getPageNumber() + 1, configPage.getPageSize(), configPage.getSortField(), configPage.getSortType(), configPage.getFilter().orElse(null));
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
            .withIgnoreCase()
            .withMatcher(PropertyPathConstants.Item.NAME, ExampleMatcher.GenericPropertyMatcher::contains)
            .withMatcher(PropertyPathConstants.Item.DESCRIPTION, ExampleMatcher.GenericPropertyMatcher::contains)
            .withMatcher(PropertyPathConstants.Item.EXTERNAL_CODE, ExampleMatcher.GenericPropertyMatcher::contains);

        final var example = Example.of(model, matcher);

        Specification<ItemEntity> spec = (root, query, cb) -> {
            Predicate examplePredicate = QueryByExamplePredicateBuilder.getPredicate(root, cb, example);
            Predicate levelPredicate = cb.equal(root.get("level").get("id"), levelId);

            if (examplePredicate != null) {
                return cb.and(levelPredicate, examplePredicate);
            } else {
                return levelPredicate;
            }
        };

        final var page = itemRepository.findAll(spec, PaginationHelper.toPageable(configPage));
        return PaginationHelper.toPageResponse(itemHierarchyResumedMapper.toDto(page.getContent()), page.getTotalElements());
    }

    private ItemHierarchyResumedDTO formatExternalItem(ItemHierarchyResumedDTO o, LevelEntity levelEntity) {
         var levelParent = levelRepository.findById(levelEntity.getParent().getId()).orElse(null);
        var resumedDTO = o.withLevel(levelHierarchyResumedMapper.toDto(levelEntity));
        if (o.parent() != null) {
            resumedDTO = resumedDTO.withParent(o.parent());
        }
        if (levelParent != null) {
            resumedDTO = resumedDTO.withLevel(levelHierarchyResumedMapper.toDto(levelParent));
        }
        return resumedDTO;
    }

    public PageableResponseModel<ItemHierarchyResumedDTO> getSubItemsPaginatedByLevel(Long levelId, String itemId, PageableRequestModel<ItemFilterDTO> configPage) {
        var levelEntity = levelRepository.findById(levelId).orElseThrow(LEVEL_NOT_FOUND_ERROR::resourceNotFoundException);

        if (LevelType.EXTERNAL.equals(levelEntity.getType())) {
            return getSubItemsPaginatedByLevelExternal(itemId, configPage, levelEntity);
        }

        var filter = configPage.getFilter();
        var model = filter
            .map(itemFilterMapper::toDto)
            .map(itemMapper::toEntity)
            .orElse(new ItemEntity());


        final var matcher = ExampleMatcher
            .matchingAny()
            .withIgnoreNullValues()
            .withIgnoreCase()
            .withMatcher(PropertyPathConstants.Item.NAME, ExampleMatcher.GenericPropertyMatcher::contains)
            .withMatcher(PropertyPathConstants.Item.DESCRIPTION, ExampleMatcher.GenericPropertyMatcher::contains)
            .withMatcher(PropertyPathConstants.Item.EXTERNAL_CODE, ExampleMatcher.GenericPropertyMatcher::contains);

        final var example = Example.of(model, matcher);

        Specification<ItemEntity> spec = (root, query, cb) -> {
            Predicate examplePredicate = QueryByExamplePredicateBuilder.getPredicate(root, cb, example);
            Predicate levelPredicate = cb.equal(root.get("level").get("id"), levelId);
            Predicate itemParentPredicate = cb.equal(root.get("parent").get("id"), itemId);

            if (examplePredicate != null) {
                return cb.and(levelPredicate,itemParentPredicate, examplePredicate);
            } else {
                return cb.and(levelPredicate, itemParentPredicate);
            }
        };
        final var page = itemRepository.findAll(spec, PaginationHelper.toPageable(configPage));
        return PaginationHelper.toPageResponse(itemHierarchyResumedMapper.toDto(page.getContent()), page.getTotalElements());
    }

    @NotNull
    private PageableResponseModel<ItemHierarchyResumedDTO> getSubItemsPaginatedByLevelExternal(String itemId, PageableRequestModel<ItemFilterDTO> configPage, LevelEntity levelEntity) {
        var levelEntityParent = levelEntity.getParent() != null ? levelEntity.getParent() : levelEntity;
        String itemExternalCodeResolved = null;
        if (!LevelType.EXTERNAL.equals(levelEntityParent.getType())) {
            itemExternalCodeResolved = itemRepository.findByLevelIdAndId(levelEntityParent.getId(), Long.parseLong(itemId)).orElseThrow(ITEM_NOT_FOUND_ERROR::resourceNotFoundException).getExternalCode();
        }

        var queryParams = ItemQueryParams.of(
            configPage.getPageNumber() + 1,
            configPage.getPageSize(),
            configPage.getSortField(),
            configPage.getSortType(),
            configPage.getFilter().orElse(null)
        );

        try {
            var page = levelClient.getSubItems(levelEntity.getExternalUrl(), levelEntity.getApiKey(), itemExternalCodeResolved, queryParams);
            var itemsFormated = page.getItems()
                .stream().map(o -> formatExternalItem(o, levelEntityParent)).toList();
            page.setItems(itemsFormated);
            return page;
        } catch (FeignException e) {
            throw ITEM_NOT_FOUND_ERROR.resourceNotFoundException();
        }
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public void importItems(Long levelId,MultipartFile file) {
        try {
            var itemsMap = new HashMap<Long, ItemEntity>();
            var level = levelRepository.findById(levelId).orElseThrow(LEVEL_NOT_FOUND_ERROR::resourceNotFoundException);
            try (Reader reader = new InputStreamReader(file.getInputStream(), StandardCharsets.UTF_8)) {

                CSVFormat format = CSVFormat.DEFAULT.builder()
                    .setHeader(PropertyPathConstants.Item.CSV_HEADERS)
                    .setSkipHeaderRecord(true)
                    .setDelimiter(',')
                    .setQuote('"')
                    .setQuoteMode(QuoteMode.ALL)
                    .setRecordSeparator("\n")
                    .get();

                try (CSVParser csvParser = format.parse(reader)) {

                    List<ItemEntity> items = new ArrayList<>();

                    for (CSVRecord csvRecord : csvParser) {
                        Long id = Long.parseLong(csvRecord.get(PropertyPathConstants.Common.ID));
                        String parentIdStr = csvRecord.get(PropertyPathConstants.Common.PARENT_ID);
                        Long parentId = parentIdStr.isEmpty() ? null : Long.parseLong(parentIdStr);
                        var parent = parentId != null ? itemsMap.get(parentId) : null;


                        var item = ItemEntity.builder()
                            .parent(parent)
                            .level(level)
                            .name(csvRecord.get(PropertyPathConstants.Item.NAME))
                            .description(csvRecord.isSet(PropertyPathConstants.Item.DESCRIPTION) ? csvRecord.get(PropertyPathConstants.Item.DESCRIPTION) : null)
                            .externalCode(csvRecord.isSet(PropertyPathConstants.Item.EXTERNAL_CODE) ? csvRecord.get(PropertyPathConstants.Item.EXTERNAL_CODE) : null)
                            .build();

                        itemsMap.put(id, item);
                        items.add(item);
                    }
                    items.forEach(this::validateItemBeforeCreate);
                    itemRepository.saveAll(items);
                }
            }

        } catch (Exception e) {
            log.error(CoreMessageSource.get().message(ERROR_IMPORT_CSV.key()), e);
            throw ERROR_IMPORT_CSV.businessException();
        }
    }


    public void exportItems(ExportationFilterDTO filter, HttpServletResponse response) {
        try {
            var items = itemRepository.findAllByLevelNameIn(filter.namesLevels());
            OutputStreamWriter writer = new OutputStreamWriter(response.getOutputStream(), StandardCharsets.UTF_8);
            writer.write("id,uuid,parentId,levelId,key,description,externalCode\n");

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


        String resolvedItemId = itemResolverService.resolveItemId(
            level,
            codeItem
        );

        if (levelType == LevelType.BUILT_IN || levelType == LevelType.BUSINESS) {
            var entityFound = itemRepository.findById(Long.parseLong(resolvedItemId))
                .orElseThrow(ITEM_NOT_FOUND_ERROR::resourceNotFoundException);
            log.info("Item found for request: {}", entityFound);
            return Optional.of(itemHierarchyResumedMapper.toDto(entityFound));
        }

        if (levelType == LevelType.EXTERNAL) {
            var itemDto = levelClient.getItemByExternalCode(level.getExternalUrl(), level.getApiKey(), resolvedItemId);
            log.info("Item found for request: {}", itemDto);
            return Optional.of(itemDto);
        }

        return Optional.empty();
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public void updateItem(Long id, String itemId, ItemDTO itemDTO) {
        var level = levelRepository.findById(id).orElseThrow(LEVEL_NOT_FOUND_ERROR::resourceNotFoundException);
        if (LevelType.BUILT_IN.equals(level.getType())) {
            throw CREATE_BUILT_IN_ITEM.businessException();
        }
        var itemEntity = itemRepository.findByLevelIdAndId(id, Long.parseLong(itemId)).orElseThrow(ITEM_NOT_FOUND_ERROR::resourceNotFoundException);
        itemMapper.fromDto(itemDTO, itemEntity);
        itemMapper.toDto(itemRepository.save(itemEntity));
    }

    public Integer getCountItemsByLevel(Long id) {
        var level = levelRepository.findById(id).orElseThrow(LEVEL_NOT_FOUND_ERROR::resourceNotFoundException);
        if (level.getType() == LevelType.EXTERNAL) {
            return levelClient.getCountLevel(level.getExternalUrl(), level.getApiKey());
        }
        return itemRepository.countByLevel(level);
    }

    public List<String> getAllSubitemCodes(Long levelId, String itemId) {
        var levelEntity = levelRepository.findById(levelId).orElseThrow(LEVEL_NOT_FOUND_ERROR::resourceNotFoundException);
        if (LevelType.EXTERNAL.equals(levelEntity.getType())) {
            return levelClient.getAllSubitemCodes(levelEntity.getExternalUrl(), levelEntity.getApiKey(), itemId);
        }
        return itemRepository.findAllSubItemCodesLevelIdAndId(levelEntity.getId(), itemId, levelEntity.getType());
    }

    public ItemHierarchyResumedDTO getItemById(Long id, String itemId) {
        var levelEntity = levelRepository.findById(id).orElseThrow(LEVEL_NOT_FOUND_ERROR::resourceNotFoundException);
        var levelType = levelEntity.getType();


        String resolvedItemId = itemResolverService.resolveItemId(
            levelEntity,
            itemId
        );

        if (LevelType.EXTERNAL.equals(levelType)) {
            var itemExternal = levelClient.getItemByExternalCode(
                levelEntity.getExternalUrl(),
                levelEntity.getApiKey(),
                resolvedItemId
            );
            return itemExternal.withLevel(levelHierarchyResumedMapper.toDto(levelEntity));
        }

        var entity = itemRepository.findByLevelIdAndId(id, Long.parseLong(resolvedItemId))
            .orElseThrow(ITEM_NOT_FOUND_ERROR::resourceNotFoundException);
        return itemHierarchyResumedMapper.toDto(entity);
    }


    @Transactional(propagation = Propagation.REQUIRED)
    public ItemDTO createItem(Long id, ItemDTO itemDTO) {
        var level = levelRepository.findById(id).orElseThrow(LEVEL_NOT_FOUND_ERROR::resourceNotFoundException);
        var entity = itemMapper.toEntity(itemDTO);
        entity.setLevel(level);
        validateItemBeforeCreate(entity);
        return itemMapper.toDto(itemRepository.save(entity));
    }

    private void validateItemBeforeCreate(ItemEntity entity) {
        if (LevelType.BUILT_IN.equals(entity.getLevel().getType()))
            throw CREATE_BUILT_IN_ITEM.businessException();

        if(itemRepository.existsItemEntityByActiveTrueAndLevelAndName(entity.getLevel(), entity.getName()))
            throw ITEM_ALREADY_EXISTS_ERROR.businessException();
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public void deleteItem(Long id, String itemId) {
        var levelEntity = levelRepository.findById(id).orElseThrow(LEVEL_NOT_FOUND_ERROR::resourceNotFoundException);
        if (!LevelType.EXTERNAL.equals(levelEntity.getType())) {
            var itemEntity = itemRepository.findByLevelIdAndId(id, Long.parseLong(itemId)).orElseThrow(ITEM_NOT_FOUND_ERROR::resourceNotFoundException);
            itemRepository.softDelete(itemEntity.getId());
        }
    }

    public List<ItemHierarchyResumedDTO> getItemHierarchy(Long levelId, String itemId) {
        var levelEntity = levelRepository.findById(levelId).orElseThrow(LEVEL_NOT_FOUND_ERROR::resourceNotFoundException);
        if (levelEntity == null) {
            throw LEVEL_NOT_FOUND_ERROR.resourceNotFoundException();
        }

        if (levelEntity.getType() != null && LevelType.EXTERNAL.equals(levelEntity.getType())) {
            var itemIdResolved = itemResolverService.resolveItemId(levelEntity, itemId);
            return getExternalHierarchy(levelEntity, itemIdResolved);
        }
        var item = itemRepository.findById(Long.valueOf(itemId))
            .orElseThrow(ITEM_NOT_FOUND_ERROR::resourceNotFoundException);

        if (!levelEntity.getId().equals(levelId)) {
            throw ITEM_NOT_FOUND_ERROR.resourceNotFoundException();
        }

        var hierarchy = Stream.iterate(item, Objects::nonNull, ItemEntity::getParent)
            .collect(Collectors.toList());

        Collections.reverse(hierarchy);

        return hierarchy.stream()
            .map(itemHierarchyResumedMapper::toDto)
            .toList();
    }

    public List<ItemHierarchyResumedDTO> getExternalHierarchy(LevelEntity levelEntity, String itemId) {
        List<ItemHierarchyResumedDTO> hierarchy = new ArrayList<>();
        ItemHierarchyResumedDTO currentItem = null;
        LevelEntity currentLevel = levelEntity;

        do {
            if (LevelType.EXTERNAL.equals(currentLevel.getType())){
            currentItem = levelClient.getItemByExternalCode(
                currentLevel.getExternalUrl(),
                currentLevel.getApiKey(),
                currentItem == null ? itemId : String.valueOf(currentItem.id())
            );

            }
            hierarchy.add(currentItem);

            currentItem = currentItem != null ? currentItem.parent() : null;
            currentLevel = currentLevel.getParent();

        } while (currentItem != null && currentLevel != null);

        Collections.reverse(hierarchy);
        return hierarchy;
    }

}

