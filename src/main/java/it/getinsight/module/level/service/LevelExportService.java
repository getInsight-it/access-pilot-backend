package it.getinsight.module.level.service;

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
        long start = System.nanoTime();
        long created = 0;
        long updated = 0;
        long ignored = 0;
        long errors = 0;
        List<LevelImportResultDTO> results = new ArrayList<>();

        List<LevelExportDTO> exports = request != null && CollectionUtils.isNotEmpty(request.exports())
            ? request.exports()
            : List.of();

        Map<String, LevelEntity> existingByName = new HashMap<>();
        levelRepository.findAll().forEach(level -> {
            String key = normalizeName(level.getName());
            if (key != null && !existingByName.containsKey(key)) {
                existingByName.put(key, level);
            }
        });

        Set<String> preExistingNames = new HashSet<>(existingByName.keySet());
        Map<String, LevelEntity> availableByName = new HashMap<>(existingByName);
        Set<String> seenNames = new HashSet<>();
        List<LevelExportDTO> pending = new ArrayList<>();

        for (LevelExportDTO export : exports) {
            if (export == null) {
                results.add(LevelImportResultDTO.builder()
                    .status(STATUS_ERROR)
                    .message("Export vazio.")
                    .build());
                errors++;
                continue;
            }

            String name = StringUtils.trimToNull(export.name());
            if (name == null) {
                results.add(LevelImportResultDTO.builder()
                    .status(STATUS_ERROR)
                    .message("Nome da esfera ausente.")
                    .build());
                errors++;
                continue;
            }

            String nameKey = normalizeName(name);
            if (!seenNames.add(nameKey)) {
                results.add(LevelImportResultDTO.builder()
                    .name(name)
                    .status(STATUS_ERROR)
                    .message("Nome duplicado no arquivo de importacao.")
                    .build());
                errors++;
                continue;
            }

            if (export.type() == null) {
                results.add(LevelImportResultDTO.builder()
                    .name(name)
                    .status(STATUS_ERROR)
                    .message("Tipo da esfera ausente.")
                    .build());
                errors++;
                continue;
            }

            if (LevelType.BUILT_IN.equals(export.type())) {
                results.add(LevelImportResultDTO.builder()
                    .name(name)
                    .status(STATUS_IGNORED)
                    .message("Esfera BUILT_IN nao pode ser importada.")
                    .build());
                ignored++;
                continue;
            }

            pending.add(export);
        }

        List<LevelExportDTO> remaining = new ArrayList<>(pending);
        boolean progress;
        while (!remaining.isEmpty()) {
            progress = false;
            Iterator<LevelExportDTO> iterator = remaining.iterator();
            while (iterator.hasNext()) {
                LevelExportDTO export = iterator.next();
                String name = StringUtils.trimToNull(export.name());
                String nameKey = normalizeName(name);
                String parentName = StringUtils.trimToNull(export.parentName());
                String parentKey = normalizeName(parentName);

                if (parentName != null && Objects.equals(nameKey, parentKey)) {
                    results.add(LevelImportResultDTO.builder()
                        .name(name)
                        .status(STATUS_ERROR)
                        .message("Esfera nao pode ser pai dela mesma.")
                        .build());
                    errors++;
                    iterator.remove();
                    progress = true;
                    continue;
                }

                LevelEntity parent = parentKey != null ? availableByName.get(parentKey) : null;
                if (parentKey != null && parent == null) {
                    continue;
                }

                LevelEntity existing = availableByName.get(nameKey);
                boolean wasExisting = preExistingNames.contains(nameKey);
                try {
                    LevelDTO dto = toLevelDTO(export, parent, existing);
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

                    if (wasExisting) {
                        updated++;
                    } else {
                        created++;
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
                    errors++;
                }

                iterator.remove();
                progress = true;
            }

            if (!progress) {
                for (LevelExportDTO export : remaining) {
                    String name = StringUtils.trimToNull(export.name());
                    String parentName = StringUtils.trimToNull(export.parentName());
                    results.add(LevelImportResultDTO.builder()
                        .name(name)
                        .status(STATUS_ERROR)
                        .message("Esfera pai nao encontrada: " + parentName)
                        .build());
                    errors++;
                }
                break;
            }
        }

        long duration = TimeUnit.NANOSECONDS.toMillis(System.nanoTime() - start);
        return LevelImportSummaryDTO.builder()
            .created(created)
            .updated(updated)
            .ignored(ignored)
            .errors(errors)
            .duration(duration)
            .results(results)
            .build();
    }

    private LevelExportDTO toExport(LevelEntity level, boolean includeItems) {
        LevelExportDTO base = levelExportMapper.toExport(level);
        List<LevelItemExportDTO> items = includeItems ? exportItems(level) : List.of();

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
                .parentCode(resolveParentCode(level, item.getParent()))
                .parentExternalCode(resolveParentExternalCode(level, item.getParent()))
                .build())
            .toList();
    }

    private void importItems(LevelEntity level, List<LevelItemExportDTO> items) {
        if (CollectionUtils.isEmpty(items)) {
            return;
        }

        if (LevelType.BUILT_IN.equals(level.getType())) {
            throw new IllegalStateException("Itens nao podem ser importados para esferas BUILT_IN.");
        }

        Map<String, ItemEntity> existingByName = new HashMap<>();
        Map<String, ItemEntity> existingByCode = new HashMap<>();
        itemRepository.findAllByLevelId(level.getId())
            .forEach(item -> {
                String nameKey = normalizeName(item.getName());
                if (nameKey != null && !existingByName.containsKey(nameKey)) {
                    existingByName.put(nameKey, item);
                }
                String codeKey = normalizeCode(item.getExternalCode());
                if (codeKey != null && !existingByCode.containsKey(codeKey)) {
                    existingByCode.put(codeKey, item);
                }
            });

        Set<String> seenNames = new HashSet<>();
        List<LevelItemExportDTO> pending = new ArrayList<>();

        for (LevelItemExportDTO item : items) {
            if (item == null) {
                throw new IllegalStateException("Item vazio na importacao.");
            }

            String name = StringUtils.trimToNull(item.name());
            if (name == null) {
                throw new IllegalStateException("Nome do item ausente.");
            }

            String nameKey = normalizeName(name);
            String externalCodeKey = normalizeCode(item.externalCode());
            if (!seenNames.add(nameKey)) {
                throw new IllegalStateException("Nome de item duplicado na importacao: " + name);
            }

            String parentName = StringUtils.trimToNull(item.parentName());
            String parentCodeKey = normalizeCode(item.parentCode());
            String parentExternalCodeKey = normalizeCode(item.parentExternalCode());
            if (parentName != null && Objects.equals(nameKey, normalizeName(parentName))) {
                throw new IllegalStateException("Item nao pode ser pai dele mesmo: " + name);
            }
            if (parentExternalCodeKey == null && (parentName != null || parentCodeKey != null)) {
                throw new IllegalStateException("parentExternalCode obrigatorio para item com pai: " + name);
            }
            if (externalCodeKey != null && Objects.equals(externalCodeKey, parentExternalCodeKey)) {
                throw new IllegalStateException("Item nao pode ser pai dele mesmo: " + name);
            }

            pending.add(item);
        }

        Map<String, ItemEntity> availableByName = new HashMap<>(existingByName);
        Map<String, ItemEntity> availableByCode = new HashMap<>(existingByCode);
        List<LevelItemExportDTO> remaining = new ArrayList<>(pending);
        boolean progress;
        while (!remaining.isEmpty()) {
            progress = false;
            Iterator<LevelItemExportDTO> iterator = remaining.iterator();
            while (iterator.hasNext()) {
                LevelItemExportDTO itemExport = iterator.next();
                String name = StringUtils.trimToNull(itemExport.name());
                String nameKey = normalizeName(name);
                String parentExternalCodeKey = normalizeCode(itemExport.parentExternalCode());
                ItemEntity parent = null;
                if (parentExternalCodeKey != null) {
                    parent = availableByCode.get(parentExternalCodeKey);
                }
                if (parentExternalCodeKey != null && parent == null) {
                    continue;
                }

                ItemEntity existing = availableByName.get(nameKey);
                if (existing != null) {
                    existing.setDescription(itemExport.description());
                    existing.setExternalCode(itemExport.externalCode());
                    existing.setParent(parent);
                    ItemEntity saved = itemRepository.save(existing);
                    availableByName.put(nameKey, saved);
                    String codeKey = normalizeCode(saved.getExternalCode());
                    if (codeKey != null) {
                        availableByCode.put(codeKey, saved);
                    }
                } else {
                    var entity = ItemEntity.builder()
                        .name(name)
                        .description(itemExport.description())
                        .externalCode(itemExport.externalCode())
                        .level(level)
                        .parent(parent)
                        .build();
                    var saved = itemRepository.save(entity);
                    availableByName.put(nameKey, saved);
                    String codeKey = normalizeCode(saved.getExternalCode());
                    if (codeKey != null) {
                        availableByCode.put(codeKey, saved);
                    }
                }

                iterator.remove();
                progress = true;
            }

            if (!progress) {
                LevelItemExportDTO pendingItem = remaining.get(0);
                String pendingName = StringUtils.trimToNull(pendingItem.name());
                String pendingParentExternalCode = StringUtils.trimToNull(pendingItem.parentExternalCode());
                throw new IllegalStateException("Item pai nao encontrado: " + pendingParentExternalCode + " para item " + pendingName);
            }
        }
    }

    private static String resolveParentCode(LevelEntity level, ItemEntity parent) {
        if (parent == null) {
            return null;
        }
        return parent.getExternalCode();
    }

    private static String resolveParentExternalCode(LevelEntity level, ItemEntity parent) {
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
