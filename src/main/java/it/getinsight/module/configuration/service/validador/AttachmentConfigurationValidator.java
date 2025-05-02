package it.getinsight.module.configuration.service.validador;

import it.getinsight.module.configuration.entity.AttachmentConfigurationEntity;
import it.getinsight.module.configuration.service.validador.strategy.AttachmentValidationStrategy;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.MultiValueMap;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AttachmentConfigurationValidator {

    private final List<AttachmentValidationStrategy> strategies;

    public void validate(List<AttachmentConfigurationEntity> configurations, MultiValueMap<String, MultipartFile> attachments) {
        for (AttachmentConfigurationEntity config : configurations) {
            final var files = attachments.get(config.getKey());
            for (AttachmentValidationStrategy strategy : strategies) {
                strategy.validate(config, files);
            }
        }
    }
}
