package it.getinsight.module.level.client;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import feign.Feign;
import feign.Request;
import feign.Retryer;
import feign.jackson.JacksonDecoder;
import feign.jackson.JacksonEncoder;
import it.getinsight.core.pagination.PageableResponseModel;
import it.getinsight.module.level.dto.ItemFilterDTO;
import it.getinsight.module.level.dto.ItemHierarchyResumedDTO;
import org.springframework.stereotype.Component;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

import static it.getinsight.message.MessageProperty.*;

@Component
public class LevelClient {

    private static final TypeReference<Map<String, Object>> MAP_TYPE = new TypeReference<>() {};

    private final ObjectMapper objectMapper;
    private final LevelClientProperties props;
    private final Map<String, GenericClient> clientCache;

    LevelClient(ObjectMapper objectMapper, LevelClientProperties props) {
        this.objectMapper = objectMapper;
        this.props = props;
        this.clientCache = buildCache();
    }


    private Map<String, GenericClient> buildCache() {
        int max = Objects.requireNonNull(props).getCache().getMaxEntries();
        if (max > 0) {
            return Collections.synchronizedMap(new LinkedHashMap<>(16, 0.75f, true) {
                @Override
                protected boolean removeEldestEntry(Map.Entry<String, GenericClient> eldest) {
                    return size() > max;
                }
            });
        }
        return new ConcurrentHashMap<>();
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
            Objects.requireNonNull(props).getTimeout().getConnectMs(),
            Objects.requireNonNull(props).getTimeout().getReadMs()
        );

        Feign.Builder builder = Feign.builder()
            .options(options)
            .decoder(new JacksonDecoder(objectMapper))
            .encoder(new JacksonEncoder(objectMapper));

        if (props.getRetry().isEnabled()) {
            builder = builder.retryer(new Retryer.Default(
                props.getRetry().getPeriodMs(),
                props.getRetry().getMaxPeriodMs(),
                props.getRetry().getMaxAttempts()
            ));
        } else {
            builder = builder.retryer(Retryer.NEVER_RETRY);
        }

        return builder.target(GenericClient.class, baseUrl);
    }


    private Map<String, Object> buildQuery(Integer pageIndex, Integer pageSize,
                                           String sortField, String sortType,
                                           ItemFilterDTO filterDTO) {
        Map<String, Object> query = new HashMap<>();
        query.put("pageIndex", pageIndex != null ? pageIndex : 0);
        query.put("pageSize", pageSize != null ? pageSize : 20);
        if (sortField != null && !sortField.isBlank()) query.put("sortField", sortField);
        if (sortType != null && !sortType.isBlank()) query.put("sortType", sortType);

        if (filterDTO != null) {
            Map<String, Object> filterMap = objectMapper.convertValue(filterDTO, MAP_TYPE);
            filterMap.entrySet().removeIf(e -> e.getValue() == null);
            query.putAll(filterMap);
        }
        return query;
    }


    public PageableResponseModel<ItemHierarchyResumedDTO> getItems(String url,
                                                                   String apiKey, Integer pageIndex, Integer pageSize,
                                                                   String sortField, String sortType,
                                                                   ItemFilterDTO filterDTO) {
        final ApiConfig cfg = ApiConfig.from(url);
        final GenericClient client = getClient(cfg.baseUrl());
        final Map<String, Object> query = buildQuery(pageIndex, pageSize, sortField, sortType, filterDTO);
        final String path = joinPath(cfg.extraPath(), "items");
        return client.getDynamicPaginated(path, apiKey, query);
    }

    public PageableResponseModel<ItemHierarchyResumedDTO> getSubItems(String url, String apiKey, String itemId,
                                                                      Integer pageIndex, Integer pageSize,
                                                                      String sortField, String sortType,
                                                                      ItemFilterDTO filterDTO) {
        if (itemId == null || itemId.isBlank()) {
            throw INVALID_URL_EMPTY_ERROR.businessException();
        }
        final ApiConfig cfg = ApiConfig.from(url);
        final GenericClient client = getClient(cfg.baseUrl());
        final Map<String, Object> query = buildQuery(pageIndex, pageSize, sortField, sortType, filterDTO);
        final String path = joinPath(cfg.extraPath(), "items", itemId, "subitems");
        return client.getDynamicPaginated(path, apiKey, query);
    }

    public ItemHierarchyResumedDTO getItemByExternalCode(String url, String apiKey, String code) {
        if (code == null || code.isBlank()) {
            throw INVALID_URL_EMPTY_ERROR.businessException();
        }
        final ApiConfig cfg = ApiConfig.from(url);
        final GenericClient client = getClient(cfg.baseUrl());
        final String path = joinPath(cfg.extraPath(), "items", code);
        return client.getDynamic(path, apiKey, Collections.emptyMap());
    }

    public Integer getCountLevel(String url, String apiKey) {
        final ApiConfig cfg = ApiConfig.from(url);
        final GenericClient client = getClient(cfg.baseUrl());
        final String path = joinPath(cfg.extraPath(), "count");
        return client.getCountDynamic(path, apiKey);
    }

    public List<String> getAllSubitemCodes(String url, String apiKey, String itemId) {
        if (itemId == null || itemId.isBlank()) {
            throw INVALID_URL_EMPTY_ERROR.businessException();
        }
        final ApiConfig cfg = ApiConfig.from(url);
        final GenericClient client = getClient(cfg.baseUrl());
        final String path = joinPath(cfg.extraPath(), "items", itemId, "subitems", "codes");
        return client.getAllSubitemCodes(path, apiKey);
    }
}
