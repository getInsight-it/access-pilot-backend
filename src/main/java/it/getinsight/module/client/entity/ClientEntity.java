package it.getinsight.module.client.entity;


import it.getinsight.core.model.jpa.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.envers.Audited;

@Getter
@Setter
@Entity
@Audited
@Table(name = "TB_CLIENTE")
@SequenceGenerator(name = "ClientEntity.sq", sequenceName = "SQ_CLIENTE", allocationSize = 1)
public class ClientEntity extends BaseEntity<Long> {

    @Id
    @Column(name = "ID")
    @GeneratedValue(generator = "ClientEntity.sq", strategy = GenerationType.SEQUENCE)
    private Long id;

    @Column(name = "CLIENT_UUID")
    private String clientUUID;

    @Column(name = "CLIENT_ID")
    private String clientId;

    @Column(name = "MANAGED")
    private Boolean managed;

    @Column(name = "DESCRICAO")
    private String description;

    @Column(name = "BASE_URL")
    private String baseUrl;

}
