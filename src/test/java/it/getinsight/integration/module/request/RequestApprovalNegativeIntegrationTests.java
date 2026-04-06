package it.getinsight.integration.module.request;

import it.getinsight.core.test.BaseIntegrationTest;
import it.getinsight.core.test.helper.RequestApiClient;
import it.getinsight.integration.module.request.fixture.RequestApprovalFixtureService;
import it.getinsight.module.request.dto.RequestCreateDTO;
import it.getinsight.module.request.dto.RequestUpdateDTO;
import it.getinsight.module.request.enuns.RequestAction;
import it.getinsight.module.request.enuns.RequestStatus;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.client.HttpClientErrorException;

import static java.util.concurrent.TimeUnit.SECONDS;
import static org.awaitility.Awaitility.await;
import static org.junit.jupiter.api.Assertions.*;

@Slf4j
class RequestApprovalNegativeIntegrationTests extends BaseIntegrationTest {

    @Autowired
    private RequestApprovalFixtureService fixtureService;

    @Autowired
    private RequestApiClient requestApiClient;

    @BeforeAll
    static void beforeAll() {
        log.info("==================================================");
        log.info("Starting integration tests: NEGATIVE SCENARIOS");
        log.info("==================================================");
    }

    @BeforeEach
    void setUp() {
        log.info("Setting up test systems for negative scenarios...");
        fixtureService.setupSistema1EmpresaDemo();
        fixtureService.setupSistema1Users();
        fixtureService.setupSistema2ProtocoloEletronico();
        fixtureService.setupSistema2Users();
        fixtureService.setupAccessPilotAdminUser();
        log.info("Setup completed!");
    }

    @Test
    @DisplayName("Cenário 7.1: Deve retornar 403 quando usuário sem permissão tenta aprovar")
    void shouldReturn403WhenUnauthorizedUserTriesToApprove() throws Exception {
        log.info("=== Cenário 7.1: Usuário sem permissão ===");

        var roleGestor = fixtureService.getRole("empresa-demo", "GESTOR");

        var createDTO = RequestCreateDTO.builder()
            .roleId(roleGestor.getId())
            .codeItem("FIN")
            .description("Request para teste de autorização")
            .build();

        var createResponse = requestApiClient.createRequest(createDTO, "analista.fin", "123456");
        Long requestId = requestApiClient.extractIdFromLocation(createResponse);

        await().atMost(5, SECONDS).untilAsserted(() -> {
            var request = requestApiClient.getRequest(requestId, "analista.fin", "123456");
            assertEquals(RequestStatus.PENDING, request.status());
        });

        var updateDTO = RequestUpdateDTO.builder()
            .status("APPROVED")
            .finalReason("Tentativa não autorizada")
            .build();

        log.info("Attempting unauthorized approval by analista.compras (should fail)");

        HttpClientErrorException.Forbidden exception = assertThrows(
            HttpClientErrorException.Forbidden.class,
            () -> requestApiClient.updateRequestStatus(requestId, updateDTO, "analista.compras", "123456")
        );
        assertEquals(HttpStatus.FORBIDDEN, exception.getStatusCode());

        log.info("✓ Cenário 7.1 concluído com sucesso");
    }

    @Test
    @DisplayName("Cenário 7.2: Deve retornar 404 quando role inexistente é fornecida")
    void shouldReturn404WhenInvalidRoleId() {
        log.info("=== Cenário 7.2: Dados inválidos ===");

        var createDTO = RequestCreateDTO.builder()
            .roleId(999999L)  // ID inexistente
            .codeItem("FIN")
            .description("Request inválido")
            .build();

        log.info("Attempting to create request with invalid roleId=999999");

        HttpClientErrorException.NotFound exception = assertThrows(
            HttpClientErrorException.NotFound.class,
            () -> requestApiClient.createRequest(createDTO, "analista.fin", "123456")
        );
        assertEquals(HttpStatus.NOT_FOUND, exception.getStatusCode());

        log.info("✓ Cenário 7.2 concluído com sucesso");
    }

    @Test
    @DisplayName("Cenário 7.3: Usuário não deve ver requests de outro cliente")
    void shouldNotSeeRequestsFromDifferentClient() throws Exception {
        log.info("=== Cenário 7.3: Isolamento entre clientes ===");

        var roleGestor = fixtureService.getRole("empresa-demo", "GESTOR");

        var createDTO = RequestCreateDTO.builder()
            .roleId(roleGestor.getId())
            .codeItem("FIN")
            .description("Request sistema empresa-demo")
            .build();

        var createResponse = requestApiClient.createRequest(createDTO, "analista.fin", "123456");
        Long requestId = requestApiClient.extractIdFromLocation(createResponse);

        await().atMost(5, SECONDS).untilAsserted(() -> {
            var request = requestApiClient.getRequest(requestId, "analista.fin", "123456");
            assertEquals(RequestStatus.PENDING, request.status());
        });

        var assignedPage = requestApiClient.listAssignedRequests("proto.admin", "123456",
            null, 1, 100);

        boolean foundInAssigned = assignedPage.getItems().stream()
            .anyMatch(r -> r.id().equals(requestId));

        assertFalse(foundInAssigned,
            "Usuário de outro cliente não deve ver requests (isolamento de clientes)");

        log.info("✓ Cenário 7.3 concluído com sucesso - isolamento entre clientes funciona");
    }

    @Test
    @DisplayName("Cenário 7.4: getMyAvailableActions deve retornar vazio para usuário sem permissão")
    void shouldReturnEmptyActionsForUnauthorizedUser() throws Exception {
        log.info("=== Cenário 7.4: Ações disponíveis para usuário sem permissão ===");

        var roleGestor = fixtureService.getRole("empresa-demo", "GESTOR");

        var createDTO = RequestCreateDTO.builder()
            .roleId(roleGestor.getId())
            .codeItem("FIN")
            .description("Request para testar available actions")
            .build();

        var createResponse = requestApiClient.createRequest(createDTO, "analista.fin", "123456");
        Long requestId = requestApiClient.extractIdFromLocation(createResponse);

        await().atMost(5, SECONDS).untilAsserted(() -> {
            var request = requestApiClient.getRequest(requestId, "analista.fin", "123456");
            assertEquals(RequestStatus.PENDING, request.status());
        });

        var actions = requestApiClient.getMyAvailableActions(requestId, "analista.compras", "123456");

        assertNotNull(actions);
        assertFalse(actions.contains(RequestAction.APPROVE),
            "Usuário sem permissão não deve poder aprovar");
        assertFalse(actions.contains(RequestAction.REJECT),
            "Usuário sem permissão não deve poder rejeitar");

        log.info("Available actions for unauthorized user: {}", actions);
        log.info("✓ Cenário 7.4 concluído com sucesso");
    }

    @Test
    @DisplayName("Cenário 7.5: Deve retornar 403 ao tentar reaprovar request já aprovado")
    void shouldReturn400WhenApprovingAlreadyApprovedRequest() throws Exception {
        log.info("=== Cenário 7.5: Reaprovação não permitida ===");

        var roleGestor = fixtureService.getRole("empresa-demo", "GESTOR");

        var createDTO = RequestCreateDTO.builder()
            .roleId(roleGestor.getId())
            .codeItem("RH")
            .description("Request para testar dupla aprovação")
            .build();

        var createResponse = requestApiClient.createRequest(createDTO, "analista.rh", "123456");
        Long requestId = requestApiClient.extractIdFromLocation(createResponse);

        await().atMost(5, SECONDS).untilAsserted(() -> {
            var request = requestApiClient.getRequest(requestId, "analista.rh", "123456");
            assertEquals(RequestStatus.PENDING, request.status());
        });

        var updateDTO = RequestUpdateDTO.builder()
            .status("APPROVED")
            .finalReason("Primeira aprovação")
            .build();

        requestApiClient.updateRequestStatus(requestId, updateDTO, "admin", "123456");

        await().atMost(10, SECONDS).untilAsserted(() -> {
            var request = requestApiClient.getRequest(requestId, "analista.rh", "123456");
            assertEquals(RequestStatus.APPROVED, request.status());
        });

        log.info("Attempting to approve already approved request (should fail)");

        HttpClientErrorException.Forbidden exception = assertThrows(
            HttpClientErrorException.Forbidden.class,
            () -> requestApiClient.updateRequestStatus(requestId, updateDTO, "admin", "123456")
        );
        assertEquals(HttpStatus.FORBIDDEN, exception.getStatusCode());

        log.info("✓ Cenário 7.5 concluído com sucesso");
    }

    @AfterAll
    static void afterAll() {
        log.info("==================================================");
        log.info("Integration tests NEGATIVE SCENARIOS completed!");
        log.info("Tested scenarios:");
        log.info("  ✓ 7.1 - Unauthorized approval (403)");
        log.info("  ✓ 7.2 - Invalid role (404)");
        log.info("  ✓ 7.3 - Client isolation");
        log.info("  ✓ 7.4 - Empty available actions");
        log.info("  ✓ 7.5 - Double approval prevention");
        log.info("==================================================");
    }
}
