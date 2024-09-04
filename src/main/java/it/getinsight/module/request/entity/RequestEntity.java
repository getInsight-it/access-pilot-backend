package it.getinsight.module.request.entity;

import it.getinsight.core.model.jpa.entity.AuditableEntity;
import it.getinsight.module.request.enuns.RequestStatus;
import it.getinsight.module.role.entity.RoleEntity;
import it.getinsight.module.user.entity.UserEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.envers.Audited;

import java.io.Serial;


@Getter
@Setter
@Entity
@Audited
@Table(name = "TB_SOLICITACAO")
@SequenceGenerator(name = "RequestEntity.sq", sequenceName = "SQ_SOLICITACAO", allocationSize = 1)
public class RequestEntity extends AuditableEntity<Long, String> {

    @Serial
    private static final long serialVersionUID = 5287296228628658948L;

    @Id
    @Column(name = "ID")
    @GeneratedValue(generator = "RequestEntity.sq", strategy = GenerationType.SEQUENCE)
    private Long id;

    @Column(name = "STATUS")
    @Enumerated(EnumType.STRING)
    private RequestStatus status = RequestStatus.CREATED;

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

}
