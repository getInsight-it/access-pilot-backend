package it.getinsight.module.domain.client;

import it.getinsight.core.pagination.PageableResponseModel;
import it.getinsight.module.domain.dto.ItemDTO;
import it.getinsight.module.domain.dto.ItemFilterDTO;
import org.springframework.cloud.openfeign.SpringQueryMap;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;


public interface DomainClient {

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

