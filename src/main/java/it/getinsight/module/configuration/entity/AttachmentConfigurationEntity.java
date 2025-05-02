package it.getinsight.module.configuration.entity;

import it.getinsight.core.model.jpa.entity.AuditableEntity;
import it.getinsight.module.client.entity.ClientEntity;
import it.getinsight.module.configuration.converter.FileExtensionTypeSetConverter;
import it.getinsight.module.configuration.enums.FileExtensionType;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.SQLRestriction;
import org.hibernate.envers.Audited;

import java.util.Set;
import java.util.UUID;
@Getter
@Setter
@Entity
@Audited
@Builder
@AllArgsConstructor
@NoArgsConstructor
@SQLRestriction(value = "ATIVO = true")
@Table(name = "TB_CONFIGURACAO_ANEXO")
@SequenceGenerator(name = "AttachmentConfigurationEntity.sq", sequenceName = "SQ_CONFIGURACAO_ANEXO", allocationSize = 1)
public class AttachmentConfigurationEntity extends AuditableEntity<Long, String> {

    @Id
    @GeneratedValue(generator = "AttachmentConfigurationEntity.sq", strategy = GenerationType.SEQUENCE)
    @Column(name = "ID")
    private Long id;

    @Column(name = "UUID", nullable = false)
    private UUID uuid;

    @Column(name = "CHAVE", nullable = false)
    private String key;

    @Column(name = "DESCRICAO")
    private String description;

    @Column(name = "OBRIGATORIO")
    private Boolean required;

    @Convert(converter = FileExtensionTypeSetConverter.class)
    @Column(name = "EXTENSOES_PERMITIDAS")
    private Set<FileExtensionType> allowedExtensions;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "CLIENTE_ID", nullable = false)
    private ClientEntity client;

    @Column(name = "ATIVO" , nullable = false)
    private Boolean active;

    @PrePersist
    public void prePersist() {
        this.uuid = UUID.randomUUID();
        this.active = true;
    }
}
