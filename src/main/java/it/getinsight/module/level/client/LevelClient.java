package it.getinsight.module.level.client;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import feign.Feign;
import feign.jackson.JacksonDecoder;
import feign.jackson.JacksonEncoder;
import it.getinsight.core.pagination.PageableResponseModel;
import it.getinsight.module.level.dto.ItemFilterDTO;
import it.getinsight.module.level.dto.ItemHierarchyResumedDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static it.getinsight.message.MessageProperty.*;

@Component
@RequiredArgsConstructor
public class LevelClient {

    private final ObjectMapper objectMapper;

    record ApiConfig(String baseUrl, String extraPath) {

        public static ApiConfig from(String url) {
            if (url == null || url.isBlank()) {
                throw INVALID_URL_EMPTY_ERROR.businessException();
            }

            url = url.trim();

            int protocolEnd = url.indexOf("://");
            if (protocolEnd == -1) {
                throw INVALID_URL_PROTOCOL_ERROR.businessException();
            }

            int start = protocolEnd + 3;
            int pathStart = url.indexOf("/", start);

            String baseUrl = (pathStart == -1) ? url : url.substring(0, pathStart);
            String extraPath = (pathStart == -1) ? "" : url.substring(pathStart).replaceAll("/+$", "");

            return new ApiConfig(baseUrl, extraPath);
        }
    }



    private GenericClient createGenericClient(String baseUrl) {
        return Feign.builder()
                .contract(new feign.Contract.Default())
                .decoder(new JacksonDecoder(objectMapper))
                .encoder(new JacksonEncoder(objectMapper))
                .target(GenericClient.class, baseUrl);
    }

    public PageableResponseModel<ItemHierarchyResumedDTO> getItems(String url,
                                                                   String apiKey, Integer pageIndex, Integer pageSize,
                                                                   String sortField, String sortType,
                                                                   ItemFilterDTO filterDTO) {
        GenericClient client = createGenericClient(ApiConfig.from(url).baseUrl());

        Map<String, Object> query = new HashMap<>();
        query.put("pageIndex", pageIndex);
        query.put("pageSize", pageSize);
        query.put("sortField", sortField);
        query.put("sortType", sortType);
        query.putAll(objectMapper.convertValue(filterDTO, new TypeReference<>() {}));

        return client.getDynamicPaginated(ApiConfig.from(url).extraPath().concat("/items"), apiKey, query);
    }

    public PageableResponseModel<ItemHierarchyResumedDTO> getSubItems(String url, String apiKey, String itemId,
                                                                    Integer pageIndex, Integer pageSize,
                                                                   String sortField, String sortType,
                                                                   ItemFilterDTO filterDTO) {

        GenericClient client = createGenericClient(ApiConfig.from(url).baseUrl());

        Map<String, Object> query = new HashMap<>();
        query.put("pageIndex", pageIndex);
        query.put("pageSize", pageSize);
        query.put("sortField", sortField);
        query.put("sortType", sortType);
        query.putAll(objectMapper.convertValue(filterDTO, new TypeReference<>() {}));

        String path = String.format("%s/items/%s/subitems", ApiConfig.from(url).extraPath(), itemId);
        return client.getDynamicPaginated(path, apiKey, query);
    }

    public ItemHierarchyResumedDTO getItemByExternalCode(String url,
                                                         String apiKey, String code) {
        GenericClient client = createGenericClient(ApiConfig.from(url).baseUrl());
        String path = String.format("%s/items/%s", ApiConfig.from(url).extraPath(), code);
        return client.getDynamic(path, apiKey, Collections.emptyMap());
    }

    public Integer getCountLevel(String url, String apiKey) {
        GenericClient client = createGenericClient(ApiConfig.from(url).baseUrl());
        String path = String.format("%s/count", ApiConfig.from(url).extraPath());
        return client.getCountDynamic(path, apiKey);
    }

    public List<String> getAllSubitemCodes(String url, String apiKey, String itemId) {
        GenericClient client = createGenericClient(ApiConfig.from(url).baseUrl());
        String path = String.format("%s/items/%s/subitems/codes", ApiConfig.from(url).extraPath(), itemId);
        return client.getAllSubitemCodes(path, apiKey);
    }

}
