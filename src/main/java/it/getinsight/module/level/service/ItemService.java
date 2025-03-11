package it.getinsight.module.level.service;

import it.getinsight.module.level.client.FeignClientFactory;
import it.getinsight.module.level.dto.LevelDTO;
import it.getinsight.module.level.dto.ItemDTO;
import it.getinsight.module.level.entity.LevelEntity;
import it.getinsight.module.level.entity.LevelType;
import it.getinsight.module.level.entity.ItemEntity;
import it.getinsight.module.level.mapper.LevelMapper;
import it.getinsight.module.level.mapper.ItemMapper;
import it.getinsight.module.level.repository.LevelRepository;
import it.getinsight.module.level.repository.ItemRepository;
import it.getinsight.module.role.entity.RoleEntity;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
import java.util.UUID;

import static it.getinsight.message.MessageProperty.*;


@Service
@RequiredArgsConstructor
@Slf4j
public class ItemService {

    private final LevelRepository levelRepository;
    private final ItemRepository itemRepository;
    private final FeignClientFactory feignClientFactory;
    private final ItemMapper itemMapper;


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
                    var level = levelsMap.getOrDefault(levelId, levelRepository.findById(levelId).orElseThrow(level_NOT_FOUND_ERROR::businessException));


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
            log.error("Erro ao importar CSV", e);
            throw ERROR_IMPORT_CSV.businessException();
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

    public LevelDTO updateStatus(Long id, String status) {
        // Implementação
        return null;
    }
}

