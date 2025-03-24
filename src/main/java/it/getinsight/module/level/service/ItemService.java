package it.getinsight.module.level.service;

import it.getinsight.core.helper.PaginationHelper;
import it.getinsight.core.message.CoreMessageSource;
import it.getinsight.core.pagination.PageableRequestModel;
import it.getinsight.core.pagination.PageableResponseModel;
import it.getinsight.module.level.client.FeignClientFactory;
import it.getinsight.module.level.dto.ItemDTO;
import it.getinsight.module.level.dto.ExportationFilterDTO;
import it.getinsight.module.level.dto.ItemFilterDTO;
import it.getinsight.module.level.entity.LevelEntity;
import it.getinsight.module.level.entity.LevelType;
import it.getinsight.module.level.entity.ItemEntity;
import it.getinsight.module.level.mapper.ItemFilterMapper;
import it.getinsight.module.level.mapper.ItemMapper;
import it.getinsight.module.level.repository.LevelRepository;
import it.getinsight.module.level.repository.ItemRepository;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
    private final ItemFilterMapper itemFilterMapper;

    public PageableResponseModel<ItemDTO> getItemsPaginatedByLevel(Long levelId, PageableRequestModel<ItemFilterDTO> configPage) {
        var levelEntity = levelRepository.findById(levelId).orElseThrow(LEVEL_NOT_FOUND_ERROR::businessException);

        if (LevelType.EXTERNAL.equals(levelEntity.getType())) {
            var client = feignClientFactory.createClient(levelEntity.getExternalUrl());
            return client.getItems(  levelEntity.getApiKey(), configPage.getPageNumber() + 1, configPage.getPageSize(), configPage.getSortField(), configPage.getSortType(), configPage.getFilter().orElse(null));
        }

        var filter = configPage.getFilter();
        var model = filter
            .map(itemFilterMapper::toDto)
            .map(itemMapper::toEntity)
            .orElse(new ItemEntity());

        final var matcher = ExampleMatcher
            .matchingAll()
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
        return PaginationHelper.toPageResponse(itemMapper.toDto(page.getContent()), page.getTotalElements());
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


    public Optional<ItemDTO> findByTypeAndCodeItem(LevelEntity level, String codeItem) {
        var levelType = level.getType();
        if (levelType == LevelType.BUILT_IN || levelType == LevelType.BUSINESS) {
            var entityFound = itemRepository.findById(Long.parseLong(codeItem))
                .orElseThrow(ITEM_NOT_FOUND_ERROR::businessException);
            log.info("Item found for request: {}", entityFound);
            return Optional.of(itemMapper.toDto(entityFound));
        }

        if (levelType == LevelType.EXTERNAL) {
            var opItemDtoFound = feignClientFactory.createClient(level.getExternalUrl())
                .getItemByExternalCode(level.getApiKey(), codeItem).orElseThrow(ITEM_NOT_FOUND_ERROR::businessException);
            log.info("Item found for request: {}", opItemDtoFound);
            return Optional.of(opItemDtoFound);
        }

        return Optional.empty();
    }

}

