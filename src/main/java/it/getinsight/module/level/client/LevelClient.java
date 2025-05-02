package it.getinsight.module.level.client;

import it.getinsight.core.pagination.PageableResponseModel;
import it.getinsight.module.level.dto.ItemFilterDTO;
import it.getinsight.module.level.dto.ItemHierarchyResumedDTO;
import org.springframework.cloud.openfeign.SpringQueryMap;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Optional;


public interface LevelClient {

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    PageableResponseModel<ItemHierarchyResumedDTO> getItems(@RequestHeader(name = "apiKey", required = false) String apiKey,
                                                            @RequestParam(defaultValue = "1") Integer pageIndex,
                                                            @RequestParam(defaultValue = "10") Integer pageSize,
                                                            @RequestParam(defaultValue = "id") String sortField,
                                                            @RequestParam(defaultValue = "ASC") String sortType,
                                                            @SpringQueryMap ItemFilterDTO filterDTO
    );

    @GetMapping(value = "/{itemExternalCode}", produces = MediaType.APPLICATION_JSON_VALUE)
    Optional<ItemHierarchyResumedDTO> getItemByExternalCode(@RequestHeader(name = "apiKey", required = false) String apiKey, @PathVariable String itemExternalCode);


    @GetMapping(value = "/count", produces = MediaType.APPLICATION_JSON_VALUE)
    Integer getCount(@RequestHeader(name = "apiKey", required = false) String apiKey);

}

