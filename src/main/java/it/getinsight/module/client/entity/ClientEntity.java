package it.getinsight.module.client.entity;


import it.getinsight.core.model.jpa.entity.BaseEntity;
import it.getinsight.module.configuration.entity.ConfigurationEntity;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.envers.Audited;

@Getter
@Setter
@Entity
@Audited
@Table(name = "TB_CLIENTE")
@Builder
@NoArgsConstructor
@AllArgsConstructor
@SequenceGenerator(name = "ClientEntity.sq", sequenceName = "SQ_CLIENTE", allocationSize = 1)
public class ClientEntity extends BaseEntity<Long> {

    @Id
    @Column(name = "ID")
    @GeneratedValue(generator = "ClientEntity.sq", strategy = GenerationType.SEQUENCE)
    private Long id;

    @Column(name = "NAME")
    private String name;

    @Column(name = "LABEL")
    private String label;

    @Column(name = "CLIENT_UUID")
    private String clientUUID;


    @Column(name = "CLIENT_ID", unique = true, nullable = false)
    private String clientId;

    @Column(name = "MANAGED")
    private Boolean managed;

    @Column(name = "STATUS")
    @Enumerated(EnumType.STRING)
    private ClientStatus status;


    @Column(name = "DESCRICAO")
    private String description;

    @Column(name = "BASE_URL")
    private String baseUrl;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "CONFIGURACAO_ID")
    private ConfigurationEntity configuration;

}
