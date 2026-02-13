package it.getinsight.module.client.dto;

public record ClientSyncSummaryDTO(
    long created,
    long updated,
    long ignored,
    long errors,
    long duration
) {
}
