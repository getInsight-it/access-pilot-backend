package it.getinsight.module.summary.service;

import it.getinsight.module.client.service.ClientService;
import it.getinsight.module.keycloak.client.KeycloakClient;
import it.getinsight.module.request.enuns.RequestStatus;
import it.getinsight.module.request.service.RequestService;
import it.getinsight.module.role.service.RoleService;
import it.getinsight.module.summary.SummaryDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
@Slf4j
public class SummaryService {

    private final ClientService clientService;
    private final KeycloakClient keycloakClient;
    private final RoleService roleService;
    private final RequestService requestService;

    public SummaryDTO getAllSummaries() {
        return  SummaryDTO.builder()
                .totalActiveUsers(keycloakClient.getTotalUsersByEnabled(true))
                .totalRegisteredUsers(requestService.getTotalRequestsByStatus(RequestStatus.APPROVED))
                .totalPendingUsers(requestService.getTotalRequestsByStatus(RequestStatus.PENDING))
                .totalClients(clientService.getTotalClients())
                .totalRoles(roleService.getTotalRoles())
                .totalInactiveUsers(keycloakClient.getTotalUsersByEnabled(false))
                .build();
    }
}
