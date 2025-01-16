package it.getinsight.module.email.dto;


import it.getinsight.module.notification.enums.NotificationType;
import lombok.Builder;

import java.io.Serializable;
import java.util.Date;
import java.util.Map;

@Builder
public record EmailFilterDTO(
    Long id,
    String uuid,
    String from,
    String to,
    String subject,
    String templateName,
    String criadoPor,
    Date criacao,
    String modificadoPor,
    Date ultimaAlteracao,
    Long userId,
    String status,
    String content,
    Boolean isHtml,
    Boolean isOpened,
    String title,
    String description,
    NotificationType type,
    Map<String, Object> variables
) implements Serializable {}










