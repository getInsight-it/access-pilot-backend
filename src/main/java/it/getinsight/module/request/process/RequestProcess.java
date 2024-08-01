package it.getinsight.module.request.process;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.camunda.zeebe.client.api.response.ActivatedJob;
import io.camunda.zeebe.client.api.worker.JobClient;
import io.camunda.zeebe.spring.client.annotation.JobWorker;
import io.minio.MinioClient;
import it.getinsight.module.request.dto.RequestDTO;
import it.getinsight.module.request.service.RequestService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.Map;

@Component
@Slf4j
public class RequestProcess {

    @Autowired
    private ObjectMapper mapper;

    @Autowired
    private RequestService requestService;

    @JobWorker(type = "task-save-request")
    public void handleSave(JobClient client, ActivatedJob job) {
        Map<String, Object> variables = job.getVariablesAsMap();
        var solicitacao = mapper.convertValue(variables, RequestDTO.class);
        variables = mapper.convertValue(requestService.save(solicitacao), new TypeReference<>() {});
        client.newCompleteCommand(job.getKey()).variables(variables).send().join();
        log.info("request salvo com sucesso {}", solicitacao);
        logJob(job, variables);
    }

    @JobWorker(type = "task-save-status")
    public void handleSaveStatus(JobClient client, ActivatedJob job) {
        String status = (String) job.getVariablesAsMap().get("status");
        var id = ((Number) job.getVariablesAsMap().get("id")).longValue();
        requestService.updateRequest(id, status);
        log.info("Status salved on request {}", id);
        logJob(job, job.getVariablesAsMap());
    }


    @JobWorker(type = "task-confirm-roles")
    public void handleConfirmRoles(JobClient client, ActivatedJob job) {
        var id = ((Number) job.getVariablesAsMap().get("id")).longValue();
        requestService.confirmRoles(id);
        log.info("Status salved on request {}", id);
        logJob(job, job.getVariablesAsMap());
    }

    @JobWorker(type = "task-send-approves")
    public void handleSendApproves(JobClient client, ActivatedJob job) {
        var id = ((Number) job.getVariablesAsMap().get("id")).longValue();
        requestService.sendApproves(id);
    }

    @JobWorker(type = "task-notification-to-user")
    public void handleSendNotificationToUser(JobClient client, ActivatedJob job) {
        var id = ((Number) job.getVariablesAsMap().get("id")).longValue();
        requestService.sendNotificationToUser(id);
    }


    public static void logJob(final ActivatedJob job, Object parameterValue) {
        log.info(
            "complete job\n>>> [type: {}, key: {}, element: {}, workflow instance: {}]\n{deadline; {}]\n[headers: {}]\n[variable parameter: {}\n[variables: {}]",
            job.getType(),
            job.getKey(),
            job.getElementId(),
            job.getProcessInstanceKey(),
            Instant.ofEpochMilli(job.getDeadline()),
            job.getCustomHeaders(),
            parameterValue,
            job.getVariables());
    }

}
