package it.getinsight.module.summary;

import lombok.Builder;

@Builder
public record SummaryDTO(
    Long totalApprovedRequests,
    Long totalPendingRequests,
    Long totalApprovedUsers,
    Long totalPendingUsers,
    Long totalClients,
    Long totalRoles
) {
}
