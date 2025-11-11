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
    public String resolveItemId(LevelEntity levelEntity, String codeItem) {
        switch (levelEntity.getType()) {
            case EXTERNAL -> {
                if (codeItem == null || codeItem.isBlank()) {
                    throw CODE_ITEM_NOT_FOUND_FOR_ROLE.businessException();
                }

                var dto = levelClient.getItemByExternalCode(levelEntity.getExternalUrl(), levelEntity.getApiKey(), codeItem);
                return dto.externalCode();
            }
            case BUILT_IN, BUSINESS -> {
                if (codeItem == null || codeItem.isBlank()) {
                    throw CODE_ITEM_NOT_FOUND_FOR_ROLE.businessException();
                }

                return itemRepository.findByLevelIdAndId(levelEntity.getId(), Long.parseLong(codeItem))
                    .map(item -> item.getId().toString())
                    .orElseThrow(ITEM_NOT_FOUND_ERROR::businessException);
            }
            default -> throw UNSUPPORTED_SPHERE_TYPE.businessException();
        }
    }
}
