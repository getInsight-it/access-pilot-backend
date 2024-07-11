package it.getinsight.module.solicitacao.entity;

import it.getinsight.core.model.jpa.entity.BaseEntity;
import it.getinsight.module.role.entity.RoleEntity;
import it.getinsight.module.solicitacao.enuns.SolicitacaoStatus;
import it.getinsight.module.usuario.entity.UsuarioEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
@Entity
@Table(name = "TB_SOLICITACAO")
@SequenceGenerator(name = "SolicitacaoEntity.sq", sequenceName = "SQ_SOLICITACAO", allocationSize = 1)
public class SolicitacaoEntity extends BaseEntity<Long> {

    private static final long serialVersionUID = 5287296228628658948L;

    @Id
    @Column(name = "ID")
    @GeneratedValue(generator = "SolicitacaoEntity.sq", strategy = GenerationType.SEQUENCE)
    private Long id;

    @Column(name = "STATUS")
    @Enumerated(EnumType.STRING)
    private SolicitacaoStatus status = SolicitacaoStatus.CREATED;

    @ManyToOne
    @JoinColumn(name = "ID_ROLE", referencedColumnName = "ID")
    private RoleEntity role;

    @ManyToOne
    @JoinColumn(name = "ID_USUARIO_SOLICITANTE", referencedColumnName = "ID")
    private UsuarioEntity usuarioSolicitante;

    @ManyToOne
    @JoinColumn(name = "ID_USUARIO_APROVADOR", referencedColumnName = "ID", nullable = true)
    private UsuarioEntity usuarioAprovador;

}
