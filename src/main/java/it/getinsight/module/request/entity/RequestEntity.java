package it.getinsight.module.request.entity;

import it.getinsight.core.model.jpa.entity.AuditableEntity;
import it.getinsight.module.request.enuns.RequestStatus;
import it.getinsight.module.role.entity.RoleEntity;
import it.getinsight.module.user.entity.UserEntity;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.envers.Audited;

import java.io.Serial;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;
import it.getinsight.module.request.util.ProtocolUtil;


@Getter
@Setter
@Entity
@Audited
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "TB_SOLICITACAO")
@SequenceGenerator(name = "RequestEntity.sq", sequenceName = "SQ_SOLICITACAO", allocationSize = 1)
public class RequestEntity extends AuditableEntity<Long, String> {

    @Serial
    private static final long serialVersionUID = 5287296228628658948L;

    @Id
    @Column(name = "ID")
    @GeneratedValue(generator = "RequestEntity.sq", strategy = GenerationType.SEQUENCE)
    private Long id;

    @Column(name = "UUID")
    private UUID uuid;

    @Column(name = "PROTOCOL_CODE", unique = true, nullable = false)
    private String protocolCode;

    @Column(name = "STATUS")
    @Enumerated(EnumType.STRING)
    private RequestStatus status;

    @Column(name = "DESCRICAO")
    private String description;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ID_ROLE", referencedColumnName = "ID")
    private RoleEntity role;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ID_USUARIO_SOLICITANTE", referencedColumnName = "ID")
    private UserEntity requestingUser;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ID_USUARIO_APROVADOR", referencedColumnName = "ID")
    private UserEntity approvingUser;


    @PrePersist
    private void generateProtocolCode() {
        if (this.protocolCode == null || this.protocolCode.isEmpty()) {
            this.protocolCode = ProtocolUtil.generateUniqueProtocolCode();
        }
        this.uuid = UUID.randomUUID();
    }
}
