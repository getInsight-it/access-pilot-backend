package it.getinsight.core.bootstrap;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import it.getinsight.core.config.properties.BootstrapImportProperties;
import it.getinsight.module.client.dto.ClientExportDTO;
import it.getinsight.module.client.dto.ClientImportRequestDTO;
import it.getinsight.module.client.dto.ClientImportSummaryDTO;
import it.getinsight.module.client.service.ClientExportService;
import it.getinsight.module.client.repository.ClientRepository;
import it.getinsight.module.keycloak.config.KeycloakProperties;
import it.getinsight.module.level.dto.LevelExportDTO;
import it.getinsight.module.level.dto.LevelImportRequestDTO;
import it.getinsight.module.level.dto.LevelImportSummaryDTO;
import it.getinsight.module.level.service.LevelExportService;
import it.getinsight.module.level.entity.LevelType;
import it.getinsight.module.level.repository.LevelRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.lang.NonNull;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.Principal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

@Slf4j
@Component
@RequiredArgsConstructor
public class BootstrapImportRunner implements ApplicationRunner {

    private final BootstrapImportProperties properties;
    private final ObjectMapper objectMapper;
    private final LevelExportService levelExportService;
    private final ClientExportService clientExportService;
    private final LevelRepository levelRepository;
    private final ClientRepository clientRepository;
    private final KeycloakProperties keycloakProperties;

    @Override
    public void run(ApplicationArguments args) {
        if (!properties.isEnabled()) {
            return;
        }

        Path basePath = Paths.get(properties.getPath());
        if (!Files.isDirectory(basePath)) {
            log.warn("Bootstrap import path not found: {}", basePath.toAbsolutePath());
            return;
        }

        Path levelsPath = basePath.resolve(properties.getLevelsFile());
        Path clientsPath = basePath.resolve(properties.getClientsFile());

        if (!Files.isRegularFile(levelsPath) || !Files.isRegularFile(clientsPath)) {
            log.warn("Bootstrap import files not found. levels={}, clients={}",
                levelsPath.toAbsolutePath(), clientsPath.toAbsolutePath());
            return;
        }

        var requestAttributes = new BootstrapRequestAttributes();
        RequestContextHolder.setRequestAttributes(requestAttributes);
        var securityContext = SecurityContextHolder.createEmptyContext();
        Principal bootstrapPrincipal = () -> "SYSTEM";
        securityContext.setAuthentication(new UsernamePasswordAuthenticationToken(bootstrapPrincipal, null, List.of()));
        SecurityContextHolder.setContext(securityContext);
        boolean levelsOk;
        boolean clientsOk;
        try {
            levelsOk = importLevels(levelsPath);
            clientsOk = importClients(clientsPath);
        } finally {
            SecurityContextHolder.clearContext();
            RequestContextHolder.resetRequestAttributes();
        }

        if (!levelsOk || !clientsOk) {
            log.warn("Bootstrap import finished with errors.");
        }
    }

    private boolean importLevels(Path levelsPath) {
        var exports = readExports(levelsPath, new TypeReference<List<LevelExportDTO>>() {});
        if (exports == null) {
            return false;
        }

        if (!properties.isForce() && shouldSkipLevels(exports)) {
            log.info("Bootstrap levels import skipped (all existing).");
            return true;
        }

        LevelImportSummaryDTO summary = levelExportService.importLevels(LevelImportRequestDTO.builder()
            .exports(exports)
            .build());

        log.info("Bootstrap levels import: created={}, updated={}, ignored={}, errors={}",
            summary.created(), summary.updated(), summary.ignored(), summary.errors());

        return summary.errors() == 0;
    }

    private boolean importClients(Path clientsPath) {
        var exports = readExports(clientsPath, new TypeReference<List<ClientExportDTO>>() {});
        if (exports == null) {
            return false;
        }

        if (!properties.isForce() && shouldSkipClients(exports)) {
            log.info("Bootstrap clients import skipped (all existing).");
            return true;
        }

        ClientImportRequestDTO request = ClientImportRequestDTO.builder()
            .force(properties.isForce())
            .importRoles(properties.isImportRoles())
            .importConfigurations(properties.isImportConfigurations())
            .exports(exports)
            .build();

        ClientImportSummaryDTO summary = clientExportService.importClients(request);

        log.info("Bootstrap clients import: created={}, updated={}, ignored={}, errors={}",
            summary.created(), summary.updated(), summary.ignored(), summary.errors());

        return summary.errors() == 0;
    }

    private <T> List<T> readExports(Path path, TypeReference<List<T>> typeReference) {
        try {
            return objectMapper.readValue(path.toFile(), typeReference);
        } catch (IOException ex) {
            log.warn("Failed to read bootstrap import file: {}", path.toAbsolutePath(), ex);
            return null;
        }
    }

    private boolean shouldSkipLevels(List<LevelExportDTO> exports) {
        if (exports.isEmpty()) {
            return true;
        }

        Set<String> namesToCheck = new HashSet<>();
        for (LevelExportDTO export : exports) {
            if (export == null) {
                return false;
            }
            String name = StringUtils.trimToNull(export.name());
            if (name == null || export.type() == null) {
                return false;
            }
            if (LevelType.BUILT_IN.equals(export.type())) {
                continue;
            }
            String normalized = normalizeName(name);
            if (normalized != null) {
                namesToCheck.add(normalized);
            }
        }

        if (namesToCheck.isEmpty()) {
            return true;
        }

        Set<String> existingNames = new HashSet<>();
        levelRepository.findByNameIgnoreCaseIn(new ArrayList<>(namesToCheck))
            .forEach(level -> {
                String key = normalizeName(level.getName());
                if (key != null) {
                    existingNames.add(key);
                }
            });

        for (String name : namesToCheck) {
            if (!existingNames.contains(name)) {
                return false;
            }
        }

        return true;
    }

    private boolean shouldSkipClients(List<ClientExportDTO> exports) {
        if (exports.isEmpty()) {
            return true;
        }

        Set<String> clientIdsToCheck = new HashSet<>();
        for (ClientExportDTO export : exports) {
            if (export == null || export.client() == null) {
                return false;
            }
            String clientId = StringUtils.trimToNull(export.client().clientId());
            if (clientId == null) {
                return false;
            }
            String normalized = normalizeClientId(clientId);
            if (isIgnoredClientId(normalized)) {
                continue;
            }
            if (normalized != null) {
                clientIdsToCheck.add(normalized);
            }
        }

        if (clientIdsToCheck.isEmpty()) {
            return true;
        }

        Set<String> existingClientIds = new HashSet<>();
        clientRepository.findByClientIdIgnoreCaseIn(new ArrayList<>(clientIdsToCheck))
            .forEach(client -> {
                String key = normalizeClientId(client.getClientId());
                if (key != null) {
                    existingClientIds.add(key);
                }
            });

        for (String clientId : clientIdsToCheck) {
            if (!existingClientIds.contains(clientId)) {
                return false;
            }
        }

        return true;
    }

    private String normalizeName(String name) {
        if (StringUtils.isBlank(name)) {
            return null;
        }
        return name.trim().toLowerCase(Locale.ROOT);
    }

    private String normalizeClientId(String clientId) {
        if (StringUtils.isBlank(clientId)) {
            return null;
        }
        return clientId.trim().toLowerCase(Locale.ROOT);
    }

    private boolean isIgnoredClientId(String clientId) {
        if (clientId == null) {
            return false;
        }
        List<String> ignoreClients = keycloakProperties.getIgnoreClients();
        if (ignoreClients == null || ignoreClients.isEmpty()) {
            return false;
        }
        String normalized = normalizeClientId(clientId);
        return ignoreClients.stream().anyMatch(c -> c != null && Objects.equals(normalized, normalizeClientId(c)));
    }

    private static final class BootstrapRequestAttributes implements RequestAttributes {
        private final Map<String, Object> attributes = new HashMap<>();

        @Override
        public Object getAttribute(@NonNull String name, int scope) {
            return scope == SCOPE_REQUEST ? attributes.get(name) : null;
        }

        @Override
        public void setAttribute(@NonNull String name, @NonNull Object value, int scope) {
            if (scope != SCOPE_REQUEST) {
                return;
            }
            attributes.put(name, value);
        }

        @Override
        public void removeAttribute(@NonNull String name, int scope) {
            if (scope == SCOPE_REQUEST) {
                attributes.remove(name);
            }
        }

        @Override
        @NonNull
        public String[] getAttributeNames(int scope) {
            if (scope != SCOPE_REQUEST) {
                return new String[0];
            }
            return attributes.keySet().toArray(new String[0]);
        }

        @Override
        public void registerDestructionCallback(@NonNull String name, @NonNull Runnable callback, int scope) {
        }

        @Override
        public Object resolveReference(@NonNull String key) {
            return null;
        }

        @Override
        @NonNull
        public String getSessionId() {
            return "bootstrap";
        }

        @Override
        @NonNull
        public Object getSessionMutex() {
            return this;
        }
    }
}
