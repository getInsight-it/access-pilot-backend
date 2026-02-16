package it.getinsight.module.client.dto;

import lombok.Builder;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

@Builder
public record ClientImportSummaryDTO(
    long created,
    long updated,
    long ignored,
    long errors,
    long duration,
    List<ClientImportResultDTO> results
) implements Serializable {
    @Serial
    private static final long serialVersionUID = 5473763927433843060L;
}
