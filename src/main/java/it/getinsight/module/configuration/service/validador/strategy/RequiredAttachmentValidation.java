package it.getinsight.module.configuration.service.validador.strategy;

import it.getinsight.module.configuration.entity.AttachmentConfigurationEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

import static it.getinsight.message.MessageProperty.ATTACHMENTS_REQUIRED_ERROR;

@Component
public class RequiredAttachmentValidation implements AttachmentValidationStrategy {

    @Override
    public void validate(AttachmentConfigurationEntity config, List<MultipartFile> files) {
        boolean isRequired = Boolean.TRUE.equals(config.getRequired());
        boolean isMissing = files == null || files.isEmpty();

        if (isRequired && isMissing) {
            throw ATTACHMENTS_REQUIRED_ERROR.bind(config.getKey()).businessException();
        }
    }
}
