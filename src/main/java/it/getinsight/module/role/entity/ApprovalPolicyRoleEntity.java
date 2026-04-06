package it.getinsight.module.role.entity;

import it.getinsight.core.model.jpa.entity.AuditableEntity;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.SQLRestriction;
import org.hibernate.envers.Audited;

@Getter
@Setter
@Entity
@Audited
@Table(name = "TB_POLITICA_APROVACAO_ROLE")
@NoArgsConstructor
@AllArgsConstructor
@SQLRestriction(value = "ATIVO = true")
@Builder
@SequenceGenerator(name = "ApprovalPolicyRoleEntity.sq", sequenceName = "SQ_POLITICA_APROVACAO_ROLE", allocationSize = 1)
public class ApprovalPolicyRoleEntity extends AuditableEntity<Long, String> {

    @Id
    @GeneratedValue(generator = "ApprovalPolicyRoleEntity.sq", strategy = GenerationType.SEQUENCE)
    @Column(name = "ID")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "ID_POLITICA", referencedColumnName = "ID", nullable = false)
    private ApprovalPolicyEntity policy;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "ID_ROLE", referencedColumnName = "ID", nullable = false)
    private RoleEntity role;

    @Column(name = "PODE_APROVAR", nullable = false)
    private Boolean canApprove;

    @Column(name = "PODE_REJEITAR", nullable = false)
    private Boolean canReject;

    @Column(name = "PODE_REVOGAR", nullable = false)
    private Boolean canRevoke;

    @Column(name = "ATIVO", nullable = false)
    private Boolean active;
}
