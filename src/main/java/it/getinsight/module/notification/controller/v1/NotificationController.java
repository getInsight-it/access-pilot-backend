package it.getinsight.module.notification.controller.v1;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import it.getinsight.core.pagination.PageableRequestModel;
import it.getinsight.core.pagination.PageableResponseModel;
import it.getinsight.module.notification.dto.Notification;
import it.getinsight.module.notification.dto.NotificationFilterDTO;
import it.getinsight.module.notification.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/notifications")
@RequiredArgsConstructor
@Tag(name = "Notification", description = "Operations on notification.")
public class NotificationController {

    private final NotificationService notificationService;

    @Operation(summary = "Creates a new notification.",
        description = "Creates a new notification with the given notificationDTO"
    )
    @PostMapping
    public ResponseEntity<Notification> createNotification(@RequestBody Notification notificationDTO) {
        return ResponseEntity.ok(notificationService.send(notificationDTO));
    }

    @GetMapping
    @Operation(summary = "Get notifications.",
        description = "Get notifications with the given filter"
    )
    @PreAuthorize("hasRole('ADMIN') OR @userService.checkExternalId(#filter.userId)")
    public ResponseEntity<PageableResponseModel<Notification>> getNotification(
        @RequestParam(defaultValue = "1") Integer pageIndex,
        @RequestParam(defaultValue = "10") Integer pageSize,
        @RequestParam(defaultValue = "id") String sortField,
        @RequestParam(defaultValue = "ASC") String sortType,
        @ParameterObject NotificationFilterDTO filter) {
        final var pageRequest = PageableRequestModel.of(pageIndex - 1, pageSize, sortType, sortField, filter);
        return ResponseEntity.ok(notificationService.getNotifications(pageRequest));
    }

}
