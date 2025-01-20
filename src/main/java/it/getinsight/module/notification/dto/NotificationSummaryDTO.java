package it.getinsight.module.notification.dto;


import lombok.Builder;

@Builder
public record NotificationSummaryDTO(
    Long totalRead,
    Long totalUnread,
    Long total
){}
