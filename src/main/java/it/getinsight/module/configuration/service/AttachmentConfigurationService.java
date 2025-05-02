package it.getinsight.module.configuration.service;

import it.getinsight.module.client.entity.ClientEntity;
import it.getinsight.module.configuration.dto.AttachmentConfigurationDTO;
import it.getinsight.module.configuration.entity.AttachmentConfigurationEntity;
import it.getinsight.module.configuration.mapper.AttachmentConfigurationMapper;
import it.getinsight.module.configuration.repository.AttachmentConfigurationRepository;
import it.getinsight.module.configuration.service.validador.AttachmentConfigurationValidator;
import lombok.RequiredArgsConstructor;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.MultiValueMap;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

import static it.getinsight.message.MessageProperty.ERROR_CONFIGURATION_NOT_FOUND;
import static it.getinsight.message.MessageProperty.ERROR_EXPORT_CSV;


@Service
@RequiredArgsConstructor
public class AttachmentConfigurationService {

    private final AttachmentConfigurationRepository attachmentConfigurationRepository;
    private final AttachmentConfigurationMapper attachmentConfigurationMapper;
    private final AttachmentConfigurationCsvParser attachmentConfigurationCsvParser;
    private final AttachmentConfigurationValidator attachmentConfigurationValidator;

    @Transactional(propagation = Propagation.REQUIRED)
    public void delete(Long id) {
        var configurationEntity = attachmentConfigurationRepository.findById(id).orElseThrow(ERROR_CONFIGURATION_NOT_FOUND::businessException);
        attachmentConfigurationRepository.softDelete(configurationEntity.getId());
    }

    public AttachmentConfigurationDTO findById(Long id) {
        var configurationEntity = attachmentConfigurationRepository.findById(id).orElseThrow(ERROR_CONFIGURATION_NOT_FOUND::businessException);
        return attachmentConfigurationMapper.toDto(configurationEntity);
    }


    public void importAttachmentConfiguration(ClientEntity client, MultipartFile file) {
        var configs = attachmentConfigurationCsvParser.parse(client, file);
        attachmentConfigurationRepository.saveAll(configs);
    }


    public byte[] toCsv(List<AttachmentConfigurationEntity> configs) {
        return Optional.of(attachmentConfigurationCsvParser.toCsv(configs)).orElseThrow(ERROR_EXPORT_CSV::businessException);
    }


    public void validate(List<AttachmentConfigurationEntity> config, MultiValueMap<String, MultipartFile> attachments) {
        if(CollectionUtils.isNotEmpty(config)) {
            attachmentConfigurationValidator.validate(config, attachments);
        }
    }

}
