package it.getinsight.module.notification.queue.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NotificationPayloadDTO implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Builder.Default
    private String version = "v1";

    private Long requestId;
    private Long recipientId;
    private String notificationType;

    private Long invitationId;
    private String invitationToken;
    private String invitationEmail;
    private String invitationRoleLabel;
    private String invitationClientLabel;
    private String invitationLevelName;
    private String invitationCodeItem;
    private String invitationExpiresAt;
}
