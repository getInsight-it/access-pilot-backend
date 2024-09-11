package it.getinsight.module.summary.controller.v1;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import it.getinsight.core.pagination.PageableRequestModel;
import it.getinsight.core.pagination.PageableResponseModel;
import it.getinsight.module.email.dto.EmailDTO;
import it.getinsight.module.email.service.EmailService;
import it.getinsight.module.summary.SummaryDTO;
import it.getinsight.module.summary.service.SummaryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/v1/summaries")
@Tag(name = "Summary", description = "Operations on summaries.")
@RequiredArgsConstructor
public class SummaryController {

    private final SummaryService summaryService;

    @Operation(
        summary = "Retrieve the list of summaries",
        description = "Retrieve all summaries"
    )
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<SummaryDTO> getAllSummaries() {
        return ResponseEntity.ok(summaryService.getAllSummaries());
    }

}
