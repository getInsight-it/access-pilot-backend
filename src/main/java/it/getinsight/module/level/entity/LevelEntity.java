package it.getinsight.module.level.entity;


import it.getinsight.core.model.jpa.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.envers.Audited;

import java.util.UUID;

@Getter
@Setter
@Entity
@Audited
@Table(name = "TB_ESFERA")
@Builder
@NoArgsConstructor
@AllArgsConstructor
@SequenceGenerator(name = "LevelEntity.sq", sequenceName = "SQ_ESFERA", allocationSize = 1)
public class LevelEntity extends BaseEntity<Long> {

    @Id
    @Column(name = "ID")
    @GeneratedValue(generator = "LevelEntity.sq", strategy = GenerationType.SEQUENCE)
    private Long id;

    @Column(name = "UUID")
    private UUID uuid;

    @JoinColumn(name = "ID_PARENT")
    @ManyToOne(fetch = FetchType.LAZY)
    private LevelEntity parent;

    @Column(name = "SIGLA")
    private String sigla;

    @Column(name = "NOME")
    private String name;

    @Column(name = "DESCRICAO")
    private String description;

    @Column(name = "URL_EXTERNA")
    private String externalUrl;

    @Column(name = "TIPO")
    @Enumerated(EnumType.STRING)
    private LevelType type;

    @Column(name = "ICON")
    private String icon;

    @Column(name = "API_KEY")
    private String apiKey;

    @PrePersist
    public void prePersist() {
        this.uuid = UUID.randomUUID();
    }
}
