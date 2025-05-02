package it.getinsight.module.configuration.service.validador.strategy;

import it.getinsight.module.configuration.entity.AttachmentConfigurationEntity;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface AttachmentValidationStrategy {
    void validate(AttachmentConfigurationEntity config, List<MultipartFile> files);
}
