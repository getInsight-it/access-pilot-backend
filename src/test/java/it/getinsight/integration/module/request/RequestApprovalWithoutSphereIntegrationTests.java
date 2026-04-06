package it.getinsight.integration.module.request;

import it.getinsight.core.test.BaseIntegrationTest;
import it.getinsight.core.test.helper.RequestApiClient;
import it.getinsight.integration.module.request.fixture.RequestApprovalFixtureService;
import it.getinsight.module.request.dto.RequestCreateDTO;
import it.getinsight.module.request.dto.RequestFilterDTO;
import it.getinsight.module.request.dto.RequestUpdateDTO;
import it.getinsight.module.request.enuns.RequestAction;
import it.getinsight.module.request.enuns.RequestStatus;
import it.getinsight.module.role.enuns.ApprovalPolicyType;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;

import java.util.List;

import static java.util.concurrent.TimeUnit.SECONDS;
import static org.awaitility.Awaitility.await;
import static org.junit.jupiter.api.Assertions.*;

@Slf4j
class RequestApprovalWithoutSphereIntegrationTests extends BaseIntegrationTest {

    @Autowired
    private RequestApprovalFixtureService fixtureService;

    @Autowired
    private RequestApiClient requestApiClient;

    @BeforeAll
    static void beforeAll() {
        log.info("==================================================");
        log.info("Starting integration tests: WITHOUT SPHERE (Sistema 2)");
        log.info("==================================================");
    }

    @BeforeEach
    void setUp() {
        log.info("Setting up Sistema 2: protocolo-eletronico-interno...");
        fixtureService.setupSistema2ProtocoloEletronico();
        fixtureService.setupSistema2Users();
        fixtureService.setupAccessPilotAdminUser();
        log.info("Setup completed!");
    }

    @Test
    @DisplayName("Cenário 6.1: Deve aprovar via hierarquia simples (sem esfera)")
    void shouldApproveWithoutSphere() throws Exception {
        log.info("=== Cenário 6.1: Hierarquia simples sem esfera ===");

        var roleAnalista = fixtureService.getRole("protocolo-eletronico-interno", "ANALISTA_PROTOCOLO");
        assertNotNull(roleAnalista, "Role ANALISTA_PROTOCOLO deve existir");

        assertNull(roleAnalista.getLevel(), "ANALISTA_PROTOCOLO não deve ter level (sem esfera)");

        var createDTO = RequestCreateDTO.builder()
            .roleId(roleAnalista.getId())
            .codeItem(null)  // SEM codeItem (sem esfera)
            .description("Solicito analista de protocolo")
            .build();

        log.info("Creating request for proto.tramitador: roleId={}, codeItem=null", roleAnalista.getId());

        var createResponse = requestApiClient.createRequest(createDTO, "proto.tramitador", "123456");
        assertEquals(HttpStatus.CREATED, createResponse.getStatusCode());

        Long requestId = requestApiClient.extractIdFromLocation(createResponse);
        log.info("Request created with ID: {}", requestId);

        await().atMost(5, SECONDS).untilAsserted(() -> {
            var request = requestApiClient.getRequest(requestId, "proto.tramitador", "123456");
            assertEquals(RequestStatus.PENDING, request.status());
        });

        var assignedPage = requestApiClient.listAssignedRequests("proto.admin", "123456",
            RequestFilterDTO.builder().build(), 1, 10);

        log.info("Assigned requests for proto.admin: {}", assignedPage.getTotal());
        boolean foundInAssigned = assignedPage.getItems().stream()
            .anyMatch(r -> r.id().equals(requestId));

        assertTrue(foundInAssigned,
            "Admin protocolo deve ver solicitação (parent na hierarquia)");

        var actions = requestApiClient.getMyAvailableActions(requestId, "proto.admin", "123456");
        assertTrue(actions.contains(RequestAction.APPROVE),
            "Admin protocolo deve poder aprovar");

        var updateDTO = RequestUpdateDTO.builder()
            .status("APPROVED")
            .finalReason("Aprovado por admin de protocolo")
            .build();

        var approveResponse = requestApiClient.updateRequestStatus(requestId, updateDTO,
            "proto.admin", "123456");

        assertEquals(HttpStatus.ACCEPTED, approveResponse.getStatusCode());

        await().atMost(10, SECONDS).untilAsserted(() -> {
            var updated = requestApiClient.getRequest(requestId, "proto.tramitador", "123456");
            assertEquals(RequestStatus.APPROVED, updated.status());
        });

        log.info("✓ Cenário 6.1 concluído com sucesso");
    }

    @Test
    @DisplayName("Cenário 6.2: Deve usar fallback admin para role topo sem esfera")
    void shouldRespectMultiLevelHierarchy() throws Exception {
        log.info("=== Cenário 6.2: Fallback admin para role topo ===");

        var roleAdmin = fixtureService.getRole("protocolo-eletronico-interno", "ADMIN_PROTOCOLO");
        assertNotNull(roleAdmin);

        assertNull(roleAdmin.getRole(), "ADMIN_PROTOCOLO não deve ter parent");

        var createDTO = RequestCreateDTO.builder()
            .roleId(roleAdmin.getId())
            .codeItem(null)
            .description("Solicito admin protocolo (topo)")
            .build();

        log.info("Creating request for proto.tramitador: roleId={} (ADMIN_PROTOCOLO - topo)", roleAdmin.getId());

        var createResponse = requestApiClient.createRequest(createDTO, "proto.tramitador", "123456");
        assertEquals(HttpStatus.CREATED, createResponse.getStatusCode());

        Long requestId = requestApiClient.extractIdFromLocation(createResponse);
        log.info("Request created with ID: {}", requestId);

        await().atMost(5, SECONDS).untilAsserted(() -> {
            var request = requestApiClient.getRequest(requestId, "proto.tramitador", "123456");
            assertEquals(RequestStatus.PENDING, request.status());
        });

        var assignedAdmin = requestApiClient.listAssignedRequests("admin", "123456",
            RequestFilterDTO.builder().build(), 1, 10);
        assertTrue(
            assignedAdmin.getItems().stream().anyMatch(r -> r.id().equals(requestId)),
            "Admin deve enxergar request de role topo no fallback"
        );

        var actions = requestApiClient.getMyAvailableActions(requestId, "admin", "123456");
        assertTrue(actions.contains(RequestAction.APPROVE), "Admin deve poder aprovar no fallback");

        var updateDTO = RequestUpdateDTO.builder()
            .status("APPROVED")
            .finalReason("Aprovado por fallback admin")
            .build();
        var approveResponse = requestApiClient.updateRequestStatus(requestId, updateDTO, "admin", "123456");
        assertEquals(HttpStatus.ACCEPTED, approveResponse.getStatusCode());

        await().atMost(10, SECONDS).untilAsserted(() -> {
            var updated = requestApiClient.getRequest(requestId, "proto.tramitador", "123456");
            assertEquals(RequestStatus.APPROVED, updated.status());
        });

        log.info("✓ Cenário 6.2 concluído com sucesso");
    }

    @Test
    @DisplayName("Cenário 6.3: Usuário intermediário NÃO deve ver solicitação fora de seu escopo")
    void shouldNotSeeRequestOutOfScope() throws Exception {
        log.info("=== Cenário 6.3: Isolamento de escopo ===");

        var roleAdmin = fixtureService.getRole("protocolo-eletronico-interno", "ADMIN_PROTOCOLO");

        var createDTO = RequestCreateDTO.builder()
            .roleId(roleAdmin.getId())
            .codeItem(null)
            .description("Solicito admin")
            .build();

        var createResponse = requestApiClient.createRequest(createDTO, "proto.tramitador", "123456");
        Long requestId = requestApiClient.extractIdFromLocation(createResponse);

        await().atMost(5, SECONDS).untilAsserted(() -> {
            var request = requestApiClient.getRequest(requestId, "proto.tramitador", "123456");
            assertNotNull(request);
        });

        var assignedPage = requestApiClient.listAssignedRequests("proto.analista", "123456",
            RequestFilterDTO.builder().build(), 1, 10);

        boolean foundInAssigned = assignedPage.getItems().stream()
            .anyMatch(r -> r.id().equals(requestId));

        assertFalse(foundInAssigned,
            "Analista não deve ver solicitação de role que não tem ele como parent indireto");

        log.info("✓ Cenário 6.3 concluído com sucesso - isolamento funciona");
    }

    @Test
    @DisplayName("Cenário 6.4: Deve listar corretamente 'minhas solicitações' sem esfera")
    void shouldListMyRequestsWithoutSphere() throws Exception {
        log.info("=== Cenário 6.4: Listar minhas solicitações ===");

        var roleAnalista = fixtureService.getRole("protocolo-eletronico-interno", "ANALISTA_PROTOCOLO");

        Long[] createdIds = new Long[2];
        for (int i = 0; i < 2; i++) {
            var createDTO = RequestCreateDTO.builder()
                .roleId(roleAnalista.getId())
                .codeItem(null)
                .description("Solicitação " + (i + 1))
                .build();

            var response = requestApiClient.createRequest(createDTO, "proto.tramitador", "123456");
            createdIds[i] = requestApiClient.extractIdFromLocation(response);
        }

        await().atMost(5, SECONDS).untilAsserted(() -> {
            for (Long requestId : createdIds) {
                var request = requestApiClient.getRequest(requestId, "proto.tramitador", "123456");
                assertEquals(RequestStatus.PENDING, request.status());
            }
        });

        var myRequests = requestApiClient.listMyRequests("proto.tramitador", "123456", 1, 10);

        assertNotNull(myRequests);
        assertTrue(
            myRequests.getItems().stream().anyMatch(item -> item.id().equals(createdIds[0])),
            "Lista deve conter primeira solicitação criada no cenário"
        );
        assertTrue(
            myRequests.getItems().stream().anyMatch(item -> item.id().equals(createdIds[1])),
            "Lista deve conter segunda solicitação criada no cenário"
        );

        log.info("✓ Cenário 6.4 concluído - {} solicitações encontradas", myRequests.getTotal());
    }

    @Test
    @DisplayName("Cenário 6.5: Deve permitir aprovação apenas pelo parent imediato sem esfera")
    void shouldAllowOnlyImmediateParentWithoutSphere() throws Exception {
        log.info("=== Cenário 6.5: Parent imediato sem esfera ===");

        var roleTramitador = fixtureService.getRole("protocolo-eletronico-interno", "TRAMITADOR");
        assertNotNull(roleTramitador, "Role TRAMITADOR deve existir");
        assertNull(roleTramitador.getLevel(), "TRAMITADOR não deve ter level");

        var createDTO = RequestCreateDTO.builder()
            .roleId(roleTramitador.getId())
            .codeItem(null)
            .description("Solicito confirmação tramitador")
            .build();

        var createResponse = requestApiClient.createRequest(createDTO, "proto.tramitador", "123456");
        assertEquals(HttpStatus.CREATED, createResponse.getStatusCode());
        Long requestId = requestApiClient.extractIdFromLocation(createResponse);

        await().atMost(5, SECONDS).untilAsserted(() -> {
            var request = requestApiClient.getRequest(requestId, "proto.tramitador", "123456");
            assertEquals(RequestStatus.PENDING, request.status());
        });
        var request = requestApiClient.getRequest(requestId, "proto.tramitador", "123456");

        var assignedAnalista = requestApiClient.listAssignedRequests("proto.analista", "123456",
            RequestFilterDTO.builder().protocolCode(request.protocolCode()).build(), 1, 10);
        var assignedAdmin = requestApiClient.listAssignedRequests("proto.admin", "123456",
            RequestFilterDTO.builder().protocolCode(request.protocolCode()).build(), 1, 10);

        boolean analistaCanSee = assignedAnalista.getItems().stream()
            .anyMatch(r -> r.id().equals(requestId));
        boolean adminCanSee = assignedAdmin.getItems().stream()
            .anyMatch(r -> r.id().equals(requestId));

        assertTrue(analistaCanSee, "Analista deve ver solicitação de tramitador (parent imediato)");
        assertFalse(adminCanSee, "Admin não deve ver solicitação de tramitador (apenas neto)");

        var analistaActions = requestApiClient.getMyAvailableActions(requestId, "proto.analista", "123456");
        var adminActions = requestApiClient.getMyAvailableActions(requestId, "proto.admin", "123456");
        assertTrue(analistaActions.contains(RequestAction.APPROVE), "Analista deve poder aprovar");
        assertFalse(adminActions.contains(RequestAction.APPROVE), "Admin não deve poder aprovar neto");

        var updateDTO = RequestUpdateDTO.builder()
            .status("APPROVED")
            .finalReason("Aprovado por parent imediato sem esfera")
            .build();
        var approveResponse = requestApiClient.updateRequestStatus(requestId, updateDTO, "proto.analista", "123456");
        assertEquals(HttpStatus.ACCEPTED, approveResponse.getStatusCode());

        await().atMost(10, SECONDS).untilAsserted(() -> {
            var updated = requestApiClient.getRequest(requestId, "proto.tramitador", "123456");
            assertEquals(RequestStatus.APPROVED, updated.status());
        });

        log.info("✓ Cenário 6.5 concluído com sucesso");
    }

    @Test
    @DisplayName("Cenário 6.6: Deve permitir aprovação lateral sem esfera na mesma topologia")
    void shouldAllowLateralApprovalWithoutSphereWithinSameTopology() throws Exception {
        log.info("=== Cenário 6.6: Aprovação lateral sem esfera na mesma topologia ===");

        var roleOrigem = fixtureService.ensureRole(
            "protocolo-eletronico-interno",
            "TRAMITADOR_ORIGEM_LATERAL",
            "ANALISTA_PROTOCOLO",
            null
        );
        var roleLateral = fixtureService.ensureRole(
            "protocolo-eletronico-interno",
            "TRAMITADOR_LATERAL",
            "ANALISTA_PROTOCOLO",
            null
        );

        assertNull(roleOrigem.getLevel(), "Role de origem deve ser sem esfera");
        assertNull(roleLateral.getLevel(), "Role lateral deve ser sem esfera");
        assertNotNull(roleOrigem.getRole(), "Role de origem deve ter parent");
        assertNotNull(roleLateral.getRole(), "Role lateral deve ter parent");
        assertEquals(roleOrigem.getRole().getId(), roleLateral.getRole().getId(),
            "Aprovação lateral deve ocorrer entre roles da mesma topologia (mesmo parent)");

        fixtureService.ensureUser("proto.tramitador.origem", "123456", "proto.tramitador.origem@protocolo.test");
        fixtureService.ensureUser("proto.tramitador.lateral", "123456", "proto.tramitador.lateral@protocolo.test");
        fixtureService.ensureUserRoleWithScope(
            "proto.tramitador.origem",
            "protocolo-eletronico-interno",
            "TRAMITADOR_ORIGEM_LATERAL",
            null
        );
        fixtureService.ensureUserRoleWithScope(
            "proto.tramitador.lateral",
            "protocolo-eletronico-interno",
            "TRAMITADOR_LATERAL",
            null
        );

        fixtureService.ensureApprovalPolicy(
            "protocolo-eletronico-interno",
            "TRAMITADOR_ORIGEM_LATERAL",
            ApprovalPolicyType.LATERAL_APPROVAL,
            List.of("TRAMITADOR_LATERAL")
        );

        var createDTO = RequestCreateDTO.builder()
            .roleId(roleOrigem.getId())
            .codeItem(null)
            .description("Solicito confirmação tramitador com aprovação lateral sem esfera")
            .build();

        var createResponse = requestApiClient.createRequest(createDTO, "proto.tramitador.origem", "123456");
        assertEquals(HttpStatus.CREATED, createResponse.getStatusCode());
        Long requestId = requestApiClient.extractIdFromLocation(createResponse);

        await().atMost(5, SECONDS).untilAsserted(() -> {
            var request = requestApiClient.getRequest(requestId, "proto.tramitador.origem", "123456");
            assertEquals(RequestStatus.PENDING, request.status());
        });
        var request = requestApiClient.getRequest(requestId, "proto.tramitador.origem", "123456");

        var assignedLateral = requestApiClient.listAssignedRequests("proto.tramitador.lateral", "123456",
            RequestFilterDTO.builder().protocolCode(request.protocolCode()).build(), 1, 10);

        boolean lateralCanSee = assignedLateral.getItems().stream()
            .anyMatch(r -> r.id().equals(requestId));
        assertTrue(lateralCanSee, "Role lateral deve ver solicitação da role de origem na mesma topologia");

        var lateralActions = requestApiClient.getMyAvailableActions(requestId, "proto.tramitador.lateral", "123456");
        assertTrue(lateralActions.contains(RequestAction.APPROVE),
            "Role lateral deve poder aprovar via política lateral sem esfera");

        var updateDTO = RequestUpdateDTO.builder()
            .status("APPROVED")
            .finalReason("Aprovado por role lateral na mesma topologia sem esfera")
            .build();
        var approveResponse = requestApiClient.updateRequestStatus(requestId, updateDTO,
            "proto.tramitador.lateral", "123456");
        assertEquals(HttpStatus.ACCEPTED, approveResponse.getStatusCode());

        await().atMost(10, SECONDS).untilAsserted(() -> {
            var updated = requestApiClient.getRequest(requestId, "proto.tramitador.origem", "123456");
            assertEquals(RequestStatus.APPROVED, updated.status());
        });

        log.info("✓ Cenário 6.6 concluído com sucesso (lateral sem esfera na mesma topologia)");
    }

    @AfterAll
    static void afterAll() {
        log.info("==================================================");
        log.info("Integration tests WITHOUT SPHERE completed!");
        log.info("Tested scenarios:");
        log.info("  ✓ 6.1 - Simple hierarchy without sphere");
        log.info("  ✓ 6.2 - Multi-level hierarchy");
        log.info("  ✓ 6.3 - Scope isolation");
        log.info("  ✓ 6.4 - List my requests");
        log.info("  ✓ 6.5 - Immediate parent only");
        log.info("  ✓ 6.6 - Lateral approval without sphere");
        log.info("==================================================");
    }
}
