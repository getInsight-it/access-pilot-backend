package it.getinsight.module.web_notification.entity;

import it.getinsight.module.notification.entity.NotificationEntity;
import it.getinsight.module.notification.enums.NotificationType;
import it.getinsight.module.request.entity.RequestEntity;
import it.getinsight.module.user.entity.UserEntity;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.envers.Audited;

import java.util.UUID;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@PrimaryKeyJoinColumn(name= "id")
@Builder(toBuilder = true)
@Audited
@Table(name = "TB_WEB_NOTIFICACAO")
public class WebNotificationEntity extends NotificationEntity {

    @Column(name = "PRIORIDADE")
    private Integer priority;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ID_SOLICITACAO")
    private RequestEntity request;

    @PrePersist
    public void prePersist() {
        this.uuid = UUID.randomUUID();
        this.isOpened = false;
        this.type = NotificationType.WEB;
    }


    public static CustomWebNotificationEntityBuilder builder() {
        return new CustomWebNotificationEntityBuilder();
    }

    public static class CustomWebNotificationEntityBuilder extends WebNotificationEntity.WebNotificationEntityBuilder {
        private UserEntity userEntity;
        private Boolean isOpened;
        private NotificationType type;
        private String title;
        private String description;

        public CustomWebNotificationEntityBuilder user(UserEntity userEntity) {
            this.userEntity = userEntity;
            return this;
        }

        public CustomWebNotificationEntityBuilder opened(Boolean isOpened) {
            this.isOpened = isOpened;
            return this;
        }

        public CustomWebNotificationEntityBuilder type(NotificationType type) {
            this.type = type;
            return this;
        }


        public CustomWebNotificationEntityBuilder title(String title) {
            this.title = title;
            return this;
        }

        public CustomWebNotificationEntityBuilder description(String description) {
            this.description = description;
            return this;
        }

        @Override
        public WebNotificationEntity build() {
            WebNotificationEntity webNotificationEntity = super.build();
            webNotificationEntity.setUser(userEntity);
            webNotificationEntity.setIsOpened(isOpened);
            webNotificationEntity.setType(type);
            webNotificationEntity.setTitle(title);
            webNotificationEntity.setDescription(description);
            return webNotificationEntity;
        }
    }

}
