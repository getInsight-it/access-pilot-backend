package it.getinsight.module.request.service;

import it.getinsight.module.level.client.LevelClient;
import it.getinsight.module.level.entity.LevelType;
import it.getinsight.module.level.repository.ItemRepository;
import it.getinsight.module.role.entity.RoleEntity;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.Optional;

import static it.getinsight.message.MessageProperty.*;


@Service
@RequiredArgsConstructor
@Slf4j
public class ItemValidationService {

    private final ItemRepository itemRepository;
    private final LevelClient levelClient;


    public void validateItemExistence(String codeItem, RoleEntity roleEntity) {
        Optional.ofNullable(roleEntity.getLevel()).ifPresent(level -> {
            var levelType = level.getType();

            if (StringUtils.isBlank(codeItem)) {
                log.warn("Request received without codeItem for role {}", roleEntity.getName());
                throw CODE_ITEM_NOT_FOUND_FOR_ROLE.businessException();
            }

            if (levelType == LevelType.BUILT_IN || levelType == LevelType.BUSINESS) {
                var entityFound = itemRepository.findById(Long.parseLong(codeItem))
                    .orElseThrow(ITEM_NOT_FOUND_ERROR::businessException);
                log.info("Item found for request: {}", entityFound);
            }

            if (levelType == LevelType.EXTERNAL) {
                var opItemDtoFound = Optional.of(levelClient.getItemByExternalCode(level.getExternalUrl(), level.getApiKey(), codeItem))
                    .orElseThrow(ITEM_NOT_FOUND_ERROR::businessException);
                log.info("Item found for request: {}", opItemDtoFound);
            }
        });
    }
}
