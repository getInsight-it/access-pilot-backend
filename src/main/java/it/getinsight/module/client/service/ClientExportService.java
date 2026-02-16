package it.getinsight.module.client.service;

import it.getinsight.core.helper.PaginationHelper;
import it.getinsight.core.pagination.PageableRequestModel;
import it.getinsight.module.client.dto.ClientExportClientDTO;
import it.getinsight.module.client.dto.ClientExportDTO;
import it.getinsight.module.client.dto.ClientExportRoleDTO;
import it.getinsight.module.client.dto.ClientImportCountersDTO;
import it.getinsight.module.client.dto.ClientImportRequestDTO;
import it.getinsight.module.client.dto.ClientImportResultDTO;
import it.getinsight.module.client.dto.ClientImportSummaryDTO;
import it.getinsight.module.client.dto.ClientDTO;
import it.getinsight.module.client.dto.ClientFilterDTO;
import it.getinsight.module.client.entity.ClientEntity;
import it.getinsight.module.client.entity.ClientStatus;
import it.getinsight.module.client.mapper.ClientExportMapper;
import it.getinsight.module.client.mapper.ClientFilterMapper;
import it.getinsight.module.client.mapper.ClientMapper;
import it.getinsight.module.client.mapper.ClientRepresentationMapper;
import it.getinsight.module.client.repository.ClientRepository;
import it.getinsight.module.client.repository.specification.ClientSpecification;
import it.getinsight.module.configuration.dto.AttachmentConfigurationDTO;
import it.getinsight.module.configuration.entity.AttachmentConfigurationEntity;
import it.getinsight.module.configuration.mapper.AttachmentConfigurationMapper;
import it.getinsight.module.configuration.repository.AttachmentConfigurationRepository;
import it.getinsight.module.keycloak.config.KeycloakProperties;
import it.getinsight.module.keycloak.dto.ClientRepresentationDTO;
import it.getinsight.module.keycloak.dto.RoleRepresentationDTO;
import it.getinsight.module.keycloak.service.IdentityProviderService;
import it.getinsight.module.level.repository.LevelRepository;
import it.getinsight.module.level.entity.LevelEntity;
import it.getinsight.module.level.entity.LevelType;
import it.getinsight.module.role.entity.RoleEntity;
import it.getinsight.module.role.repository.RoleRepository;
import it.getinsight.module.role.service.RoleLevelPolicyService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.data.jpa.domain.Specification;

import java.util.*;
import java.util.Locale;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import static it.getinsight.message.MessageProperty.*;

@Service
@RequiredArgsConstructor
@Slf4j
public class ClientExportService {

    private static final String STATUS_CREATED = "CREATED";
    private static final String STATUS_UPDATED = "UPDATED";
    private static final String STATUS_IGNORED = "IGNORED";
    private static final String STATUS_ERROR = "ERROR";

    private final ClientRepository clientRepository;
    private final ClientExportMapper clientExportMapper;
    private final ClientFilterMapper clientFilterMapper;
    private final ClientMapper clientMapper;
    private final ClientRepresentationMapper clientRepresentationMapper;
    private final AttachmentConfigurationRepository attachmentConfigurationRepository;
    private final AttachmentConfigurationMapper attachmentConfigurationMapper;
    private final RoleRepository roleRepository;
    private final LevelRepository levelRepository;
    private final IdentityProviderService identityProviderService;
    private final RoleLevelPolicyService roleLevelPolicyService;
    private final ClientValidationService clientValidationService;
    private final KeycloakProperties keycloakProperties;

    @Transactional(readOnly = true)
    public ClientExportDTO exportClient(Long clientId, boolean includeRoles, boolean includeConfigurations) {
        var client = clientRepository.findById(clientId)
            .orElseThrow(CLIENT_NOT_FOUND_ERROR::resourceNotFoundException);

        return exportClient(client, includeRoles, includeConfigurations);
    }

    @Transactional(readOnly = true)
    public List<ClientExportDTO> exportClientsPage(PageableRequestModel<ClientFilterDTO> configPage,
                                                   boolean includeRoles,
                                                   boolean includeConfigurations) {
        final var model = configPage
            .getFilter()
            .map(clientFilterMapper::toDto)
            .map(clientMapper::toEntity)
            .orElse(new ClientEntity());

        final var spec = Specification.anyOf(
            ClientSpecification.nameContains(model.getName()),
            ClientSpecification.clientIdContains(model.getClientId()),
            ClientSpecification.descriptionContains(model.getDescription()),
            ClientSpecification.statusContains(model.getStatus())
        );

        final var page = clientRepository.findAll(spec, PaginationHelper.toPageable(configPage));

        return page.getContent()
            .stream()
            .map(client -> exportClient(client, includeRoles, includeConfigurations))
            .toList();
    }

    private ClientExportDTO exportClient(ClientEntity client, boolean includeRoles, boolean includeConfigurations) {
        ClientExportClientDTO clientExport = clientExportMapper.toExportClient(client);

        List<AttachmentConfigurationDTO> configurations = includeConfigurations
            ? Optional.ofNullable(client.getConfigurations()).orElse(List.of()).stream()
                .map(attachmentConfigurationMapper::toDto)
                .toList()
            : List.of();

        List<ClientExportRoleDTO> roles = includeRoles
            ? roleRepository.findAllByClient(client).stream()
                .filter(role -> !isIgnoredRoleName(role.getName()))
                .map(clientExportMapper::toExportRole)
                .toList()
            : List.of();

        return ClientExportDTO.builder()
            .client(clientExport)
            .configurations(configurations)
            .roles(roles)
            .build();
    }

    @CacheEvict(value = {"clients", "getTotalClients"}, allEntries = true)
    public ClientImportSummaryDTO importClients(ClientImportRequestDTO request) {
        long start = System.nanoTime();
        long created = 0;
        long updated = 0;
        long ignored = 0;
        long errors = 0;
        List<ClientImportResultDTO> results = new ArrayList<>();

        boolean force = request != null && Boolean.TRUE.equals(request.force());
        boolean importRoles = request == null || request.importRoles() == null || request.importRoles();
        boolean importConfigurations = request == null || request.importConfigurations() == null || request.importConfigurations();

        List<ClientExportDTO> exports = request != null && CollectionUtils.isNotEmpty(request.exports())
            ? request.exports()
            : List.of();

        for (ClientExportDTO export : exports) {
            ClientImportResultDTO result = importClient(export, force, importRoles, importConfigurations);
            results.add(result);
            switch (result.status()) {
                case STATUS_CREATED -> created++;
                case STATUS_UPDATED -> updated++;
                case STATUS_IGNORED -> ignored++;
                case STATUS_ERROR -> errors++;
                default -> ignored++;
            }
        }

        long duration = TimeUnit.NANOSECONDS.toMillis(System.nanoTime() - start);
        return ClientImportSummaryDTO.builder()
            .created(created)
            .updated(updated)
            .ignored(ignored)
            .errors(errors)
            .duration(duration)
            .results(results)
            .build();
    }

    private ClientImportResultDTO importClient(
        ClientExportDTO export,
        boolean force,
        boolean importRoles,
        boolean importConfigurations
    ) {
        if (export == null || export.client() == null || StringUtils.isBlank(export.client().clientId())) {
            return ClientImportResultDTO.builder()
                .status(STATUS_ERROR)
                .message("ClientId ausente no export.")
                .build();
        }

        String clientId = normalizeClientId(export.client().clientId());
        if (isIgnoredClientId(clientId)) {
            return ClientImportResultDTO.builder()
                .clientId(clientId)
                .status(STATUS_IGNORED)
                .message("Cliente ignorado por configuracao.")
                .build();
        }

        try {
            var existingEntity = clientRepository.findByClientId(clientId);
            if (existingEntity.isPresent() && !force) {
                return ClientImportResultDTO.builder()
                    .clientId(clientId)
                    .status(STATUS_IGNORED)
                    .message("Cliente ja existe e importacao nao forçada.")
                    .build();
            }

            ClientEntity entity = existingEntity.orElse(new ClientEntity());
            boolean isNew = existingEntity.isEmpty();
            applyClientFromExport(entity, export.client());
            entity = clientRepository.save(entity);

            if (Boolean.TRUE.equals(entity.getManaged())) {
                entity = ensureManagedClientInIdp(entity);
            }

            ClientImportCountersDTO roleCounters = new ClientImportCountersDTO();
            ClientImportCountersDTO configurationCounters = new ClientImportCountersDTO();

            if (importConfigurations) {
                configurationCounters = importConfigurations(entity, export.configurations(), force);
            }

            if (importRoles) {
                roleCounters = importRoles(entity, export.roles(), force);
            }

            return ClientImportResultDTO.builder()
                .clientId(clientId)
                .status(isNew ? STATUS_CREATED : STATUS_UPDATED)
                .rolesCreated(roleCounters.getCreated())
                .rolesUpdated(roleCounters.getUpdated())
                .rolesDeleted(roleCounters.getDeleted())
                .configurationsCreated(configurationCounters.getCreated())
                .configurationsUpdated(configurationCounters.getUpdated())
                .configurationsDeleted(configurationCounters.getDeleted())
                .build();
        } catch (Exception ex) {
            log.error("Falha ao importar client '{}'", clientId, ex);
            return ClientImportResultDTO.builder()
                .clientId(clientId)
                .status(STATUS_ERROR)
                .message(ex.getMessage())
                .build();
        }
    }

    private void applyClientFromExport(ClientEntity entity, ClientExportClientDTO exportClient) {
        String clientId = normalizeClientId(exportClient.clientId());
        entity.setClientId(clientId);
        String name = StringUtils.trimToNull(exportClient.name());
        if (name == null) {
            name = clientId;
        }
        entity.setName(name);

        String label = StringUtils.trimToNull(exportClient.label());
        if (label == null) {
            label = name;
        }
        entity.setLabel(label);

        entity.setDescription(exportClient.description());
        entity.setBaseUrl(exportClient.baseUrl());
        entity.setManaged(Boolean.TRUE.equals(exportClient.managed()));
        entity.setStatus(resolveStatus(exportClient.status()));
    }

    private ClientEntity ensureManagedClientInIdp(ClientEntity entity) {
        List<ClientRepresentationDTO> clients = identityProviderService.getClientsByClientId(entity.getClientId());
        ClientRepresentationDTO representation;
        if (CollectionUtils.isEmpty(clients)) {
            var defaultClient = ClientRepresentationDTO.createDefault(entity.getClientId(), entity.getDescription(), entity.getBaseUrl());
            identityProviderService.createClient(defaultClient);
            clients = identityProviderService.getClientsByClientId(entity.getClientId());
            if (CollectionUtils.isEmpty(clients)) {
                throw CLIENT_NOT_FOUND_ERROR.businessException();
            }
        }

        representation = clients.getFirst();
        if (!Objects.equals(entity.getClientUUID(), representation.getId())) {
            entity.setClientUUID(representation.getId());
        }

        ClientDTO dto = ClientDTO.builder()
            .id(entity.getId())
            .name(entity.getName())
            .label(entity.getLabel())
            .clientId(entity.getClientId())
            .clientUUID(entity.getClientUUID())
            .managed(entity.getManaged())
            .status(entity.getStatus() != null ? entity.getStatus().name() : null)
            .description(entity.getDescription())
            .baseUrl(entity.getBaseUrl())
            .configurations(List.of())
            .allowedItemsHierarchy(List.of())
            .build();
        clientRepresentationMapper.fromDtoRepresentation(dto, representation);
        identityProviderService.updateClient(representation.getId(), representation);

        return clientRepository.save(entity);
    }

    private ClientImportCountersDTO importConfigurations(ClientEntity client, List<AttachmentConfigurationDTO> configurations, boolean force) {
        ClientImportCountersDTO counters = new ClientImportCountersDTO();
        List<AttachmentConfigurationDTO> configurationList = configurations != null ? configurations : List.of();

        Set<String> providedNames = configurationList.stream()
            .map(AttachmentConfigurationDTO::name)
            .filter(StringUtils::isNotBlank)
            .map(name -> name.trim().toLowerCase())
            .collect(Collectors.toSet());

        for (AttachmentConfigurationDTO dto : configurationList) {
            if (dto == null || StringUtils.isBlank(dto.name())) {
                continue;
            }
            String name = dto.name().trim();
            AttachmentConfigurationEntity entity = findConfigurationByName(client.getId(), name);
            boolean created = false;
            if (entity == null) {
                entity = AttachmentConfigurationEntity.builder().build();
                entity.setName(name);
                created = true;
            }

            entity.setClient(client);
            entity.setDescription(dto.description());
            entity.setRequired(dto.required() != null ? dto.required() : Boolean.FALSE);
            entity.setAllowedExtensions(dto.allowedExtensions());
            entity.setIcon(dto.icon());
            entity.setActive(dto.active() != null ? dto.active() : Boolean.TRUE);

            attachmentConfigurationRepository.save(entity);
            if (created) {
                counters.setCreated(counters.getCreated() + 1);
            } else {
                counters.setUpdated(counters.getUpdated() + 1);
            }
        }

        if (force) {
            for (AttachmentConfigurationEntity existing : attachmentConfigurationRepository.findAllByClient(client)) {
                String name = existing.getName();
                if (StringUtils.isBlank(name)) {
                    continue;
                }
                if (!providedNames.contains(name.trim().toLowerCase())) {
                    attachmentConfigurationRepository.softDelete(existing.getId());
                    counters.setDeleted(counters.getDeleted() + 1);
                }
            }
        }

        clientValidationService.validateAttachmentConfigurations(attachmentConfigurationRepository.findAllByClient(client));
        return counters;
    }

    private ClientImportCountersDTO importRoles(ClientEntity client, List<ClientExportRoleDTO> roles, boolean force) {
        ClientImportCountersDTO counters = new ClientImportCountersDTO();
        List<ClientExportRoleDTO> roleList = roles != null ? roles : List.of();

        Set<String> providedNames = roleList.stream()
            .map(ClientExportRoleDTO::name)
            .filter(StringUtils::isNotBlank)
            .map(name -> name.trim().toLowerCase())
            .filter(name -> !isIgnoredRoleName(name))
            .collect(Collectors.toSet());

        Map<String, RoleEntity> upserted = new HashMap<>();
        for (ClientExportRoleDTO dto : roleList) {
            if (dto == null || StringUtils.isBlank(dto.name())) {
                continue;
            }
            String name = dto.name().trim();
            if (isIgnoredRoleName(name)) {
                continue;
            }

            RoleEntity entity = findRoleByName(client.getId(), name);
            boolean created = false;
            if (entity == null) {
                entity = RoleEntity.builder().build();
                entity.setName(name);
                entity.setActive(true);
                created = true;
            }

            entity.setClient(client);
            String label = StringUtils.trimToNull(dto.label());
            entity.setLabel(label != null ? label : name);
            entity.setDescription(dto.description());
            entity.setIcon(dto.icon());
            entity.setActive(true);
            entity.setLevel(resolveRoleLevel(dto));
            entity.setRole(null);

            roleRepository.save(entity);
            upserted.put(name.toLowerCase(), entity);
            if (created) {
                counters.setCreated(counters.getCreated() + 1);
            } else {
                counters.setUpdated(counters.getUpdated() + 1);
            }

            synchronizeRoleInIdp(client, entity);
        }

        applyRoleParents(client, roleList, upserted);

        if (force) {
            for (RoleEntity existing : roleRepository.findAllByClient(client)) {
                String name = existing.getName();
                if (StringUtils.isBlank(name) || isIgnoredRoleName(name)) {
                    continue;
                }
                if (!providedNames.contains(name.trim().toLowerCase())) {
                    roleRepository.softDelete(existing.getId());
                    counters.setDeleted(counters.getDeleted() + 1);
                    deleteRoleFromIdp(client, name);
                }
            }
        }

        return counters;
    }

    private LevelEntity resolveRoleLevel(ClientExportRoleDTO dto) {
        if (dto == null) {
            return null;
        }
        boolean hasLevelName = StringUtils.isNotBlank(dto.levelName());
        boolean hasLevelType = StringUtils.isNotBlank(dto.levelType());
        if (!hasLevelName && !hasLevelType) {
            return null;
        }
        if (!hasLevelName || !hasLevelType) {
            throw LEVEL_NOT_FOUND_ERROR.resourceNotFoundException();
        }

        LevelType type;
        try {
            type = LevelType.valueOf(dto.levelType().trim().toUpperCase(Locale.ROOT));
        } catch (IllegalArgumentException ex) {
            throw LEVEL_NOT_FOUND_ERROR.resourceNotFoundException();
        }

        return levelRepository.findByNameIgnoreCaseAndTypeAndActiveTrue(dto.levelName().trim(), type)
            .orElseThrow(LEVEL_NOT_FOUND_ERROR::resourceNotFoundException);
    }

    private void applyRoleParents(ClientEntity client, List<ClientExportRoleDTO> roles, Map<String, RoleEntity> upserted) {
        for (ClientExportRoleDTO dto : roles) {
            if (dto == null || StringUtils.isBlank(dto.name()) || StringUtils.isBlank(dto.parentName())) {
                continue;
            }
            String roleName = dto.name().trim().toLowerCase();
            String parentName = dto.parentName().trim().toLowerCase();
            if (isIgnoredRoleName(roleName) || isIgnoredRoleName(parentName)) {
                continue;
            }

            RoleEntity role = upserted.get(roleName);
            RoleEntity parent = upserted.get(parentName);
            if (role == null || parent == null) {
                continue;
            }

            Long parentLevelId = parent.getLevel() != null ? parent.getLevel().getId() : null;
            Long roleLevelId = role.getLevel() != null ? role.getLevel().getId() : null;
            roleLevelPolicyService.validateChildLevelAssignment(parentLevelId, roleLevelId);

            role.setRole(parent);
            roleRepository.save(role);
        }
    }

    private void synchronizeRoleInIdp(ClientEntity client, RoleEntity role) {
        if (!Boolean.TRUE.equals(client.getManaged()) || StringUtils.isBlank(client.getClientUUID())) {
            return;
        }

        RoleRepresentationDTO representation;
        try {
            representation = identityProviderService.getRole(client.getClientUUID(), role.getName());
            identityProviderService.updateRole(client.getClientUUID(), role.getName(), RoleRepresentationDTO.builder()
                .name(role.getName())
                .description(role.getDescription())
                .build());
        } catch (Exception ex) {
            identityProviderService.createRole(client.getClientUUID(), RoleRepresentationDTO.builder()
                .name(role.getName())
                .description(role.getDescription())
                .build());
            representation = identityProviderService.getRole(client.getClientUUID(), role.getName());
        }

        if (representation != null && representation.id() != null) {
            role.setRoleExternalId(representation.id());
            roleRepository.save(role);
        }
    }

    private void deleteRoleFromIdp(ClientEntity client, String roleName) {
        if (!Boolean.TRUE.equals(client.getManaged()) || StringUtils.isBlank(client.getClientUUID())) {
            return;
        }
        try {
            identityProviderService.deleteRole(client.getClientUUID(), roleName);
        } catch (Exception ex) {
            log.warn("Role '{}' ja estava removida no IDP", roleName);
        }
    }

    private RoleEntity findRoleByName(Long clientId, String name) {
        if (clientId == null || StringUtils.isBlank(name)) {
            return null;
        }
        return roleRepository.findByClientIdAndNameIgnoreCase(clientId, name).stream().findFirst().orElse(null);
    }

    private AttachmentConfigurationEntity findConfigurationByName(Long clientId, String name) {
        if (clientId == null || StringUtils.isBlank(name)) {
            return null;
        }
        return attachmentConfigurationRepository.findByClientIdAndNameIgnoreCase(clientId, name).stream().findFirst().orElse(null);
    }

    private ClientStatus resolveStatus(String status) {
        if (StringUtils.isBlank(status)) {
            return ClientStatus.UNPUBLISHED;
        }
        return Arrays.stream(ClientStatus.values())
            .filter(s -> StringUtils.equalsIgnoreCase(s.name(), status))
            .findFirst()
            .orElseThrow(() -> CLIENT_INVALID_STATUS.bind(status).businessException());
    }

    private String normalizeClientId(String clientId) {
        return clientId.trim().toLowerCase();
    }

    private boolean isIgnoredClientId(String clientId) {
        if (clientId == null) {
            return false;
        }
        List<String> ignoreClients = keycloakProperties.getIgnoreClients();
        if (CollectionUtils.isEmpty(ignoreClients)) {
            return false;
        }
        return ignoreClients.contains(clientId.trim().toLowerCase());
    }

    private boolean isIgnoredRoleName(String roleName) {
        if (roleName == null) {
            return false;
        }
        var ignoreRoles = keycloakProperties.getIgnoreRoles();
        if (CollectionUtils.isEmpty(ignoreRoles)) {
            return false;
        }
        String normalized = roleName.trim().toLowerCase();
        return ignoreRoles.stream().anyMatch(r -> r != null && normalized.equals(r.trim().toLowerCase()));
    }

}
