package it.getinsight.module.storage.entity;

import it.getinsight.core.model.jpa.entity.AuditableEntity;
import it.getinsight.module.request.entity.RequestEntity;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.envers.Audited;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Builder
@Table(name = "TB_STORAGE_FILE")
@Audited
@SequenceGenerator(name = "StorageFileEntity.sq", sequenceName = "SQ_STORAGE_FILE", allocationSize = 1)
public class StorageFileEntity extends AuditableEntity<Long, String> {

    @Id
    @GeneratedValue(generator = "StorageFileEntity.sq", strategy = GenerationType.SEQUENCE)
    private Long id;

    @Column(name = "EXCLUDED")
    private Boolean excluded;

    @Column(name = "ORIGINAL_FILENAME")
    private String originalFilename;

    @Column(name = "FILESIZE")
    private long filesize;
    @Column(name = "MIME_TYPE")
    private String mimeType;

    @Column(name = "BUCKET")
    private String bucket;

    @Column(name = "IS_PUBLIC")
    private Boolean isPublic;

    @Column(name = "EPHEMERAL")
    private Boolean ephemeral;

    @Column(name = "DOWNLOAD_COUNT")
    private Long downloadCount;

    @Column(name = "FILE_ID")
    private UUID fileId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "SOLICITACAO_ID")
    private RequestEntity request;

}
