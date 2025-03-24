package it.getinsight.module.level.entity;

import it.getinsight.core.model.jpa.entity.AuditableEntity;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.SQLJoinTableRestriction;
import org.hibernate.annotations.SQLRestriction;
import org.hibernate.annotations.Subselect;
import org.hibernate.envers.Audited;

import java.util.UUID;

@Getter
@Setter
@Entity
@Audited
@Table(name = "TB_ESFERA_ITEM")
@Builder
@NoArgsConstructor
@AllArgsConstructor
@SequenceGenerator(name = "ItemEntity.sq", sequenceName = "SQ_ESFERA_ITEM", allocationSize = 1)
public class ItemEntity extends AuditableEntity<Long, String> {

    @Id
    @Column(name = "ID")
    @GeneratedValue(generator = "ItemEntity.sq", strategy = GenerationType.SEQUENCE)
    private Long id;

    @Column(name = "UUID")
    private UUID uuid;

    @Column(name = "NAME")
    private String name;

    @ManyToOne
    @JoinColumn(name = "ID_ESFERA", nullable = false)
    private LevelEntity level;

    @Column(name = "CODIGO_EXTERNO")
    private String externalCode;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ID_PARENT")
    private ItemEntity parent;

    @Column(name = "DESCRICAO")
    private String description;

    @Column(name = "ATIVO")
    private Boolean active;

    @PrePersist
    public void prePersist() {
        this.uuid = UUID.randomUUID();
    }
}
