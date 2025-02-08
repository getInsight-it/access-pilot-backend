package it.getinsight.module.domain.service;

import it.getinsight.module.domain.dto.DomainDTO;
import it.getinsight.module.domain.dto.ItemDTO;
import it.getinsight.module.domain.entity.DomainEntity;
import it.getinsight.module.domain.entity.DomainType;
import it.getinsight.module.domain.entity.ItemEntity;
import it.getinsight.module.domain.mapper.DomainMapper;
import it.getinsight.module.domain.mapper.ItemMapper;
import it.getinsight.module.domain.repository.DomainRepository;
import it.getinsight.module.domain.repository.ItemRepository;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
import java.util.Optional;
import java.util.UUID;

import static it.getinsight.message.MessageProperty.*;


@Service
@RequiredArgsConstructor
@Slf4j
public class ItemService {

    private final DomainRepository domainRepository;
    private final ItemRepository itemRepository;
    private final DomainMapper domainMapper;
    private final ItemMapper itemMapper;


    @Transactional(propagation = Propagation.REQUIRED)
    public void importDomains(MultipartFile file) {
        try {
            var itemsMap = new HashMap<Long, ItemEntity>();
            var domainsMap = new HashMap<Long, DomainEntity>();

            List<ItemEntity> items = new BufferedReader(new InputStreamReader(file.getInputStream(), StandardCharsets.UTF_8))
                .lines()
                .skip(1)
                .map(line -> {
                    String[] values = line.split(",");
                    Long domainId = Long.parseLong(values[0]);
                    Long id = Long.parseLong(values[1]);
                    Long parentId = values[2].isEmpty() ? null : Long.parseLong(values[2]);
                    var parent = parentId != null ? itemsMap.get(parentId) : null;
                    var domain = domainsMap.getOrDefault(domainId, domainRepository.findById(domainId).orElseThrow(DOMAIN_NOT_FOUND_ERROR::businessException));


                    var item = ItemEntity.builder()
                        .parent(parent)
                        .domain(domain)
                        .name(values[2])
                        .description(values[4])
                        .externalCode(values.length > 5 ? values[5] : null)
                        .build();

                    itemsMap.put(id, item);
                    domainsMap.put(domainId, domain);
                    return item;
                }).toList();
            itemRepository.saveAll(items);
        } catch (Exception e) {
            log.error("Erro ao importar CSV", e);
            throw ERROR_IMPORT_CSV.businessException();
        }
    }

    public DomainDTO updateStatus(Long id, String status) {
        // Implementação
        return null;
    }
}

