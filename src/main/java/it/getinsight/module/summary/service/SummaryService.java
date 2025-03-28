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
        var rolesChildren = roleRepository.findAllByRoleIn(roles);

        Map<Boolean, Function<List<RoleEntity>, SummaryDTO>> summaryStrategies = Map.of(
            true, ignored -> getAdminSummary(),
            false, r -> getNonAdminSummary(roles, rolesChildren)
        );

        return summaryStrategies.get(userService.isUserLoggedAdmin()).apply(roles);
    }


    private SummaryDTO getAdminSummary() {
        return SummaryDTO.builder()
            .totalRegisteredUsers(requestService.getTotalRequestsByStatus(RequestStatus.APPROVED))
            .totalPendingUsers(requestService.getTotalRequestsByStatus(RequestStatus.PENDING))
            .totalClients(clientService.getTotalClients())
            .totalRoles(roleService.getTotalRoles())
            .totalActiveUsers(requestService.getTotalUsers(RequestStatus.APPROVED))
            .totalInactiveUsers(requestService.getTotalUsers(RequestStatus.PENDING))
            .build();
    }

    private SummaryDTO getNonAdminSummary(List<RoleEntity> roles, List<RoleEntity> rolesChildren) {
        return SummaryDTO.builder()
            .totalRegisteredUsers(requestService.getTotalRequests(RequestStatus.APPROVED, rolesChildren))
            .totalPendingUsers(requestService.getTotalRequests(RequestStatus.PENDING, rolesChildren))
            .totalClients(clientService.getTotalClients(roles))
            .totalRoles(roleService.getTotalRoles(roles))
            .totalActiveUsers(requestService.getTotalUsers(RequestStatus.APPROVED, rolesChildren))
            .totalInactiveUsers(requestService.getTotalUsers(RequestStatus.PENDING, rolesChildren))
            .build();
    }


}
