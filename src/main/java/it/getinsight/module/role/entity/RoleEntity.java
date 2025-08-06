package it.getinsight.module.role.entity;

import it.getinsight.core.model.jpa.entity.AuditableEntity;
import it.getinsight.module.client.entity.ClientEntity;
import it.getinsight.module.level.entity.LevelEntity;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.SQLRestriction;
import org.hibernate.envers.Audited;

@Getter
@Setter
@Entity
@Audited
@Table(name = "TB_ROLE")
@NoArgsConstructor
@AllArgsConstructor
@SQLRestriction(value = "ATIVO = true")
@Builder
@SequenceGenerator(name = "RoleEntity.sq", sequenceName = "SQ_ROLE", allocationSize = 1)
public class RoleEntity extends AuditableEntity<Long, String> {

    @Id
    @Column(name = "ID")
    @GeneratedValue(generator = "RoleEntity.sq", strategy = GenerationType.SEQUENCE)
    private Long id;

    @Column(name = "ID_ROLE_EXTERNO")
    private String roleExternalId;

    @Column(name = "NOME")
    private String name;

    @Column(name = "LABEL")
    private String label;

    @Column(name = "icon")
    private String icon;

    @Column(name = "DESCRICAO")
    private String description;

    @Column(name = "ATIVO")
    private Boolean active;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ID_ROLE_PARENT")
    private RoleEntity role;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ID_CLIENTE")
    private ClientEntity client;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ID_ESFERA")
    private LevelEntity level;

}
