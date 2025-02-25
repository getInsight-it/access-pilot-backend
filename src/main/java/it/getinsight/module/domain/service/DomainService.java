package it.getinsight.module.domain.service;

import it.getinsight.module.domain.client.FeignClientFactory;
import it.getinsight.core.helper.PaginationHelper;
import it.getinsight.core.pagination.PageableRequestModel;
import it.getinsight.core.pagination.PageableResponseModel;
import it.getinsight.module.domain.dto.DomainDTO;
import it.getinsight.module.domain.dto.DomainFilterDTO;
import it.getinsight.module.domain.dto.ItemDTO;
import it.getinsight.module.domain.dto.ItemFilterDTO;
import it.getinsight.module.domain.entity.DomainEntity;
import it.getinsight.module.domain.entity.DomainType;
import it.getinsight.module.domain.entity.ItemEntity;
import it.getinsight.module.domain.mapper.DomainFilterMapper;
import it.getinsight.module.domain.mapper.DomainMapper;
import it.getinsight.module.domain.mapper.ItemFilterMapper;
import it.getinsight.module.domain.mapper.ItemMapper;
import it.getinsight.module.domain.repository.DomainRepository;
import it.getinsight.module.domain.repository.ItemRepository;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.text.MessageFormat;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import static it.getinsight.message.MessageProperty.*;


@Service
@RequiredArgsConstructor
@Slf4j
public class DomainService {

    private final DomainRepository domainRepository;
    private final ItemRepository itemRepository;
    private final DomainMapper domainMapper;
    private final ItemMapper itemMapper;
    private final ItemFilterMapper itemFilterMapper;
    private final DomainFilterMapper domainFilterMapper;
    private final FeignClientFactory feignClientFactory;

    public PageableResponseModel<DomainDTO> getAllPaginatedDomains(PageableRequestModel<DomainFilterDTO> configPage) {
        final var filter = configPage.getFilter();
        final var model = filter
            .map(domainFilterMapper::toDto)
            .map(domainMapper::toEntity)
            .orElse(new DomainEntity());

        final var matcher = ExampleMatcher
            .matchingAll()
            .withIgnoreNullValues()
            .withMatcher("name", ExampleMatcher.GenericPropertyMatcher::contains)
            .withMatcher("description", ExampleMatcher.GenericPropertyMatcher::contains)
            .withMatcher("externalUrl", ExampleMatcher.GenericPropertyMatcher::contains);

        final var example = Example.of(model, matcher);

        final var page = domainRepository.findAll(example, PaginationHelper.toPageable(configPage));
        return PaginationHelper.toPageResponse(domainMapper.toDto(page.getContent()), page.getTotalElements());
    }

    public DomainDTO findById(Long id) {
        return domainRepository.findById(id).map(domainMapper::toDto).orElse(null);
    }


    public PageableResponseModel<ItemDTO> getItemsPaginatedByDomain(Long domainId, PageableRequestModel<ItemFilterDTO> configPage) {
        var domainEntity = domainRepository.findById(domainId).orElseThrow(DOMAIN_NOT_FOUND_ERROR::businessException);

        if (DomainType.EXTERNAL.equals(domainEntity.getType())) {
            var client = feignClientFactory.createClient(domainEntity.getExternalUrl());
            return client.getItems(  domainEntity.getApiKey(), configPage.getPageNumber() + 1, configPage.getPageSize(), configPage.getSortField(), configPage.getSortType(), configPage.getFilter().orElse(null));
        }

        var filter = configPage.getFilter();
        var model = filter
            .map(itemFilterMapper::toDto)
            .map(itemMapper::toEntity)
            .orElse(new ItemEntity());

        final var matcher = ExampleMatcher
            .matchingAll()
            .withIgnoreNullValues()
            .withMatcher("name", ExampleMatcher.GenericPropertyMatcher::contains)
            .withMatcher("description", ExampleMatcher.GenericPropertyMatcher::contains)
            .withMatcher("externalCode", ExampleMatcher.GenericPropertyMatcher::contains)
            .withMatcher("domain.id", ExampleMatcher.GenericPropertyMatcher::exact);
        model.setDomain(DomainEntity.builder().id(domainId).build());

        final var example = Example.of(model, matcher);


        final var page = itemRepository.findAll(example, PaginationHelper.toPageable(configPage));
        return PaginationHelper.toPageResponse(itemMapper.toDto(page.getContent()), page.getTotalElements());
    }

    public PageableResponseModel<ItemDTO> getSubItemsPaginatedByDomain(Long domainId,Long itemId, PageableRequestModel<ItemFilterDTO> configPage) {
        var filter = configPage.getFilter();
        var model = filter
            .map(itemFilterMapper::toDto)
            .map(itemMapper::toEntity)
            .orElse(new ItemEntity());

        final var matcher = ExampleMatcher
            .matchingAll()
            .withIgnoreNullValues()
            .withMatcher("name", ExampleMatcher.GenericPropertyMatcher::contains)
            .withMatcher("description", ExampleMatcher.GenericPropertyMatcher::contains)
            .withMatcher("externalCode", ExampleMatcher.GenericPropertyMatcher::contains)
            .withMatcher("domain.id", ExampleMatcher.GenericPropertyMatcher::exact)
            .withMatcher("parent.id", ExampleMatcher.GenericPropertyMatcher::exact);

        model.setParent(ItemEntity.builder().id(domainId).build());
        model.setDomain(DomainEntity.builder().id(itemId).build());

        final var example = Example.of(model, matcher);
        final var page = itemRepository.findAll(example, PaginationHelper.toPageable(configPage));
        return PaginationHelper.toPageResponse(itemMapper.toDto(page.getContent()), page.getTotalElements());
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public DomainDTO create(DomainDTO domainDTO) {
        if (DomainType.BUILT_IN.equals(domainDTO.type())) {
            throw CREATE_BUILT_IN_DOMAIN.businessException();
        }
        var entity = domainMapper.toEntity(domainDTO);
        return domainMapper.toDto(domainRepository.save(entity));
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public void importDomains(MultipartFile file) {
        try {
            var domainsMap = new HashMap<Long, DomainEntity>();

            List<DomainEntity> domains = new BufferedReader(new InputStreamReader(file.getInputStream(), StandardCharsets.UTF_8))
                .lines()
                .skip(1)
                .map(line -> {
                    String[] values = line.split(",");
                    Long id = Long.parseLong(values[0]);
                    UUID uuid = UUID.fromString(values[1]);
                    Long parentId = values[2].isEmpty() ? null : Long.parseLong(values[2]);
                    var parent = parentId != null ? domainsMap.get(parentId) : null;

                    var domain = DomainEntity.builder()
                        .uuid(uuid)
                        .parent(parent)
                        .sigla(values[3])
                        .name(values[4])
                        .description(values[5])
                        .type(DomainType.valueOf(values[6]))
                        .apiKey(values[7])
                        .build();

                    domainsMap.put(id, domain);
                    return domain;
                }).toList();
            domainRepository.saveAll(domains);
        } catch (Exception e) {
            log.error("Erro ao importar CSV", e);
            throw ERROR_IMPORT_CSV.businessException();
        }
    }

    public void exportDomains(HttpServletResponse response) {
        try {
            List<DomainEntity> domains = domainRepository.findAll();
            OutputStreamWriter writer = new OutputStreamWriter(response.getOutputStream(), StandardCharsets.UTF_8);
            writer.write("id,uuid,parentId,sigla,name,description,type,apiKey\n");
            for (DomainEntity domain : domains) {
                writer.write(MessageFormat.format("{0},{1},{2},{3},{4},{5},{6},{7}\n",
                    domain.getId(),
                    domain.getUuid(),
                    domain.getParent() != null ? domain.getParent().getId() : "",
                    domain.getSigla(),
                    domain.getName(),
                    domain.getDescription(),
                    domain.getType().name(),
                    domain.getApiKey()));
            }
            writer.flush();
        } catch (Exception e) {
            log.error("Erro ao exportar CSV", e);
            throw ERROR_EXPORT_CSV.businessException();
        }
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public void update(Long id, DomainDTO domainDTO) {
        if (DomainType.BUILT_IN.equals(domainDTO.type())) {
            throw UPDATE_BUILT_IN_DOMAIN.businessException();
        }
        if (domainRepository.existsById(id)) {
            var domainEntity = domainRepository.findById(id).orElseThrow(DOMAIN_NOT_FOUND_ERROR::businessException);
            domainMapper.fromDto(domainDTO, domainEntity);
            domainEntity.setId(id);
            domainRepository.save(domainEntity);
        }
    }


    @Transactional(propagation = Propagation.REQUIRED)
    public ItemDTO createItem(Long id, ItemDTO itemDTO) {
        var domain = domainRepository.findById(id).orElseThrow(DOMAIN_NOT_FOUND_ERROR::businessException);
        if (DomainType.BUILT_IN.equals(domain.getType())) {
            throw CREATE_BUILT_IN_ITEM.businessException();
        }
        var entity = itemMapper.toEntity(itemDTO);
        entity.setDomain(domain);
        return itemMapper.toDto(itemRepository.save(entity));
    }

    public ItemDTO getItemById(Long id, String itemId) {
        var domainEntity = domainRepository.findById(id).orElseThrow(DOMAIN_NOT_FOUND_ERROR::businessException);
        if (DomainType.EXTERNAL.equals(domainEntity.getType())) {
            feignClientFactory.createClient(domainEntity.getExternalUrl()).getItemByExternalCode(domainEntity.getApiKey(), itemId);
        }
        var entity = itemRepository.findByDomainIdAndId(id, Long.parseLong(itemId)).orElseThrow(ITEM_NOT_FOUND_ERROR::businessException);
        return itemMapper.toDto(entity);
    }
}

