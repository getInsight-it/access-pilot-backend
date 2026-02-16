package it.getinsight.module.summary.service;

import it.getinsight.module.client.service.ClientService;
import it.getinsight.module.request.enuns.RequestStatus;
import it.getinsight.module.request.service.RequestService;
import it.getinsight.module.role.entity.RoleEntity;
import it.getinsight.module.role.repository.RoleRepository;
import it.getinsight.module.role.repository.specification.RoleSpecification;
import it.getinsight.module.role.service.RoleService;
import it.getinsight.module.summary.SummaryDTO;
import it.getinsight.module.user.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.function.Function;


@Service
@RequiredArgsConstructor
@Slf4j
public class SummaryService {

    private final ClientService clientService;
    private final RoleService roleService;
    private final RoleRepository roleRepository;
    private final RequestService requestService;
    private final UserService userService;

    public SummaryDTO getAllSummaries() {
        var principal = (Jwt) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Map<String, List<String>> resourceAccess = principal.getClaim("resource_access");
        var roles = roleRepository.findAll(RoleSpecification.byResourceAccess(resourceAccess)).stream().toList();

        Map<Boolean, Function<List<RoleEntity>, SummaryDTO>> summaryStrategies = Map.of(
            true, ignored -> getAdminSummary(),
            false, r -> getNonAdminSummary(roles)
        );

        return summaryStrategies.get(userService.isUserLoggedAdmin()).apply(roles);
    }


    private SummaryDTO getAdminSummary() {
        return SummaryDTO.builder()
            .totalApprovedRequests(requestService.getTotalRequestsByStatus(RequestStatus.APPROVED))
            .totalPendingRequests(requestService.getTotalRequestsByStatus(RequestStatus.PENDING))
            .totalClients(clientService.getTotalClients())
            .totalRoles(roleService.getTotalRoles())
            .totalApprovedUsers(requestService.getTotalUsers(RequestStatus.APPROVED))
            .totalPendingUsers(requestService.getTotalUsers(RequestStatus.PENDING))
            .build();
    }

    private SummaryDTO getNonAdminSummary(List<RoleEntity> roles) {
        return SummaryDTO.builder()
            .totalApprovedRequests(requestService.getTotalAssignedRequests(RequestStatus.APPROVED))
            .totalPendingRequests(requestService.getTotalAssignedRequests(RequestStatus.PENDING))
            .totalClients(clientService.getTotalClients(roles))
            .totalRoles(roleService.getTotalRoles(roles))
            .totalApprovedUsers(requestService.getTotalAssignedUsers(RequestStatus.APPROVED))
            .totalPendingUsers(requestService.getTotalAssignedUsers(RequestStatus.PENDING))
            .build();
    }


}
