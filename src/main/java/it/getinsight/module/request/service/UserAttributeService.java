package it.getinsight.module.request.service;

import it.getinsight.module.keycloak.service.IdentityProviderService;
import it.getinsight.module.keycloak.dto.RoleRepresentationDTO;
import it.getinsight.module.level.client.LevelClient;
import it.getinsight.module.request.entity.RequestEntity;
import it.getinsight.module.role.entity.RoleEntity;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Map;

/**
 * Serviço responsável exclusivamente por atualização de atributos de usuário.
 * Aplica SRP de forma agressiva - apenas atualização de atributos.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class UserAttributeService {

    private final IdentityProviderService identityProviderService;
    private final LevelClient levelClient;

    /**
     * Atualiza atributos do usuário no Keycloak com informações de nível.
     */
    public void updateUserAttributes(RequestEntity entity, RoleEntity roleEntity, RoleRepresentationDTO role) {
        var user = identityProviderService.getUsers(Map.of("externalId", entity.getRequestingUser().getExternalId())).get(0);
        var item = levelClient.getItemByExternalCode(entity.getLevel().getExternalUrl(), entity.getLevel().getApiKey(), entity.getCodeItem());
        
        String levelAccess = String.join("::",
            roleEntity.getClient().getClientId(),
            role.name(),
            entity.getLevel().getName(),
            item.name());

        var levelAttributes = new ArrayList<>(user.attributes().getOrDefault("levelAttributes", Collections.emptyList()));

        if (!levelAttributes.contains(levelAccess)) {
            levelAttributes.add(levelAccess);
            log.info("Assigning level attribute '{}' to user {}", levelAccess, entity.getRequestingUser().getExternalId());
            identityProviderService.updateUser(entity.getRequestingUser().getExternalId(), user.withLevelAttributes(levelAttributes));
        }
    }
}
