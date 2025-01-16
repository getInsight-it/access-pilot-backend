package it.getinsight.module.configuration.entity;

import it.getinsight.core.model.jpa.entity.AuditableEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.envers.Audited;

import java.io.Serial;

@Getter
@Setter
@Entity
@Audited
@Table(name = "TB_CONFIGURATION")
@SequenceGenerator(name = "ConfigurationEntity.sq", sequenceName = "SQ_CONFIGURATION", allocationSize = 1)
public class ConfigurationEntity extends AuditableEntity<Long, String> {
    @Serial
    private static final long serialVersionUID = 5287296228628658948L;

    @Id
    @Column(name = "ID")
    @GeneratedValue(generator = "ConfigurationEntity.sq", strategy = GenerationType.SEQUENCE)
    private Long id;

    @Column(name = "NAME")
    private String name;

    @Column(name = "DESCRIPTION")
    private String description;

    @Column(name = "ICON")
    private String icon;

    @Column(name = "AUTHORITY")
    private String authority;

    @Column(name = "REDIRECT_URL")
    private String redirectUrl;

    @Column(name = "CLIENT_ID")
    private String clientId;

    @Column(name = "RESPONSE_TYPE")
    private String responseType;

    @Column(name = "SCOPE")
    private String scope;

    @Column(name = "POST_LOGOUT_REDIRECT_URI")
    private String postLogoutRedirectUri;

    @Column(name = "START_CHECKSESSION")
    private Boolean startChecksession;

    @Column(name = "SILENT_RENEW")
    private Boolean silentRenew;

    @Column(name = "STARTUP_ROUTE")
    private String startupRoute;

    @Column(name = "FORBIDDEN_ROUTE")
    private String forbiddenRoute;

    @Column(name = "UNAUTHORIZED_ROUTE")
    private String unauthorizedRoute;

    @Column(name = "LOG_LEVEL")
    private Integer logLevel;

    @Column(name = "MAX_ID_TOKEN_IAT_OFFSET_ALLOWED_IN_SECONDS")
    private Integer maxIdTokenIatOffsetAllowedInSeconds;

    @Column(name = "HISTORY_CLEANUP_OFF")
    private Boolean historyCleanupOff;

}
