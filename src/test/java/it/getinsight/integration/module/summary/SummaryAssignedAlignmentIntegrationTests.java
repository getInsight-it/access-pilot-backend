package it.getinsight.integration.module.summary;

import it.getinsight.core.test.BaseIntegrationTest;
import it.getinsight.core.test.helper.RequestApiClient;
import it.getinsight.integration.module.request.fixture.RequestApprovalFixtureService;
import it.getinsight.module.request.dto.RequestCreateDTO;
import it.getinsight.module.request.enuns.RequestStatus;
import it.getinsight.module.summary.SummaryDTO;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;

import static java.util.concurrent.TimeUnit.SECONDS;
import static org.awaitility.Awaitility.await;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@Slf4j
class SummaryAssignedAlignmentIntegrationTests extends BaseIntegrationTest {

    @Autowired
    private RequestApprovalFixtureService fixtureService;

    @Autowired
    private RequestApiClient requestApiClient;

    @BeforeEach
    void setUp() {
        fixtureService.setupSistema3SistemaEducacional();
        fixtureService.setupSistema3Users();
    }

    @Test
    @DisplayName("Summary não-admin deve refletir regra de parent imediato")
    void shouldAlignNonAdminSummaryWithImmediateParentRule() throws Exception {
        var profSummaryBefore = getSummary("prof.mun.sp", "123456");
        var dirSummaryBefore = getSummary("dir.mun.sp", "123456");
        long profPendingBefore = valueOrZero(profSummaryBefore.totalPendingRequests());
        long dirPendingBefore = valueOrZero(dirSummaryBefore.totalPendingRequests());

        var roleAluno = fixtureService.getRole("sistema-educacional", "ALUNO_MUNICIPAL");
        assertNotNull(roleAluno, "Role ALUNO_MUNICIPAL deve existir");

        var createDTO = RequestCreateDTO.builder()
            .roleId(roleAluno.getId())
            .codeItem("SAO_PAULO")
            .description("Teste summary aligned with assigned")
            .build();

        var createResponse = requestApiClient.createRequest(createDTO, "aluno.mun.sp", "123456");
        assertEquals(HttpStatus.CREATED, createResponse.getStatusCode());
        Long requestId = requestApiClient.extractIdFromLocation(createResponse);

        await().atMost(5, SECONDS).untilAsserted(() -> {
            var request = requestApiClient.getRequest(requestId, "aluno.mun.sp", "123456");
            assertEquals(RequestStatus.PENDING, request.status());
        });

        await().atMost(10, SECONDS).untilAsserted(() -> {
            var profSummaryAfter = getSummary("prof.mun.sp", "123456");
            var dirSummaryAfter = getSummary("dir.mun.sp", "123456");

            assertEquals(profPendingBefore + 1, valueOrZero(profSummaryAfter.totalPendingRequests()),
                "Summary do professor deve aumentar com nova solicitação atribuída ao parent imediato");
            assertEquals(dirPendingBefore, valueOrZero(dirSummaryAfter.totalPendingRequests()),
                "Summary do diretor não deve aumentar para solicitação de neto");
        });
    }

    private SummaryDTO getSummary(String username, String password) {
        HttpEntity<Void> request = new HttpEntity<>(authenticatedUser(username, password));
        var response = testRestTemplate.exchange("/v1/summaries", HttpMethod.GET, request, SummaryDTO.class);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        return response.getBody();
    }

    private long valueOrZero(Long value) {
        return value == null ? 0L : value;
    }
}
