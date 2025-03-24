package it.getinsight.module.level.dto;

import lombok.Builder;

import java.io.Serializable;
import java.util.List;


@Builder
public record ExportationFilterDTO(
    List<String> namesLevels
) implements Serializable{
}
