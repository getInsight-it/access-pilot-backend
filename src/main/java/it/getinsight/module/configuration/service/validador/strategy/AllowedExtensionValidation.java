package it.getinsight.module.configuration.service.validador.strategy;

import it.getinsight.module.configuration.entity.AttachmentConfigurationEntity;
import it.getinsight.module.configuration.enums.FileExtensionType;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Set;

import static it.getinsight.message.MessageProperty.ATTACHMENTS_EXTENSION_NOT_ALLOWED_ERROR;


@Component
public class AllowedExtensionValidation implements AttachmentValidationStrategy {

    @Override
    public void validate(AttachmentConfigurationEntity config, List<MultipartFile> files) {
        if (files == null || files.isEmpty()) return;

        Set<FileExtensionType> allowed = config.getAllowedExtensions();
        if (allowed == null || allowed.isEmpty()) return;

        for (MultipartFile file : files) {
            String ext = getExtension(file.getOriginalFilename());
            try {
                FileExtensionType extEnum = FileExtensionType.valueOf(ext.toUpperCase());
                if (!allowed.contains(extEnum)) {
                    throw ATTACHMENTS_EXTENSION_NOT_ALLOWED_ERROR.bind(ext, config.getKey()).businessException();
                }
            } catch (IllegalArgumentException e) {
                throw ATTACHMENTS_EXTENSION_NOT_ALLOWED_ERROR.bind("", file.getOriginalFilename()).businessException();
            }
        }
    }

    private String getExtension(String filename) {
        if (filename == null || !filename.contains(".")) return "";
        return filename.substring(filename.lastIndexOf('.') + 1);
    }
}
