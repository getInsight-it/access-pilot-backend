package it.getinsight.integration.module.request;

import it.getinsight.core.test.BaseIntegrationTest;
import it.getinsight.core.test.helper.RequestApiClient;
import it.getinsight.integration.module.request.fixture.RequestApprovalFixtureService;
import it.getinsight.module.request.dto.RequestCreateDTO;
import it.getinsight.module.request.dto.RequestFilterDTO;
import it.getinsight.module.request.dto.RequestUpdateDTO;
import it.getinsight.module.request.enuns.RequestAction;
import it.getinsight.module.request.enuns.RequestStatus;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import static java.util.concurrent.TimeUnit.SECONDS;
import static org.awaitility.Awaitility.await;
import static org.junit.jupiter.api.Assertions.*;

@Slf4j
class RequestApprovalWithSphereIntegrationTests extends BaseIntegrationTest {

    @Autowired
    private RequestApprovalFixtureService fixtureService;

    @Autowired
    private RequestApiClient requestApiClient;

    @BeforeAll
    static void beforeAll() {
        log.info("==================================================");
        log.info("Starting integration tests: WITH SPHERE (Sistema 1)");
        log.info("==================================================");
    }

    @BeforeEach
    void setUp() {
        log.info("Setting up Sistema 1: empresa-demo...");
        fixtureService.setupSistema1EmpresaDemo();
        fixtureService.setupSistema1Users();
        fixtureService.setupAccessPilotAdminUser();
        log.info("Setup completed!");
    }

    @Test
    @DisplayName("Cenário 5.1: Deve aprovar via acesso hierárquico (mesmo item)")
    void shouldApproveViaHierarchicalAccess() throws Exception {
        log.info("=== Cenário 5.1: Hierarquia direta - mesmo item ===");

        var roleAnalistaRh = fixtureService.getRole("empresa-demo", "ANALISTA_RH");
        assertNotNull(roleAnalistaRh, "Role ANALISTA_RH deve existir");

        var createDTO = RequestCreateDTO.builder()
            .roleId(roleAnalistaRh.getId())
            .codeItem("RH")
            .description("Solicito confirmação de acesso analista RH")
            .build();

        log.info("Creating request for user analista.rh: roleId={}, codeItem=RH", roleAnalistaRh.getId());

        var createResponse = requestApiClient.createRequest(createDTO, "analista.rh", "123456");

        assertEquals(HttpStatus.CREATED, createResponse.getStatusCode());
        assertNotNull(createResponse.getHeaders().getLocation());

        Long requestId = requestApiClient.extractIdFromLocation(createResponse);
        log.info("Request created with ID: {}", requestId);

        await().atMost(5, SECONDS).untilAsserted(() -> {
            var request = requestApiClient.getRequest(requestId, "analista.rh", "123456");
            assertNotNull(request);
            log.info("Request status: {}", request.status());
        });

        var request = requestApiClient.getRequest(requestId, "analista.rh", "123456");
        assertEquals(RequestStatus.PENDING, request.status());

        var assignedPage = requestApiClient.listAssignedRequests("gestor.rh", "123456",
            RequestFilterDTO.builder().build(), 1, 10);

        log.info("Assigned requests for gestor.rh: {}", assignedPage.getTotal());
        assertTrue(assignedPage.getItems().stream()
                .anyMatch(r -> r.id().equals(requestId)),
            "Gestor RH deve ver solicitação no assigned");

        var actions = requestApiClient.getMyAvailableActions(requestId, "gestor.rh", "123456");
        assertTrue(actions.contains(RequestAction.APPROVE),
            "Gestor RH deve poder aprovar");

        var updateDTO = RequestUpdateDTO.builder()
            .status("APPROVED")
            .finalReason("Aprovado por gestor hierárquico")
            .build();

        var approveResponse = requestApiClient.updateRequestStatus(requestId, updateDTO,
            "gestor.rh", "123456");

        assertEquals(HttpStatus.ACCEPTED, approveResponse.getStatusCode());

        await().atMost(10, SECONDS).untilAsserted(() -> {
            var updated = requestApiClient.getRequest(requestId, "analista.rh", "123456");
            assertEquals(RequestStatus.APPROVED, updated.status(),
                "Request deve estar APPROVED após processamento");
        });

        log.info("✓ Cenário 5.1 concluído com sucesso");
    }

    @Test
    @DisplayName("Cenário 5.2: Deve aprovar via política lateral")
    void shouldApproveViaLateralPolicy() throws Exception {
        log.info("=== Cenário 5.2: Aprovação lateral ===");

        var roleAnalistaFinanceiro = fixtureService.getRole("empresa-demo", "ANALISTA_FINANCEIRO");
        assertNotNull(roleAnalistaFinanceiro);

        var createDTO = RequestCreateDTO.builder()
            .roleId(roleAnalistaFinanceiro.getId())
            .codeItem("RH")
            .description("Solicito acesso analista financeiro com aprovação lateral")
            .build();

        log.info("Creating request for user analista.fin: roleId={}, codeItem=RH", roleAnalistaFinanceiro.getId());

        var createResponse = requestApiClient.createRequest(createDTO, "analista.fin", "123456");
        assertEquals(HttpStatus.CREATED, createResponse.getStatusCode());

        Long requestId = requestApiClient.extractIdFromLocation(createResponse);
        log.info("Request created with ID: {}", requestId);

        await().atMost(5, SECONDS).untilAsserted(() -> {
            var request = requestApiClient.getRequest(requestId, "analista.fin", "123456");
            assertEquals(RequestStatus.PENDING, request.status());
        });

        var assignedPage = requestApiClient.listAssignedRequests("analista.rh", "123456",
            RequestFilterDTO.builder().build(), 1, 10);

        log.info("Assigned requests for analista.rh (lateral): {}", assignedPage.getTotal());
        boolean foundInAssigned = assignedPage.getItems().stream()
            .anyMatch(r -> r.id().equals(requestId));

        assertTrue(foundInAssigned,
            "Analista RH deve ver solicitação FIN via aprovação lateral");

        var actions = requestApiClient.getMyAvailableActions(requestId, "analista.rh", "123456");
        assertTrue(actions.contains(RequestAction.APPROVE),
            "Analista RH deve poder aprovar via política lateral");

        var updateDTO = RequestUpdateDTO.builder()
            .status("APPROVED")
            .finalReason("Aprovado por lateral (analista de outro setor)")
            .build();

        var approveResponse = requestApiClient.updateRequestStatus(requestId, updateDTO,
            "analista.rh", "123456");

        assertEquals(HttpStatus.ACCEPTED, approveResponse.getStatusCode());

        await().atMost(10, SECONDS).untilAsserted(() -> {
            var updated = requestApiClient.getRequest(requestId, "analista.fin", "123456");
            assertEquals(RequestStatus.APPROVED, updated.status());
        });

        log.info("✓ Cenário 5.2 concluído com sucesso");
    }

    @Test
    @DisplayName("Cenário 5.3: Deve fazer fallback para admin quando role é topo da hierarquia")
    void shouldFallbackToAdmin() throws Exception {
        log.info("=== Cenário 5.3: Fallback admin ===");

        var roleGestor = fixtureService.getRole("empresa-demo", "GESTOR");
        assertNotNull(roleGestor);
        assertNull(roleGestor.getRole(), "GESTOR deve ser role topo");

        var createDTO = RequestCreateDTO.builder()
            .roleId(roleGestor.getId())
            .codeItem("FIN")
            .description("Solicito acesso gestor via fallback admin")
            .build();

        log.info("Creating request for analista.fin: roleId={}, codeItem=FIN", roleGestor.getId());

        var createResponse = requestApiClient.createRequest(createDTO, "analista.fin", "123456");
        assertEquals(HttpStatus.CREATED, createResponse.getStatusCode());

        Long requestId = requestApiClient.extractIdFromLocation(createResponse);
        log.info("Request created with ID: {}", requestId);

        await().atMost(5, SECONDS).untilAsserted(() -> {
            var request = requestApiClient.getRequest(requestId, "analista.fin", "123456");
            assertEquals(RequestStatus.PENDING, request.status());
        });

        var assignedPage = requestApiClient.listAssignedRequests("admin", "123456",
            RequestFilterDTO.builder().build(), 1, 10);
        assertTrue(
            assignedPage.getItems().stream().anyMatch(r -> r.id().equals(requestId)),
            "Admin deve enxergar request de role topo no fallback"
        );

        var actions = requestApiClient.getMyAvailableActions(requestId, "admin", "123456");
        assertTrue(actions.contains(RequestAction.APPROVE), "Admin deve poder aprovar no fallback");

        var updateDTO = RequestUpdateDTO.builder()
            .status("APPROVED")
            .finalReason("Aprovado por fallback administrativo")
            .build();
        var approveResponse = requestApiClient.updateRequestStatus(requestId, updateDTO, "admin", "123456");
        assertEquals(HttpStatus.ACCEPTED, approveResponse.getStatusCode());

        await().atMost(10, SECONDS).untilAsserted(() -> {
            var updated = requestApiClient.getRequest(requestId, "analista.fin", "123456");
            assertEquals(RequestStatus.APPROVED, updated.status());
        });

        log.info("✓ Cenário 5.3 concluído com sucesso");
    }

    @Test
    @DisplayName("Cenário 8.1: NÃO deve permitir aprovação em mesmo nível mas item diferente")
    void shouldNotAllowApprovalInDifferentItem() throws Exception {
        log.info("=== Cenário 8.1: Isolamento por item ===");

        var roleGestor = fixtureService.getRole("empresa-demo", "GESTOR");

        var createDTO = RequestCreateDTO.builder()
            .roleId(roleGestor.getId())
            .codeItem("FIN")
            .description("Solicito gestor financeiro")
            .build();

        var createResponse = requestApiClient.createRequest(createDTO, "analista.fin", "123456");
        Long requestId = requestApiClient.extractIdFromLocation(createResponse);

        await().atMost(5, SECONDS).untilAsserted(() -> {
            var request = requestApiClient.getRequest(requestId, "analista.fin", "123456");
            assertEquals(RequestStatus.PENDING, request.status());
        });

        var assignedPage = requestApiClient.listAssignedRequests("gestor.rh", "123456",
            RequestFilterDTO.builder().build(), 1, 10);

        boolean foundInAssigned = assignedPage.getItems().stream()
            .anyMatch(r -> r.id().equals(requestId));

        assertFalse(foundInAssigned,
            "Gestor RH não deve ver solicitação FIN (item diferente, sem política lateral)");

        log.info("✓ Cenário 8.1 concluído com sucesso - isolamento por item funciona");
    }

    @AfterAll
    static void afterAll() {
        log.info("==================================================");
        log.info("Integration tests WITH SPHERE completed!");
        log.info("Tested scenarios:");
        log.info("  ✓ 5.1 - Hierarchical approval (same item)");
        log.info("  ✓ 5.2 - Lateral approval policy");
        log.info("  ✓ 5.3 - Admin fallback / Auto-approval");
        log.info("  ✓ 8.1 - Item isolation (different items)");
        log.info("==================================================");
    }
}
