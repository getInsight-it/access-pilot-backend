package it.getinsight.module.email.entity;

import it.getinsight.core.model.jpa.entity.AuditableEntity;
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
@Builder
@Audited
@Table(name = "TB_EMAIL_SENT")
public class EmailSent extends AuditableEntity<Long, String> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "EMAIL_ID")
    private Long id;

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

    @Column(name = "CONTENT", length = 1000)
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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "USER_ID")
    private UserEntity user;

}
