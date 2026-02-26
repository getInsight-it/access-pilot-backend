package it.getinsight.core.config.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "app.bootstrap.import")
@Data
public class BootstrapImportProperties {

    private boolean enabled = false;
    private String path = "./etc/import";
    private String levelsFile = "levels_exported.json";
    private String clientsFile = "clients_exported.json";
    private boolean force = false;
    private boolean importRoles = true;
    private boolean importConfigurations = true;
}
