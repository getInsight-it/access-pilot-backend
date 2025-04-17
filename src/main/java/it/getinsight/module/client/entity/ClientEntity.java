package it.getinsight.module.client.entity;


import it.getinsight.core.model.jpa.entity.BaseEntity;
import it.getinsight.module.configuration.entity.AttachmentConfigurationEntity;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.envers.Audited;

import java.util.List;

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

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "client")
    private List<AttachmentConfigurationEntity> configurations;

}
