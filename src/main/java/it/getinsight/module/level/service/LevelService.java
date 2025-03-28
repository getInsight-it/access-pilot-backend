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
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static it.getinsight.message.MessageProperty.*;


@Service
@RequiredArgsConstructor
@Slf4j
public class LevelService {

    private final LevelRepository levelRepository;
    private final ItemRepository itemRepository;
    private final LevelMapper levelMapper;
    private final LevelResponseMapper levelResponseMapper;
    private final LevelHierarchyResponseMapper levelHierarchyResponseMapper;
    private final ItemMapper itemMapper;
    private final ItemHierarchyMapper itemHierarchyMapper;
    private final ItemFilterMapper itemFilterMapper;
    private final LevelFilterMapper levelFilterMapper;
    private final FeignClientFactory feignClientFactory;

    public PageableResponseModel<LevelResponseDTO> getAllPaginatedLevels(PageableRequestModel<LevelFilterDTO> configPage) {
        final var filter = configPage.getFilter();
        final var model = filter
            .map(levelFilterMapper::toDto)
            .map(levelMapper::toEntity)
            .orElse(new LevelEntity());

        final var matcher = ExampleMatcher
            .matchingAll()
            .withIgnoreNullValues()
            .withMatcher("name", ExampleMatcher.GenericPropertyMatcher::contains)
            .withMatcher("description", ExampleMatcher.GenericPropertyMatcher::contains)
            .withMatcher("externalUrl", ExampleMatcher.GenericPropertyMatcher::contains);

        final var example = Example.of(model, matcher);

        final var page = levelRepository.findAll(example, PaginationHelper.toPageable(configPage));
        return PaginationHelper.toPageResponse(levelResponseMapper.toDto(page.getContent()), page.getTotalElements());
    }

    public List<LevelResponseDTO> getHierarchy(Long id) {
        var hierarchy = Stream.iterate(levelRepository.findById(id).orElseThrow(LEVEL_NOT_FOUND_ERROR::businessException),
                Objects::nonNull,
                LevelEntity::getParent)
            .collect(Collectors.toList());

        Collections.reverse(hierarchy);
        return levelResponseMapper.toDto(hierarchy);
    }



    public LevelResponseDTO findById(Long id) {
        return levelRepository.findById(id).map(levelHierarchyResponseMapper::toDto).orElse(null);
    }



    public PageableResponseModel<ItemDTO> getSubItemsPaginatedByLevel(Long levelId,Long itemId, PageableRequestModel<ItemFilterDTO> configPage) {
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
            .withMatcher("parent.id", ExampleMatcher.GenericPropertyMatcher::exact);

        model.setParent(ItemEntity.builder().id(itemId).build());
        model.setLevel(LevelEntity.builder().id(levelId).build());

        final var example = Example.of(model, matcher);
        final var page = itemRepository.findAll(example, PaginationHelper.toPageable(configPage));
        return PaginationHelper.toPageResponse(itemMapper.toDto(page.getContent()), page.getTotalElements());
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public LevelDTO create(LevelDTO levelDTO) {
        if (LevelType.BUILT_IN.equals(levelDTO.type())) {
            throw CREATE_BUILT_IN_LEVEL.businessException();
        }
        if (Boolean.TRUE.equals(levelRepository.existsByName(levelDTO.name()))) {
            throw LEVEL_ALREADY_EXISTS_ERROR.businessException();
        }
        var entity = levelMapper.toEntity(levelDTO);
        return levelMapper.toDto(levelRepository.save(entity));
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public void importLevels(MultipartFile file) {
        try {
            var levelsMap = new HashMap<Long, LevelEntity>();

            List<LevelEntity> levels = new BufferedReader(new InputStreamReader(file.getInputStream(), StandardCharsets.UTF_8))
                .lines()
                .skip(1)
                .map(line -> {
                    String[] values = line.split(",");
                    Long id = Long.parseLong(values[0]);
                    UUID uuid = UUID.fromString(values[1]);
                    Long parentId = values[2].isEmpty() ? null : Long.parseLong(values[2]);
                    var parent = parentId != null ? levelsMap.get(parentId) : null;

                    var level = LevelEntity.builder()
                        .uuid(uuid)
                        .parent(parent)
                        .sigla(values[3])
                        .name(values[4])
                        .description(values[5])
                        .type(LevelType.valueOf(values[6]))
                        .apiKey(values[7])
                        .build();

                    levelsMap.put(id, level);
                    return level;
                }).toList();
            levelRepository.saveAll(levels);
        } catch (Exception e) {
            log.error(CoreMessageSource.get().message(ERROR_IMPORT_CSV.key()), e);
            throw ERROR_IMPORT_CSV.businessException();
        }
    }

    public void exportLevels(ExportationFilterDTO filter, HttpServletResponse response) {
        try {
            var levels = levelRepository.findByNameIn(filter.namesLevels());
            OutputStreamWriter writer = new OutputStreamWriter(response.getOutputStream(), StandardCharsets.UTF_8);
            writer.write("id,uuid,parentId,sigla,name,description,type,apiKey\n");
            for (LevelEntity level : levels) {
                writer.write(MessageFormat.format("{0},{1},{2},{3},{4},{5},{6},{7}\n",
                    level.getId(),
                    level.getUuid(),
                    level.getParent() != null ? level.getParent().getId() : "",
                    level.getSigla(),
                    level.getName(),
                    level.getDescription(),
                    level.getType().name(),
                    level.getApiKey()));
            }
            writer.flush();
        } catch (Exception e) {
            log.error(CoreMessageSource.get().message(ERROR_EXPORT_CSV.key()), e);
            throw ERROR_EXPORT_CSV.businessException();
        }
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public void update(Long id, LevelDTO levelDTO) {
        if (LevelType.BUILT_IN.equals(levelDTO.type())) {
            throw UPDATE_BUILT_IN_LEVEL.businessException();
        }
        if (levelRepository.existsById(id)) {
            var levelEntity = levelRepository.findById(id).orElseThrow(LEVEL_NOT_FOUND_ERROR::businessException);
            levelMapper.fromDto(levelDTO, levelEntity);
            levelEntity.setId(id);
            levelRepository.save(levelEntity);
        }
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

    public ItemHierarchyDTO getItemById(Long id, String itemId) {
        var levelEntity = levelRepository.findById(id).orElseThrow(LEVEL_NOT_FOUND_ERROR::businessException);
        if (LevelType.EXTERNAL.equals(levelEntity.getType())) {
            feignClientFactory.createClient(levelEntity.getExternalUrl()).getItemByExternalCode(levelEntity.getApiKey(), itemId);
        }
        var entity = itemRepository.findByLevelIdAndId(id, Long.parseLong(itemId)).orElseThrow(ITEM_NOT_FOUND_ERROR::businessException);
        return itemHierarchyMapper.toDto(entity);
    }

    public void delete(Long id) {
        if (levelRepository.existsById(id)) {
            var levelEntity = levelRepository.findById(id).orElseThrow(LEVEL_NOT_FOUND_ERROR::businessException);
            levelRepository.softDelete(levelEntity.getId());
        }
    }

    public void deleteItem(Long id, String itemId) {
        var levelEntity = levelRepository.findById(id).orElseThrow(LEVEL_NOT_FOUND_ERROR::businessException);
        if (!LevelType.EXTERNAL.equals(levelEntity.getType())) {
            var itemEntity = itemRepository.findByLevelIdAndId(id, Long.parseLong(itemId)).orElseThrow(ITEM_NOT_FOUND_ERROR::businessException);
            itemRepository.softDelete(itemEntity.getId());
        }
    }
}

