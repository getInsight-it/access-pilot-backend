package it.getinsight.module.configuration.entity;

import com.fasterxml.jackson.databind.JsonNode;
import com.vladmihalcea.hibernate.type.json.JsonType;
import it.getinsight.core.model.jpa.entity.AuditableEntity;
import it.getinsight.module.configuration.util.HashMapConverter;
import it.getinsight.module.notification.enums.NotificationType;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.SQLRestriction;
import org.hibernate.annotations.Type;
import org.hibernate.envers.Audited;

import java.io.Serial;
import java.util.Map;
import java.util.UUID;

@Getter
@Setter
@Entity
@Audited
@SQLRestriction(value = "ACTIVE = true")
@Table(name = "TB_CONFIGURATION")
@SequenceGenerator(name = "ConfigurationEntity.sq", sequenceName = "SQ_CONFIGURATION", allocationSize = 1)
public class ConfigurationEntity extends AuditableEntity<Long, String> {
    @Serial
    private static final long serialVersionUID = 5287296228628658948L;

    @Id
    @Column(name = "ID")
    @GeneratedValue(generator = "ConfigurationEntity.sq", strategy = GenerationType.SEQUENCE)
    private Long id;

    @Column(name = "UUID")
    private UUID uuid;

    @Column(name = "NAME", nullable = false)
    private String name;

    @Column(name = "DESCRIPTION")
    private String description;


    @Column(name = "value", columnDefinition = "jsonb")
    @Type(JsonType.class)
    private JsonNode value;

    @Column(name = "ACTIVE")
    private Boolean active = true;

    @PrePersist
    public void prePersist() {
        this.uuid = UUID.randomUUID();
    }

}
