package it.getinsight.module.email.controller.v1;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import it.getinsight.core.pagination.PageableRequestModel;
import it.getinsight.core.pagination.PageableResponseModel;
import it.getinsight.module.email.dto.EmailDTO;
import it.getinsight.module.email.service.EmailService;
import it.getinsight.module.notification.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1/notifications/emails")
@Tag(name = "Notification", description = "Operations on notification.")
@RequiredArgsConstructor
public class EmailController {

    private final EmailService emailService;
    private final NotificationService notificationService;

    @Operation(
        summary = "Send an e-mail",
        responses = {
            @ApiResponse(responseCode = "200", description = "E-mail was send successfully"),
            @ApiResponse(responseCode = "400", description = "Some error occurred while sending the e-mail")
        }
    )
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> sendEmail(@RequestBody EmailDTO emailDTO) {
        notificationService.send(emailDTO);
        return ResponseEntity.ok().build();
    }

    @Operation(
        summary = "Consult notifications",
        responses = {
            @ApiResponse(responseCode = "200", description = "Notifications were consulted successfully"),
            @ApiResponse(responseCode = "400", description = "Some error occurred while consulting the notifications")
        }
    )
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<PageableResponseModel<EmailDTO>> getNotifications(@RequestParam(defaultValue = "1") Integer pageIndex,
                                                                                    @RequestParam(defaultValue = "10") Integer pageSize,
                                                                                    @RequestParam(defaultValue = "id") String sortField,
                                                                                    @RequestParam(defaultValue = "ASC") String sortType) {
        final var configPage = PageableRequestModel.of(pageIndex, pageSize, sortType, sortField, EmailDTO.builder().build());
        return ResponseEntity.ok(emailService.getNotifications(configPage));
    }


    @Operation(
        summary = "Update e-mail",
        responses = {
            @ApiResponse(responseCode = "200", description = "E-mail status was updated successfully"),
            @ApiResponse(responseCode = "400", description = "Some error occurred while updating the e-mail status")
        }
    )
    @PutMapping(value = "/{emailId}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> updateEmail(@PathVariable Long emailId, @RequestBody EmailDTO emailDTO) {
        emailService.updateEmail(emailId, emailDTO);
        return ResponseEntity.ok().build();
    }


    @Operation(
        summary = "Consult e-mail by id",
        responses = {
            @ApiResponse(responseCode = "200", description = "E-mail was consulted successfully"),
            @ApiResponse(responseCode = "400", description = "Some error occurred while consulting the e-mail")
        }
    )
    @GetMapping(value = "/{emailId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<EmailDTO> getEmailById(@PathVariable Long emailId) {
        return ResponseEntity.ok(emailService.getEmailById(emailId));
    }

}
