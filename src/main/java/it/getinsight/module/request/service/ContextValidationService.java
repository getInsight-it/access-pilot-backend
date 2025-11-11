package it.getinsight.module.request.service;

import it.getinsight.module.client.entity.ClientStatus;
import it.getinsight.module.request.entity.RequestEntity;
import it.getinsight.module.role.entity.RoleEntity;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import static it.getinsight.message.MessageProperty.CLIENT_NOT_PUBLISHED_ERROR;
import static it.getinsight.message.MessageProperty.ROLE_NOT_FOUND_PARENT_ERROR;


@Service
@RequiredArgsConstructor
@Slf4j
public class ContextValidationService {


    public void validateClientStatus(RequestEntity requestEntity) {
        if (!ClientStatus.PUBLISHED.equals(requestEntity.getRole().getClient().getStatus())) {
            throw CLIENT_NOT_PUBLISHED_ERROR.businessException();
        }
    }


    public void validateRoleParent(RoleEntity roleEntity) {
        if (roleEntity.getRole() == null) {
            throw ROLE_NOT_FOUND_PARENT_ERROR.resourceNotFoundException();
        }
    }
}
