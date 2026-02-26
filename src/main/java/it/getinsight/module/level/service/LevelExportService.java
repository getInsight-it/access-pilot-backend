package it.getinsight.module.level.service;

import it.getinsight.core.exception.BusinessException;
import it.getinsight.module.level.dto.LevelDTO;
import it.getinsight.module.level.dto.LevelExportDTO;
import it.getinsight.module.level.dto.LevelImportRequestDTO;
import it.getinsight.module.level.dto.LevelImportResultDTO;
import it.getinsight.module.level.dto.LevelImportSummaryDTO;
import it.getinsight.module.level.dto.LevelItemExportDTO;
import it.getinsight.module.level.entity.LevelEntity;
import it.getinsight.module.level.entity.LevelType;
import it.getinsight.module.level.entity.ItemEntity;
import it.getinsight.module.level.mapper.LevelExportMapper;
import it.getinsight.module.level.repository.ItemRepository;
import it.getinsight.module.level.repository.LevelRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static it.getinsight.message.MessageProperty.*;

@Service
@RequiredArgsConstructor
@Slf4j
public class LevelExportService {

    private static final String STATUS_CREATED = "CREATED";
    private static final String STATUS_UPDATED = "UPDATED";
    private static final String STATUS_IGNORED = "IGNORED";
    private static final String STATUS_ERROR = "ERROR";

    private final LevelRepository levelRepository;
    private final LevelExportMapper levelExportMapper;
    private final LevelService levelService;
    private final ItemRepository itemRepository;

    @Transactional(readOnly = true)
    public List<LevelExportDTO> exportLevels(boolean includeItems, boolean includeBuiltIn) {
        return levelRepository.findAll(Sort.by("id"))
            .stream()
            .filter(level -> includeBuiltIn || !LevelType.BUILT_IN.equals(level.getType()))
            .map(level -> toExport(level, includeItems))
            .toList();
    }

    public LevelImportSummaryDTO importLevels(LevelImportRequestDTO request) {
        var start = System.nanoTime();
        var results = new ArrayList<LevelImportResultDTO>();

        var exports = resolveExports(request);
        var existingByName = loadExistingLevelsByName(exports);
        var preExistingNames = new HashSet<>(existingByName.keySet());
        var availableByName = new HashMap<>(existingByName);

        var pending = collectPendingLevels(exports, results);
        processPendingLevels(pending, preExistingNames, availableByName, results);

        return buildSummary(results, start);
    }

    private List<LevelExportDTO> resolveExports(LevelImportRequestDTO request) {
        return request != null && CollectionUtils.isNotEmpty(request.exports())
            ? request.exports()
            : List.of();
    }

    private Map<String, LevelEntity> loadExistingLevelsByName(List<LevelExportDTO> exports) {
        var existingByName = new HashMap<String, LevelEntity>();
        var namesToLoad = exports.stream()
            .filter(Objects::nonNull)
            .flatMap(export -> Stream.of(export.name(), export.parentName()))
            .filter(StringUtils::isNotBlank)
            .map(LevelExportService::normalizeName)
            .collect(Collectors.toSet());

        if (!namesToLoad.isEmpty()) {
            levelRepository.findByNameIgnoreCaseIn(new ArrayList<>(namesToLoad)).forEach(level -> {
                var key = normalizeName(level.getName());
                if (key != null && !existingByName.containsKey(key)) {
                    existingByName.put(key, level);
                }
            });
        }

        return existingByName;
    }

    private List<LevelExportDTO> collectPendingLevels(List<LevelExportDTO> exports,
                                                      List<LevelImportResultDTO> results) {
        var seenNames = new HashSet<String>();
        var pending = new ArrayList<LevelExportDTO>();

        for (var export : exports) {
            if (export == null) {
                results.add(LevelImportResultDTO.builder()
                    .status(STATUS_ERROR)
                    .message(LEVEL_IMPORT_EMPTY.message())
                    .build());
                continue;
            }

            var name = StringUtils.trimToNull(export.name());
            if (name == null) {
                results.add(LevelImportResultDTO.builder()
                    .status(STATUS_ERROR)
                    .message(LEVEL_IMPORT_NAME_REQUIRED.message())
                    .build());
                continue;
            }

            var nameKey = normalizeName(name);
            if (!seenNames.add(nameKey)) {
                results.add(LevelImportResultDTO.builder()
                    .name(name)
                    .status(STATUS_ERROR)
                    .message(LEVEL_IMPORT_NAME_DUPLICATE.message())
                    .build());
                continue;
            }

            if (export.type() == null) {
                results.add(LevelImportResultDTO.builder()
                    .name(name)
                    .status(STATUS_ERROR)
                    .message(LEVEL_IMPORT_TYPE_REQUIRED.message())
                    .build());
                continue;
            }

            if (LevelType.BUILT_IN.equals(export.type())) {
                results.add(LevelImportResultDTO.builder()
                    .name(name)
                    .status(STATUS_IGNORED)
                    .message(LEVEL_IMPORT_BUILT_IN_NOT_ALLOWED.message())
                    .build());
                continue;
            }

            pending.add(export);
        }

        return pending;
    }

    private void processPendingLevels(List<LevelExportDTO> pending,
                                      Set<String> preExistingNames,
                                      Map<String, LevelEntity> availableByName,
                                      List<LevelImportResultDTO> results) {
        var remaining = new ArrayList<>(pending);
        boolean progress;
        while (!remaining.isEmpty()) {
            progress = false;
            var iterator = remaining.iterator();
            while (iterator.hasNext()) {
                var export = iterator.next();
                if (tryProcessLevel(export, preExistingNames, availableByName, results)) {
                    iterator.remove();
                    progress = true;
                }
            }

            if (!progress) {
                addMissingParentErrors(remaining, results);
                break;
            }
        }
    }

    private boolean tryProcessLevel(LevelExportDTO export,
                                    Set<String> preExistingNames,
                                    Map<String, LevelEntity> availableByName,
                                    List<LevelImportResultDTO> results) {
        var name = StringUtils.trimToNull(export.name());
        var nameKey = normalizeName(name);
        var parentName = StringUtils.trimToNull(export.parentName());
        var parentKey = normalizeName(parentName);

        if (parentName != null && Objects.equals(nameKey, parentKey)) {
            results.add(LevelImportResultDTO.builder()
                .name(name)
                .status(STATUS_ERROR)
                .message(LEVEL_IMPORT_SELF_PARENT.message())
                .build());
            return true;
        }

        var parent = parentKey != null ? availableByName.get(parentKey) : null;
        if (parentKey != null && parent == null) {
            return false;
        }

        var existing = availableByName.get(nameKey);
        var wasExisting = preExistingNames.contains(nameKey);
        try {
            var dto = toLevelDTO(export, parent, existing);
            if (wasExisting && existing != null) {
                levelService.update(existing.getId(), dto);
            } else {
                levelService.create(dto);
            }

            var saved = levelRepository.findByNameIgnoreCaseAndTypeAndActiveTrue(dto.name(), dto.type()).orElse(null);
            if (saved != null) {
                availableByName.put(nameKey, saved);
            }

            if (saved != null) {
                importItems(saved, export.items());
            }

            results.add(LevelImportResultDTO.builder()
                .name(name)
                .status(wasExisting ? STATUS_UPDATED : STATUS_CREATED)
                .build());
        } catch (Exception ex) {
            log.error("Falha ao importar esfera '{}'", name, ex);
            results.add(LevelImportResultDTO.builder()
                .name(name)
                .status(STATUS_ERROR)
                .message(ex.getMessage())
                .build());
        }

        return true;
    }

    private void addMissingParentErrors(List<LevelExportDTO> remaining, List<LevelImportResultDTO> results) {
        for (var export : remaining) {
            var name = StringUtils.trimToNull(export.name());
            var parentName = StringUtils.trimToNull(export.parentName());
            results.add(LevelImportResultDTO.builder()
                .name(name)
                .status(STATUS_ERROR)
                .message(LEVEL_IMPORT_PARENT_NOT_FOUND.bind(parentName).message())
                .build());
        }
    }

    private LevelImportSummaryDTO buildSummary(List<LevelImportResultDTO> results, long start) {
        var created = countByStatus(results, STATUS_CREATED);
        var updated = countByStatus(results, STATUS_UPDATED);
        var ignored = countByStatus(results, STATUS_IGNORED);
        var errors = countByStatus(results, STATUS_ERROR);

        var duration = TimeUnit.NANOSECONDS.toMillis(System.nanoTime() - start);
        return LevelImportSummaryDTO.builder()
            .created(created)
            .updated(updated)
            .ignored(ignored)
            .errors(errors)
            .duration(duration)
            .results(results)
            .build();
    }

    private long countByStatus(List<LevelImportResultDTO> results, String status) {
        return results.stream()
            .filter(result -> Objects.equals(status, result.status()))
            .count();
    }

    private LevelExportDTO toExport(LevelEntity level, boolean includeItems) {
        var base = levelExportMapper.toExport(level);
        var items = includeItems ? exportItems(level) : List.<LevelItemExportDTO>of();

        return LevelExportDTO.builder()
            .name(base.name())
            .sigla(base.sigla())
            .description(base.description())
            .type(base.type())
            .parentName(base.parentName())
            .externalUrl(base.externalUrl())
            .icon(base.icon())
            .apiKey(base.apiKey())
            .uuid(base.uuid())
            .items(items)
            .build();
    }

    private List<LevelItemExportDTO> exportItems(LevelEntity level) {
        return itemRepository.findAllByLevelId(level.getId())
            .stream()
            .map(item -> LevelItemExportDTO.builder()
                .name(item.getName())
                .description(item.getDescription())
                .externalCode(item.getExternalCode())
                .parentName(item.getParent() != null ? item.getParent().getName() : null)
                .parentCode(resolveParentCode(item.getParent()))
                .parentExternalCode(resolveParentExternalCode(item.getParent()))
                .build())
            .toList();
    }

    private void importItems(LevelEntity level, List<LevelItemExportDTO> items) {
        if (CollectionUtils.isEmpty(items)) {
            return;
        }

        if (LevelType.BUILT_IN.equals(level.getType())) {
            throw ITEM_IMPORT_BUILT_IN_NOT_ALLOWED.businessException();
        }

        var existingItems = itemRepository.findAllByLevelId(level.getId());
        var existingByName = indexItemsByName(existingItems);
        var existingByCode = indexItemsByCode(existingItems);
        var pending = validateItems(items);

        var availableByName = new HashMap<>(existingByName);
        var availableByCode = new HashMap<>(existingByCode);
        resolveAndPersistItems(level, pending, availableByName, availableByCode);
    }

    private Map<String, ItemEntity> indexItemsByName(List<ItemEntity> items) {
        var existingByName = new HashMap<String, ItemEntity>();
        for (var item : items) {
            var nameKey = normalizeName(item.getName());
            if (nameKey != null && !existingByName.containsKey(nameKey)) {
                existingByName.put(nameKey, item);
            }
        }
        return existingByName;
    }

    private Map<String, ItemEntity> indexItemsByCode(List<ItemEntity> items) {
        var existingByCode = new HashMap<String, ItemEntity>();
        for (var item : items) {
            var codeKey = normalizeCode(item.getExternalCode());
            if (codeKey != null && !existingByCode.containsKey(codeKey)) {
                existingByCode.put(codeKey, item);
            }
        }
        return existingByCode;
    }

    private List<LevelItemExportDTO> validateItems(List<LevelItemExportDTO> items) {
        var seenNames = new HashSet<String>();
        var pending = new ArrayList<LevelItemExportDTO>();

        for (var item : items) {
            if (item == null) {
                throw ITEM_IMPORT_EMPTY.businessException();
            }

            var name = StringUtils.trimToNull(item.name());
            if (name == null) {
                throw ITEM_IMPORT_NAME_REQUIRED.businessException();
            }

            var nameKey = normalizeName(name);
            var externalCodeKey = normalizeCode(item.externalCode());
            if (!seenNames.add(nameKey)) {
                throw ITEM_IMPORT_NAME_DUPLICATE.bind(name).businessException();
            }

            var parentName = StringUtils.trimToNull(item.parentName());
            var parentCodeKey = normalizeCode(item.parentCode());
            var parentExternalCodeKey = normalizeCode(item.parentExternalCode());
            if (parentName != null && Objects.equals(nameKey, normalizeName(parentName))) {
                throw ITEM_IMPORT_SELF_PARENT.bind(name).businessException();
            }
            if (parentExternalCodeKey == null && (parentName != null || parentCodeKey != null)) {
                throw ITEM_IMPORT_PARENT_EXTERNAL_REQUIRED.bind(name).businessException();
            }
            if (externalCodeKey != null && Objects.equals(externalCodeKey, parentExternalCodeKey)) {
                throw ITEM_IMPORT_SELF_PARENT.bind(name).businessException();
            }

            pending.add(item);
        }

        return pending;
    }

    private void resolveAndPersistItems(LevelEntity level,
                                        List<LevelItemExportDTO> pending,
                                        Map<String, ItemEntity> availableByName,
                                        Map<String, ItemEntity> availableByCode) {
        var remaining = new ArrayList<>(pending);
        var parentByCode = new HashMap<String, ItemEntity>();
        boolean progress;
        while (!remaining.isEmpty()) {
            progress = false;
            var iterator = remaining.iterator();
            while (iterator.hasNext()) {
                var itemExport = iterator.next();
                var name = StringUtils.trimToNull(itemExport.name());
                var nameKey = normalizeName(name);
                var parentExternalCodeKey = normalizeCode(itemExport.parentExternalCode());
                var parent = resolveParent(level, parentExternalCodeKey, parentByCode);
                if (parentExternalCodeKey != null && parent == null) {
                    continue;
                }

                var existing = availableByName.get(nameKey);
                var saved = saveItem(level, itemExport, name, existing, parent);
                availableByName.put(nameKey, saved);
                var codeKey = normalizeCode(saved.getExternalCode());
                if (codeKey != null) {
                    availableByCode.put(codeKey, saved);
                }

                iterator.remove();
                progress = true;
            }

            if (!progress) {
                throw buildMissingParentException(remaining.getFirst());
            }
        }
    }

    private ItemEntity resolveParent(LevelEntity level,
                                     String parentExternalCodeKey,
                                     Map<String, ItemEntity> parentByCode) {
        if (parentExternalCodeKey == null) {
            return null;
        }

        var parentLevel = level.getParent();
        if (parentLevel == null) {
            return null;
        }

        return parentByCode.computeIfAbsent(parentExternalCodeKey, code ->
            itemRepository.findByLevelIdAndExternalCode(parentLevel.getId(), code).orElse(null)
        );
    }

    private ItemEntity saveItem(LevelEntity level,
                                LevelItemExportDTO itemExport,
                                String name,
                                ItemEntity existing,
                                ItemEntity parent) {
        if (existing != null) {
            existing.setDescription(itemExport.description());
            existing.setExternalCode(itemExport.externalCode());
            existing.setParent(parent);
            return itemRepository.save(existing);
        }

        var entity = ItemEntity.builder()
            .name(name)
            .description(itemExport.description())
            .externalCode(itemExport.externalCode())
            .level(level)
            .parent(parent)
            .build();
        return itemRepository.save(entity);
    }

    private BusinessException buildMissingParentException(LevelItemExportDTO pendingItem) {
        var pendingName = StringUtils.trimToNull(pendingItem.name());
        var pendingParentExternalCode = StringUtils.trimToNull(pendingItem.parentExternalCode());
        return ITEM_IMPORT_PARENT_NOT_FOUND.bind(pendingParentExternalCode, pendingName).businessException();
    }

    private static String resolveParentCode(ItemEntity parent) {
        if (parent == null) {
            return null;
        }
        return parent.getExternalCode();
    }

    private static String resolveParentExternalCode(ItemEntity parent) {
        if (parent == null) {
            return null;
        }
        return parent.getExternalCode();
    }

    private static String normalizeName(String name) {
        if (name == null) {
            return null;
        }
        return name.trim().toLowerCase(Locale.ROOT);
    }

    private static String normalizeCode(String code) {
        if (code == null) {
            return null;
        }
        String trimmed = code.trim();
        return trimmed.isEmpty() ? null : trimmed;
    }

    private static LevelDTO toLevelDTO(LevelExportDTO export, LevelEntity parent, LevelEntity existing) {
        String name = StringUtils.trimToNull(export.name());
        return LevelDTO.builder()
            .id(existing != null ? existing.getId() : null)
            .uuid(export.uuid() != null ? export.uuid() : existing != null ? existing.getUuid() : null)
            .parentId(parent != null ? parent.getId() : null)
            .sigla(export.sigla())
            .name(name)
            .description(StringUtils.defaultString(export.description()))
            .externalUrl(export.externalUrl())
            .type(export.type())
            .icon(export.icon())
            .apiKey(export.apiKey())
            .build();
    }
}
