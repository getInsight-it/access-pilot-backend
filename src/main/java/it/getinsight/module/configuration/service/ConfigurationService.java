package it.getinsight.module.configuration.service;

import it.getinsight.core.helper.PaginationHelper;
import it.getinsight.core.pagination.PageableRequestModel;
import it.getinsight.core.pagination.PageableResponseModel;
import it.getinsight.module.configuration.dto.ConfigurationDTO;
import it.getinsight.module.configuration.mapper.ConfigurationMapper;
import it.getinsight.module.configuration.repository.ConfigurationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import static it.getinsight.message.MessageProperty.ERROR_CONFIGURATION_NOT_FOUND;


@Service
@RequiredArgsConstructor
public class ConfigurationService {

    private final ConfigurationRepository configurationRepository;
    private final ConfigurationMapper configurationMapper;

    @Transactional(propagation = Propagation.REQUIRED)
    public ConfigurationDTO create(ConfigurationDTO configurationDTO) {
        var configurationEntity = configurationMapper.toEntity(configurationDTO);
        configurationEntity = configurationRepository.save(configurationEntity);
        return configurationMapper.toDto(configurationEntity);
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public void delete(Long id) {
        var configurationEntity = configurationRepository.findById(id).orElseThrow(ERROR_CONFIGURATION_NOT_FOUND::businessException);
        configurationRepository.softDelete(configurationEntity.getId());
    }

    public ConfigurationDTO findById(Long id) {
        var configurationEntity = configurationRepository.findById(id).orElseThrow(ERROR_CONFIGURATION_NOT_FOUND::businessException);
        return configurationMapper.toDto(configurationEntity);
    }


    public PageableResponseModel<ConfigurationDTO> findAll(PageableRequestModel<String> configPage) {
        final var page = configurationRepository.findAll(PaginationHelper.toPageable(configPage));
        return PaginationHelper.toPageResponse(configurationMapper.toDto(page.getContent()), page.getTotalElements());
    }


}
