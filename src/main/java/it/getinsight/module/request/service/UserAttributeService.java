package it.getinsight.module.request.service;

import it.getinsight.module.keycloak.service.IdentityProviderService;
import it.getinsight.module.level.client.LevelClient;
import it.getinsight.module.level.entity.LevelEntity;
import it.getinsight.module.level.repository.ItemRepository;
import it.getinsight.module.request.entity.RequestEntity;
import it.getinsight.module.role.entity.RoleEntity;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Map;
import java.util.Optional;

import static it.getinsight.message.MessageProperty.CODE_ITEM_NOT_FOUND_FOR_ROLE;
import static it.getinsight.message.MessageProperty.ITEM_NOT_FOUND_ERROR;


@Service
@RequiredArgsConstructor
@Slf4j
public class UserAttributeService {

    private final IdentityProviderService identityProviderService;
    private final LevelClient levelClient;
    private final ItemRepository itemRepository;


    public void updateUserAttributes(RequestEntity entity, RoleEntity roleEntity) {
        var user = identityProviderService.getUsers(Map.of("externalId", entity.getRequestingUser().getExternalId())).get(0);
        var item = resolveItemCodeItem(entity.getLevel(), entity.getCodeItem());
        String levelAccess = String.join(":",
            roleEntity.getClient().getId().toString(),
            roleEntity.getId().toString(),
            entity.getLevel().getId().toString(),
            item);

        var levelAttributes = new ArrayList<>(Optional.ofNullable(user.attributes()).orElse(Collections.emptyMap()).getOrDefault("levelAttributes", Collections.emptyList()));

        if (!levelAttributes.contains(levelAccess)) {
            levelAttributes.add(levelAccess);
            log.info("Assigning level attribute '{}' to user {}", levelAccess, entity.getRequestingUser().getExternalId());
            identityProviderService.updateUser(entity.getRequestingUser().getExternalId(), user.withLevelAttributes(levelAttributes));
        }
    }

    public void removeUserAttributes(RequestEntity entity, RoleEntity roleEntity) {
        var user = identityProviderService.getUsers(Map.of("externalId", entity.getRequestingUser().getExternalId())).get(0);
        var item = resolveItemCodeItem(entity.getLevel(), entity.getCodeItem());
        String levelAccess = String.join(":",
            roleEntity.getClient().getId().toString(),
            roleEntity.getId().toString(),
            entity.getLevel().getId().toString(),
            item);

        var levelAttributes = new ArrayList<>(Optional.ofNullable(user.attributes()).orElse(Collections.emptyMap()).getOrDefault("levelAttributes", Collections.emptyList()));

        if (levelAttributes.contains(levelAccess)) {
            levelAttributes.remove(levelAccess);
            log.info("Revoking level attribute '{}' from user {}", levelAccess, entity.getRequestingUser().getExternalId());
            identityProviderService.updateUser(entity.getRequestingUser().getExternalId(), user.withLevelAttributes(levelAttributes));
        } else {
            log.debug("Level attribute '{}' not found for user {}, nothing to revoke", levelAccess, entity.getRequestingUser().getExternalId());
        }
    }

    private String resolveItemCodeItem(LevelEntity level, String codeItem) {
        if (codeItem == null || codeItem.isBlank()) {
            throw CODE_ITEM_NOT_FOUND_FOR_ROLE.resourceNotFoundException();
        }
        return switch (level.getType()) {
            case EXTERNAL -> {
                var dto = levelClient.getItemByExternalCode(
                    level.getExternalUrl(),
                    level.getApiKey(),
                    codeItem
                );
                yield dto.externalCode();
            }
            case BUILT_IN, BUSINESS -> {
                var local = itemRepository.findByLevelIdAndId(level.getId(), Long.parseLong(codeItem))
                    .orElseThrow(ITEM_NOT_FOUND_ERROR::resourceNotFoundException);
                yield local.getId().toString();
            }
        };
    }

}
