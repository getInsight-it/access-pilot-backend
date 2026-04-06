package it.getinsight.integration.module.request;

import it.getinsight.core.test.BaseIntegrationTest;
import it.getinsight.core.test.helper.RequestApiClient;
import it.getinsight.integration.module.request.fixture.RequestApprovalFixtureService;
import it.getinsight.module.request.dto.RequestCreateDTO;
import it.getinsight.module.request.dto.RequestFilterDTO;
import it.getinsight.module.request.enuns.RequestStatus;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;

import static java.util.concurrent.TimeUnit.SECONDS;
import static org.awaitility.Awaitility.await;
import static org.junit.jupiter.api.Assertions.*;

@Slf4j
class RequestApprovalCrossSystemIntegrationTests extends BaseIntegrationTest {

    @Autowired
    private RequestApprovalFixtureService fixtureService;

    @Autowired
    private RequestApiClient requestApiClient;

    @BeforeAll
    static void beforeAll() {
        log.info("==================================================");
        log.info("Starting integration tests: CROSS-SYSTEM (Multi-system)");
        log.info("==================================================");
    }

    @BeforeEach
    void setUp() {
        log.info("Setting up all 3 test systems...");
        fixtureService.setupSistema1EmpresaDemo();
        fixtureService.setupSistema1Users();

        fixtureService.setupSistema2ProtocoloEletronico();
        fixtureService.setupSistema2Users();

        fixtureService.setupSistema3SistemaEducacional();
        fixtureService.setupSistema3Users();

        fixtureService.setupAccessPilotAdminUser();
        log.info("Setup completed for all 3 systems!");
    }

    @Test
    @DisplayName("Cenário 9.1: Deve listar corretamente requests de múltiplos sistemas")
    void shouldHandleMultipleSystemsIndependently() throws Exception {
        log.info("=== Cenário 9.1: Requests em múltiplos sistemas ===");

        var roleGestorEmpresa = fixtureService.getRole("empresa-demo", "GESTOR");
        var createDTO1 = RequestCreateDTO.builder()
            .roleId(roleGestorEmpresa.getId())
            .codeItem("FIN")
            .description("Request sistema empresa-demo")
            .build();

        var response1 = requestApiClient.createRequest(createDTO1, "analista.fin", "123456");
        assertEquals(HttpStatus.CREATED, response1.getStatusCode());
        Long requestId1 = requestApiClient.extractIdFromLocation(response1);

        var roleAnalistaProtocolo = fixtureService.getRole("protocolo-eletronico-interno", "ANALISTA_PROTOCOLO");
        var createDTO2 = RequestCreateDTO.builder()
            .roleId(roleAnalistaProtocolo.getId())
            .codeItem(null)  // sem esfera
            .description("Request sistema protocolo")
            .build();

        var response2 = requestApiClient.createRequest(createDTO2, "proto.tramitador", "123456");
        assertEquals(HttpStatus.CREATED, response2.getStatusCode());
        Long requestId2 = requestApiClient.extractIdFromLocation(response2);

        await().atMost(5, SECONDS).untilAsserted(() -> {
            var requestEmpresa = requestApiClient.getRequest(requestId1, "analista.fin", "123456");
            var requestProtocolo = requestApiClient.getRequest(requestId2, "proto.tramitador", "123456");
            assertEquals(RequestStatus.PENDING, requestEmpresa.status());
            assertEquals(RequestStatus.PENDING, requestProtocolo.status());
        });

        var myRequestsEmpresa = requestApiClient.listMyRequests("analista.fin", "123456", 1, 10);
        var myRequestsProtocolo = requestApiClient.listMyRequests("proto.tramitador", "123456", 1, 10);

        assertNotNull(myRequestsEmpresa);
        assertNotNull(myRequestsProtocolo);

        boolean foundRequest1 = myRequestsEmpresa.getItems().stream()
            .anyMatch(r -> r.id().equals(requestId1));
        boolean foundRequest2 = myRequestsProtocolo.getItems().stream()
            .anyMatch(r -> r.id().equals(requestId2));

        assertTrue(foundRequest1, "analista.fin deve ver seu request em empresa-demo");
        assertTrue(foundRequest2, "proto.tramitador deve ver seu request em protocolo");

        log.info("✓ Cenário 9.1 concluído - requests listados corretamente por sistema");
    }

    @Test
    @DisplayName("Cenário 9.2: NÃO deve permitir aprovação cross-system")
    void shouldNotAllowCrossSystemApproval() throws Exception {
        log.info("=== Cenário 9.2: Isolamento de aprovação entre sistemas ===");

        var roleGestor = fixtureService.getRole("empresa-demo", "GESTOR");
        var createDTO = RequestCreateDTO.builder()
            .roleId(roleGestor.getId())
            .codeItem("FIN")
            .description("Request para testar isolamento")
            .build();

        var createResponse = requestApiClient.createRequest(createDTO, "analista.fin", "123456");
        Long requestId = requestApiClient.extractIdFromLocation(createResponse);

        await().atMost(5, SECONDS).untilAsserted(() -> {
            var request = requestApiClient.getRequest(requestId, "analista.fin", "123456");
            assertEquals(RequestStatus.PENDING, request.status());
        });

        var assignedProtocolo = requestApiClient.listAssignedRequests("proto.admin", "123456",
            RequestFilterDTO.builder().build(), 1, 100);

        boolean foundInAssigned = assignedProtocolo.getItems().stream()
            .anyMatch(r -> r.id().equals(requestId));

        assertFalse(foundInAssigned,
            "Aprovador de outro sistema não deve ver requests (isolamento de sistemas)");

        log.info("✓ Cenário 9.2 concluído - isolamento entre sistemas funciona");
    }

    @Test
    @DisplayName("Cenário 9.3: Deve validar topologias diferentes funcionam corretamente")
    void shouldValidateDifferentTopologies() throws Exception {
        log.info("=== Cenário 9.3: Validação de topologias distintas ===");

        log.info("Testing Sistema 1: lateral approval...");
        var roleAnalistaFinanceiro = fixtureService.getRole("empresa-demo", "ANALISTA_FINANCEIRO");
        var createDTO1 = RequestCreateDTO.builder()
            .roleId(roleAnalistaFinanceiro.getId())
            .codeItem("RH")
            .description("Teste lateral approval")
            .build();

        var response1 = requestApiClient.createRequest(createDTO1, "analista.fin", "123456");
        Long requestId1 = requestApiClient.extractIdFromLocation(response1);

        await().atMost(5, SECONDS).untilAsserted(() -> {
            var request = requestApiClient.getRequest(requestId1, "analista.fin", "123456");
            assertEquals(RequestStatus.PENDING, request.status());
        });

        var assignedLateral = requestApiClient.listAssignedRequests("analista.rh", "123456",
            RequestFilterDTO.builder().build(), 1, 10);

        boolean lateralCanSee = assignedLateral.getItems().stream()
            .anyMatch(r -> r.id().equals(requestId1));

        assertTrue(lateralCanSee,
            "Sistema 1 deve suportar aprovação lateral");

        log.info("Testing Sistema 2: simple hierarchy without sphere...");
        var roleAnalistaProtocolo = fixtureService.getRole("protocolo-eletronico-interno", "ANALISTA_PROTOCOLO");
        var createDTO2 = RequestCreateDTO.builder()
            .roleId(roleAnalistaProtocolo.getId())
            .codeItem(null)
            .description("Teste hierarquia simples")
            .build();

        var response2 = requestApiClient.createRequest(createDTO2, "proto.tramitador", "123456");
        Long requestId2 = requestApiClient.extractIdFromLocation(response2);

        await().atMost(5, SECONDS).untilAsserted(() -> {
            var request = requestApiClient.getRequest(requestId2, "proto.tramitador", "123456");
            assertEquals(RequestStatus.PENDING, request.status());
        });

        var assignedHierarchy = requestApiClient.listAssignedRequests("proto.admin", "123456",
            RequestFilterDTO.builder().build(), 1, 10);

        boolean hierarchyCanSee = assignedHierarchy.getItems().stream()
            .anyMatch(r -> r.id().equals(requestId2));

        assertTrue(hierarchyCanSee,
            "Sistema 2 deve funcionar sem esfera");

        log.info("Testing Sistema 3: mixed hierarchy...");
        var roleDiretorMunicipal = fixtureService.getRole("sistema-educacional", "DIRETOR_MUNICIPAL");
        var createDTO3 = RequestCreateDTO.builder()
            .roleId(roleDiretorMunicipal.getId())
            .codeItem("SAO_PAULO")
            .description("Teste hierarquia mista")
            .build();

        var response3 = requestApiClient.createRequest(createDTO3, "prof.mun.sp", "123456");
        Long requestId3 = requestApiClient.extractIdFromLocation(response3);

        await().atMost(5, SECONDS).untilAsserted(() -> {
            var request = requestApiClient.getRequest(requestId3, "prof.mun.sp", "123456");
            assertEquals(RequestStatus.PENDING, request.status());
        });

        var assignedMixed = requestApiClient.listAssignedRequests("dir.est.sp", "123456",
            RequestFilterDTO.builder().build(), 1, 10);

        boolean mixedCanSee = assignedMixed.getItems().stream()
            .anyMatch(r -> r.id().equals(requestId3));

        assertTrue(mixedCanSee,
            "Sistema 3 deve suportar hierarquia administrativa entre esferas");

        log.info("✓ Cenário 9.3 concluído - todas topologias funcionam corretamente");
    }

    @Test
    @DisplayName("Cenário 9.4: Deve listar corretamente requests assigned de múltiplos sistemas")
    void shouldListAssignedFromMultipleSystems() throws Exception {
        log.info("=== Cenário 9.4: Assigned requests em múltiplos sistemas ===");


        var roleAnalistaFinanceiro = fixtureService.getRole("empresa-demo", "ANALISTA_FINANCEIRO");
        var createDTO1 = RequestCreateDTO.builder()
            .roleId(roleAnalistaFinanceiro.getId())
            .codeItem("FIN")
            .description("Request empresa para gestor.fin")
            .build();

        var createResponse = requestApiClient.createRequest(createDTO1, "analista.fin", "123456");
        assertEquals(HttpStatus.CREATED, createResponse.getStatusCode());
        Long requestId = requestApiClient.extractIdFromLocation(createResponse);

        await().atMost(5, SECONDS).untilAsserted(() -> {
            var request = requestApiClient.getRequest(requestId, "analista.fin", "123456");
            assertEquals(RequestStatus.PENDING, request.status());
        });

        var assignedGestor = requestApiClient.listAssignedRequests("gestor.fin", "123456",
            RequestFilterDTO.builder().build(), 1, 10);

        assertNotNull(assignedGestor);
        assertTrue(
            assignedGestor.getItems().stream().anyMatch(item -> item.id().equals(requestId)),
            "Gestor deve ter o request recém-criado em assigned"
        );

        log.info("Gestor has {} requests assigned", assignedGestor.getTotal());
        log.info("✓ Cenário 9.4 concluído");
    }

    @AfterAll
    static void afterAll() {
        log.info("==================================================");
        log.info("Integration tests CROSS-SYSTEM completed!");
        log.info("Tested scenarios:");
        log.info("  ✓ 9.1 - Multiple systems handling");
        log.info("  ✓ 9.2 - Cross-system approval isolation");
        log.info("  ✓ 9.3 - Different topologies validation");
        log.info("  ✓ 9.4 - Assigned requests across systems");
        log.info("==================================================");
        log.info("");
        log.info("ALL INTEGRATION TESTS COMPLETED SUCCESSFULLY!");
        log.info("==================================================");
        log.info("Summary:");
        log.info("  - Sistema 1 (empresa-demo): COM esfera + lateral approval");
        log.info("  - Sistema 2 (protocolo-eletronico): SEM esfera");
        log.info("  - Sistema 3 (sistema-educacional): Hierarquia mista");
        log.info("  - Negative scenarios: Authorization, validation");
        log.info("  - Cross-system: Isolation and independence");
        log.info("==================================================");
    }
}
