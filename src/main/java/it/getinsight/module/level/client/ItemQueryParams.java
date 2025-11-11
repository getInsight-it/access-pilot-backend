package it.getinsight.module.level.client;

import it.getinsight.module.level.dto.ItemFilterDTO;

public record ItemQueryParams(
    Integer pageIndex,
    Integer pageSize,
    String sortField,
    String sortType,
    ItemFilterDTO filterDTO
) {
    public static ItemQueryParams of(Integer pageIndex, Integer pageSize, 
                                   String sortField, String sortType,
                                   ItemFilterDTO filterDTO) {
        return new ItemQueryParams(pageIndex, pageSize, sortField, sortType, filterDTO);
    }
}
