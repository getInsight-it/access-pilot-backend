package it.getinsight.module.notification.dto;


import it.getinsight.module.notification.enums.NotificationType;

import java.util.Date;

public record NotificationDTO(Long id, String uuid, Boolean isOpened, Long userId, String title, String description, NotificationType type, String criadoPor, Date criacao, String modificadoPor, Date ultimaAlteracao) implements Notification{}
