package it.getinsight.module.request.controller.v1;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import it.getinsight.module.request.dto.RequestAttachmentDTO;
import it.getinsight.module.request.service.RequestAttachmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/v1/requests/{id}/attachments")
@Tag(name = "Request", description = "Operations on requests attachments.")
@RequiredArgsConstructor
public class RequestAttachmentController {

    private final RequestAttachmentService requestAttachmentService;

    @Operation(
        summary = "Retrieve all attachments of a request",
        description = "Retrieve all attachments of a request"
    )
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<RequestAttachmentDTO>> findAll(@PathVariable Long id) {
        return ResponseEntity.ok(requestAttachmentService.findAllByRequest(id));
    }



}
