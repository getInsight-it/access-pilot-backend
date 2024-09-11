package it.getinsight.module.summary;

import lombok.Builder;

@Builder
public record SummaryDTO(
    Long totalActiveUsers,
    Long totalPendingUsers,
    Long totalRegisteredUsers,
    Long totalClients,
    Long totalRoles,
    Long totalInactiveUsers
) {
}
