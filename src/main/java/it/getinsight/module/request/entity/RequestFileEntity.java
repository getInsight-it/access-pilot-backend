package it.getinsight.module.request.entity;

import it.getinsight.core.model.jpa.entity.AuditableEntity;
import it.getinsight.module.configuration.entity.AttachmentConfigurationEntity;
import it.getinsight.module.storage.entity.StorageFileEntity;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.envers.Audited;

import java.util.UUID;



@Getter
@Setter
@Entity
@Audited
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "TB_SOLICITACAO_ARQUIVO")
@SequenceGenerator(name = "RequestFileEntity.sq", sequenceName = "SQ_SOLICITACAO_ARQUIVO", allocationSize = 1)
public class RequestFileEntity extends AuditableEntity<Long, String> {

    @Id
    @GeneratedValue(generator = "RequestFileEntity.sq", strategy = GenerationType.SEQUENCE)
    private Long id;

    @Column(name = "UUID")
    private UUID uuid;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ID_SOLICITACAO", nullable = false)
    private RequestEntity request;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ID_ARQUIVO", nullable = false)
    private StorageFileEntity file;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ID_CONFIGURACAO")
    private AttachmentConfigurationEntity configuration;

    @Column(name = "TIPO_ARQUIVO")
    private String fileType;

    @Column(name = "DESCRICAO")
    private String description;

    @Column(name = "ATIVO")
    private Boolean active;


}
