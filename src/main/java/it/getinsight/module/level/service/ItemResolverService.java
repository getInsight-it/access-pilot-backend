package it.getinsight.module.level.service;

import it.getinsight.module.level.client.LevelClient;
import it.getinsight.module.level.entity.LevelEntity;
import it.getinsight.module.level.repository.ItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static it.getinsight.message.MessageProperty.*;

@Service
@RequiredArgsConstructor
public class ItemResolverService {

    private final ItemRepository itemRepository;
    private final LevelClient levelClient;

    @Transactional(readOnly = true)
    /**
     * @deprecated Use resolveCodeItem instead of resolveItemId because in the feature the itemId will be the externalCode
     **/
    public String resolveItemId(LevelEntity levelEntity, String codeItem) {
        switch (levelEntity.getType()) {
            case EXTERNAL -> {
                if (codeItem == null || codeItem.isBlank()) {
                    throw CODE_ITEM_NOT_FOUND_FOR_ROLE.resourceNotFoundException();
                }

                var dto = levelClient.getItemByExternalCode(levelEntity.getExternalUrl(), levelEntity.getApiKey(), codeItem);
                return dto.externalCode();
            }
            case BUILT_IN, BUSINESS -> {
                if (codeItem == null || codeItem.isBlank()) {
                    throw CODE_ITEM_NOT_FOUND_FOR_ROLE.resourceNotFoundException();
                }

                return itemRepository.findByLevelIdAndId(levelEntity.getId(), Long.valueOf(codeItem))
                    .map(item -> item.getId().toString())
                    .orElseThrow(ITEM_NOT_FOUND_ERROR::resourceNotFoundException);
            }
            default -> throw UNSUPPORTED_SPHERE_TYPE.businessException();
        }
    }

    @Transactional(readOnly = true)
    public String resolveCodeItem(LevelEntity levelEntity, String codeItem) {
        switch (levelEntity.getType()) {
            case EXTERNAL -> {
                if (codeItem == null || codeItem.isBlank()) {
                    throw CODE_ITEM_NOT_FOUND_FOR_ROLE.resourceNotFoundException();
                }

                var dto = levelClient.getItemByExternalCode(levelEntity.getExternalUrl(), levelEntity.getApiKey(), codeItem);
                return dto.externalCode();
            }
            case BUILT_IN, BUSINESS -> {
                if (codeItem == null || codeItem.isBlank()) {
                    throw CODE_ITEM_NOT_FOUND_FOR_ROLE.resourceNotFoundException();
                }

                return itemRepository.findByLevelIdAndExternalCode(levelEntity.getId(), codeItem)
                    .map(item -> item.getId().toString())
                    .orElseThrow(ITEM_NOT_FOUND_ERROR::resourceNotFoundException);
            }
            default -> throw UNSUPPORTED_SPHERE_TYPE.businessException();
        }
    }
}
