package it.getinsight.module.request.service;

import it.getinsight.module.configuration.entity.AttachmentConfigurationEntity;
import it.getinsight.module.request.entity.RequestEntity;
import it.getinsight.module.role.entity.RoleEntity;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.MultiValueMap;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class RequestValidationService {

    private final UserAccessValidationService userAccessValidationService;
    private final ItemValidationService itemValidationService;
    private final AttachmentValidationService attachmentValidationService;
    private final ContextValidationService contextValidationService;

    public void validateUserAccessToRequest(Long requestId, RequestEntity requestEntity) {
        userAccessValidationService.validateUserAccessToRequest(requestId, requestEntity);
    }

    public void validateUserPermissionToUpdateToAllowOrDenyRequest(RequestEntity requestEntity) {
        userAccessValidationService.validateUserPermissionToUpdateToAllowOrDenyRequest(requestEntity);
    }

    public void validateClientStatus(RequestEntity requestEntity) {
        contextValidationService.validateClientStatus(requestEntity);
    }

    public void validateItemExistence(String codeItem, RoleEntity roleEntity) {
        itemValidationService.validateItemExistence(codeItem, roleEntity);
    }

    public void validateAttachments(List<AttachmentConfigurationEntity> configurations, MultiValueMap<String, MultipartFile> attachments) {
        attachmentValidationService.validateAttachments(configurations, attachments);
    }

    public void validateRoleParent(RoleEntity roleEntity) {
        contextValidationService.validateRoleParent(roleEntity);
    }
}
