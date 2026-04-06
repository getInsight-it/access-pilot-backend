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
class RequestApprovalMixedHierarchyIntegrationTests extends BaseIntegrationTest {

    @Autowired
    private RequestApprovalFixtureService fixtureService;

    @Autowired
    private RequestApiClient requestApiClient;

    @BeforeAll
    static void beforeAll() {
        log.info("==================================================");
        log.info("Starting integration tests: MIXED HIERARCHY (Sistema 3)");
        log.info("==================================================");
    }

    @BeforeEach
    void setUp() {
        log.info("Setting up Sistema 3: sistema-educacional (mixed hierarchy)...");
        fixtureService.setupSistema3SistemaEducacional();
        fixtureService.setupSistema3Users();
        log.info("Setup completed!");
    }

    @Test
    @DisplayName("Cenário 8.3: Deve aprovar via parent imediato na hierarquia funcional")
    void shouldApproveViaFunctionalHierarchyWithinSphere() throws Exception {
        log.info("=== Cenário 8.3: Hierarquia funcional intra-esfera ===");

        var roleAluno = fixtureService.getRole("sistema-educacional", "ALUNO_MUNICIPAL");
        assertNotNull(roleAluno, "Role ALUNO_MUNICIPAL deve existir");

        var createDTO = RequestCreateDTO.builder()
            .roleId(roleAluno.getId())
            .codeItem("SAO_PAULO")
            .description("Solicito confirmação de acesso aluno municipal SP")
            .build();

        log.info("Creating request for aluno.mun.sp: roleId={}, codeItem=SAO_PAULO", roleAluno.getId());

        var createResponse = requestApiClient.createRequest(createDTO, "aluno.mun.sp", "123456");
        assertEquals(HttpStatus.CREATED, createResponse.getStatusCode());

        Long requestId = requestApiClient.extractIdFromLocation(createResponse);
        log.info("Request created with ID: {}", requestId);

        await().atMost(5, SECONDS).untilAsserted(() -> {
            var request = requestApiClient.getRequest(requestId, "aluno.mun.sp", "123456");
            assertEquals(RequestStatus.PENDING, request.status());
        });
        var request = requestApiClient.getRequest(requestId, "aluno.mun.sp", "123456");
        var profAssigned = requestApiClient.listAssignedRequests("prof.mun.sp", "123456",
            RequestFilterDTO.builder().protocolCode(request.protocolCode()).build(), 1, 10);
        var dirAssigned = requestApiClient.listAssignedRequests("dir.mun.sp", "123456",
            RequestFilterDTO.builder().protocolCode(request.protocolCode()).build(), 1, 10);

        log.info("Assigned for prof.mun.sp: {}", profAssigned.getTotal());
        log.info("Assigned for dir.mun.sp: {}", dirAssigned.getTotal());

        boolean profCanSee = profAssigned.getItems().stream()
            .anyMatch(r -> r.id().equals(requestId));
        boolean dirCanSee = dirAssigned.getItems().stream()
            .anyMatch(r -> r.id().equals(requestId));

        assertTrue(profCanSee,
            "Professor municipal SP deve ver solicitação (parent direto na hierarquia funcional)");
        assertFalse(dirCanSee,
            "Diretor municipal SP não deve ver solicitação de neto funcional");

        var actions = requestApiClient.getMyAvailableActions(requestId, "prof.mun.sp", "123456");
        assertTrue(actions.contains(RequestAction.APPROVE),
            "Professor deve poder aprovar");
        var directorActions = requestApiClient.getMyAvailableActions(requestId, "dir.mun.sp", "123456");
        assertFalse(directorActions.contains(RequestAction.APPROVE),
            "Diretor municipal SP não deve poder aprovar solicitação de neto funcional");

        var updateDTO = RequestUpdateDTO.builder()
            .status("APPROVED")
            .finalReason("Aprovado na hierarquia funcional municipal")
            .build();

        var approveResponse = requestApiClient.updateRequestStatus(requestId, updateDTO,
            "prof.mun.sp", "123456");

        assertEquals(HttpStatus.ACCEPTED, approveResponse.getStatusCode());

        await().atMost(10, SECONDS).untilAsserted(() -> {
            var updated = requestApiClient.getRequest(requestId, "aluno.mun.sp", "123456");
            assertEquals(RequestStatus.APPROVED, updated.status());
        });

        log.info("✓ Cenário 8.3 concluído com sucesso");
    }

    @Test
    @DisplayName("Cenário 8.4: NÃO deve permitir aprovação cross-sphere no mesmo nível funcional")
    void shouldNotAllowCrossSphereApproval() throws Exception {
        log.info("=== Cenário 8.4: Isolamento entre esferas ===");

        var roleProfessor = fixtureService.getRole("sistema-educacional", "PROFESSOR_MUNICIPAL");

        var createDTO = RequestCreateDTO.builder()
            .roleId(roleProfessor.getId())
            .codeItem("SAO_PAULO")
            .description("Solicito professor municipal")
            .build();

        var createResponse = requestApiClient.createRequest(createDTO, "aluno.mun.sp", "123456");
        Long requestId = requestApiClient.extractIdFromLocation(createResponse);

        await().atMost(5, SECONDS).untilAsserted(() -> {
            var request = requestApiClient.getRequest(requestId, "aluno.mun.sp", "123456");
            assertEquals(RequestStatus.PENDING, request.status());
        });

        var profEstadualAssigned = requestApiClient.listAssignedRequests("prof.est.sp", "123456",
            RequestFilterDTO.builder().build(), 1, 10);

        boolean foundInAssigned = profEstadualAssigned.getItems().stream()
            .anyMatch(r -> r.id().equals(requestId));

        assertFalse(foundInAssigned,
            "Professor estadual não deve aprovar request municipal (isolamento de esfera)");

        log.info("✓ Cenário 8.4 concluído com sucesso - isolamento entre esferas funciona");
    }

    @Test
    @DisplayName("Cenário 8.5: Deve permitir hierarquia administrativa entre esferas (Estadual → Municipal)")
    void shouldAllowAdministrativeHierarchyApproval() throws Exception {
        log.info("=== Cenário 8.5: Hierarquia administrativa inter-esfera ===");

        var roleDiretor = fixtureService.getRole("sistema-educacional", "DIRETOR_MUNICIPAL");

        var createDTO = RequestCreateDTO.builder()
            .roleId(roleDiretor.getId())
            .codeItem("SAO_PAULO")
            .description("Solicito confirmação de cargo diretor municipal")
            .build();

        log.info("Creating request for prof.mun.sp (solicitando DIRETOR): roleId={}", roleDiretor.getId());

        var createResponse = requestApiClient.createRequest(createDTO, "prof.mun.sp", "123456");
        assertEquals(HttpStatus.CREATED, createResponse.getStatusCode());

        Long requestId = requestApiClient.extractIdFromLocation(createResponse);
        log.info("Request created with ID: {}", requestId);

        await().atMost(5, SECONDS).untilAsserted(() -> {
            var request = requestApiClient.getRequest(requestId, "prof.mun.sp", "123456");
            assertNotNull(request);
        });
        var request = requestApiClient.getRequest(requestId, "prof.mun.sp", "123456");

        var dirEstadualAssigned = requestApiClient.listAssignedRequests("dir.est.sp", "123456",
            RequestFilterDTO.builder().protocolCode(request.protocolCode()).build(), 1, 10);
        var dirFederalAssigned = requestApiClient.listAssignedRequests("dir.fed", "123456",
            RequestFilterDTO.builder().protocolCode(request.protocolCode()).build(), 1, 10);

        log.info("Assigned for dir.est.sp (estadual): {}", dirEstadualAssigned.getTotal());

        boolean foundInAssigned = dirEstadualAssigned.getItems().stream()
            .anyMatch(r -> r.id().equals(requestId));
        boolean foundForFederal = dirFederalAssigned.getItems().stream()
            .anyMatch(r -> r.id().equals(requestId));

        assertTrue(foundInAssigned,
            "Diretor estadual SP deve aprovar requests municipais em seu estado (parent imediato)");
        assertFalse(foundForFederal,
            "Diretor federal não deve aprovar request municipal quando há parent imediato estadual");

        var actions = requestApiClient.getMyAvailableActions(requestId, "dir.est.sp", "123456");
        assertTrue(actions.contains(RequestAction.APPROVE),
            "Diretor estadual deve poder aprovar via hierarquia administrativa");
        var federalActions = requestApiClient.getMyAvailableActions(requestId, "dir.fed", "123456");
        assertFalse(federalActions.contains(RequestAction.APPROVE),
            "Diretor federal não deve poder aprovar quando não é parent imediato");

        var updateDTO = RequestUpdateDTO.builder()
            .status("APPROVED")
            .finalReason("Aprovado pela hierarquia administrativa estadual")
            .build();

        var approveResponse = requestApiClient.updateRequestStatus(requestId, updateDTO,
            "dir.est.sp", "123456");

        assertEquals(HttpStatus.ACCEPTED, approveResponse.getStatusCode());

        await().atMost(10, SECONDS).untilAsserted(() -> {
            var updated = requestApiClient.getRequest(requestId, "prof.mun.sp", "123456");
            assertEquals(RequestStatus.APPROVED, updated.status());
        });

        log.info("✓ Cenário 8.5 concluído com sucesso");
    }

    @Test
    @DisplayName("Cenário 8.6: Deve permitir aprovação administrativa por parent imediato (Federal → Estadual)")
    void shouldAllowFederalDirectorToApproveImmediateAdministrativeChild() throws Exception {
        log.info("=== Cenário 8.6: Hierarquia administrativa por parent imediato ===");

        var roleDiretorEstadual = fixtureService.getRole("sistema-educacional", "DIRETOR_ESTADUAL");

        var createDTO = RequestCreateDTO.builder()
            .roleId(roleDiretorEstadual.getId())
            .codeItem("SP")
            .description("Solicito confirmação diretor estadual")
            .build();

        log.info("Creating request for prof.est.sp (solicitando DIRETOR_ESTADUAL): roleId={}",
            roleDiretorEstadual.getId());

        var createResponse = requestApiClient.createRequest(createDTO, "prof.est.sp", "123456");
        assertEquals(HttpStatus.CREATED, createResponse.getStatusCode());

        Long requestId = requestApiClient.extractIdFromLocation(createResponse);
        log.info("Request created with ID: {}", requestId);

        await().atMost(5, SECONDS).untilAsserted(() -> {
            var request = requestApiClient.getRequest(requestId, "prof.est.sp", "123456");
            assertNotNull(request);
        });

        var dirFederalAssigned = requestApiClient.listAssignedRequests("dir.fed", "123456",
            RequestFilterDTO.builder().build(), 1, 10);

        log.info("Assigned for dir.fed (federal): {}", dirFederalAssigned.getTotal());

        boolean foundInAssigned = dirFederalAssigned.getItems().stream()
            .anyMatch(r -> r.id().equals(requestId));

        assertTrue(foundInAssigned,
            "Diretor federal deve aprovar request estadual quando é parent imediato");

        var actions = requestApiClient.getMyAvailableActions(requestId, "dir.fed", "123456");
        assertTrue(actions.contains(RequestAction.APPROVE),
            "Diretor federal deve poder aprovar");

        var updateDTO = RequestUpdateDTO.builder()
            .status("APPROVED")
            .finalReason("Aprovado pelo diretor federal (topo da hierarquia)")
            .build();

        var approveResponse = requestApiClient.updateRequestStatus(requestId, updateDTO,
            "dir.fed", "123456");

        assertEquals(HttpStatus.ACCEPTED, approveResponse.getStatusCode());

        await().atMost(10, SECONDS).untilAsserted(() -> {
            var updated = requestApiClient.getRequest(requestId, "prof.est.sp", "123456");
            assertEquals(RequestStatus.APPROVED, updated.status());
        });

        log.info("✓ Cenário 8.6 concluído com sucesso");
    }

    @Test
    @DisplayName("Cenário 8.7: Deve validar item hierarchy (São Paulo pertence a SP, não a RJ)")
    void shouldValidateItemHierarchy() throws Exception {
        log.info("=== Cenário 8.7: Validação de item hierarchy ===");

        var roleDiretorMunicipal = fixtureService.getRole("sistema-educacional", "DIRETOR_MUNICIPAL");

        var createDTO = RequestCreateDTO.builder()
            .roleId(roleDiretorMunicipal.getId())
            .codeItem("SAO_PAULO")
            .description("Solicito confirmação de diretor em São Paulo capital")
            .build();

        var createResponse = requestApiClient.createRequest(createDTO, "prof.mun.sp", "123456");
        Long requestId = requestApiClient.extractIdFromLocation(createResponse);

        await().atMost(5, SECONDS).untilAsserted(() -> {
            var request = requestApiClient.getRequest(requestId, "prof.mun.sp", "123456");
            assertEquals(RequestStatus.PENDING, request.status());
        });

        var assignedSp = requestApiClient.listAssignedRequests("dir.est.sp", "123456",
            RequestFilterDTO.builder().build(), 1, 10);

        boolean spCanSee = assignedSp.getItems().stream()
            .anyMatch(r -> r.id().equals(requestId));

        assertTrue(spCanSee,
            "Diretor estadual SP deve aprovar requests municipais de São Paulo (item hierarchy)");

        var assignedRj = requestApiClient.listAssignedRequests("dir.est.rj", "123456",
            RequestFilterDTO.builder().build(), 1, 10);

        boolean rjCanSee = assignedRj.getItems().stream()
            .anyMatch(r -> r.id().equals(requestId));

        assertFalse(rjCanSee,
            "Diretor estadual RJ não deve aprovar requests de São Paulo (item hierarchy validation)");

        log.info("✓ Cenário 8.7 concluído com sucesso - item hierarchy validation funciona");
    }

    @AfterAll
    static void afterAll() {
        log.info("==================================================");
        log.info("Integration tests MIXED HIERARCHY completed!");
        log.info("Tested scenarios:");
        log.info("  ✓ 8.3 - Functional hierarchy within sphere");
        log.info("  ✓ 8.4 - Isolation between spheres");
        log.info("  ✓ 8.5 - Administrative hierarchy (State → Municipal)");
        log.info("  ✓ 8.6 - Immediate administrative parent (Federal → State)");
        log.info("  ✓ 8.7 - Item hierarchy validation");
        log.info("==================================================");
    }
}
