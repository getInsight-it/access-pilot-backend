package it.getinsight.module.role.entity;

import it.getinsight.core.model.jpa.entity.AuditableEntity;
import it.getinsight.module.role.enuns.ApprovalPolicyType;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.SQLRestriction;
import org.hibernate.envers.Audited;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Entity
@Audited
@Table(name = "TB_POLITICA_APROVACAO")
@NoArgsConstructor
@AllArgsConstructor
@SQLRestriction(value = "ATIVO = true")
@Builder
@SequenceGenerator(name = "ApprovalPolicyEntity.sq", sequenceName = "SQ_POLITICA_APROVACAO", allocationSize = 1)
public class ApprovalPolicyEntity extends AuditableEntity<Long, String> {

    @Id
    @GeneratedValue(generator = "ApprovalPolicyEntity.sq", strategy = GenerationType.SEQUENCE)
    @Column(name = "ID")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "ID_ROLE", referencedColumnName = "ID", nullable = false)
    private RoleEntity role;

    @Enumerated(EnumType.STRING)
    @Column(name = "TIPO_POLITICA", nullable = false)
    private ApprovalPolicyType type;

    @Column(name = "HABILITADA", nullable = false)
    private Boolean enabled;

    @Column(name = "ATIVO", nullable = false)
    private Boolean active;

    @Builder.Default
    @OneToMany(mappedBy = "policy", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ApprovalPolicyRoleEntity> roles = new ArrayList<>();
}
