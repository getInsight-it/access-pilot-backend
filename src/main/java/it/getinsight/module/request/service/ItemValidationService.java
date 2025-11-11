package it.getinsight.module.request.service;

import it.getinsight.module.level.service.ItemResolverService;
import it.getinsight.module.role.entity.RoleEntity;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.Optional;

import static it.getinsight.message.MessageProperty.CODE_ITEM_NOT_FOUND_FOR_ROLE;
import static it.getinsight.message.MessageProperty.ITEM_NOT_FOUND_ERROR;

@Service
@RequiredArgsConstructor
@Slf4j
public class ItemValidationService {

    private final ItemResolverService itemResolverService;

    public void validateItemExistence(String codeItem, RoleEntity roleEntity) {
        Optional.ofNullable(roleEntity.getLevel()).ifPresent(level -> {
            if (StringUtils.isBlank(codeItem)) {
                log.warn("Request received without codeItem for role {}", roleEntity.getName());
                throw CODE_ITEM_NOT_FOUND_FOR_ROLE.resourceNotFoundException();
            }

            try {
                String resolvedItemId = itemResolverService.resolveItemId(level, codeItem);
                log.info("Item found for request with ID: {}", resolvedItemId);
            } catch (Exception e) {
                log.warn("Item validation failed for codeItem: {} and role: {}", codeItem, roleEntity.getName(), e);
                throw ITEM_NOT_FOUND_ERROR.resourceNotFoundException();
            }
        });
    }
}
