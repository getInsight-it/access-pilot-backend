package it.getinsight.module.configuration.service;

import it.getinsight.core.helper.PaginationHelper;
import it.getinsight.core.pagination.PageableRequestModel;
import it.getinsight.core.pagination.PageableResponseModel;
import it.getinsight.module.configuration.dto.ConfigurationDTO;
import it.getinsight.module.configuration.dto.ConfigurationFilterDTO;
import it.getinsight.module.configuration.entity.ConfigurationEntity;
import it.getinsight.module.configuration.mapper.ConfigurationFilterMapper;
import it.getinsight.module.configuration.mapper.ConfigurationMapper;
import it.getinsight.module.configuration.repository.ConfigurationRepository;
import it.getinsight.module.level.entity.LevelEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import static it.getinsight.message.MessageProperty.ERROR_CONFIGURATION_NOT_FOUND;


@Service
@RequiredArgsConstructor
public class ConfigurationService {

    private final ConfigurationRepository configurationRepository;
    private final ConfigurationMapper configurationMapper;
    private final ConfigurationFilterMapper configurationFilterMapper;

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


    public PageableResponseModel<ConfigurationDTO> getAllConfigurations(PageableRequestModel<ConfigurationFilterDTO> configPage) {
        final var filter = configPage.getFilter();
        final var model = filter
            .map(configurationFilterMapper::toDto)
            .map(configurationMapper::toEntity)
            .orElse(new ConfigurationEntity());

        final var matcher = ExampleMatcher
            .matchingAll()
            .withIgnoreNullValues()
            .withMatcher("name", ExampleMatcher.GenericPropertyMatcher::contains)
            .withMatcher("description", ExampleMatcher.GenericPropertyMatcher::contains);

        final var example = Example.of(model, matcher);

        final var page = configurationRepository.findAll(example, PaginationHelper.toPageable(configPage));
        return PaginationHelper.toPageResponse(configurationMapper.toDto(page.getContent()), page.getTotalElements());
    }


}
