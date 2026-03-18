package it.getinsight.module.invitation.entity;

import it.getinsight.core.model.jpa.entity.AuditableEntity;
import it.getinsight.module.invitation.enuns.InvitationStatus;
import it.getinsight.module.request.entity.RequestEntity;
import it.getinsight.module.role.entity.RoleEntity;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.envers.Audited;

import java.io.Serial;
import java.time.Instant;
import java.util.UUID;

@Getter
@Setter
@Entity
@Audited
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "TB_CONVITE")
@SequenceGenerator(name = "InvitationEntity.sq", sequenceName = "SQ_CONVITE", allocationSize = 1)
public class InvitationEntity extends AuditableEntity<Long, String> {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "ID")
    @GeneratedValue(generator = "InvitationEntity.sq", strategy = GenerationType.SEQUENCE)
    private Long id;

    @Column(name = "UUID", nullable = false)
    private UUID uuid;

    @Column(name = "TOKEN_HASH", nullable = false, unique = true, length = 64)
    private String tokenHash;

    @Column(name = "EMAIL", nullable = false)
    private String email;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ID_ROLE", nullable = false)
    private RoleEntity role;

    @Column(name = "CODIGO_ITEM", nullable = false)
    private String codeItem;

    @Column(name = "DESCRICAO", length = 1000)
    private String description;

    @Column(name = "STATUS", nullable = false)
    @Enumerated(EnumType.STRING)
    private InvitationStatus status;

    @Column(name = "EXPIRES_AT")
    private Instant expiresAt;

    @Column(name = "PROTOCOL_CODE", nullable = false, length = 64)
    private String protocolCode;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ID_SOLICITACAO")
    private RequestEntity request;

    @PrePersist
    public void prePersist() {
        if (this.uuid == null) {
            this.uuid = UUID.randomUUID();
        }
        if (this.status == null) {
            this.status = InvitationStatus.PENDING;
        }
    }
}
