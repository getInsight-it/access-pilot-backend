package it.getinsight.module.role.entity;


import it.getinsight.core.model.jpa.entity.BaseEntity;
import it.getinsight.module.client.entity.ClienteEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "TB_ROLE")
@SequenceGenerator(name = "ClienteEntity.sq", sequenceName = "SQ_ROLE", allocationSize = 1)
public class RoleEntity extends BaseEntity<Long> {

    @Id
    @Column(name = "ID")
    @GeneratedValue(generator = "ClienteEntity.sq", strategy = GenerationType.SEQUENCE)
    private Long id;

    @Column(name = "ID_ROLE_EXTERNO")
    private String idRoleExterno;


    @Column(name = "NOME")
    private String nome;

    @Column(name = "DESCRICAO")
    private String descricao;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ID_ROLE_PARENT")
    private RoleEntity role;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ID_CLIENTE")
    private ClienteEntity cliente;

}
