package it.getinsight.module.request.service;

import it.getinsight.module.request.entity.RequestEntity;
import it.getinsight.module.role.entity.RoleEntity;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import static it.getinsight.message.MessageProperty.*;

/**
 * Serviço responsável exclusivamente por validações de status e contexto.
 * Aplica SRP de forma agressiva - apenas validações de status e contexto.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class ContextValidationService {

    /**
     * Valida se o cliente está publicado e pode receber atualizações.
     */
    public void validateClientStatus(RequestEntity requestEntity) {
        if (!it.getinsight.module.client.entity.ClientStatus.PUBLISHED.equals(requestEntity.getRole().getClient().getStatus())) {
            throw CLIENT_NOT_PUBLISHED_ERROR.businessException();
        }
    }

    /**
     * Valida se a role tem um role pai válido.
     */
    public void validateRoleParent(RoleEntity roleEntity) {
        if (roleEntity.getRole() == null) {
            throw ROLE_NOT_FOUND_PARENT_ERROR.businessException();
        }
    }
}
