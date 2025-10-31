package it.getinsight.module.level.client;

import feign.Headers;
import feign.Param;
import feign.QueryMap;
import feign.RequestLine;
import it.getinsight.core.pagination.PageableResponseModel;
import it.getinsight.module.level.dto.ItemHierarchyResumedDTO;

import java.util.List;
import java.util.Map;

public interface GenericClient {

    @RequestLine("GET {path}")
    @Headers("apiKey: {apiKey}")
    PageableResponseModel<ItemHierarchyResumedDTO> getDynamicPaginated(
            @Param("path") String path,
            @Param("apiKey") String apiKey,
            @QueryMap Map<String, Object> queryParams
    );

    @RequestLine("GET {path}")
    @Headers("apiKey: {apiKey}")
    ItemHierarchyResumedDTO getDynamic(
        @Param("path") String path,
        @Param("apiKey") String apiKey,
        @QueryMap Map<String, Object> queryParams
    );

    @RequestLine("GET {path}")
    @Headers("apiKey: {apiKey}")
    Integer getCountDynamic(
        @Param("path") String path,
        @Param("apiKey") String apiKey
    );

    @RequestLine("GET {path}")
    @Headers("apiKey: {apiKey}")
    List<String> getAllSubitemCodes(
        @Param("path") String path,
        @Param("apiKey") String apiKey
    );

}
