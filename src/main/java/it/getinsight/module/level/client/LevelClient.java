package it.getinsight.module.level.client;

import it.getinsight.core.pagination.PageableResponseModel;
import it.getinsight.module.level.dto.ItemDTO;
import it.getinsight.module.level.dto.ItemFilterDTO;
import org.springframework.cloud.openfeign.SpringQueryMap;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;


public interface LevelClient {

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    PageableResponseModel<ItemDTO> getItems(
        @RequestHeader(name = "apiKey", required = false) String apiKey,
        @RequestParam(defaultValue = "1") Integer pageIndex,
        @RequestParam(defaultValue = "10") Integer pageSize,
        @RequestParam(defaultValue = "id") String sortField,
        @RequestParam(defaultValue = "ASC") String sortType,
        @SpringQueryMap ItemFilterDTO filterDTO
    );

    @GetMapping(value = "/{itemExternalCode}", produces = MediaType.APPLICATION_JSON_VALUE)
    Optional<ItemDTO> getItemByExternalCode(@RequestHeader(name = "apiKey", required = false) String apiKey, @PathVariable String itemExternalCode);
}

