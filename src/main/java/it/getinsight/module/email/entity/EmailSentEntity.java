package it.getinsight.module.email.entity;

import it.getinsight.module.notification.entity.NotificationEntity;
import it.getinsight.module.notification.enums.NotificationType;
import it.getinsight.module.user.entity.UserEntity;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.envers.Audited;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@PrimaryKeyJoinColumn(name= "id")
@Builder(toBuilder = true)
@Audited
@Table(name = "TB_EMAIL_SENT")
public class EmailSentEntity extends NotificationEntity {

    @Column(name = "EMAIL_TO")
    private String to;

    @Column(name = "EMAIL_FROM")
    private String from;

    @Column(name = "ANONYMOUS_COPY")
    private String anonymousCopy;

    @Column(name = "COPY")
    private String copy;

    @Column(name = "SUBJECT")
    private String subject;

    @Transient
    private String content;

    @Column(name = "HTML_FLAG")
    private Boolean isHtml;

    @Column(name = "SUCCESS_FLG")
    private Boolean success;

    @Column(name = "APP_NAME")
    private String appName;

    @Column(name = "STATUS")
    @Enumerated(EnumType.STRING)
    private EmailStatus status;

    @Column(name = "ATTACHMENT_ID")
    private UUID attachmentId;

    @CreatedDate
    @Column(name = "CREATED_DATE")
    private LocalDateTime createdDate;

    @LastModifiedDate
    @Column(name = "UPDATED_DATE")
    private LocalDateTime updatedDate;

    @PrePersist
    public void prePersist() {
        this.uuid = UUID.randomUUID();
        this.isOpened = false;
        this.type = NotificationType.EMAIL;
    }


    public static CustomEmailSentEntityBuilder builder() {
        return new CustomEmailSentEntityBuilder();
    }

    public static class CustomEmailSentEntityBuilder extends EmailSentEntityBuilder {
        private UserEntity userEntity;
        private Boolean isOpened;
        private NotificationType type;

        public CustomEmailSentEntityBuilder user(UserEntity userEntity) {
            this.userEntity = userEntity;
            return this;
        }

        public CustomEmailSentEntityBuilder opened(Boolean isOpened) {
            this.isOpened = isOpened;
            return this;
        }

        public CustomEmailSentEntityBuilder type(NotificationType type) {
            this.type = type;
            return this;
        }

        @Override
        public EmailSentEntity build() {
            EmailSentEntity emailSentEntity = super.build();
            emailSentEntity.setUser(userEntity);
            emailSentEntity.setIsOpened(isOpened);
            emailSentEntity.setType(type);
            return emailSentEntity;
        }
    }

}
