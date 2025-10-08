package it.getinsight.module.request.service;

import it.getinsight.module.configuration.entity.AttachmentConfigurationEntity;
import it.getinsight.module.configuration.service.AttachmentConfigurationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.MultiValueMap;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * Serviço responsável exclusivamente por validações de anexos.
 * Aplica SRP de forma agressiva - apenas validações relacionadas a anexos.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class AttachmentValidationService {

    private final AttachmentConfigurationService attachmentConfigurationService;

    /**
     * Valida as configurações de anexos.
     */
    public void validateAttachments(List<AttachmentConfigurationEntity> configurations, MultiValueMap<String, MultipartFile> attachments) {
        attachmentConfigurationService.validate(configurations, attachments);
    }
}
