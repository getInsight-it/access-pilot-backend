package it.getinsight.module.level.service;

import it.getinsight.core.helper.PaginationHelper;
import it.getinsight.core.message.CoreMessageSource;
import it.getinsight.core.pagination.PageableRequestModel;
import it.getinsight.core.pagination.PageableResponseModel;
import it.getinsight.module.level.client.FeignClientFactory;
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
import jakarta.persistence.criteria.Predicate;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.apache.commons.csv.QuoteMode;
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
            .withIgnoreCase()
            .withMatcher("name", ExampleMatcher.GenericPropertyMatcher::contains)
            .withMatcher("description", ExampleMatcher.GenericPropertyMatcher::contains)
            .withMatcher("externalCode", ExampleMatcher.GenericPropertyMatcher::contains);

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
        return o.withLevel(levelHierarchyResumedMapper.toDto(levelEntity)).withParent(o.parent().withLevel(levelHierarchyResumedMapper.toDto(levelEntity.getParent())));
    }


    @Transactional(propagation = Propagation.REQUIRED)
    public void importItems(Long levelId,MultipartFile file) {
        try {
            var itemsMap = new HashMap<Long, ItemEntity>();
            var level = levelRepository.findById(levelId).orElseThrow(LEVEL_NOT_FOUND_ERROR::businessException);
            try (Reader reader = new InputStreamReader(file.getInputStream(), StandardCharsets.UTF_8)) {

                CSVFormat format = CSVFormat.DEFAULT.builder()
                    .setHeader("id", "parentId", "name", "description", "externalCode")
                    .setSkipHeaderRecord(true)
                    .setDelimiter(',')
                    .setQuote('"')
                    .setQuoteMode(QuoteMode.ALL)
                    .setRecordSeparator("\n")
                    .get();

                try (CSVParser csvParser = format.parse(reader)) {

                    List<ItemEntity> items = new ArrayList<>();

                    for (CSVRecord csvRecord : csvParser) {
                        Long id = Long.parseLong(csvRecord.get("id"));
                        String parentIdStr = csvRecord.get("parentId");
                        Long parentId = parentIdStr.isEmpty() ? null : Long.parseLong(parentIdStr);
                        var parent = parentId != null ? itemsMap.get(parentId) : null;


                        var item = ItemEntity.builder()
                            .parent(parent)
                            .level(level)
                            .name(csvRecord.get("name"))
                            .description(csvRecord.isSet("description") ? csvRecord.get("description") : null)
                            .externalCode(csvRecord.isSet("externalCode") ? csvRecord.get("externalCode") : null)
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
        if (levelType == LevelType.BUILT_IN || levelType == LevelType.BUSINESS) {
            var entityFound = itemRepository.findById(Long.parseLong(codeItem))
                .orElseThrow(ITEM_NOT_FOUND_ERROR::businessException);
            log.info("Item found for request: {}", entityFound);
            return Optional.of(itemHierarchyResumedMapper.toDto(entityFound));
        }

        if (levelType == LevelType.EXTERNAL) {
            var opItemDtoFound = Optional.of(feignClientFactory.createClient(level.getExternalUrl())
                .getItemByExternalCode(level.getApiKey(), codeItem)).orElseThrow(ITEM_NOT_FOUND_ERROR::businessException);
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
            var itemExternal = Optional.of(feignClientFactory.createClient(levelEntity.getExternalUrl()).getItemByExternalCode(levelEntity.getApiKey(), itemId));
            var itemHierarchyDTO = itemExternal.orElseThrow(ITEM_NOT_FOUND_ERROR::businessException);
            return itemHierarchyDTO.withLevel(levelHierarchyResumedMapper.toDto(levelEntity));
        }
        var entity = itemRepository.findByLevelIdAndId(id, Long.parseLong(itemId)).orElseThrow(ITEM_NOT_FOUND_ERROR::businessException);
        return itemHierarchyResumedMapper.toDto(entity);
    }


    @Transactional(propagation = Propagation.REQUIRED)
    public ItemDTO createItem(Long id, ItemDTO itemDTO) {
        var level = levelRepository.findById(id).orElseThrow(LEVEL_NOT_FOUND_ERROR::businessException);
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
        var levelEntity = levelRepository.findById(id).orElseThrow(LEVEL_NOT_FOUND_ERROR::businessException);
        if (!LevelType.EXTERNAL.equals(levelEntity.getType())) {
            var itemEntity = itemRepository.findByLevelIdAndId(id, Long.parseLong(itemId)).orElseThrow(ITEM_NOT_FOUND_ERROR::businessException);
            itemRepository.softDelete(itemEntity.getId());
        }
    }

    public List<ItemHierarchyResumedDTO> getItemHierarchy(Long levelId, String itemId) {
        var levelEntity = levelRepository.findById(levelId).orElseThrow(LEVEL_NOT_FOUND_ERROR::businessException);
        if (levelEntity == null) {
            throw LEVEL_NOT_FOUND_ERROR.businessException();
        }

        if (levelEntity.getType() != null && LevelType.EXTERNAL.equals(levelEntity.getType())) {
            return getExternalHierarchy(levelEntity, itemId);
        }
        var item = itemRepository.findById(Long.valueOf(itemId))
            .orElseThrow(ITEM_NOT_FOUND_ERROR::businessException);

        if (!levelEntity.getId().equals(levelId)) {
            throw ITEM_NOT_FOUND_ERROR.businessException();
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
        LevelEntity currentLevel = null;

        do {
            var client = feignClientFactory.createClient(levelEntity.getExternalUrl());
            currentItem = client.getItemByExternalCode(
                currentLevel == null ? levelEntity.getApiKey() : currentLevel.getApiKey(),
                currentItem == null ? itemId : String.valueOf(currentItem.id()));
            hierarchy.add(currentItem);
            currentItem = currentItem.parent();
            currentLevel = levelEntity.getParent();
        }while (currentItem != null);

        Collections.reverse(hierarchy);
        return hierarchy;
    }



}

