package it.getinsight.module.level.client;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Lists;
import feign.Feign;
import feign.Request;
import feign.Retryer;
import feign.jackson.JacksonDecoder;
import feign.jackson.JacksonEncoder;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import it.getinsight.core.pagination.PageableResponseModel;
import it.getinsight.module.level.dto.ItemFilterDTO;
import it.getinsight.module.level.dto.ItemHierarchyResumedDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

import static it.getinsight.message.MessageProperty.INVALID_URL_EMPTY_ERROR;
import static it.getinsight.message.MessageProperty.INVALID_URL_PROTOCOL_ERROR;

@Component
@Slf4j
public class LevelClient {

    private static final TypeReference<Map<String, Object>> MAP_TYPE = new TypeReference<>() {};
    public static final String ITEMS_ENDPOINT = "items";

    private static final int CONNECT_TIMEOUT_MS = 10_000;
    private static final int READ_TIMEOUT_MS = 30_000;

    private final ObjectMapper objectMapper;
    private final Map<String, GenericClient> clientCache = new ConcurrentHashMap<>();

    LevelClient(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Retry(name = "levelClient")
    @CircuitBreaker(name = "levelClient", fallbackMethod = "getItemsHierarchyFallback")
    public List<ItemHierarchyResumedDTO> getItemsHierarchy(String externalUrl, String apiKey, String id) {
        final ApiConfig cfg = ApiConfig.from(externalUrl);
        final GenericClient client = getClient(cfg.baseUrl());
        final String path = joinPath(cfg.extraPath(), ITEMS_ENDPOINT, id, "hierarchies");
        return client.getDynamicAsList(path, apiKey, Collections.emptyMap());
    }

    record ApiConfig(String baseUrl, String extraPath) {
        static ApiConfig from(String rawUrl) {
            if (rawUrl == null || rawUrl.isBlank()) {
                throw INVALID_URL_EMPTY_ERROR.businessException();
            }
            try {
                URI uri = new URI(rawUrl.trim());
                if (uri.getScheme() == null || uri.getHost() == null) {
                    throw INVALID_URL_PROTOCOL_ERROR.businessException();
                }
                StringBuilder base = new StringBuilder()
                    .append(uri.getScheme()).append("://").append(uri.getHost());
                if (uri.getPort() != -1) base.append(":").append(uri.getPort());
                return new ApiConfig(base.toString(), normalizePath(uri.getPath()));
            } catch (URISyntaxException e) {
                throw INVALID_URL_PROTOCOL_ERROR.businessException();
            }
        }
    }

    private static String normalizePath(String path) {
        if (path == null || path.isBlank() || "/".equals(path)) return "";
        String p = path.replaceAll("//+", "/");
        if (p.endsWith("/")) p = p.substring(0, p.length() - 1);
        return p;
    }

    private static String joinPath(String... parts) {
        String joined = String.join("/", Arrays.stream(parts)
            .filter(Objects::nonNull)
            .map(s -> s.replaceAll("^/+", "").replaceAll("/+$", ""))
            .filter(s -> !s.isBlank())
            .toArray(String[]::new));
        return joined.isBlank() ? "" : "/" + joined;
    }


    private GenericClient getClient(String baseUrl) {
        return clientCache.computeIfAbsent(baseUrl, this::createGenericClient);
    }

    private GenericClient createGenericClient(String baseUrl) {
        Request.Options options = new Request.Options(
            CONNECT_TIMEOUT_MS,
            TimeUnit.MILLISECONDS,
            READ_TIMEOUT_MS,
            TimeUnit.MILLISECONDS,
            true
        );

        Feign.Builder builder = Feign.builder()
            .options(options)
            .decoder(new JacksonDecoder(objectMapper))
            .encoder(new JacksonEncoder(objectMapper))
            .retryer(Retryer.NEVER_RETRY);

        return builder.target(GenericClient.class, baseUrl);
    }


    private Map<String, Object> buildQuery(ItemQueryParams queryParams) {
        Map<String, Object> query = new HashMap<>();
        query.put("pageIndex", queryParams.pageIndex() != null ? queryParams.pageIndex() : 0);
        query.put("pageSize", queryParams.pageSize() != null ? queryParams.pageSize() : 20);
        if (queryParams.sortField() != null && !queryParams.sortField().isBlank()) query.put("sortField", queryParams.sortField());
        if (queryParams.sortType() != null && !queryParams.sortType().isBlank()) query.put("sortType", queryParams.sortType());

        if (queryParams.filterDTO() != null) {
            Map<String, Object> filterMap = objectMapper.convertValue(queryParams.filterDTO(), MAP_TYPE);
            filterMap.entrySet().removeIf(e -> e.getValue() == null);
            query.putAll(filterMap);
        }
        return query;
    }


    public PageableResponseModel<ItemHierarchyResumedDTO> getItems(String url,
                                                                   String apiKey, Integer pageIndex, Integer pageSize,
                                                                   String sortField, String sortType,
                                                                   ItemFilterDTO filterDTO) {
        return getItems(url, apiKey, ItemQueryParams.of(pageIndex, pageSize, sortField, sortType, filterDTO));
    }

    @Retry(name = "levelClient")
    @CircuitBreaker(name = "levelClient", fallbackMethod = "getItemsFallback")
    public PageableResponseModel<ItemHierarchyResumedDTO> getItems(String url, String apiKey, ItemQueryParams queryParams) {
        final ApiConfig cfg = ApiConfig.from(url);
        final GenericClient client = getClient(cfg.baseUrl());
        final Map<String, Object> query = buildQuery(queryParams);
        final String path = joinPath(cfg.extraPath(), ITEMS_ENDPOINT);
        return client.getDynamicPaginated(path, apiKey, query);
    }


    @Retry(name = "levelClient")
    @CircuitBreaker(name = "levelClient", fallbackMethod = "getSubItemsFallback")
    public PageableResponseModel<ItemHierarchyResumedDTO> getSubItems(String url, String apiKey, String itemId,
                                                                    ItemQueryParams queryParams) {
        if (itemId == null || itemId.isBlank()) {
            throw INVALID_URL_EMPTY_ERROR.businessException();
        }
        final ApiConfig cfg = ApiConfig.from(url);
        final GenericClient client = getClient(cfg.baseUrl());
        final Map<String, Object> query = buildQuery(queryParams);
        final String path = joinPath(cfg.extraPath(), ITEMS_ENDPOINT, itemId, "subitems");
        return client.getDynamicPaginated(path, apiKey, query);
    }

    @Retry(name = "levelClient")
    @CircuitBreaker(name = "levelClient", fallbackMethod = "getItemByExternalCodeFallback")
    public ItemHierarchyResumedDTO getItemByExternalCode(String url, String apiKey, String code) {
        if (code == null || code.isBlank()) {
            throw INVALID_URL_EMPTY_ERROR.businessException();
        }
        final ApiConfig cfg = ApiConfig.from(url);
        final GenericClient client = getClient(cfg.baseUrl());
        final String path = joinPath(cfg.extraPath(), ITEMS_ENDPOINT, code);
        return client.getDynamic(path, apiKey, Collections.emptyMap());
    }

    @Retry(name = "levelClient")
    @CircuitBreaker(name = "levelClient", fallbackMethod = "getCountLevelFallback")
    public Integer getCountLevel(String url, String apiKey) {
        final ApiConfig cfg = ApiConfig.from(url);
        final GenericClient client = getClient(cfg.baseUrl());
        final String path = joinPath(cfg.extraPath(), "count");
        return client.getCountDynamic(path, apiKey);
    }

    @Retry(name = "levelClient")
    @CircuitBreaker(name = "levelClient", fallbackMethod = "getAllSubitemCodesFallback")
    public List<String> getAllSubitemCodes(String url, String apiKey, String itemId) {
        if (itemId == null || itemId.isBlank()) {
            throw INVALID_URL_EMPTY_ERROR.businessException();
        }
        final ApiConfig cfg = ApiConfig.from(url);
        final GenericClient client = getClient(cfg.baseUrl());
        final String path = joinPath(cfg.extraPath(), ITEMS_ENDPOINT, itemId, "siblings", "codes");
        return client.getAllSubitemCodes(path, apiKey);
    }

    @SuppressWarnings("unused")
    private List<ItemHierarchyResumedDTO> getItemsHierarchyFallback(String externalUrl, String apiKey, String id, Throwable throwable) {
        log.error("Fallback: error fetching items hierarchy for id {}: {}", id, throwable.toString());
        return Collections.emptyList();
    }

    @SuppressWarnings("unused")
    private PageableResponseModel<ItemHierarchyResumedDTO> getItemsFallback(String url, String apiKey, ItemQueryParams queryParams, Throwable throwable) {
        log.error("Fallback: error fetching items for level {}: {}", url, throwable.toString());
        return PageableResponseModel.of(0L, Lists.newArrayList());
    }

    @SuppressWarnings("unused")
    private PageableResponseModel<ItemHierarchyResumedDTO> getSubItemsFallback(String url, String apiKey, String itemId, ItemQueryParams queryParams, Throwable throwable) {
        log.error("Fallback: error fetching subitems for item {}: {}", itemId, throwable.toString());
        return PageableResponseModel.of(0L, Lists.newArrayList());
    }

    @SuppressWarnings("unused")
    private ItemHierarchyResumedDTO getItemByExternalCodeFallback(String url, String apiKey, String code, Throwable throwable) {
        log.error("Fallback: error fetching item by external code {}: {}", code, throwable.toString());
        return null;
    }

    @SuppressWarnings("unused")
    private Integer getCountLevelFallback(String url, String apiKey, Throwable throwable) {
        log.error("Fallback: error fetching count for level {}: {}", url, throwable.toString());
        return 0;
    }

    @SuppressWarnings("unused")
    private List<String> getAllSubitemCodesFallback(String url, String apiKey, String itemId, Throwable throwable) {
        log.error("Fallback: error fetching subitem codes for item {}: {}", itemId, throwable.toString());
        return Collections.emptyList();
    }
}
