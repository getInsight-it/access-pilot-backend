package it.getinsight.module.role.entity;


import it.getinsight.core.model.jpa.entity.AuditableEntity;
import it.getinsight.core.model.jpa.entity.BaseEntity;
import it.getinsight.module.client.entity.ClientEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.envers.Audited;

@Getter
@Setter
@Entity
@Audited
@Table(name = "TB_ROLE")
@SequenceGenerator(name = "ClientEntity.sq", sequenceName = "SQ_ROLE", allocationSize = 1)
public class RoleEntity extends AuditableEntity<Long, String> {

    @Id
    @Column(name = "ID")
    @GeneratedValue(generator = "ClientEntity.sq", strategy = GenerationType.SEQUENCE)
    private Long id;

    @Column(name = "ID_ROLE_EXTERNO")
    private String roleExternalId;


    @Column(name = "NOME")
    private String name;

    @Column(name = "DESCRICAO")
    private String description;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ID_ROLE_PARENT")
    private RoleEntity role;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ID_CLIENTE")
    private ClientEntity client;

}
