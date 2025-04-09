package it.getinsight.module.web_notification.controller.v1;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import it.getinsight.core.pagination.PageableRequestModel;
import it.getinsight.core.pagination.PageableResponseModel;
import it.getinsight.module.web_notification.dto.WebNotificationDTO;
import it.getinsight.module.web_notification.dto.WebNotificationFilterDTO;
import it.getinsight.module.web_notification.service.WebNotificationService;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1/notifications/web")
@Tag(name = "Notification", description = "Operations on notification.")
@RequiredArgsConstructor
public class WebNotificationController {

    private final WebNotificationService webNotificationService;

    @Operation(
        summary = "Send an web notification",
        responses = {
            @ApiResponse(responseCode = "200", description = "Web notification was send successfully"),
            @ApiResponse(responseCode = "400", description = "Some error occurred while sending the web notification")
        }
    )
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> sendWebNotification(@RequestBody WebNotificationDTO webNotificationDTO) {
        webNotificationService.send(webNotificationDTO);
        return ResponseEntity.ok().build();
    }

    @Operation(
        summary = "Retrieve the paginated list of web notifications",
        description = "Retrieve a list of web notifications, with pagination, using a filter by title"
    )
    @GetMapping( produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<PageableResponseModel<WebNotificationDTO>> getAllPaginated(
        @Min(value = 1, message = "O índice da página deve ser no mínimo 1") @RequestParam(defaultValue = "1") Integer pageIndex,
        @RequestParam(defaultValue = "10") Integer pageSize,
        @RequestParam(defaultValue = "id") String sortField,
        @RequestParam(defaultValue = "ASC") String sortType,
        @ParameterObject WebNotificationFilterDTO filter
    ) {
        final var pageRequest = PageableRequestModel.of(pageIndex -1, pageSize, sortType, sortField, filter);
        return ResponseEntity.ok(webNotificationService.getNotificationsFull(pageRequest));
    }

}
