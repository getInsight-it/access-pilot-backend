package it.getinsight.module.configuration.service;

import it.getinsight.core.dynamicquery.parameters.DynamicParameters;
import it.getinsight.core.exception.ResourceNotFoundException;
import it.getinsight.core.helper.PaginationHelper;
import it.getinsight.core.pagination.PageableRequestModel;
import it.getinsight.core.pagination.PageableResponseModel;
import it.getinsight.module.configuration.dto.ConfigurationDTO;
import it.getinsight.module.configuration.mapper.ConfigurationMapper;
import it.getinsight.module.configuration.repository.ConfigurationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
@RequiredArgsConstructor
public class ConfigurationService {

    private final ConfigurationRepository configurationRepository;
    private final ConfigurationMapper configurationMapper;

    private static final String NAME_QUERY_FIND_ALL_CONFIGURATIONS = "find-all-configurations";

    public ConfigurationDTO create(ConfigurationDTO configurationDTO) {
        var configurationEntity = configurationMapper.toEntity(configurationDTO);
        configurationEntity = configurationRepository.save(configurationEntity);
        return configurationMapper.toDto(configurationEntity);
    }

    public void update(Long id, ConfigurationDTO configurationDTO) {
        var configurationEntity = configurationRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Configuração não encontrada"));
        configurationMapper.fromDto(configurationDTO, configurationEntity);
        configurationRepository.save(configurationEntity);
    }

    public void delete(Long id) {
        var configurationEntity = configurationRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Configuração não encontrada"));
        configurationRepository.delete(configurationEntity);
    }

    public ConfigurationDTO findById(Long id) {
        var configurationEntity = configurationRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Configuração não encontrada"));
        return configurationMapper.toDto(configurationEntity);
    }


    public PageableResponseModel<ConfigurationDTO> findAll(PageableRequestModel<String> configPage) {
        final var page = configurationRepository.findAll(PaginationHelper.toPageable(configPage));
        return PaginationHelper.toPageResponse(configurationMapper.toDto(page.getContent()), page.getTotalElements());
    }


    public List<ConfigurationDTO> getAllConfigurationsDynamicQuery() {
        final var parameters = DynamicParameters.get();
        return configurationRepository.findAllNative(NAME_QUERY_FIND_ALL_CONFIGURATIONS, parameters, configurationMapper);
    }


}
