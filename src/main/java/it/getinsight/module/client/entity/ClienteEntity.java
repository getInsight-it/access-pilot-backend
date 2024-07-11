package it.getinsight.module.client.entity;


import it.getinsight.core.model.jpa.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "TB_CLIENTE")
@SequenceGenerator(name = "ClienteEntity.sq", sequenceName = "SQ_CLIENTE", allocationSize = 1)
public class ClienteEntity extends BaseEntity<Long> {

    @Id
    @Column(name = "ID")
    @GeneratedValue(generator = "ClienteEntity.sq", strategy = GenerationType.SEQUENCE)
    private Long id;

    @Column(name = "CLIENT_UUID")
    private String clientUUID;

    @Column(name = "CLIENT_ID")
    private String clientId;

    @Column(name = "DESCRICAO")
    private String descricao;
}
