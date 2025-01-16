package it.getinsight.module.notification.entity;

import it.getinsight.core.model.jpa.entity.AuditableEntity;
import it.getinsight.module.notification.dto.Notification;
import it.getinsight.module.notification.enums.NotificationType;
import it.getinsight.module.user.entity.UserEntity;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.envers.Audited;

import java.util.UUID;

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@Getter
@Setter
@Audited
@NoArgsConstructor
@AllArgsConstructor
@SequenceGenerator(name = "NotificationEntity.sq", sequenceName = "SQ_NOTIFICACAO", allocationSize = 1)
@Table(name = "TB_NOTIFICACAO")
public abstract class  NotificationEntity extends AuditableEntity<Long, String> {

    @Id
    @GeneratedValue(generator = "NotificationEntity.sq", strategy = GenerationType.SEQUENCE)
    @Column(name = "id")
    protected Long id;

    @Column(name = "UUID")
    protected UUID uuid;

    @Column(name = "TIPO")
    @Enumerated(EnumType.STRING)
    protected NotificationType type;

    @Column(name = "IS_OPENED")
    protected Boolean isOpened;

    @Column(name = "TITULO")
    protected String title;

    @Column(name = "DESCRICAO")
    protected String description;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "USER_ID")
    protected UserEntity user;


}
