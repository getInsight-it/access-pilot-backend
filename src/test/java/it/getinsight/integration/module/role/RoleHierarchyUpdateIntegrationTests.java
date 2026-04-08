package it.getinsight.integration.module.role;

import it.getinsight.core.test.BaseIntegrationTest;
import it.getinsight.core.test.helper.RequestApiClient;
import it.getinsight.integration.module.request.fixture.RequestApprovalFixtureService;
import it.getinsight.module.request.dto.RequestCreateDTO;
import it.getinsight.module.request.enuns.RequestStatus;
import it.getinsight.module.role.dto.RoleUpdateHierarchyDTO;
import it.getinsight.module.role.entity.RoleEntity;
import it.getinsight.module.role.repository.RoleRepository;
import it.getinsight.module.request.repository.RequestRepository;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.UUID;

import static java.util.concurrent.TimeUnit.SECONDS;
import static org.awaitility.Awaitility.await;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@Slf4j
class RoleHierarchyUpdateIntegrationTests extends BaseIntegrationTest {

    @Autowired
    private RequestApprovalFixtureService fixtureService;

    @Autowired
    private RequestApiClient requestApiClient;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private RequestRepository requestRepository;

    @BeforeEach
    void setUp() {
        fixtureService.setupSistema2ProtocoloEletronico();
        fixtureService.setupSistema2Users();
        fixtureService.setupAccessPilotAdminUser();
    }

    @Test
    @DisplayName("Deve bloquear update de hierarquia quando role tem solicitacao pendente")
    void shouldBlockHierarchyUpdateWhenRoleHasPendingRequests() {
        var blockedRole = fixtureService.ensureRole(
            "protocolo-eletronico-interno",
            uniqueRoleName("ROLE_HIER_BLOCKED"),
            "ANALISTA_PROTOCOLO",
            null
        );
        var adminProtocolo = fixtureService.getRole("protocolo-eletronico-interno", "ADMIN_PROTOCOLO");
        var originalParentId = blockedRole.getRole().getId();

        createPendingRequestForRole(blockedRole, "proto.tramitador");
        assertTrue(requestRepository.countByStatusAndRole(RequestStatus.PENDING, blockedRole) > 0);

        ResponseEntity<Void> response = updateHierarchy(List.of(toHierarchyUpdate(blockedRole, adminProtocolo.getId())));
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());

        var reloaded = roleRepository.findById(blockedRole.getId()).orElseThrow();
        assertEquals(originalParentId, reloaded.getRole().getId());
    }

    @Test
    @DisplayName("Deve permitir update de hierarquia quando role nao tem solicitacao pendente")
    void shouldAllowHierarchyUpdateWhenRoleHasNoPendingRequests() {
        var role = fixtureService.ensureRole(
            "protocolo-eletronico-interno",
            uniqueRoleName("ROLE_HIER_ALLOWED"),
            "ANALISTA_PROTOCOLO",
            null
        );
        var adminProtocolo = fixtureService.getRole("protocolo-eletronico-interno", "ADMIN_PROTOCOLO");

        ResponseEntity<Void> response = updateHierarchy(List.of(toHierarchyUpdate(role, adminProtocolo.getId())));
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());

        var reloaded = roleRepository.findById(role.getId()).orElseThrow();
        assertEquals(adminProtocolo.getId(), reloaded.getRole().getId());
    }

    @Test
    @DisplayName("Deve fazer rollback do batch quando uma role da lista tem solicitacao pendente")
    void shouldRollbackBatchHierarchyUpdateWhenAnyRoleHasPendingRequests() {
        var cleanRole = fixtureService.ensureRole(
            "protocolo-eletronico-interno",
            uniqueRoleName("ROLE_HIER_BATCH"),
            "ANALISTA_PROTOCOLO",
            null
        );
        var blockedRole = fixtureService.ensureRole(
            "protocolo-eletronico-interno",
            uniqueRoleName("ROLE_HIER_BLOCKED_BATCH"),
            "ANALISTA_PROTOCOLO",
            null
        );
        var adminProtocolo = fixtureService.getRole("protocolo-eletronico-interno", "ADMIN_PROTOCOLO");

        var originalCleanParentId = cleanRole.getRole().getId();
        var originalBlockedParentId = blockedRole.getRole().getId();

        createPendingRequestForRole(blockedRole, "proto.tramitador");
        assertTrue(requestRepository.countByStatusAndRole(RequestStatus.PENDING, blockedRole) > 0);

        var updates = List.of(
            toHierarchyUpdate(cleanRole, adminProtocolo.getId()),
            toHierarchyUpdate(blockedRole, adminProtocolo.getId())
        );

        ResponseEntity<Void> response = updateHierarchy(updates);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());

        var cleanReloaded = roleRepository.findById(cleanRole.getId()).orElseThrow();
        var blockedReloaded = roleRepository.findById(blockedRole.getId()).orElseThrow();
        assertEquals(originalCleanParentId, cleanReloaded.getRole().getId());
        assertEquals(originalBlockedParentId, blockedReloaded.getRole().getId());
    }

    @Test
    @DisplayName("Deve ignorar pendencia de role sem alteracao no batch de hierarquia")
    void shouldIgnorePendingRequestsForUnchangedRolesInHierarchyBatch() {
        var cleanRole = fixtureService.ensureRole(
            "protocolo-eletronico-interno",
            uniqueRoleName("ROLE_HIER_CLEAN_CHANGED"),
            "ANALISTA_PROTOCOLO",
            null
        );
        var unchangedPendingRole = fixtureService.ensureRole(
            "protocolo-eletronico-interno",
            uniqueRoleName("ROLE_HIER_PENDING_UNCHANGED"),
            "ANALISTA_PROTOCOLO",
            null
        );
        var adminProtocolo = fixtureService.getRole("protocolo-eletronico-interno", "ADMIN_PROTOCOLO");

        var originalUnchangedParentId = unchangedPendingRole.getRole().getId();

        createPendingRequestForRole(unchangedPendingRole, "proto.tramitador");
        assertTrue(requestRepository.countByStatusAndRole(RequestStatus.PENDING, unchangedPendingRole) > 0);

        var updates = List.of(
            toHierarchyUpdate(cleanRole, adminProtocolo.getId()),
            toHierarchyUpdate(unchangedPendingRole, originalUnchangedParentId)
        );

        ResponseEntity<Void> response = updateHierarchy(updates);
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());

        var cleanReloaded = roleRepository.findById(cleanRole.getId()).orElseThrow();
        var unchangedReloaded = roleRepository.findById(unchangedPendingRole.getId()).orElseThrow();
        assertEquals(adminProtocolo.getId(), cleanReloaded.getRole().getId());
        assertEquals(originalUnchangedParentId, unchangedReloaded.getRole().getId());
    }

    private ResponseEntity<Void> updateHierarchy(List<RoleUpdateHierarchyDTO> roles) {
        HttpEntity<List<RoleUpdateHierarchyDTO>> request = new HttpEntity<>(roles, jsonAuth("admin", "123456"));
        return testRestTemplate.exchange("/v1/roles", HttpMethod.PUT, request, Void.class);
    }

    private RoleUpdateHierarchyDTO toHierarchyUpdate(RoleEntity role, Long newParentId) {
        return RoleUpdateHierarchyDTO.builder()
            .id(role.getId())
            .parentId(newParentId)
            .clientId(role.getClient().getId())
            .build();
    }

    private void createPendingRequestForRole(RoleEntity role, String requesterUsername) {
        var createDTO = RequestCreateDTO.builder()
            .roleId(role.getId())
            .codeItem(null)
            .description("E2E-pending-hierarchy-" + UUID.randomUUID())
            .build();

        var createResponse = requestApiClient.createRequest(createDTO, requesterUsername, "123456");
        Long requestId = requestApiClient.extractIdFromLocation(createResponse);

        await().atMost(5, SECONDS).untilAsserted(() -> {
            var request = requestApiClient.getRequest(requestId, requesterUsername, "123456");
            assertEquals(RequestStatus.PENDING, request.status());
        });
    }

    private String uniqueRoleName(String prefix) {
        return prefix + "_" + UUID.randomUUID().toString().replace("-", "").substring(0, 8).toUpperCase();
    }
}
