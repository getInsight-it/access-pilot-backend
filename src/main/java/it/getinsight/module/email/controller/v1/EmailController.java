package it.getinsight.module.email.controller.v1;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import it.getinsight.module.email.dto.EmailDTO;
import it.getinsight.module.email.service.EmailService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/notifications/emails")
@Tag(name = "Email", description = "Operations on e-mails.")
@RequiredArgsConstructor
public class EmailController {

    private final EmailService emailService;

    @Operation(
        summary = "Send an e-mail",
        responses = {
            @ApiResponse(responseCode = "200", description = "E-mail was send successfully"),
            @ApiResponse(responseCode = "400", description = "Some error occurred while sending the e-mail")
        }
    )
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> sendEmail(@RequestBody EmailDTO emailDTO) {
        emailService.sendMail(emailDTO);
        return ResponseEntity.ok().build();
    }

}
