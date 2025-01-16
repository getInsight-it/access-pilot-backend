package it.getinsight.module.notification.dto;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import it.getinsight.module.email.dto.EmailDTO;
import it.getinsight.module.notification.enums.NotificationType;
import it.getinsight.module.web_notification.dto.WebNotificationDTO;

import java.io.Serializable;
import java.util.Date;

@JsonTypeInfo(
    use = JsonTypeInfo.Id.NAME,
    include = JsonTypeInfo.As.PROPERTY,
    property = "type"
)
@JsonSubTypes({
    @JsonSubTypes.Type(value = EmailDTO.class, name = "email"),
    @JsonSubTypes.Type(value = WebNotificationDTO.class, name = "web")
})
public interface Notification extends Serializable {
    Long id();
    String uuid();
    String title();
    String description();
    NotificationType type();
    String criadoPor();
    Date criacao();
    String modificadoPor();
    Date ultimaAlteracao();
    Boolean isOpened();
    Long userId();

}
